package ru.zekoh.core;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import ru.zekoh.core.socket.Check;
import ru.zekoh.core.socket.Good;
import ru.zekoh.core.socket.Greeting;
import ru.zekoh.core.socket.HelloMessage;
import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.Goods;
import ru.zekoh.properties.Properties;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerDisplay {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void initData(){

        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());

        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session;
        try {
            session = stompClient
                    .connect(
                            "ws://localhost:8080/websocket",
                            new StompSessionHandlerAdapter() {})
                    .get();

            Properties.stompSessionGlobal = session;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void convert(CheckObject check) {
        List<Goods> goodList = convertGoodsInCheck(check);

        Check checkSocket = new Check();
        checkSocket.setSellingPrice(check.getSellingPrice().toString());
        Double discount = check.getAmountByPrice() -  check.getSellingPrice();
        checkSocket.setDiscount(discount.toString());
        checkSocket.setPriceWithoutDiscounts(check.getAmountByPrice().toString());
        List<Good> goods = new ArrayList<Good>();
        for (int i = 0; i < goodList.size(); i++) {
            Good good = new Good(goodList.get(i).getProductName(),
                    goodList.get(i).getCount().toString(),
                    goodList.get(i).getPriceAfterDiscount().toString(),
                    goodList.get(i).getSellingPrice().toString());
            goods.add(good);
        }
        checkSocket.setGoods(goods);

        if (Properties.bakaryId == 8) {
            Properties.stompSessionGlobal.send("/v1/update", checkSocket);
        }

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

    private static List<Goods> convertGoodsInCheck(CheckObject checkObject) {
        List<Goods> goods = new ArrayList<Goods>();
        List<Goods> goodsFromCheck = checkObject.getGoodsList();
        if (goodsFromCheck.size() > 0) {
            Goods newGood = new Goods(goodsFromCheck.get(0));
            goods.add(newGood);
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
                    Goods newGood1 = new Goods(goodsFromCheck.get(i));
                    goods.add(newGood1);
                }
            }
        }

        return goods;
    }
}
