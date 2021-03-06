package com.kakao.kakaogift.data;

public class UrlUtil {

	 /*
	  * 阿里云
	  */
	 public static final String SERVERY3 = "https://api.kakaogift.cn";
	 public static final String SERVERY1 = "https://shopping.kakaogift.cn";
	 public static final String SERVERY4 = "https://id.kakaogift.cn";
	 public static final String SERVERY5 = "https://promotion.kakaogift.cn";
	 public static final String SERVERY2 = "https://cash.kakaogift.cn";
	 public static final String SERVERY7 = "http://dl.kakaogift.cn";
	 public static final String SERVERY6 = "https://m.kakaogift.cn";
	 
	/*
	 * http://172.28.3.78	孙宝娜
	 */
//	 public static final String SERVERY3 = "http://172.28.3.78:9001";
//	 public static final String SERVERY1 = "http://172.28.3.78:9003";
//	 public static final String SERVERY4 = "http://172.28.3.78:9004";
//	 public static final String SERVERY5 = "http://172.28.3.78:9005";
//	 public static final String SERVERY7 = "http://172.28.3.78:9006";
	/*
	 * http://172.28.3.51	熊浩
	 */
//	public static final String SERVERY3 = "http://172.28.3.51:9001";
//	public static final String SERVERY1 = "http://172.28.3.51:9003";
//	public static final String SERVERY4 = "http://172.28.3.51:9004";
//	public static final String SERVERY5 = "http://172.28.3.51:9005";
	/*
	 * http://172.28.3.18	内部测试
	 */
//	 public static final String SERVERY3 = "http://172.28.3.18:9001";
//	 public static final String SERVERY1 = "http://172.28.3.18:9003";
//	 public static final String SERVERY2 = "http://172.28.3.18:9003";
//	 public static final String SERVERY4 = "http://172.28.3.18:9004";
//	 public static final String SERVERY5 = "http://172.28.3.18:9005";
//	 public static final String SERVERY7 = "http://172.28.3.18:9006";
//	 public static final String SERVERY6 = "http://172.28.3.18:3008";

	/*
	 * http://172.28.3.46	武多文
	 */
//	 public static final String SERVERY3 = "http://172.28.3.46:9001";
//	 public static final String SERVERY1 = "http://172.28.3.46:9003";
//	 public static final String SERVERY4 = "http://172.28.3.46:9004";
//	 public static final String SERVERY5 = "http://172.28.3.46:9005";
	 
	 //拼购
	 public static final String PINGOU_URL = SERVERY3 + "/comm/theme/2/";
	 //礼品
	 public static final String LIPIN_URL = SERVERY3 + "/comm/theme/3/";
	//提交意见
	public static final String SUGGESTION_URL = SERVERY1 + "/client/feedback";
	//baidu denglu
	 public static final String BAIDU_CHECK = SERVERY4 + "/api/user/open?idType=B&openId=";
	//微信登录检查
	public static final String WEIXIN_CHECK = SERVERY4 + "/api/user/open?idType=WO&unionId=";
	//微博登陆校验
	public static final String WEIBO_CHECK = SERVERY4 + "/api/user/open?idType=S&openId=";
	//QQ登录校验
	public static final String QQ_CHECK = SERVERY4 + "/api/user/open?idType=Q&openId=";
	//新浪微博登录校验
	public static final String SINA_CHECK = SERVERY4 + "";
	//评价中心
	public static final String COMMENT_CENTER = SERVERY1 + "/client/comment/center/";
	//发表评价
	public static final String PUB_COMMENT  = SERVERY1 + "/client/comment/add";
	// 图形验证码接口
	public static final String GET_IMG_CODE = SERVERY4 + "/getImageCodes/";
	// 忘记密码，手机号检测接口
	public static final String CHECK_PHONE_FORGET = SERVERY4 + "/api/verify";
	// 首页数据接口
	public static final String HOME_LIST_URL = SERVERY3 + "/index/";
	// 地址列表接口
	public static final String ADDRESS_LIST_URL = SERVERY4+ "/api/address/list";
	// 地址删除接口
	public static final String ADDRESS_DEL_URL = SERVERY4 + "/api/address/del";
	// 添加地址接口
	public static final String ADDRESS_ADD_URL = SERVERY4 + "/api/address/add";
	// 更新地址接口
	public static final String ADDRESS_UPDATE_URL = SERVERY4  + "/api/address/update";
	// 重置密码接口
	public static final String RESET_PWD_URL = SERVERY4 + "/api/reset_password";
	// 验证码获取接口
	public static final String GET_CODE_URL = SERVERY4 + "/api/send_code";
	// 更新用户信息接口
	public static final String UPDATE_USERINFO = SERVERY4 + "/api/user/update";
	// 更新token
	public static final String UPDATE_TOKEN = SERVERY4 + "/api/refresh_token";
	// 登录后，将本地购物车同步到网络购物车
	public static final String GET_CAR_LIST_URL = SERVERY1 + "/client/cart";
	//登录状态下请求购物车数据
	public static final String GET_CAR_LIST = SERVERY1 + "/client/cart/list";
	//未登录状态下请求购物车数据
	public static final String CAR_LIST_URL = SERVERY1 + "/client/cart/get/sku/list";
	//登陆状态下购物车的选中状态
	public static final String UPDATA_SHOPPING_STATE = SERVERY1 + "/client/cart/check";
	// 未登录状态加入购物车
	@Deprecated
	public static final String SEND_CAR_TO_SERVER_UN = SERVERY1 + "/client/cart/verify/amount/";
	
	public static final String POST_ADD_CART =SERVERY1 + "/client/cart/nologin/verify/amount";
	// 用户登录接口
	public static final String LOGIN_URL = SERVERY4 + "/api/login_phone_num";
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
	// 订单提交接口
	public static final String POST_CLIENT_ORDER_SUBMIT = SERVERY1
			+ "/client/order/submit";
	//
	public static final String CLIENT_PAY_ORDER_GET = SERVERY2
			+ "/client/pay/order/get/";
	// 退货申请接口
	public static final String CUSTOMER_SERVICE_APPLY = SERVERY1
			+ "/client/order/apply/refund";

	// 关于我们
	public static final String ABOUT_WE = SERVERY3 + "/comm/views/about";
	// 服务条款
	public static final String SERVICE = SERVERY3 + "/comm/views/agreement";
	// 隐私协议
	public static final String AGREEMENT = SERVERY3 + "/comm/views/privacy";
	// 检测更新
	public static final String UPDATE_HMM = SERVERY7 + "/android/kakaogift.xml?";
	// 添加收藏
	public static final String ADD_COLLECTION = SERVERY1
			+ "/client/collect/submit";
	// 收藏列表
	public static final String COLLECTION_LIST = SERVERY1
			+ "/client/collect/get/collect/list";
	// 删除收藏
	public static final String DEL_COLLECTION = SERVERY1
			+ "/client/collect/del/";

	// 查询我的拼团
	public static final String GET_MY_PINTUAN = SERVERY5
			+ "/promotion/pin/activity/list";
	// 消息type
	public static final String GET_MSG_TYPE = SERVERY1 + "/client/msg/get/type";
	// 单个删除消息
	public static final String DEL_MSG = SERVERY1 + "/client/msg/delMsg/";
	// 获取消息列表
	public static final String GET_MSG_LIST = SERVERY1 + "/client/msg/list/";
	// 删除消息 按type删除
	public static final String DEL_MSG_TYPE = SERVERY1 + "/client/msg/clean/";
	//确认收货
	public static final String CONFIRM_DELIVERY = SERVERY1 + "/client/order/confirm/delivery/";
	//查询物流
	public static final String WULIU_LIST = SERVERY1 + "/client/order/express/";
	
	//商品所有评价/comm/detail/comment/$skuType<item|vary|customize|pin>/$skuTypeId<[0-9]+>/$pageNum<[0-9]+>
	public static final String GOODS_REMARK_ALL = SERVERY3 +"/comm/detail/comment/";
	
	//商品好评/comm/comment/rank/best/$skuType<item|vary|customize|pin>/$skuTypeId<[0-9]+>/$pageNum<[0-9]+>  
	public static final String GOODS_REMARK_GOOD = SERVERY3 +"/comm/comment/rank/best/";
	
	//商品差评   /comm/comment/rank/worst/$skuType<item|vary|customize|pin>/$skuTypeId<[0-9]+>/$pageNum<[0-9]+> 
	public static final String GOODS_REMARK_BAD = SERVERY3 +"/comm/comment/rank/worst/";
	
	//商品品论图片/comm/comment/rank/img/$skuType<item|vary|customize|pin>/$skuTypeId<[0-9]+>/$pageNum<[0-9]+>   
	public static final String GOODS_REMARK_PICTURE = SERVERY3 +"/comm/comment/rank/img/";
	//商品品论图片/comm/comment/rank/img/$skuType<item|vary|customize|pin>/$skuTypeId<[0-9]+>/$pageNum<[0-9]+>   
	public static final String GOODS_LIKE = SERVERY3 +"/comm/recommend/";
}
