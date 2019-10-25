package ru.zekoh.core.loyalty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Loyalty {
    public static StoreCard searchByNumber(String number) {
        String url = "https://loyalty.jacq.ru/customer/search/"+number;
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
        }catch (Exception e) {

        }
        // Нет ошибок, возвращаю сущность

        // Ошибка
        return storeCard;
    }
}
