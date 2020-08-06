package ru.zekoh.subtotal;

import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.zekoh.db.DAO.CheckDao;
import ru.zekoh.db.DAO.SynchronizeDao;
import ru.zekoh.db.DAOImpl.CheckDaoImpl;
import ru.zekoh.db.DAOImpl.SynchronizeDaoImpl;
import ru.zekoh.db.entity.*;
import ru.zekoh.properties.Properties;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static ru.zekoh.core.Сatalog.generate;

public class Subtotal {
    private static final Logger logger = LogManager.getLogger(Subtotal.class);
    private static Subtotal subtotal = null;
    // Сохраняем куки через context в свойство
    private HttpClientContext context = new HttpClientContext();
    CloseableHttpClient httpСlient = HttpClients.createDefault();
    private String mail = Properties.subtotalMail;
    private String password = Properties.subtotalPassword;
    // Количество попыток аутентификации на сервере
    int numberOfAttempts = 2;

    public static Subtotal getInstance() {
        if (subtotal == null) {
            subtotal = new Subtotal();
            // Проходим аутентификацию и сохраняем куки в context
            boolean status = subtotal.autontification();
            if (status) {

            } else {
                subtotal = null;
                logger.error("Произошла ошибка при попытки аутенфтификации в Subtotal.");
            }
        }
        return subtotal;
    }

    // Синхронизация товаров и групп с сервером Subtotal
    void synchroniseGoods() {
    }

    // Получить список товаров и групп
    void getGoodsList() {
    }

    // Передать список сущностей и добавить/обновить в бд данные
    public boolean updateGoodsInDataBase() {
        // Пробуем получить список продуктов и папок
        List<ProductSubtotal> productSubtotals = new ArrayList<>();
        List<FolderSubtotal> folderSubtotals = new ArrayList<>();
        Session session = Properties.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            boolean flag = true;
            int page_numner = 1;
            while (flag) {
                String path = "https://app.subtotal.ru/"+Properties.subtotalId+"/data/store_goods.php?store="+Properties.subtotalStoreId+"&good_type=all&favourite_goods=0&page_num=" + page_numner + "&page_size=5000&return_suggestions=false&search=&tags=&ch_from=0";
                page_numner++;
                HttpGet httpGet = new HttpGet(path);
                CloseableHttpResponse response = httpСlient.execute(httpGet, context);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    Header encodingHeader = entity.getContentEncoding();

                    // you need to know the encoding to parse correctly
                    Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 :
                            Charsets.toCharset(encodingHeader.getValue());

                    // use org.apache.http.util.EntityUtils to read json as string
                    String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);

                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray goods = (JSONArray) jsonObject.get("goods");

                    if (goods.length() > 0) {
                        for (int i = 0; i < goods.length(); i++) {
                            JSONObject good = (JSONObject) goods.get(i);
                            System.out.println();
                            ProductSubtotal product = new ProductSubtotal();
                            product.setId((int) good.get("id"));
                            product.setName((String) good.get("name"));
                            product.setPrice((Double) good.get("price"));
                            product.setUnit((String) good.get("unit_code"));
                            product.setUnit_name((String) good.get("unit_name"));
                            JSONArray tagsArray = (JSONArray) good.get("tags");
                            if (tagsArray.length() > 0) {
                                product.setFolder_name((String) tagsArray.get(0));
                                FolderSubtotal folderSubtotal = new FolderSubtotal();
                                folderSubtotals.add(folderSubtotal);
                            } else {
                                product.setFolder_name("Корневая папка");
                            }
                            productSubtotals.add(product);
                        }
                    } else {
                        break;
                    }
                } else {
                    // Попытаться пройти аутентификацию еще раз
                    if (autontification()) {
                        updateGoodsInDataBase();
                    } else {
                        logger.error("Ошибка при аутентификации на сервере Subtotal!");
                        return false;
                    }
                }
            }

            if (productSubtotals.size() > 0) {
                // Добавить в базу данных
                // Проверка группы
                List<DataEntity> dataFolderEntities = new ArrayList<>();

                session.createSQLQuery("truncate table product").executeUpdate();

                // Создаем корневую папку
                DataEntity basicFolder = new DataEntity();
                basicFolder.setFullName("Корневая папка");
                basicFolder.setShortName("Корневая папка");
                basicFolder.setFolder(1);
                basicFolder.setLive(true);
                basicFolder.setPrice(0.0);
                basicFolder.setParentId(1);
                session.save(basicFolder);
                dataFolderEntities.add(basicFolder);

                for (ProductSubtotal product : productSubtotals) {
                    if (product.getFolder_name().length() > 0) {
                        int folderId = checkFolderInDataEntity(dataFolderEntities, product.getFolder_name());
                        if (folderId == 0) {
                            DataEntity newFolder = new DataEntity();
                            newFolder.setFullName(product.getFolder_name());
                            newFolder.setShortName(product.getFolder_name());
                            newFolder.setFolder(1);
                            newFolder.setLive(true);
                            newFolder.setPrice(0.0);
                            newFolder.setParentId(1);
                            folderId = (int) session.save(newFolder);
                            product.setFolder_id(folderId);

                            DataEntity d = new DataEntity();
                            d.setFullName(product.getFolder_name());
                            d.setId(folderId);
                            dataFolderEntities.add(d);
                        } else {
                            product.setFolder_id(folderId);
                        }
                    }

                    boolean unit = false;
                    if (product.getUnit().equalsIgnoreCase("шт.")) {
                        unit = true;
                    }

                    DataEntity dataEntity = new DataEntity(product.getName(), product.getName(), product.getPrice(), 0, product.getFolder_id(), product.getId(), true, 0, 0, unit, false, true);
                    session.save(dataEntity);
                }

                transaction.commit();
                session.close();
            }

            generate();
            return true;
        } catch (IOException e) {
            session.close();
            logger.error(e.toString());
            return false;
        }
    }


    // Передать список сущностей и добавить/обновить в бд данные
    public boolean updateGoodsInDataBasePat() {
        // Пробуем получить список продуктов и папок
        List<ProductSubtotal> productSubtotals = new ArrayList<>();
        List<FolderSubtotal> folderSubtotals = new ArrayList<>();
        try {
            boolean flag = true;
            int page_numner = 1;
            while (flag) {
                String path = "https://app.subtotal.ru/"+Properties.subtotalId+"/data/store_goods.php?store="+Properties.subtotalStoreId+"&good_type=all&favourite_goods=0&page_num=" + page_numner + "&page_size=5000&return_suggestions=false&search=&tags=&ch_from=0";
                page_numner++;
                HttpGet httpGet = new HttpGet(path);
                CloseableHttpResponse response = httpСlient.execute(httpGet, context);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    Header encodingHeader = entity.getContentEncoding();

                    // you need to know the encoding to parse correctly
                    Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 :
                            Charsets.toCharset(encodingHeader.getValue());

                    // use org.apache.http.util.EntityUtils to read json as string
                    String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);

                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray goods = (JSONArray) jsonObject.get("goods");

                    if (goods.length() > 0) {
                        for (int i = 0; i < goods.length(); i++) {
                            JSONObject good = (JSONObject) goods.get(i);
                            System.out.println();
                            ProductSubtotal product = new ProductSubtotal();
                            product.setId((int) good.get("id"));
                            product.setName((String) good.get("name"));
                            product.setPrice((Double) good.get("price"));
                            product.setUnit((String) good.get("unit_code"));
                            product.setUnit_name((String) good.get("unit_name"));
                            JSONArray tagsArray = (JSONArray) good.get("tags");
                            if (tagsArray.length() > 0) {
                                product.setFolder_name((String) tagsArray.get(0));
                                FolderSubtotal folderSubtotal = new FolderSubtotal();
                                folderSubtotals.add(folderSubtotal);
                            } else {
                                product.setFolder_name("Корневая папка");
                            }
                            productSubtotals.add(product);
                        }
                    } else {
                        break;
                    }
                } else {
                    // Попытаться пройти аутентификацию еще раз
                    if (autontification()) {
                        updateGoodsInDataBase();
                    } else {
                        logger.error("Ошибка при аутентификации на сервере Subtotal!");
                        return false;
                    }
                }
            }

            if (productSubtotals.size() > 0) {
                // Добавить в базу данных
                Session sessionFolderGet = Properties.sessionFactory.openSession();
                // Проверка группы
                List<DataEntity> dataFolderEntities = sessionFolderGet.createQuery("SELECT a FROM DataEntity a WHERE a.folder = 1 AND a.live = true ORDER BY a.serialNumber asc ", DataEntity.class).getResultList();
                sessionFolderGet.close();

                Session session = Properties.sessionFactory.openSession();
                Transaction productTransaction = session.beginTransaction();
                session.createQuery("UPDATE DataEntity SET live = false WHERE live = true");
                for (ProductSubtotal product : productSubtotals) {
                    if (product.getFolder_name().length() > 0) {
                        int folderId = checkFolderInDataEntity(dataFolderEntities, product.getFolder_name());
                        if (folderId == 0) {
                            // Создать папку
                            Session sessionFolder = Properties.sessionFactory.openSession();
                            Transaction t = sessionFolder.beginTransaction();
                            DataEntity newFolder = new DataEntity();
                            newFolder.setFullName(product.getFolder_name());
                            newFolder.setShortName(product.getFolder_name());
                            newFolder.setFolder(1);
                            newFolder.setLive(true);
                            newFolder.setPrice(0.0);
                            folderId = (int) sessionFolder.save(newFolder);
                            t.commit();
                            sessionFolder.close();

                            product.setFolder_id(folderId);

                            DataEntity d = new DataEntity();
                            d.setFullName(product.getFolder_name());
                            d.setId(folderId);
                            dataFolderEntities.add(d);
                        } else {
                            product.setFolder_id(folderId);
                        }
                    }

                    boolean unit = false;
                    if (product.getUnit() == "шт.") {
                        unit = true;
                    }

                    DataEntity dataEntity = new DataEntity(product.getName(), product.getName(), product.getPrice(), 0, 0, product.getId(), true, product.getFolder_id(), 0, unit, false, false);
                    session.save(dataEntity);
                }

                productTransaction.commit();
                session.close();
            }

            generate();
            return true;
        } catch (IOException e) {
            logger.error(e.toString());
            return false;
        }
    }

    // Отправить чек по продаже в Subtotal
    boolean sendCheck() {
        return false;
    }

    // Возврат продажи
    boolean returnCheck() {
        return false;
    }

    // Метод аутентификации в личном кабинете Subtotal
    private boolean autontification() {
        try {
            JSONObject json = new JSONObject();
            json.put("login", Properties.subtotalMail);
            json.put("password", Properties.subtotalPassword);

            HttpPost httpPost = new HttpPost("https://app.subtotal.ru/webapi/login");
            httpPost.setEntity(new StringEntity(json.toString()));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("charset", "utf-8");
            CloseableHttpResponse response = httpСlient.execute(httpPost, context);

            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            } else {
                logger.error("Произошла ошибка при попытки аутенфтификации в Subtotal. Код: " + response.getStatusLine().getStatusCode());
                return false;
            }
        } catch (Exception e) {
            logger.error("Произошла ошибка при попытки аутенфтификации в Subtotal: " + e.toString());
            return false;
        }
    }

    private int checkFolderInDataEntity(List<DataEntity> dataFolderEntities, String folderName) {
        for (DataEntity folder : dataFolderEntities) {
            if (folderName.equals(folder.getFullName())) {
                return folder.getId();
            }
        }
        return 0;
    }

    public boolean sendSalesToSubtotal() {
        // Получить последнию дату успешно синхронизированного чека
        // Если таблица синхронизации пуста, то берем все чеки
        String timeFrom = "";
        SynchronizeDao synchronizeDao = new SynchronizeDaoImpl();
        Synchronize lastSynchronize = synchronizeDao.getLast();
        System.out.println("Последний чек тут: " +lastSynchronize);
        if (lastSynchronize != null) {
            timeFrom = lastSynchronize.getDateCloseOfLastCheck();
        }
        CheckDao checkDao = new CheckDaoImpl();
        List<CheckSubtotal> checkSubtotalList = checkDao.getChecksFrom(timeFrom);
        System.out.println("Старт отправки данных");
        if (checkSubtotalList != null) {
            // По одному отправляем чек в subtotal и если чек успешно сохранен то
            for (CheckSubtotal check : checkSubtotalList) {
                if(sendCheck(check)){
                    // Добавить запись в о синхронизация

                }else {
                    //todo Ошибка отобразить сообщение "Произошла ошибка попробуйте еще раз"
                    return false;
                }
            }
            // Сохраняем информацию об этом в таблице синхронизаций
            return true;
        } else {
            //todo Отобразить сообщения "Нет новых чеков для отправки на сервер Subtotal"
            logger.info("Нет новых чеков для отправки на сервер Subtotal");
            return false;
        }

    }

    // Отправляем чек на сервер Subtotal
    public boolean sendCheck(CheckSubtotal check) {
        try {
            long unixSeconds = Long.parseLong(check.getDateOfClosing());
            Date date = new java.util.Date(unixSeconds * 1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+3"));
            String formattedDate = sdf.format(date);
            System.out.println(formattedDate);

            JSONObject addPay = new JSONObject();
            addPay.put("pay_date", formattedDate);
            addPay.put("pay_type", check.getTypeOfPayment());
            addPay.put("pay_sum", check.getTotal());
            addPay.put("pos", Properties.subtotalPosId);
            addPay.put("description", JSONObject.NULL);
            JSONArray addPayArray = new JSONArray();
            addPayArray.put(addPay);

            JSONObject outcome = new JSONObject();
            outcome.put("pos_id", Properties.subtotalPosId);
            outcome.put("store_id", Properties.subtotalStoreId);
            outcome.put("add_pay", addPayArray);
            outcome.put("description", "Bonuses: " + check.getPayWithBonus());
            outcome.put("is_carry_out", true);

            JSONArray goods = new JSONArray();
            for (GoodSubtotal goodSubtotal : check.getGoodsList()) {
                JSONObject good = new JSONObject();
                good.put("cnt", goodSubtotal.getQuantity());
                good.put("id", JSONObject.NULL);
                good.put("init_price", goodSubtotal.getPriceFromThePriceList());
                good.put("price", goodSubtotal.getPriceAfterDiscount());
                JSONObject id = new JSONObject();
                id.put("id", goodSubtotal.getGeneralId());
                good.put("good", id);
                goods.put(good);
            }

            JSONObject json = new JSONObject();
            json.put("outcome", outcome);
            json.put("goods", goods);
            json.put("uid", Properties.subtotalPosId + "-" + check.getDateOfClosing());

            System.out.println("JSON вот такой вот: " + json.toString());
            // Отправка json на сервер


            HttpPost httpPost = new HttpPost("https://app.subtotal.ru/"+Properties.subtotalId+"/webapi/stock/outcomes");
            httpPost.setEntity(new StringEntity(json.toString()));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("charset", "utf-8");
            CloseableHttpResponse response = httpСlient.execute(httpPost, context);

            if (response.getStatusLine().getStatusCode() == 200) {
              // Внести запись в бд
                HttpEntity entity = response.getEntity();
                Header encodingHeader = entity.getContentEncoding();

                // you need to know the encoding to parse correctly
                Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 :
                        Charsets.toCharset(encodingHeader.getValue());

                // use org.apache.http.util.EntityUtils to read json as string
                String json1 = EntityUtils.toString(entity, StandardCharsets.UTF_8);

                JSONObject jsonObject = new JSONObject(json1);
                System.out.println(jsonObject);

                Synchronize synchronize = new Synchronize();
                synchronize.setStatus(true);
                synchronize.setCheckId(check.getId());
                synchronize.setDateCloseOfLastCheck(check.getDateOfClosing());
                SynchronizeDao synchronizeDao = new SynchronizeDaoImpl();
                if(synchronizeDao.create(synchronize)){
                    return true;
                }else {
                    logger.error("Чек с id: "+check.getId()+" успешно отправлен в subtotal, но не добавлен в локальную БД!");
                    return false;
                }
            } else {
                logger.error("Произошла ошибка при попытки аутенфтификации в Subtotal. Код: " + response.getStatusLine().getStatusCode());
                return false;
            }
        } catch (Exception e) {
            logger.error("Ошибка при отправки чека с id: " + check.getId() + " на сервер subtotal. Ошибка: " + e.toString());
            return false;
        }
    }
}
