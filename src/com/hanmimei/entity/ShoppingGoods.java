package com.hanmimei.entity;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "SHOPPING_GOODS".
 */
public class ShoppingGoods {

    private Long id;
    private Integer cartId;
    private Integer goodsId;
    private String goodsImg;
    private String goodsUrl;
    private String goodsName;
    private Integer goodsPrice;
    private Boolean isChecked;
    private String state;
    private Integer goodsNums;
    private String itemColor;
    private String itemSize;
    private Integer shipFee;
    private String delUrl;
    private String invArea;
    private Integer restrictAmount;
    private Integer restAmount;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ShoppingGoods() {
    }

    public ShoppingGoods(Long id) {
        this.id = id;
    }
    public ShoppingGoods(Integer cartId, Integer goodsId, String state, Integer goodsNums) {
        this.cartId = cartId;
        this.goodsId = goodsId;
        this.state = state;
        this.goodsNums = goodsNums;
    }
    public ShoppingGoods(Long id, Integer cartId, Integer goodsId, String goodsImg, String goodsUrl, String goodsName, Integer goodsPrice, Boolean isChecked, String state, Integer goodsNums, String itemColor, String itemSize, Integer shipFee, String delUrl, String invArea, Integer restrictAmount, Integer restAmount) {
        this.id = id;
        this.cartId = cartId;
        this.goodsId = goodsId;
        this.goodsImg = goodsImg;
        this.goodsUrl = goodsUrl;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.isChecked = isChecked;
        this.state = state;
        this.goodsNums = goodsNums;
        this.itemColor = itemColor;
        this.itemSize = itemSize;
        this.shipFee = shipFee;
        this.delUrl = delUrl;
        this.invArea = invArea;
        this.restrictAmount = restrictAmount;
        this.restAmount = restAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Integer goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getGoodsNums() {
        return goodsNums;
    }

    public void setGoodsNums(Integer goodsNums) {
        this.goodsNums = goodsNums;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public Integer getShipFee() {
        return shipFee;
    }

    public void setShipFee(Integer shipFee) {
        this.shipFee = shipFee;
    }

    public String getDelUrl() {
        return delUrl;
    }

    public void setDelUrl(String delUrl) {
        this.delUrl = delUrl;
    }

    public String getInvArea() {
        return invArea;
    }

    public void setInvArea(String invArea) {
        this.invArea = invArea;
    }

    public Integer getRestrictAmount() {
        return restrictAmount;
    }

    public void setRestrictAmount(Integer restrictAmount) {
        this.restrictAmount = restrictAmount;
    }

    public Integer getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(Integer restAmount) {
        this.restAmount = restAmount;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
