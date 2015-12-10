package com.hanmimei.data;

public class UrlUtil {
	public static final String SERVER01 = "http://172.28.3.18:9003";	//服务器	
	public static final String SERVER02 = "http://172.28.3.51:9003";   //  测试服务器
	public static final String SERVER05 = "http://172.28.3.51:9004";   //  测试服务器
	public static final String SERVER03 = "http://172.28.3.18:9001";	//服务器	
	public static final String SERVER04 = "http://172.28.3.18:9004";	//服务器	
	
	
	
	public static final String HOME_LIST_URL = SERVER03 + "/index/";

	public static final String ADDRESS_LIST_URL = SERVER04 + "/api/address/list";
	public static final String ADDRESS_DEL_URL = SERVER04 + "/api/address/del";
	public static final String ADDRESS_ADD_URL = SERVER04 + "/api/address/add";
	public static final String ADDRESS_UPDATE_URL = SERVER04 + "/api/address/update";
	public static final String RESET_PWD_URL = SERVER04 + "/api/reset_password";
	public static final String GET_CODE_URL = SERVER04 + "/api/send_code";
	public static final String UPDATE_USERINFO = SERVER04 + "/api/user/update";
	public static final String GET_CAR_LIST_URL = SERVER01 + "/client/cart";
	public static final String SEND_CAR_TO_SERVER_UN = SERVER01 + "/client/cart/verify/amount/";
	public static final String LOGIN_URL = SERVER04 + "/api/login_user_name";
	public static final String REGIST_URL = SERVER04 + "/api/reg";
	public static final String CANCLE_ORDER_URL = SERVER01 + "/client/order/state/update";
	public static final String GET_USERINFO_URL = SERVER05 + "/api/user/get/info";
	public static final String GET_ORDER_LIST_URL = SERVER01 + "/client/order";
	public static final String GET_COUPON_LIST_URL = SERVER02 + "/client/coupons/list";
	
	//结算页接口
	public static final String POST_CLIENT_SETTLE = SERVER02 +"/client/settle";
	
}
