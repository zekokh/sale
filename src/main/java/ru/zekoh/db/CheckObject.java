package ru.zekoh.db;

import ru.zekoh.core.loyalty.Loyalty;
import ru.zekoh.core.loyalty.LoyaltyCard;
import ru.zekoh.core.privilege.discount.discountProgram.*;
import ru.zekoh.core.privilege.bonuses.Bonus;
import ru.zekoh.db.entity.Discount;
import ru.zekoh.db.entity.Goods;
import ru.zekoh.properties.Properties;

import java.util.ArrayList;
import java.util.List;

public class CheckObject {

    //Список товаров
    private List<Goods> goodsList = new ArrayList<Goods>();

    //Итоговая стоимость за весь чек с учетом скидок
    private Double sellingPrice = 0.0;

    //Сумма чека по прайсу
    private Double amountByPrice = 0.0;

    //Сумма оплаченная бонусами
    private Double amountBonus = 0.0;

    // Чек оплаичается частью бонусов
    private boolean payWithEarnedBonuses = false;

    // Тип бонусов
    // 1 - стандартные (можно оплатить до 30% продукции собственного производства)
    // 2 - подарочные (можно оплатить всю покупку)
    private Bonus bonus;

    //Статус оплаты
    private boolean paymentState = false;

    //Вид оплаты (Наличка (1), безналичный(2), промоушен(3), бонусами(4))
    private int typeOfPayment;

    //Дата создания чека (фиксируем дату создания когда в чеке появляетя первый товар)
    private String dateOfCreation;

    //Дата закрытие чека
    private String dateOfClosing;

    // Статус жизни чека
    private boolean live = true;

    // Статус отображения окна для ввода скидок
    private boolean panelForFindDiscountCard = false;

    // Скидка по карте
    private boolean discountAccountExist = false;

    // Скидка по карте
    private boolean discountAppExist = false;

    // Скидка
    private DiscountProgram discountProgram = null;

    // Блокировка продажи
    private boolean blockForSale = false;

    // Сохранен в базе данных
    private boolean saveInDB = false;

    // На чек действуют внутреннии скидки
    private boolean internalDiscountApplyToCheck = true;

    // Карта лояльности
    private LoyaltyCard loyaltyCard;

    // Полуить сумму стоимости товаров собственного производства
    public Double getAmountPriceOfGoodsOfOwnProduction() {
        Double amountPriceOfGoodsOfOwnProduction = 0.0;
        for (Goods good : goodsList) {
            // Если товар принимает участие в акциях и его можно оплатить бонусами
            if (good.isParticipatesInpromotions() && !good.isGift()) {
                amountPriceOfGoodsOfOwnProduction += good.getSellingPrice();
            }
        }
        return amountPriceOfGoodsOfOwnProduction;
    }

    public Double getAmountWichPayBonus30percent() {
        Double balance = loyaltyCard.getBalance();
        Double result = getAmountPriceOfGoodsOfOwnProduction();
        if (getAmountPriceOfGoodsOfOwnProduction() >= balance) {
            result = balance;
        }
        return result;
    }

    // Вче товары собственного производста
    // на которые можно делать скидки и оплачивать их бонусами
    public List<Goods> getGoodsOfOwnProduction() {
        // Список продуктов собственного производства на которые можно делать скидки и оплачивать бонусы
        List<Goods> goodsOfOwnProduction = new ArrayList<Goods>();
        for (Goods good : goodsList) {
            // Если товар принимает участие в акциях и его можно оплатить бонусами
            if (good.isParticipatesInpromotions() && !good.isGift()) {
                goodsOfOwnProduction.add(good);
            }
        }
        return goodsOfOwnProduction;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getAmountByPrice() {
        return amountByPrice;
    }

    public void setAmountByPrice(Double amountByPrice) {
        this.amountByPrice = amountByPrice;
    }

    public boolean isPaymentState() {
        return paymentState;
    }

    public void setPaymentState(boolean paymentState) {
        this.paymentState = paymentState;
    }

    public int getTypeOfPayment() {
        return typeOfPayment;
    }

    public void setTypeOfPayment(int typeOfPayment) {
        this.typeOfPayment = typeOfPayment;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getDateOfClosing() {
        return dateOfClosing;
    }

    public void setDateOfClosing(String dateOfClosing) {
        this.dateOfClosing = dateOfClosing;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public Double getAmountBonus() {
        return amountBonus;
    }

    public void setAmountBonus(Double amountBonus) {
        this.amountBonus = amountBonus;
    }

    public boolean isPanelForFindDiscountCard() {
        return panelForFindDiscountCard;
    }

    public void setPanelForFindDiscountCard(boolean panelForFindDiscountCard) {
        this.panelForFindDiscountCard = panelForFindDiscountCard;
    }

    public boolean isDiscountAccountExist() {
        return discountAccountExist;
    }

    public void setDiscountAccountExist(boolean discountAccountExist) {
        this.discountAccountExist = discountAccountExist;
    }

    public boolean isBlockForSale() {
        return blockForSale;
    }

    public void setBlockForSale(boolean blockForSale) {
        this.blockForSale = blockForSale;
    }

    public boolean isDiscountAppExist() {
        return discountAppExist;
    }

    public void setDiscountAppExist(boolean discountAppExist) {
        this.discountAppExist = discountAppExist;
    }

    public boolean isSaveInDB() {
        return saveInDB;
    }

    public void setSaveInDB(boolean saveInDB) {
        this.saveInDB = saveInDB;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    private List<DiscountProgram> discountProgramList;

    public List<DiscountProgram> getDiscountProgramList() {
        return discountProgramList;
    }

    public void setDiscountProgramList(List<DiscountProgram> discountProgramList) {
        this.discountProgramList = discountProgramList;
    }

    public boolean isInternalDiscountApplyToCheck() {
        return internalDiscountApplyToCheck;
    }

    public void setInternalDiscountApplyToCheck(boolean internalDiscountApplyToCheck) {
        this.internalDiscountApplyToCheck = internalDiscountApplyToCheck;
    }

    public DiscountProgram getDiscountProgram() {
        return discountProgram;
    }

    public void setDiscountProgram(DiscountProgram discountProgram) {
        this.discountProgram = discountProgram;
    }

    public LoyaltyCard getLoyaltyCard() {
        return loyaltyCard;
    }

    public void setLoyaltyCard(LoyaltyCard loyaltyCard) {
        this.loyaltyCard = loyaltyCard;
    }

    public boolean isPayWithEarnedBonuses() {
        return payWithEarnedBonuses;
    }

    public void setPayWithEarnedBonuses(boolean payWithEarnedBonuses) {
        this.payWithEarnedBonuses = payWithEarnedBonuses;
    }
}
