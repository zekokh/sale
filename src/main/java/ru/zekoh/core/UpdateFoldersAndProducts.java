package ru.zekoh.core;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.dmt.spi.ExecPlugin;
import ru.zekoh.db.entity.DataEntity;
import ru.zekoh.db.entity.UserEntity;
import ru.zekoh.db.entity.UserFromBonus;
import ru.zekoh.properties.Properties;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static ru.zekoh.core.Сatalog.generate;

public class UpdateFoldersAndProducts {

    static String url = "http://jacques-andre-center.herokuapp.com/changes/get";
    static String urlAnswer = "http://jacques-andre-center.herokuapp.com/changes/check";

    // static String url = "http://localhost:3000/changes/get";
    // static String urlAnswer = "http://localhost:3000/changes/check";

    public static boolean start() {

        // Направить запрос
        try {
            ArrayList<Integer> array = new ArrayList<Integer>();
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            int responseCode = con.getResponseCode();

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "UTF8"));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONArray myresponse = new JSONArray(response.toString());
                for (int i = 0; i < myresponse.length(); i++) {
                    JSONObject json = new JSONObject(myresponse.get(i).toString());
                    int action = json.getInt("action");
                    System.out.println(json.getString("short_name"));
                    if (action == 1) {
                        System.out.println("Создать");

                        Session session = Properties.sessionFactory.openSession();

                        Transaction t = session.beginTransaction();

                        int folder = 0;
                        if (json.getBoolean("folder")) {
                            folder = 1;
                        }

                        boolean unit = false;
                        if(json.getInt("unit") == 1){
                            unit = true;
                        }

                        DataEntity dataEntity = new DataEntity(json.getInt("product_id"),
                                json.getString("short_name"),
                                json.getString("full_name"),
                                json.getDouble("price"),
                                folder,
                                json.getInt("parent_id"),
                                json.getInt("general_id"),
                                json.getBoolean("live"),
                                json.getInt("classifier"),
                                json.getInt("serial_number"),
                                unit);


                        session.save(dataEntity);
                        t.commit();
                        session.close();

                        array.add(json.getInt("id"));

                    } else if (action == 2) {
                        System.out.println("Обновить");


                        Session session = Properties.sessionFactory.openSession();

                        DataEntity dataEntity = session.createQuery("SELECT a FROM DataEntity a WHERE a.id = "+json.getInt("product_id")+"", DataEntity.class).getSingleResult();
                        Transaction t = session.beginTransaction();
                        dataEntity.setShortName(json.getString("short_name"));
                        dataEntity.setFullName(json.getString("full_name"));
                        dataEntity.setPrice(json.getDouble("price"));
                        int folder = 0;
                        if (json.getBoolean("folder")) {
                            folder = 1;
                        }
                        dataEntity.setFolder(folder);
                        dataEntity.setParentId(json.getInt("parent_id"));
                        dataEntity.setGeneralId(json.getInt("general_id"));
                        dataEntity.setLive(json.getBoolean("live"));
                        dataEntity.setClassifier(json.getInt("classifier"));
                        dataEntity.setSerialNumber(json.getInt("serial_number"));

                        boolean unit = false;
                        if(json.getInt("unit") == 1){
                            unit = true;
                        }
                        dataEntity.setUnit(unit);

                        System.out.println("Имя: "+dataEntity.getShortName());

                        session.save(dataEntity);
                        t.commit();
                        session.close();
                        array.add(json.getInt("id"));

                    } else if (action == 3) {
                        System.out.println("Удалить");

                        Session session = Properties.sessionFactory.openSession();

                        DataEntity dataEntity = session.createQuery("SELECT a FROM DataEntity a WHERE a.id = "+json.getInt("product_id")+"", DataEntity.class).getSingleResult();
                        Transaction t = session.beginTransaction();
                        dataEntity.setLive(false);
                        session.save(dataEntity);
                        t.commit();
                        session.close();
                        array.add(json.getInt("id"));

                    }
                }

                HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

                try {

                    HttpPost request = new HttpPost(urlAnswer);

                    String param = "[";
                    for(int i = 0; i < array.size(); i++){
                        param = param + "{\"id\":"+array.get(i)+"}";
                        if(i < array.size()-1){
                            param += ",";
                        }
                    }
                    param += "]";
                    StringEntity params =new StringEntity(param);
                    //request.addHeader("content-type", "application/json");
                    params.setContentType("application/json");
                    request.setEntity(params);
                    HttpResponse response2 = httpClient.execute(request);
                    System.out.println(response2);
                    System.out.println(params);

                    generate();

                    return true;
                    //handle response here...

                }catch (Exception ex) {

                    //handle exception here

                } finally {
                    //Deprecated
                    //httpClient.getConnectionManager().shutdown();
                }


            } else {
                System.out.println("Ошибка! Проблемы на сервере!");
            }

        } catch (Exception e) {
            System.out.println("Ошибка обновления данных!");
            System.out.println(e.toString());
        }
        // Распарсить ответ
        return false;
    }
}
