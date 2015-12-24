package com.hanmimei.data;

public class UrlUtil {
	public static final String SERVER01 = "http://172.28.3.18:9003";	//服务器	
	public static final String SERVER02 = "http://172.28.3.51:9003";   //  测试服务器
	public static final String SERVER05 = "http://172.28.3.51:9004";   //  测试服务器
	public static final String SERVER06 = "http://172.28.3.51:9001";   //  测试服务器
	public static final String SERVER03 = "http://172.28.3.18:9001";	//服务器	
	public static final String SERVER04 = "http://172.28.3.18:9004";	//服务器	
	
	
	//主页数据接口
	public static final String HOME_LIST_URL = SERVER03 + "/index/";
	//地址列表接口
	public static final String ADDRESS_LIST_URL = SERVER04 + "/api/address/list";
	//地址删除接口
	public static final String ADDRESS_DEL_URL = SERVER04 + "/api/address/del";
	//添加地址接口
	public static final String ADDRESS_ADD_URL = SERVER04 + "/api/address/add";
	//更新地址接口
	public static final String ADDRESS_UPDATE_URL = SERVER04 + "/api/address/update";
	//重置密码接口
	public static final String RESET_PWD_URL = SERVER04 + "/api/reset_password";
	//验证码获取接口
	public static final String GET_CODE_URL = SERVER04 + "/api/send_code";
	//更新用户信息接口
	public static final String UPDATE_USERINFO = SERVER04 + "/api/user/update";
	//登录状态请求购物车数据接口
	public static final String GET_CAR_LIST_URL = SERVER01 + "/client/cart";
	//未登录状态请求购物车数据
	public static final String SEND_CAR_TO_SERVER_UN = SERVER01 + "/client/cart/verify/amount/";
	//用户登录接口
	public static final String LOGIN_URL = SERVER04 + "/api/login_user_name";
	//用户注册接口
	public static final String REGIST_URL = SERVER04 + "/api/reg";
	//订单状态更新接口
	public static final String CANCLE_ORDER_URL = SERVER02 + "/client/order/cancel/";
	//获取用户信息接口
	public static final String GET_USERINFO_URL = SERVER04 + "/api/user/get/info";
	//获取订单列表接口
	public static final String GET_ORDER_LIST_URL = SERVER02 + "/client/order";
	//获取订单列表接口
	public static final String GET_ORDER_IS_TIME = SERVER02 + "/client/order/verify/";
	
	public static final String DEL_ORDER = SERVER02 + "/client/order/del/";
	//获取优惠券列表接口
	public static final String GET_COUPON_LIST_URL = SERVER01 + "/client/coupons/list";
	
	//结算页接口
	public static final String POST_CLIENT_SETTLE = SERVER01 +"/client/settle";
	public static final String POST_CLIENT_ORDER_SUBMIT = SERVER01 +"/client/order/submit";
	public static final String GOODS_DETAIL_URL = SERVER03 +"/comm/detail/web/";
	public static final String CLIENT_PAY_ORDER_GET = SERVER01 +"/client/pay/order/get/";
	
}
