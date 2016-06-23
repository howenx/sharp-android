package com.hanmimei.data;

public class AppConstant {
	public static final int ADR_ADD_SU = 1;
	public static final int ADR_UP_SU = 2;
	public static final int KITKAT_LESS = 3;
	public static final int KITKAT_ABOVE = 4;
	public static final int INTENT_CROP = 5;
	public static final int LOGIN_CODE = 6;
	public static final int CAR_TO_GOODS_CODE = 7;
	public static final int UP_USER_NAME_CODE = 8;
	//登录成功
	public static final String MESSAGE_BROADCAST_LOGIN_ACTION = "MESSAGE_BROADCAST_LOGIN_ACTION";
	//退出登录
	public static final String MESSAGE_BROADCAST_QUIT_LOGIN_ACTION = "MESSAGE_BROADCAST_QUIT_LOGIN_ACTION";
	//优惠券改变数量
	public static final String MESSAGE_BROADCAST_COUNPON_ACTION = "MESSAGE_BROADCAST_COUNPON_ACTION";
	//购物车发生变化
	public static final String MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR = "MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR_ACTION";
	public static final String MESSAGE_BROADCAST_UPDATE_CARVIEW = "MESSAGE_BROADCAST_UPDATE_CARVIEW";
	//回到首页
	public static final String MESSAGE_BROADCAST_GO_HOME = "MESSAGE_BROADCAST_GO_HOME_ACTION";
	//取消订单
	public static final String MESSAGE_BROADCAST_CANCLE_ORDER = "MESSAGE_BROADCAST_CANCLE_ORDER_ACTION";
	
	public static final String MESSAGE_BROADCAST_IMG_SEL_OK_ACTION="MESSAGE_BROADCAST_IMG_SEL_OK_ACTION";
	//注册成功
	public static final String MESSAGE_BROADCAST_REGIST_OK_ACTION="MESSAGE_BROADCAST_REGIST_OK_ACTION";
	//找回密码成功
	public static final String MESSAGE_BROADCAST_FORGET_OK_ACTION="MESSAGE_BROADCAST_FORGET_OK_ACTION";
	//更新主页
	public static final String MESSAGE_BROADCAST_UP_HOME_ACTION = "MESSAGE_BROADCAST_UP_HOME_ACTION";
	//收藏添加或2减少
	public static final String MESSAGE_BROADCAST_COLLECTION_ACTION = "MESSAGE_BROADCAST_COLLECTION_ACTION";
	
	//更新购物车商品数量角标
	public static final String MESSAGE_BROADCAST_UPDATE_NUMBER = "MESSAGE_BROADCAST_UPDATE_NUMBER";
	//更新消息盒子
	public static final String MESSAGE_BROADCAST_UPDATE_MSG = "MESSAGE_BROADCAST_UPDATE_MSG";
	//更新评价
	public static final String MESSAGE_BROADCAST_UPDATE_COMMENT = "MESSAGE_BROADCAST_UPDATE_COMMENT";
	// 支付失败
	public static final String MESSAGE_BROADCAST_WEIXINPAY_FAIL = "MESSAGE_BROADCAST_WEIXINPAY_FAIL";
	//weibo
	public static final String WEIBO_APPKEY = "794664710";
	public static final String WEIBO_APPSecret = "0dc274fafeabec336673331c633a115e";
	public static final String WEIBO_REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
	 /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * 
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * 
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * 
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    //微信
    public static final String WEIXIN_APP = "wx578f993da4b29f97";
	//baidu
    public static final String BAIDU_ID = "x6opgN1tKTyIy3GuzA4camG6reNnGtiT";
	public static final String HTTP_ERROR = "网络出现异常";
}
