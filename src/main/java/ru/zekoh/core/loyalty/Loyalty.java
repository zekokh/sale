package ru.zekoh.core.loyalty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.zekoh.controller.Sale;
import ru.zekoh.properties.Properties;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Loyalty {

    private static Logger logger = LogManager.getLogger(Loyalty.class);

    public static StoreCard searchByNumber(String number) {
        String url = Properties.loyaltyUrl + number;
        StoreCard storeCard = null;
        // Запрашиваю на сервере информацию
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
                JSONObject json = new JSONObject(response.toString());
                int customerTypeId = json.getInt("customer_type_id");

                if (customerTypeId == 1) {
                    StoreCard customer = new Customer(json.getLong("id"),
                            json.getString("mail"),
                            json.getString("name"),
                            json.getDouble("balance"),
                            json.getDouble("discount"),
                            json.getString("grace_period"),
                            json.getInt("level")
                    );
                    storeCard = customer;
                } else if (customerTypeId == 2) {
                    StoreCard employee = new Employee(json.getLong("id"),
                            json.getString("name"),
                            json.getDouble("balance"),
                            json.getDouble("discount"),
                            json.getDouble("limit"),
                            json.getString("grace_period")
                    );
                    storeCard = employee;
                } else {

                }
            } else {

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            System.out.println("Ошибка при запросе");
            System.out.println(e.getMessage());
        }
        // Нет ошибок, возвращаю сущность

        // Ошибка
        return storeCard;
    }
}
