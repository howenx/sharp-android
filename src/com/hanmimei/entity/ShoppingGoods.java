package com.hanmimei.entity;

import java.io.Serializable;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "SHOPPING_GOODS".
 */
public class ShoppingGoods implements Serializable{

    private Long id;
    private String cartId;
    private String goodsId;
    private String goodsImg;
    private String goodsUrl;
    private String goodsName;
    private Double goodsPrice;
    private Boolean isChecked;
    private String state="G";
    private Integer goodsNums;
    private String itemColor;
    private String itemSize;
    private String shipFee;
    private String delUrl;
    private String invArea;
    private Integer restrictAmount;
    private Integer restAmount;
    private String postalTaxRate;
    private Integer postalStandard;
    private Integer postalLimit;
    private String invCustoms;
    private String skuType = "item";
    private String skuTypeId;
    private String pinTieredPriceId;
    private String invAreaNm;
    private Double poastalFee;
    private String orCheck;  //提交勾选 'Y'为提交勾选,'N'为提交取消勾选

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ShoppingGoods() {
    }

    public ShoppingGoods(Long id) {
        this.id = id;
    }

    public ShoppingGoods(Long id, String cartId, String goodsId, String goodsImg, String goodsUrl, String goodsName, Double goodsPrice, Boolean isChecked, String state, Integer goodsNums, String itemColor, String itemSize, String shipFee, String delUrl, String invArea, Integer restrictAmount, Integer restAmount, String postalTaxRate, Integer postalStandard, Integer postalLimit, String invCustoms, String skuType, String skuTypeId, String pinTieredPriceId, String invAreaNm, Double poastalFee, String orCheck) {
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
        this.postalTaxRate = postalTaxRate;
        this.postalStandard = postalStandard;
        this.postalLimit = postalLimit;
        this.invCustoms = invCustoms;
        this.skuType = skuType;
        this.skuTypeId = skuTypeId;
        this.pinTieredPriceId = pinTieredPriceId;
        this.invAreaNm = invAreaNm;
        this.poastalFee = poastalFee;
        this.orCheck = orCheck;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
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

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
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

    public String getShipFee() {
        return shipFee;
    }

    public void setShipFee(String shipFee) {
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

    public String getPostalTaxRate() {
        return postalTaxRate;
    }

    public void setPostalTaxRate(String postalTaxRate) {
        this.postalTaxRate = postalTaxRate;
    }

    public Integer getPostalStandard() {
        return postalStandard;
    }

    public void setPostalStandard(Integer postalStandard) {
        this.postalStandard = postalStandard;
    }

    public Integer getPostalLimit() {
        return postalLimit;
    }

    public void setPostalLimit(Integer postalLimit) {
        this.postalLimit = postalLimit;
    }

    public String getInvCustoms() {
        return invCustoms;
    }

    public void setInvCustoms(String invCustoms) {
        this.invCustoms = invCustoms;
    }

    public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }

    public String getSkuTypeId() {
        return skuTypeId;
    }

    public void setSkuTypeId(String skuTypeId) {
        this.skuTypeId = skuTypeId;
    }

    public String getPinTieredPriceId() {
        return pinTieredPriceId;
    }

    public void setPinTieredPriceId(String pinTieredPriceId) {
        this.pinTieredPriceId = pinTieredPriceId;
    }

    public String getInvAreaNm() {
        return invAreaNm;
    }

    public void setInvAreaNm(String invAreaNm) {
        this.invAreaNm = invAreaNm;
    }

    public Double getPoastalFee() {
        return poastalFee;
    }

    public void setPoastalFee(Double poastalFee) {
        this.poastalFee = poastalFee;
    }

    public String getOrCheck() {
        return orCheck;
    }

    public void setOrCheck(String orCheck) {
        this.orCheck = orCheck;
    }
    
    public double getPostalTaxRate_() {
		if (postalTaxRate != null)
			return Double.valueOf(postalTaxRate);
		return 0;
	}

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
