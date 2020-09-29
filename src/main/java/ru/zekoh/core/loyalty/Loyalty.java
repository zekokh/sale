package ru.zekoh.core.loyalty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.stjs.javascript.dom.Pre;
import ru.zekoh.controller.Sale;
import ru.zekoh.core.privilege.bonuses.Bonus;
import ru.zekoh.core.privilege.bonuses.GiftBonuses;
import ru.zekoh.db.entity.Present;
import ru.zekoh.properties.Properties;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Loyalty {

    private static Logger logger = LogManager.getLogger(Loyalty.class);

    public static LoyaltyCard searchByNumber(String number) {
        String url = Properties.loyaltyUrl + number;
        LoyaltyCard loyaltyCard = null;
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
                    CustomerCard customer = new CustomerCard(json.getLong("id"),
                            json.getString("mail"),
                            json.getString("name"),
                            json.getDouble("balance"),
                            json.getInt("level"),
                            json.getBoolean("exist_presents")
                    );

                    if (customer.isExistPresents()) {
                        // Если есть подарки добавляем их к клиенту
                        List<Present> presentList = new ArrayList<Present>();
                        JSONArray presentsJSON = new JSONArray(json.getString("presents"));
                        if (presentsJSON.length() > 0) {
                            for (int i = 0; i < presentsJSON.length(); i++) {
                                JSONObject presentJSON = (JSONObject) presentsJSON.get(0);
                                switch (presentJSON.getInt("type_present")) {
                                    case (1):
                                        // Оплата всего чека бонусами
                                        Bonus bonus = new GiftBonuses(presentJSON.getDouble("value"),
                                                presentJSON.getInt("date_limit_unix"),
                                                presentJSON.getString("description"));
                                        //customer
                                        break;
                                    case (2):
                                        // Продукт в подарок
                                        break;
                                }

                                Present present = new Present();
                                present.setId(presentJSON.getInt("id"));
                                present.setCustomer_id(presentJSON.getInt("customer_id"));
                                present.setType_present(presentJSON.getInt("type_present"));
                                present.setName_type_present(presentJSON.getString("name_type_present"));
                                present.setValue(presentJSON.getString("value"));
                                present.setDescription(presentJSON.getString("description"));
                                present.setDate_limit_unix(presentJSON.getInt("date_limit_unix"));
                                presentList.add(present);
                            }
                            customer.setPresents(presentList);
                        } else {
                            customer.setExistPresents(false);
                        }
                    }
                    loyaltyCard = customer;
                } else if (customerTypeId == 2) {
                    StoreCard employee = new Employee(json.getLong("id"),
                            json.getString("name"),
                            json.getDouble("balance"),
                            json.getDouble("discount"),
                            json.getDouble("limit"),
                            json.getString("grace_period")
                    );
                   // loyaltyCard = employee;
                    loyaltyCard = null;
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
        return loyaltyCard;
    }
}
