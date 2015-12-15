package com.hanmimei.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Customs implements Serializable{
	private String invArea;
	private String  invCustoms;
	private List<ShoppingGoods> list;
	
	private int postFee;
	private double tax;
	private int postalLimit;
	private int postalStandard;
	
	private double portalSingleCustomsFee = 0.00;
	private double factSingleCustomsShipFee =0.00;
	private double shipSingleCustomsFee= 0.00;
	private double factPortalFeeSingleCustoms= 0.00;
	
	
	public int getPostFee() {
		return postFee;
	}
	public Double getPortalSingleCustomsFee() {
		return portalSingleCustomsFee;
	}
	public String getPortalSingleCustomsFeeFormat() {
		return new DecimalFormat("##0.00").format(portalSingleCustomsFee);
	}
	public void setPortalSingleCustomsFee(Double portalSingleCustomsFee) {
		this.portalSingleCustomsFee = portalSingleCustomsFee;
	}
	public Double getFactSingleCustomsShipFee() {
		return factSingleCustomsShipFee;
	}
	public String getFactSingleCustomsShipFeeFormat() {
		return new DecimalFormat("##0.00").format(factSingleCustomsShipFee);
	}
	public void setFactSingleCustomsShipFee(Double factSingleCustomsShipFee) {
		this.factSingleCustomsShipFee = factSingleCustomsShipFee;
	}
	public Double getShipSingleCustomsFee() {
		return shipSingleCustomsFee;
	}
	public void setShipSingleCustomsFee(Double shipSingleCustomsFee) {
		this.shipSingleCustomsFee = shipSingleCustomsFee;
	}
	public Double getFactPortalFeeSingleCustoms() {
		return factPortalFeeSingleCustoms;
	}
	public String getFactPortalFeeSingleCustomsFormat() {
		return new DecimalFormat("##0.00").format(factPortalFeeSingleCustoms);
	}
	public void setFactPortalFeeSingleCustoms(Double factPortalFeeSingleCustoms) {
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
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
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
	
	public Customs(String invArea, String invCustoms, List<ShoppingGoods> list) {
		super();
		this.invArea = invArea;
		this.invCustoms = invCustoms;
		this.list = list;
	
	}
	public Customs() {
		super();
	}
	
	
	public Double getAllPrice() {
		Double allPrice = 0.00;
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
