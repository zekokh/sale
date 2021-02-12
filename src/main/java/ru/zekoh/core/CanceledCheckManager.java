package ru.zekoh.core;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.Goods;
import ru.zekoh.properties.Properties;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.zekoh.properties.Properties.loyalty_url;

public class CanceledCheckManager {

    public static void send(CheckObject check) {
        List<Goods> goodList = convertGoodsInCheck(check);

        // Формируем JSON
        JSONObject json = new JSONObject();
        json.put("bakery_id", Properties.bakaryId);
        json.put("bakery_name", Properties.bakaryAddress);
        Date date = new Date();
        String currentDate = "" + date.getTime() / 1000 + "";
        json.put("date", currentDate);

        JSONArray goods = new JSONArray();
        for (int i = 0; i < goodList.size(); i++) {
            JSONObject good = new JSONObject();
            good.put("good_id", goodList.get(i).getProductId());
            good.put("name", goodList.get(i).getProductName());
            good.put("quantity", goodList.get(i).getCount());
            good.put("price", goodList.get(i).getPriceFromThePriceList());
            good.put("price_after_discount", goodList.get(i).getPriceAfterDiscount());
            good.put("total", goodList.get(i).getSellingPrice());
            goods.put(good);
        }
        json.put("goods", goods);
        System.out.println("JSON тут:");
        System.out.println(json.toString());

        // Если это не локальная сборка для теста отправляем на сервак для генерации email уведомлений
        if (Properties.bakaryId != 8) {

            // Отправка данных на сервер
            HttpClient httpClient = HttpClientBuilder.create().build();

            try {
                String url = "http://5.63.159.130/check/cancel";
                HttpPost request = new HttpPost(url);
                StringEntity params = new StringEntity(json.toString(), "UTF-8");
                request.addHeader("content-type", "application/json;");
                request.setEntity(params);
                org.apache.http.HttpResponse response = httpClient.execute(request);
                //logger.info("Статус отправки данных о чеке на сервер системы лояльности: " + response.getStatusLine().getStatusCode());


            } catch (Exception ex) {
//            logger.error("Отправка данных на сервер системы лояльности не произведена! \n" + ex.getMessage());
                //handle exception here

            } finally {
                //Deprecated
                //httpClient.getConnectionManager().shutdown();
            }
        }
    }

    private static List<Goods> convertGoodsInCheck(CheckObject checkObject) {
        List<Goods> goods = new ArrayList<Goods>();
        List<Goods> goodsFromCheck = checkObject.getGoodsList();
        if (goodsFromCheck.size() > 0) {
            goods.add(goodsFromCheck.get(0));
            for (int i = 1; i < goodsFromCheck.size(); i++) {
                Goods good = check(goodsFromCheck.get(i), goods);
                if (good != null) {
                    // Обновить количество товара в массиве
                    Double quantity = good.getCount() + goodsFromCheck.get(i).getCount();
                    good.setCount(quantity);
                    Double total = good.getPriceAfterDiscount() * good.getCount();
                    total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    good.setSellingPrice(total);
                } else {
                    // Добавить товар в массив
                    goods.add(goodsFromCheck.get(i));
                }
            }
        }

        return goods;
    }

    private static Goods check(Goods good, List<Goods> goods) {
        boolean flag = false;
        // Проверить есть ли такой продукт в списке
        for (int x = 0; x < goods.size(); x++) {
            if (goods.get(x).getProductId() == good.getProductId() && areEqualDouble(goods.get(x).getPriceAfterDiscount(), good.getPriceAfterDiscount(), 2)) {
                return goods.get(x);
            }
        }
        return null;
    }

    public static boolean areEqualDouble(double a, double b, int precision) {
        return Math.abs(a - b) <= Math.pow(10, -precision);
    }
}
