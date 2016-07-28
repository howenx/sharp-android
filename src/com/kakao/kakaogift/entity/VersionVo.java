package com.kakao.kakaogift.entity;

/**
 * 版本 Created by howen on 16/1/29.
 */
public class VersionVo {

	public final static int UPDATA_NONEED = 0;
	public final static int UPDATA_CLIENT = 1;
	public final static int GET_UNDATAINFO_ERROR = 2;
	// private final int SDCARD_NOMOUNTED = 3;
	public final static int DOWN_ERROR = 4;

	private Long id;
	private String releaseNumber;
	private String productType;
	private String downloadLink;
	private String adminUserId;
	private String releaseDesc;
	private String releaseAt;
	private String fileName;
	private String updateReqXml;
	private Integer currentVersion;
	private String releaseName;

	public VersionVo() {
	}

	public String getReleaseName() {
		return releaseName;
	}

	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}

	public void setReleaseNumber(String releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	public Integer getReleaseNumber() {
		return Integer.valueOf(releaseNumber);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

	public String getReleaseDesc() {
		return releaseDesc;
	}

	public void setReleaseDesc(String releaseDesc) {
		this.releaseDesc = releaseDesc;
	}

	public String getReleaseAt() {
		return releaseAt;
	}

	public void setReleaseAt(String releaseAt) {
		this.releaseAt = releaseAt;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUpdateReqXml() {
		return updateReqXml;
	}

	public void setUpdateReqXml(String updateReqXml) {
		this.updateReqXml = updateReqXml;
	}

	public Integer getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(Integer currentVersion) {
		this.currentVersion = currentVersion;
	}

	@Override
	public String toString() {
		return "VersionVo{" + "id=" + id + ", releaseNumber='" + releaseNumber
				+ '\'' + ", productType='" + productType + '\''
				+ ", downloadLink='" + downloadLink + '\'' + ", adminUserId="
				+ adminUserId + ", releaseDesc='" + releaseDesc + '\''
				+ ", releaseAt='" + releaseAt + '\'' + ", fileName='"
				+ fileName + '\'' + ", updateReqXml='" + updateReqXml + '\''
				+ ", currentVersion=" + currentVersion + '}';
	}
}
