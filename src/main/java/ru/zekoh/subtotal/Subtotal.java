package ru.zekoh.subtotal;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import ru.zekoh.properties.Properties;

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
        if (numberOfAttempts > 0) {
            try {
                String path = "https://app.subtotal.ru/id50538/data/store_goods.php?store=123994&good_type=all&favourite_goods=0&page_num=1&page_size=20&price=21&return_suggestions=false&search=&tags=&totals=0&ch_from=0";
                HttpGet httpGet = new HttpGet(path);
                CloseableHttpResponse response = httpСlient.execute(httpGet, context);
                if (response.getStatusLine().getStatusCode() == 200) {

                } else {
                    // Переподключится и попробовать еще раз
                    numberOfAttempts--;
                    getGoodsList();
                }

                // Получить список товаров и групп
                // Добавить в БД
            } catch (Exception e) {

            }
        }
    }

    // Передать список сущностей и добавить/обновить в бд данные
    void updateGoodsInDataBase() {

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
}
