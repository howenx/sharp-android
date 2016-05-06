package com.hanmimei.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class CustomsVo implements Serializable{
	private String invArea;
	private String  invCustoms;
	private String invAreaNm;
	private List<ShoppingGoods> list;
	private String state;
	private int postFee;
	private String tax;
	private int postalLimit;
	private int postalStandard;
	
	private BigDecimal portalSingleCustomsFee = BigDecimal.ZERO;
	private BigDecimal factSingleCustomsShipFee = BigDecimal.ZERO;
	private BigDecimal shipSingleCustomsFee=  BigDecimal.ZERO;
	private BigDecimal factPortalFeeSingleCustoms=  BigDecimal.ZERO;
	
	
	public String getInvAreaNm() {
		return invAreaNm;
	}
	public void setInvAreaNm(String invAreaNm) {
		this.invAreaNm = invAreaNm;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getPostFee() {
		return postFee;
	}
	
	public BigDecimal getPortalSingleCustomsFee() {
		return portalSingleCustomsFee;
	}
	public void setPortalSingleCustomsFee(BigDecimal portalSingleCustomsFee) {
		this.portalSingleCustomsFee = portalSingleCustomsFee;
	}
	public BigDecimal getFactSingleCustomsShipFee() {
		return factSingleCustomsShipFee;
	}
	public void setFactSingleCustomsShipFee(BigDecimal factSingleCustomsShipFee) {
		this.factSingleCustomsShipFee = factSingleCustomsShipFee;
	}
	public BigDecimal getShipSingleCustomsFee() {
		return shipSingleCustomsFee;
	}
	public void setShipSingleCustomsFee(BigDecimal shipSingleCustomsFee) {
		this.shipSingleCustomsFee = shipSingleCustomsFee;
	}
	public BigDecimal getFactPortalFeeSingleCustoms() {
		return factPortalFeeSingleCustoms;
	}
	public void setFactPortalFeeSingleCustoms(BigDecimal factPortalFeeSingleCustoms) {
		this.factPortalFeeSingleCustoms = factPortalFeeSingleCustoms;
	}
	public void setPostFee(int postFee) {
		this.postFee = postFee;
	}
	public int getPostalLimit() {
		return postalLimit;
	}
	public void setPostalLimit(int postalLimit) {
		this.postalLimit = postalLimit;
	}
	public int getPostalStandard() {
		return postalStandard;
	}
	public void setPostalStandard(int postalStandard) {
		this.postalStandard = postalStandard;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getInvArea() {
		return invArea;
	}
	public void setInvArea(String invArea) {
		this.invArea = invArea;
	}
	public String getInvCustoms() {
		return invCustoms;
	}
	public void setInvCustoms(String invCustoms) {
		this.invCustoms = invCustoms;
	}
	public List<ShoppingGoods> getList() {
		return list;
	}
	public void setList(List<ShoppingGoods> list) {
		this.list = list;
	}
	
	public CustomsVo(String invArea, String invCustoms, List<ShoppingGoods> list) {
		super();
		this.invArea = invArea;
		this.invCustoms = invCustoms;
		this.list = list;
	
	}
	public CustomsVo() {
		super();
	}
	
	
	public Double getAllPrice() {
		Double allPrice = 0.0;
		for(ShoppingGoods sg : list){
			allPrice += sg.getGoodsPrice() *sg.getGoodsNums();
		}
		return allPrice;
	}
	public void addShoppingGoods(ShoppingGoods shoppingGoods){
		if(list == null)
			list = new ArrayList<ShoppingGoods>();
		list.add(shoppingGoods);
	}
	
	public Double getAllPoastalFee(){
		Double postFee = 0.00;
		for(ShoppingGoods sg : list){
			Log.i(sg.getGoodsName(), postFee+"");
			postFee += sg.getPoastalFee();
		}
		return postFee;
	}

}
