package com.kakao.kakaogift.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StockVo implements Serializable {

	// 普通商品属性
	private String id;
	private String itemColor;
	private String itemSize;
	private BigDecimal itemSrcPrice;
	private BigDecimal itemPrice;
	private BigDecimal itemDiscount;
	private Boolean orMasterInv;
	private String state; // 状态  'Y'--正常,'D'--下架,'N'--删除,'K'--售空，'P'--预售
	private BigDecimal shipFee;
	private String invArea;
	private String invAreaNm;
	private Integer restrictAmount; // 每个ID限购数量
	private Integer restAmount;
	private String invImg;
	private String itemPreviewImgs;
	private String invUrl;
	private String invTitle;
	private String invCustoms;
	private String postalTaxRate;
	private Integer postalStandard;// 关税收费标准
	// 拼购商品属性
	private String pinTitle; // 拼购商品标题
	private String startAt; // 开始时间
	private String endAt;// 结束时间
	private List<PinTieredPrice> pinTieredPrices; // 价格阶梯
	private String floorPrice; // 拼购最低价
	private String pinDiscount;// 拼购最低折扣
	private String pinRedirectUrl;// 拼购跳转链接
	private String invWeight;// 商品重量单位g
	private String browseCount;// 浏览次数
	private String collectCount;// 收藏数
	private String pinId; // 拼购ID
	private String shareUrl; // 分享短连接
	private String status;// 状态  'Y'--正常,'D'--下架,'N'--删除,'K'--售空，'P'--预售
	private BigDecimal invPrice; // 商品原价
	private long collectId;

	private String soldAmount;// 已售数量

	// 共有属性
	private String skuType;// 商品类型 1.vary,2.item,3.customize,4.pin
	private String skuTypeId;// 商品类型所对应的ID

	public String getSkuType() {
		return skuType;
	}

	public void setSkuType(String skuType) {
		this.skuType = skuType;
	}

	public long getCollectId() {
		return collectId;
	}

	public void setCollectId(long collectId) {
		this.collectId = collectId;
	}

	public String getSkuTypeId() {
		return skuTypeId;
	}

	public void setSkuTypeId(String skuTypeId) {
		this.skuTypeId = skuTypeId;
	}

	public BigDecimal getInvPrice() {
		return invPrice;
	}

	public void setInvPrice(BigDecimal invPrice) {
		this.invPrice = invPrice;
	}

	public String getSoldAmount() {
		return soldAmount;
	}

	public void setSoldAmount(String soldAmount) {
		this.soldAmount = soldAmount;
	}

	public String getPinTitle() {
		return pinTitle;
	}

	public void setPinTitle(String pinTitle) {
		this.pinTitle = pinTitle;
	}

	public String getStartAt() {
		return startAt;
	}

	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}

	public String getEndAt() {
		return endAt;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}

	public void setFloorPrice(String floorPrice) {
		this.floorPrice = floorPrice;
	}

	public String getPinDiscount() {
		return pinDiscount;
	}

	public void setPinDiscount(String pinDiscount) {
		this.pinDiscount = pinDiscount;
	}

	public List<PinTieredPrice> getPinTieredPrices() {
		Collections.sort(pinTieredPrices, new Comparator<PinTieredPrice>() {
			public int compare(PinTieredPrice arg0, PinTieredPrice arg1) {
				return arg1.getPeopleNum().compareTo(arg0.getPeopleNum());
			}
		});
		return pinTieredPrices;
	}

	public List<PinTieredPrice> getPinTieredPricesDatas() {
		List<PinTieredPrice> list = new ArrayList<PinTieredPrice>();
		for (PinTieredPrice p : getPinTieredPrices()) {
			if (p.getPeopleNum() == 1)
				continue;
			list.add(p);
		}
		return list;
	}

	public void setPinTieredPrices(List<PinTieredPrice> pinTieredPrices) {
		this.pinTieredPrices = pinTieredPrices;
	}

	public Map<String, String> getFloorPrice() {
		return new Gson().fromJson(floorPrice,
				new TypeToken<Map<String, String>>() {
				}.getType());
	}

	public String getPinRedirectUrl() {
		return pinRedirectUrl;
	}

	public void setPinRedirectUrl(String pinRedirectUrl) {
		this.pinRedirectUrl = pinRedirectUrl;
	}

	public String getInvWeight() {
		return invWeight;
	}

	public void setInvWeight(String invWeight) {
		this.invWeight = invWeight;
	}

	public String getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(String browseCount) {
		this.browseCount = browseCount;
	}

	public String getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(String collectCount) {
		this.collectCount = collectCount;
	}

	public String getPinId() {
		return pinId;
	}

	public void setPinId(String pinId) {
		this.pinId = pinId;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getStatus() {
		return status;
	}
	public String getStatus_values() {
		String values = null;
		if(status.equals("Y")){
			values = "销售中";
		}else if(status.equals("D")){
			values = "已下架";
		}else if(status.equals("N")){
			values = "已删除";
		}else if(status.equals("K")){
			values = "已抢光";
		}else if(status.equals("P")){
			values = "未开售";
		}
		return values;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ImageVo getInvImgForObj() {
		return new Gson().fromJson(invImg, ImageVo.class);
	}

	public String getInvImg() {
		return invImg;
	}

	public ArrayList<ImageVo> getItemPreviewImgsForList() {
		return new Gson().fromJson(itemPreviewImgs,
				new TypeToken<ArrayList<ImageVo>>() {
				}.getType());
	}

	public String getItemPreviewImgs() {
		return itemPreviewImgs;
	}

	public String getInvAreaNm() {
		return invAreaNm;
	}

	public void setInvAreaNm(String invAreaNm) {
		this.invAreaNm = invAreaNm;
	}

	public Integer getPostalStandard() {
		return postalStandard;
	}

	public void setPostalStandard(Integer postalStandard) {
		this.postalStandard = postalStandard;
	}

	public String getInvCustoms() {
		return invCustoms;
	}

	public void setInvCustoms(String invCustoms) {
		this.invCustoms = invCustoms;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Boolean getOrMasterInv() {
		return orMasterInv;
	}

	public void setOrMasterInv(Boolean orMasterInv) {
		this.orMasterInv = orMasterInv;
	}

	public String getState() {
		return state;
	}
	public String getState_values() {
		String values = null;
		if(state.equals("Y")){
			values = "销售中";
		}else if(state.equals("D")){
			values = "已下架";
		}else if(state.equals("N")){
			values = "已删除";
		}else if(state.equals("K")){
			values = "已抢光";
		}else if(state.equals("P")){
			values = "未开售";
		}
		return values;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getInvArea() {
		return invArea;
	}

	public void setInvArea(String invArea) {
		this.invArea = invArea;
	}

	public void setInvImg(String invImg) {
		this.invImg = invImg;
	}

	public void setItemPreviewImgs(String itemPreviewImgs) {
		this.itemPreviewImgs = itemPreviewImgs;
	}

	public String getInvUrl() {
		return invUrl;
	}

	public void setInvUrl(String invUrl) {
		this.invUrl = invUrl;
	}

	public String getInvTitle() {
		return invTitle;
	}

	public void setInvTitle(String invTitle) {
		this.invTitle = invTitle;
	}

	public BigDecimal getItemSrcPrice() {
		return itemSrcPrice;
	}

	public void setItemSrcPrice(BigDecimal itemSrcPrice) {
		this.itemSrcPrice = itemSrcPrice;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public BigDecimal getItemDiscount() {
		return itemDiscount;
	}

	public void setItemDiscount(BigDecimal itemDiscount) {
		this.itemDiscount = itemDiscount;
	}

	public BigDecimal getShipFee() {
		return shipFee;
	}

	public void setShipFee(BigDecimal shipFee) {
		this.shipFee = shipFee;
	}

	public double getPostalTaxRate_() {
		if (postalTaxRate != null)
			return Double.valueOf(postalTaxRate);
		return 0;
	}

	public String getPostalTaxRate() {
		return postalTaxRate;
	}

	public void setPostalTaxRate(String postalTaxRate) {
		this.postalTaxRate = postalTaxRate;
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

	public PinTieredPrice getOnePersonPinTieredPrice() {
		for (PinTieredPrice p : getPinTieredPrices()) {
			if (p.getPeopleNum() == 1)
				return p;
		}
		return null;
	}

}
