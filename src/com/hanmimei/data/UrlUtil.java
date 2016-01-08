package com.hanmimei.data;

public class UrlUtil {
	
	
	//地址
//	public static final String SERVERY3 = "https://api.hanmimei.com";	//  18:9001
//	public static final String SERVERY1 = "https://shopping.hanmimei.com";	// 18:9003
//	public static final String SERVERY4 = "https://id.hanmimei.com";	// 18:9004
//	
	//公网地址
		public static final String SERVERY3 = "http://172.28.3.18:9001";	//  18:9001
		public static final String SERVERY1 = "http://172.28.3.18:9003";	// 18:9003
		public static final String SERVERY4 = "http://172.28.3.18:9004";	// 18:9004

	

	public static final String HOME_LIST_URL = SERVERY3 + "/index/";
	// 地址列表接口
	public static final String ADDRESS_LIST_URL = SERVERY4
			+ "/api/address/list";
	// 地址删除接口
	public static final String ADDRESS_DEL_URL = SERVERY4 + "/api/address/del";
	// 添加地址接口
	public static final String ADDRESS_ADD_URL = SERVERY4 + "/api/address/add";
	// 更新地址接口
	public static final String ADDRESS_UPDATE_URL = SERVERY4
			+ "/api/address/update";
	// 重置密码接口
	public static final String RESET_PWD_URL = SERVERY4 + "/api/reset_password";
	// 验证码获取接口
	public static final String GET_CODE_URL = SERVERY4 + "/api/send_code";
	// 更新用户信息接口
	public static final String UPDATE_USERINFO = SERVERY4 + "/api/user/update";
	// 登录状态请求购物车数据接口
	public static final String GET_CAR_LIST_URL = SERVERY1 + "/client/cart";

	public static final String CAR_LIST_URL = SERVERY1
			+ "/client/cart/get/sku/list";
	// 未登录状态请求购物车数据
	public static final String SEND_CAR_TO_SERVER_UN = SERVERY1
			+ "/client/cart/verify/amount/";
	// 用户登录接口
	public static final String LOGIN_URL = SERVERY4 + "/api/login_user_name";
	// 用户注册接口
	public static final String REGIST_URL = SERVERY4 + "/api/reg";
	// 取消订单
	public static final String CANCLE_ORDER_URL = SERVERY1
			+ "/client/order/cancel/";
	// 获取用户信息接口
	public static final String GET_USERINFO_URL = SERVERY4
			+ "/api/user/get/info";
	// 获取订单列表接口
	public static final String GET_ORDER_LIST_URL = SERVERY1 + "/client/order";
	// 订单是否过期（是否可以去支付）
	public static final String GET_ORDER_IS_TIME = SERVERY1
			+ "/client/order/verify/";
	// 删除订单
	public static final String DEL_ORDER = SERVERY1 + "/client/order/del/";
	// 获取优惠券列表接口
	public static final String GET_COUPON_LIST_URL = SERVERY1
			+ "/client/coupons/list";
	// 获取购物车数量接口
	public static final String GET_CART_NUM_URL = SERVERY3 + "/comm/cart/amount";

	// 结算页接口
	public static final String POST_CLIENT_SETTLE = SERVERY1 + "/client/settle";
	public static final String POST_CLIENT_ORDER_SUBMIT = SERVERY1
			+ "/client/order/submit";
	public static final String GOODS_DETAIL_URL = SERVERY3
			+ "/comm/detail/web/";
	public static final String CLIENT_PAY_ORDER_GET = SERVERY1
			+ "/client/pay/order/get/";

}
