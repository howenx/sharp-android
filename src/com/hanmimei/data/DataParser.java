package com.hanmimei.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hanmimei.entity.Collection;
import com.hanmimei.entity.CollectionInfo;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.GoodsDetail;
import com.hanmimei.entity.HMMAddress;
import com.hanmimei.entity.HMMThemeGoods;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.Home;
import com.hanmimei.entity.MessageInfo;
import com.hanmimei.entity.MessageType;
import com.hanmimei.entity.MsgResult;
import com.hanmimei.entity.Notify;
import com.hanmimei.entity.Order;
import com.hanmimei.entity.Result;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.Sku;
import com.hanmimei.entity.Slider;
import com.hanmimei.entity.Theme;
import com.hanmimei.entity.User;

public class DataParser {
	public static Home parserHomeData(String result) {	
		Home home = new Home();
		List<Theme> list = new ArrayList<Theme>();
		try {
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("theme");
			for (int i = 0; i < array.length(); i++) {
				Theme theme = new Theme();
				JSONObject obj = array.getJSONObject(i);
				if(obj.has("id"))
					theme.setItem_id(obj.getInt("id"));
				if (obj.has("themeImg")){
					String urlResult = obj.getString("themeImg");
					JSONObject themeObject = new JSONObject(urlResult);
					if(themeObject.has("url"))
						theme.setThemeImg(themeObject.getString("url"));
					if(themeObject.has("width"))
						theme.setWidth(themeObject.getInt("width"));
					if(themeObject.has("height"))
						theme.setHeight(themeObject.getInt("height"));
				}
				if (obj.has("themeUrl"))
					theme.setThemeUrl(obj.getString("themeUrl"));
				if (obj.has("type"))
					theme.setType(obj.getString("type"));
				list.add(theme);

			}
			List<Slider> sliders = new ArrayList<Slider>();
			if(object.has("slider")){
			JSONArray array2 = object.getJSONArray("slider");
			for (int i = 0; i < array2.length(); i++) {
				JSONObject obj = array2.getJSONObject(i);
				Slider slider = new Slider();
				if (obj.has("url")){
					String sliderResult = obj.getString("url");
					JSONObject sliderObject = new JSONObject(sliderResult);
					if(sliderObject.has("url"))
						slider.setImgUrl(sliderObject.getString("url"));
					if(sliderObject.has("width"))
						slider.setWidth(sliderObject.getInt("width"));
					if(sliderObject.has("height"))
						slider.setHeight(sliderObject.getInt("height"));
				}
				if (obj.has("itemTarget"))
					slider.setUrl(obj.getString("itemTarget"));
				if(obj.has("targetType"))
					slider.setType(obj.getString("targetType"));
				sliders.add(slider);
			}
			}
			HMessage hMessage = new HMessage();
			if(object.has("message")){
				JSONObject object2 = object.getJSONObject("message");
				if(object2.has("message"))
					hMessage.setMessage(object2.getString("message"));
				if(object2.has("code"))
					hMessage.setCode(object2.getInt("code"));
			}
			if(object.has("page_count"))
				home.setPage_count(object.getInt("page_count"));
			if(object.has("msgRemind"))
				home.setHasMsg(object.getInt("msgRemind"));
			home.setSliders(sliders);
			home.sethMessage(hMessage);
			home.setThemes(list);
			

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return home;

	}

	public static HMMThemeGoods parserThemeItem(String result) {
		return new Gson().fromJson(result, HMMThemeGoods.class);
	}

	public static List<HMMAddress> parserAddressList(String result) {
		List<HMMAddress> list = new ArrayList<HMMAddress>();
		try {
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("address");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HMMAddress adress = new HMMAddress();
				if (obj.has("addId"))
					adress.setAdress_id(obj.getInt("addId"));
				// if(obj.has("userId"))
				// adress.setUser_id(obj.getInt("userId"));
				if (obj.has("tel"))
					adress.setPhone(obj.getString("tel"));
				if (obj.has("name"))
					adress.setName(obj.getString("name"));
				if (obj.has("deliveryCity"))
					adress.setCity(obj.getString("deliveryCity"));
				if (obj.has("deliveryDetail"))
					adress.setAdress(obj.getString("deliveryDetail"));
				if(obj.has("idCardNum"))
					adress.setIdCard(obj.getString("idCardNum"));
				if(obj.has("orDefault")){
					if(obj.getInt("orDefault") == 0){
						adress.setDefault(false);
					}else{
						adress.setDefault(true);
					}
				}
				list.add(adress);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static Result parserResult(String str) {
		Result result = new Result();
		try {
			JSONObject object = new JSONObject(str);
			if (object.has("message")) {
				JSONObject obj = new JSONObject(object.getString("message"));
				if (obj.has("code"))
					result.setCode(obj.getInt("code"));
				if (obj.has("message"))
					result.setMessage(obj.getString("message"));
			}
			if (object.has("address")) {
				JSONObject obj = new JSONObject(object.getString("address"));
				if (obj.has("addId"))
					result.setResult_id(obj.getInt("addId"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
		

	public static Result parserLoginResult(String str){
		Result result = new Result();
		try {
			JSONObject object = new JSONObject(str);
			if (object.has("result"))
				result.setSuccess(object.getBoolean("result"));
			if (object.has("message"))
				result.setMessage(object.getString("message"));
			if(object.has("token"))
				result.setTag(object.getString("token"));
			if(object.has("expired"))
				result.setTime(object.getInt("expired"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static List<Order> parserOrder(String result){
		List<Order> list = null;
		try {
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("orderList");
			list = new ArrayList<Order>();
			for(int i = 0; i < array.length(); i ++){
				Order order = new Order();
				JSONObject obj = array.getJSONObject(i);
				if(obj.has("address")){
					JSONObject addObject = obj.getJSONObject("address");
					HMMAddress adress = new HMMAddress();
					if(addObject.has("tel"))
						adress.setPhone(addObject.getString("tel"));
					if(addObject.has("idCardNum"))
						adress.setIdCard(addObject.getString("idCardNum"));
					if(addObject.has("name"))
						adress.setName(addObject.getString("name"));
					if(addObject.has("deliveryCity"))
						adress.setCity(addObject.getString("deliveryCity"));
					if(addObject.has("deliveryDetail"))
						adress.setAdress(addObject.getString("deliveryDetail"));
					order.setAdress(adress);
				}
				if(obj.has("order")){
					JSONObject orderObject = obj.getJSONObject("order");
					if(orderObject.has("orderId"))
						order.setOrderId(orderObject.getString("orderId"));
					if(orderObject.has("payTotal"))
						order.setPayTotal(orderObject.getDouble("payTotal"));
					if(orderObject.has("payMethod"))
						order.setPayMethod(orderObject.getString("payMethod"));
					if(orderObject.has("orderCreateAt"))
						order.setOrderCreateAt(orderObject.getString("orderCreateAt"));
					if(orderObject.has("orderStatus"))
						order.setOrderStatus(orderObject.getString("orderStatus"));
					if(orderObject.has("discount")){
						if(!orderObject.getString("discount").equals("null"))
						order.setDiscount(orderObject.getDouble("discount"));
					}
					if(orderObject.has("orderSplitId"))
						order.setOrderSplitId(orderObject.getString("orderSplitId"));
					if(orderObject.has("orderDesc"))
						order.setOrderDesc(orderObject.getString("orderDesc"));
					if(orderObject.has("shipFee"))
						order.setShipFee(orderObject.getDouble("shipFee"));
					if(orderObject.has("totalFee"))
						order.setTotalFee(orderObject.getDouble("totalFee"));
					if(orderObject.has("postalFee"))
						order.setPostalFee(orderObject.getDouble("postalFee"));
					if(orderObject.has("orderDetailUrl"))
						order.setOrderDetailUrl(orderObject.getString("orderDetailUrl"));
					if(orderObject.has("countDown")){
						if(!orderObject.getString("countDown").equals("null"))
						order.setCountDown(orderObject.getInt("countDown"));
					}
				}
				if(obj.has("sku")){
					JSONArray skuArray = obj.getJSONArray("sku");
					List<Sku> skuList = new ArrayList<Sku>();
					for(int j = 0; j < skuArray.length(); j ++){
						JSONObject skuObject = skuArray.getJSONObject(j);
						Sku sku = new Sku();
						if(skuObject.has("skuId"))
							sku.setSkuId(skuObject.getString("skuId"));
						if(skuObject.has("amount"))
							sku.setAmount(skuObject.getInt("amount"));
						if(skuObject.has("price"))
							sku.setPrice(skuObject.getInt("price"));
						if(skuObject.has("skuTitle"))
								sku.setSkuTitle(decode2(skuObject.getString("skuTitle")));
						if(skuObject.has("invImg")){
							JSONObject imgObj = new JSONObject(skuObject.getString("invImg"));
							if(imgObj.has("url"))
								sku.setInvImg(imgObj.getString("url"));
						}
						if(skuObject.has("invUrl"))
							sku.setInvUrl(skuObject.getString("invUrl"));
						if(skuObject.has("itemColor"))
							sku.setItemColor(skuObject.getString("itemColor"));
						if(skuObject.has("itemSize"))
							sku.setItemSize(skuObject.getString("itemSize"));
						skuList.add(sku);
					}
					order.setList(skuList);
				}
				list.add(order);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static String decode2(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\\' && chars[i + 1] == 'u') {
                char cc = 0;
                for (int j = 0; j < 4; j++) {
                    char ch = Character.toLowerCase(chars[i + 2 + j]);
                    if ('0' <= ch && ch <= '9' || 'a' <= ch && ch <= 'f') {
                        cc |= (Character.digit(ch, 16) << (3 - j) * 4);
                    } else {
                        cc = 0;
                        break;
                    }
                }
                if (cc > 0) {
                    i += 5;
                    sb.append(cc);
                    continue;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }
	public static ShoppingCar parserShoppingCar(String result){
		ShoppingCar car = new ShoppingCar();
		List<Customs> customs = new ArrayList<Customs>();
		HMessage msg = new HMessage();
		try {
			JSONObject object = new JSONObject(result);
			JSONObject msgObject = object.getJSONObject("message");
			if(msgObject.has("message"))
				msg.setMessage(msgObject.getString("message"));
			if(msgObject.has("code"))
				msg.setCode(msgObject.getInt("code"));
			if(object.has("cartList")){
			JSONArray array = object.getJSONArray("cartList");
			for(int i = 0; i < array.length(); i ++){
				JSONObject obj = array.getJSONObject(i);
				List<ShoppingGoods> list = new ArrayList<ShoppingGoods>();
				Customs custom = new Customs();
				if(obj.has("invCustoms"))
					custom.setInvCustoms(obj.getString("invCustoms"));
				if(obj.has("invArea"))
					custom.setInvArea(obj.getString("invArea"));
				if(obj.has("postalLimit"))
					custom.setPostalLimit(obj.getInt("postalLimit"));
				if(obj.has("freeShip"))
					custom.setPostFee(obj.getInt("freeShip"));
				if(obj.has("postalStandard"))
					custom.setPostalStandard(obj.getInt("postalStandard"));
				if(obj.has("invAreaNm"))
					custom.setInvAreaNm(obj.getString("invAreaNm"));
				custom.setState("");
				custom.setTax("0");
				JSONArray cartsArray = obj.getJSONArray("carts");
				for(int j = 0; j < cartsArray.length(); j ++){
					JSONObject goodsObject = cartsArray.getJSONObject(j);
					ShoppingGoods goods = new ShoppingGoods();
					if(goodsObject.has("cartId"))
						goods.setCartId(goodsObject.getString("cartId"));
					if(goodsObject.has("skuId"))
						goods.setGoodsId(goodsObject.getString("skuId"));
					if(goodsObject.has("amount"))
						goods.setGoodsNums(goodsObject.getInt("amount"));
					if(goodsObject.has("itemColor"))
						goods.setItemColor(goodsObject.getString("itemColor"));
					if(goodsObject.has("itemSize"))
						goods.setItemSize(goodsObject.getString("itemSize"));
					if(goodsObject.has("itemPrice"))
						goods.setGoodsPrice(goodsObject.getDouble("itemPrice"));
					if(goodsObject.has("state")){
						if(goodsObject.getString("state").equals("G")){
							goods.setState("I");
						}else{
							goods.setState(goodsObject.getString("state"));
						}
					}
					if(goodsObject.has("shipFee"))
						goods.setShipFee(goodsObject.getString("shipFee"));
					if(goodsObject.has("invArea"))
						goods.setInvArea(goodsObject.getString("invArea"));
					if(goodsObject.has("restrictAmount"))
						goods.setRestrictAmount(goodsObject.getInt("restrictAmount"));
					if(goodsObject.has("restAmount"))
						goods.setRestAmount(goodsObject.getInt("restAmount"));
					if(goodsObject.has("invImg")){
						JSONObject imgObj = new JSONObject(goodsObject.getString("invImg"));
						if(imgObj.has("url"))
							goods.setGoodsImg(imgObj.getString("url"));
					}
					if(goodsObject.has("invUrl"))
						goods.setGoodsUrl(goodsObject.getString("invUrl"));
					if(goodsObject.has("invTitle"))
						goods.setGoodsName(goodsObject.getString("invTitle"));
					if(goodsObject.has("cartDelUrl"))
						goods.setDelUrl(goodsObject.getString("cartDelUrl"));
					if(goodsObject.has("invCustoms"))
						goods.setInvCustoms(goodsObject.getString("invCustoms"));
					if(goodsObject.has("postalTaxRate"))
						goods.setPostalTaxRate(goodsObject.getInt("postalTaxRate"));
//					if(goodsObject.has("postalStandard"))
//						goods.setPostalStandard(goodsObject.getInt("postalStandard"));
//					if(goodsObject.has("postalLimit"))
//						goods.setPostalLimit(goodsObject.getInt("postalLimit"));
					if(goodsObject.has("skuType"))
						goods.setSkuType(goodsObject.getString("skuType"));
					if(goodsObject.has("skuTypeId"))
						goods.setSkuTypeId(goodsObject.getString("skuTypeId"));
					list.add(goods);
				}
				custom.setList(list);
				customs.add(custom);
			}
			}
			
			car.setList(customs);
			car.setMessage(msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return car;
	}
	public static HMessage paserResultMsg(String result){
		HMessage msg = new HMessage();
		try {
			JSONObject object = new JSONObject(result);
			if(object.has("message")){
			JSONObject obj = object.getJSONObject("message");
			if(obj.has("message"))
				msg.setMessage(obj.getString("message"));
			if(obj.has("code"))
				msg.setCode(obj.getInt("code"));
			}
			if(object.has("result")){
			JSONObject userObject = object.getJSONObject("result");
			if(userObject.has("token"))
				msg.setTag(userObject.getString("token"));
			if(userObject.has("expired"))
				msg.setTime(userObject.getInt("expired"));
			if(userObject.has("id"))
				msg.setUserId(userObject.getInt("id"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
		
	}

	public static User parserUserInfo(String result) {
		User user = new User();
		try {
			JSONObject object = new JSONObject(result);
			JSONObject obj = object.getJSONObject("userInfo");
			if(obj.has("name"))
				user.setUserName(obj.getString("name"));
			if(obj.has("photo"))
				user.setUserImg(obj.getString("photo"));
			if(obj.has("phoneNum"))
				user.setPhone(obj.getString("phoneNum"));
			if(obj.has("couponsCount"))
				user.setCouponCount(obj.getInt("couponsCount"));
			if(obj.has("gender"))
				user.setSex(obj.getString("gender"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	public static CollectionInfo parserCollect(String result){
		CollectionInfo collectionInfo = new CollectionInfo();
		try {
			JSONObject object = new JSONObject(result);
			if(object.has("message")){
				HMessage hMessage = new HMessage();
				JSONObject mObject = object.getJSONObject("message");
				if(mObject.has("message"))
					hMessage.setMessage(mObject.getString("message"));
				if(mObject.has("code"))
					hMessage.setCode(mObject.getInt("code"));
				collectionInfo.sethMessage(hMessage);
			}
			if(object.has("collectList")){
				List<Collection> list = new ArrayList<Collection>();
				JSONArray array = object.getJSONArray("collectList");
				for(int i = 0 ; i < array.length(); i ++){
					Collection collection = new Collection();
					JSONObject obj = array.getJSONObject(i);
					if(obj.has("collectId"))
						collection.setCollectId(obj.getString("collectId"));
					if(obj.has("createAt"))
						collection.setCreateAt(obj.getLong("collectId"));
					if(obj.has("skuType"))
						collection.setSkuType(obj.getString("skuType"));
					if(obj.has("skuTypeId"))
						collection.setSkuTypeId(obj.getString("skuTypeId"));
					if(obj.has("cartSkuDto")){
						Sku sku = new Sku();
						JSONObject skuObject = obj.getJSONObject("cartSkuDto");
							if(skuObject.has("skuId"))
								sku.setSkuId(skuObject.getString("skuId"));
							if(skuObject.has("price"))
								sku.setPrice(skuObject.getInt("price"));
							if(skuObject.has("skuTitle"))
								sku.setSkuTitle(skuObject.getString("skuTitle"));
							if(skuObject.has("invUrl"))
								sku.setInvUrl(skuObject.getString("invUrl"));
							if(skuObject.has("invImg")){
								JSONObject imgObj = new JSONObject(skuObject.getString("invImg"));
								if(imgObj.has("url"))
									sku.setInvImg(imgObj.getString("url"));
							}
							if(skuObject.has("itemColor"))
								sku.setItemColor(skuObject.getString("itemColor"));
							if(skuObject.has("itemSize"))
								sku.setItemSize(skuObject.getString("itemSize"));
						collection.setSku(sku);
					}
					list.add(collection);
				}
				collectionInfo.setList(list);
			}
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return collectionInfo;
	}
	public static int parserCollectId(String result){
		int collectionId = 0;
		try {
			JSONObject object = new JSONObject(result);
			if(object.has("collectId")){
				collectionId = object.getInt("collectId");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return collectionId;
	}
	public static Notify parserJPush(String result){
		Notify notify = new Notify();;
		try {
			JSONObject object = new JSONObject(result);
			if(object.has("url"))
				notify.setUrl(object.getString("url"));
			if(object.has("targetType"))
				notify.setTargetType(object.getString("targetType"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return notify;
	}
	public static MessageType  parseMsgType(String result){
		MessageType type = new MessageType();
		try {
			JSONObject object = new JSONObject(result);
			if(object.has("msgTypeMap")){
				JSONObject typeObject = object.getJSONObject("msgTypeMap");
				if(typeObject.has("system"))
					type.setSysNum(typeObject.getInt("system"));
				if(typeObject.has("coupon"))
					type.setZichanNum(typeObject.getInt("coupon"));
				if(typeObject.has("discount"))
					type.setHuodongNum(typeObject.getInt("discount"));
				if(typeObject.has("logistics"))
					type.setWuliuNum(typeObject.getInt("logistics"));
				if(typeObject.has("goods"))
					type.setGoodNum(typeObject.getInt("goods"));
			}
			if(object.has("message")){
				JSONObject msgObject = object.getJSONObject("message");
				if(msgObject.has("code"))
					type.setCode(msgObject.getInt("code"));
				if(msgObject.has("message"))
					type.setMessage(msgObject.getString("message"));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return type;
	}
	public static MsgResult parseMsgInfo(String result){
		MsgResult msgResult = new MsgResult();
		try {
			JSONObject object = new JSONObject(result);
			if(object.has("msgList")){
				List<MessageInfo> list = new ArrayList<MessageInfo>();
				JSONArray array = object.getJSONArray("msgList");
				for(int i = 0; i < array.length(); i ++){
					MessageInfo info = new MessageInfo();
					JSONObject obj = array.getJSONObject(i);
					if(obj.has("id"))
						info.setMsgId(obj.getString("id"));
					if(obj.has("msgTitle"))
						info.setMsgTitle(obj.getString("msgTitle"));
					if(obj.has("msgContent"))
						info.setMsgContent(obj.getString("msgContent"));
					if(obj.has("msgImg"))
						info.setMsgImg(obj.getString("msgImg"));
					if(obj.has("msgUrl"))
						info.setMsgUrl(obj.getString("msgUrl"));
					if(obj.has("msgType"))
						info.setMsgType(obj.getString("msgType"));
					if(obj.has("createAt"))
						info.setCreateAt(obj.getLong("createAt"));
					if(obj.has("targetType"))
						info.setTargetType(obj.getString("targetType"));
					list.add(info);
				}
				msgResult.setList(list);
			}
			HMessage message = new HMessage();
			if(object.has("message")){
				JSONObject msgObject = object.getJSONObject("message");
				if(msgObject.has("code"))
					message.setCode(msgObject.getInt("code"));
				if(msgObject.has("message"))
					message.setMessage(msgObject.getString("message"));
				msgResult.setMessage(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msgResult;
	}
	
	
}
