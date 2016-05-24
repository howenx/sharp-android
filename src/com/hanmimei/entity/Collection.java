package com.hanmimei.entity;
/**
 * 收藏商品数据
 * @author vince
 *
 */
public class Collection {
	private String collectId;
	private long createAt;
	private String skuType;
	private String skuTypeId;
	private Sku sku;
	public String getCollectId() {
		return collectId;
	}
	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}
	public long getCreateAt() {
		return createAt;
	}
	public void setCreateAt(long createAt) {
		this.createAt = createAt;
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
	public Sku getSku() {
		return sku;
	}
	public void setSku(Sku sku) {
		this.sku = sku;
	}
	public Collection() {
		super();
	}
	public Collection(String collectId, long createAt, String skuType,
			String skuTypeId, Sku sku) {
		super();
		this.collectId = collectId;
		this.createAt = createAt;
		this.skuType = skuType;
		this.skuTypeId = skuTypeId;
		this.sku = sku;
	}
	
}
