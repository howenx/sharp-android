package com.kakao.kakaogift.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.kakao.kakaogift.entity.Collection;
import com.kakao.kakaogift.entity.CollectionVo;
import com.kakao.kakaogift.entity.CustomsVo;
import com.kakao.kakaogift.entity.Entry;
import com.kakao.kakaogift.entity.HAddress;
import com.kakao.kakaogift.entity.HMessage;
import com.kakao.kakaogift.entity.HThemeGoods;
import com.kakao.kakaogift.entity.Home;
import com.kakao.kakaogift.entity.Logistics;
import com.kakao.kakaogift.entity.LogisticsData;
import com.kakao.kakaogift.entity.Notify;
import com.kakao.kakaogift.entity.Order;
import com.kakao.kakaogift.entity.OrderList;
import com.kakao.kakaogift.entity.PushMessageResult;
import com.kakao.kakaogift.entity.PushMessageType;
import com.kakao.kakaogift.entity.PushMessageTypeInfo;
import com.kakao.kakaogift.entity.PushMessageVo;
import com.kakao.kakaogift.entity.RefundVo;
import com.kakao.kakaogift.entity.Result;
import com.kakao.kakaogift.entity.ShoppingCar;
import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.entity.Sku;
import com.kakao.kakaogift.entity.Slider;
import com.kakao.kakaogift.entity.Theme;
import com.kakao.kakaogift.entity.User;

/**
 * @author eric
 * 
 */
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
				if (obj.has("id"))
					theme.setItem_id(obj.getInt("id"));
				if (obj.has("themeImg")) {
					String urlResult = obj.getString("themeImg");
					JSONObject themeObject = new JSONObject(urlResult);
					if (themeObject.has("url"))
						theme.setThemeImg(themeObject.getString("url"));
					if (themeObject.has("width"))
						theme.setWidth(themeObject.getInt("width"));
					if (themeObject.has("height"))
						theme.setHeight(themeObject.getInt("height"));
				}
				if (obj.has("themeUrl"))
					theme.setThemeUrl(obj.getString("themeUrl"));
				if (obj.has("type"))
					theme.setType(obj.getString("type"));
				if(obj.has("title"))
					theme.setTitle(obj.getString("title"));
				if(obj.has("themeConfigInfo"))
					theme.setThemeConfigInfo(obj.getString("themeConfigInfo"));
				list.add(theme);

			}
			List<Entry> entryList = new ArrayList<Entry>();
			if(object.has("sliderNav")){
				JSONArray array2 = object.getJSONArray("sliderNav");
				for(int i = 0; i < array2.length(); i ++){
					JSONObject obj = array2.getJSONObject(i);
					Entry entry = new Entry();
					if(obj.has("itemTarget"))
						entry.setItemTarget(obj.getString("itemTarget"));
					if(obj.has("targetType"))
						entry.setTargetType(obj.getString("targetType"));
					if(obj.has("navText"))
						entry.setNavText(obj.getString("navText"));
					if(obj.has("url"))
						entry.setImgUrl(obj.getString("url"));
					entryList.add(entry);
				}
			}
			List<Slider> sliders = new ArrayList<Slider>();
			if (object.has("slider")) {
				JSONArray array2 = object.getJSONArray("slider");
				for (int i = 0; i < array2.length(); i++) {
					JSONObject obj = array2.getJSONObject(i);
					Slider slider = new Slider();
					if (obj.has("url")) {
//						String sliderResult = obj.getString("url");
//						JSONObject sliderObject = new JSONObject(sliderResult);
//						if (sliderObject.has("url"))
//							slider.setImgUrl(sliderObject.getString("url"));
//						if (sliderObject.has("width"))
//							slider.setWidth(sliderObject.getInt("width"));
//						if (sliderObject.has("height"))
//							slider.setHeight(sliderObject.getInt("height"));
						slider.setImgUrl(obj.getString("url"));
					}
					if (obj.has("itemTarget"))
						slider.setUrl(obj.getString("itemTarget"));
					if (obj.has("targetType"))
						slider.setType(obj.getString("targetType"));
					sliders.add(slider);
				}
			}
			HMessage hMessage = new HMessage();
			if (object.has("message")) {
				JSONObject object2 = object.getJSONObject("message");
				if (object2.has("message"))
					hMessage.setMessage(object2.getString("message"));
				if (object2.has("code"))
					hMessage.setCode(object2.getInt("code"));
			}
			if (object.has("page_count"))
				home.setPage_count(object.getInt("page_count"));
			if (object.has("msgRemind"))
				home.setHasMsg(object.getInt("msgRemind"));
			home.setSliders(sliders);
			home.sethMessage(hMessage);
			home.setThemes(list);
			home.setEntries(entryList);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return home;

	}

	public static HThemeGoods parserThemeItem(String result) {
		return new Gson().fromJson(result, HThemeGoods.class);
	}

	public static List<HAddress> parserAddressList(String result) {
		List<HAddress> list = new ArrayList<HAddress>();
		try {
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("address");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HAddress adress = new HAddress();
				if (obj.has("addId"))
					adress.setAdress_id(obj.getLong("addId"));
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
				if (obj.has("idCardNum"))
					adress.setIdCard(obj.getString("idCardNum"));
				if (obj.has("orDefault")) {
					if (obj.getInt("orDefault") == 0) {
						adress.setDefault(false);
					} else {
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

	public static Result parserLoginResult(String str) {
		Result result = new Result();
		try {
			JSONObject object = new JSONObject(str);
			if (object.has("result"))
				result.setSuccess(object.getBoolean("result"));
			if (object.has("message"))
				result.setMessage(object.getString("message"));
			if (object.has("token"))
				result.setTag(object.getString("token"));
			if (object.has("expired"))
				result.setTime(object.getInt("expired"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static OrderList parserOrder(String result) {
		List<Order> list = null;
		OrderList orderList = new OrderList();
		HMessage hMessage = new HMessage();
		try {
			JSONObject object = new JSONObject(result);
			if (object.has("message")) {
				JSONObject msgObject = object.getJSONObject("message");
				if (msgObject.has("message"))
					hMessage.setMessage(msgObject.getString("message"));
				if (msgObject.has("code"))
					hMessage.setCode(msgObject.getInt("code"));
				orderList.setMessage(hMessage);
			}
			JSONArray array = object.getJSONArray("orderList");
			list = new ArrayList<Order>();
			for (int i = 0; i < array.length(); i++) {
				Order order = new Order();
				JSONObject obj = array.getJSONObject(i);
				if (obj.has("address")) {
					JSONObject addObject = obj.getJSONObject("address");
					HAddress adress = new HAddress();
					if (addObject.has("tel"))
						adress.setPhone(addObject.getString("tel"));
					if (addObject.has("idCardNum"))
						adress.setIdCard(addObject.getString("idCardNum"));
					if (addObject.has("name"))
						adress.setName(addObject.getString("name"));
					if (addObject.has("deliveryCity"))
						adress.setCity(addObject.getString("deliveryCity"));
					if (addObject.has("deliveryDetail"))
						adress.setAdress(addObject.getString("deliveryDetail"));
					order.setAdress(adress);
				}
				if (obj.has("order")) {
					JSONObject orderObject = obj.getJSONObject("order");
					if (orderObject.has("orderId"))
						order.setOrderId(orderObject.getString("orderId"));
					if (orderObject.has("payTotal"))
						order.setPayTotal(orderObject.getDouble("payTotal"));
					if (orderObject.has("payMethod"))
						order.setPayMethod(orderObject.getString("payMethod"));
					if (orderObject.has("orderCreateAt"))
						order.setOrderCreateAt(orderObject
								.getString("orderCreateAt"));
					if (orderObject.has("orderStatus"))
						order.setOrderStatus(orderObject
								.getString("orderStatus"));
					if (orderObject.has("discount")) {
						if (!orderObject.getString("discount").equals("null"))
							order.setDiscount(orderObject.getDouble("discount"));
					}
					if (orderObject.has("orderSplitId"))
						order.setOrderSplitId(orderObject
								.getString("orderSplitId"));
					if (orderObject.has("orderDesc"))
						order.setOrderDesc(orderObject.getString("orderDesc"));
					if (orderObject.has("shipFee"))
						order.setShipFee(orderObject.getDouble("shipFee"));
					if (orderObject.has("totalFee"))
						order.setTotalFee(orderObject.getDouble("totalFee"));
					if (orderObject.has("postalFee"))
						order.setPostalFee(orderObject.getDouble("postalFee"));
					if (orderObject.has("orderDetailUrl"))
						order.setOrderDetailUrl(orderObject
								.getString("orderDetailUrl"));
					if (orderObject.has("countDown")) {
						if (!orderObject.getString("countDown").equals("null"))
							order.setCountDown(orderObject.getInt("countDown"));
					}
					if (orderObject.has("remark"))
						order.setRemark(orderObject.getString("remark"));
				}
				if (obj.has("refund")) {
					RefundVo refund = new RefundVo();
					JSONObject refundObject = obj.getJSONObject("refund");
					if (refundObject.has("orderId"))
						refund.setOrderId(refundObject.getString("orderId"));
					if (refundObject.has("splitOrderId"))
						refund.setSplitOrderId(refundObject
								.getString("splitOrderId"));
					if (refundObject.has("payBackFee"))
						refund.setPayBackFee(refundObject
								.getString("payBackFee"));
					if (refundObject.has("reason"))
						refund.setReason(refundObject.getString("reason"));
					if (refundObject.has("state"))
						refund.setState(refundObject.getString("state"));
					if (refundObject.has("contactTel"))
						refund.setContactTel(refundObject
								.getString("contactTel"));
					if (refundObject.has("rejectReason"))
						refund.setRejectReason(refundObject
								.getString("rejectReason"));
					if (refundObject.has("refundType"))
						refund.setRefundType(refundObject
								.getString("refundType"));
					order.setRefund(refund);

				}
				if (obj.has("sku")) {
					JSONArray skuArray = obj.getJSONArray("sku");
					List<Sku> skuList = new ArrayList<Sku>();
					for (int j = 0; j < skuArray.length(); j++) {
						JSONObject skuObject = skuArray.getJSONObject(j);
						Sku sku = new Sku();
						if (skuObject.has("skuId"))
							sku.setSkuId(skuObject.getString("skuId"));
						if (skuObject.has("amount"))
							sku.setAmount(skuObject.getInt("amount"));
						if (skuObject.has("price"))
							sku.setPrice_(skuObject.getDouble("price"));
						if (skuObject.has("skuTitle"))
							sku.setSkuTitle(decode2(skuObject
									.getString("skuTitle")));
						if (skuObject.has("invImg")) {
							JSONObject imgObj = new JSONObject(
									skuObject.getString("invImg"));
							if (imgObj.has("url"))
								sku.setInvImg(imgObj.getString("url"));
						}
						if (skuObject.has("invUrl"))
							sku.setInvUrl(skuObject.getString("invUrl"));
						if (skuObject.has("itemColor"))
							sku.setItemColor(skuObject.getString("itemColor"));
						if (skuObject.has("itemSize"))
							sku.setItemSize(skuObject.getString("itemSize"));
						if (skuObject.has("skuType"))
							sku.setSkuType(skuObject.getString("skuType"));
						if (skuObject.has("skuTypeId"))
							sku.setSkuTypeId(skuObject.getString("skuTypeId"));
						skuList.add(sku);
					}
					order.setList(skuList);
				}
				list.add(order);
				orderList.setList(list);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return orderList;
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

	public static ShoppingCar parserShoppingCar(String result) {
		ShoppingCar car = new ShoppingCar();
		List<CustomsVo> customs = new ArrayList<CustomsVo>();
		HMessage msg = new HMessage();
		try {
			JSONObject object = new JSONObject(result);
			JSONObject msgObject = object.getJSONObject("message");
			if (msgObject.has("message"))
				msg.setMessage(msgObject.getString("message"));
			if (msgObject.has("code"))
				msg.setCode(msgObject.getInt("code"));
			if (object.has("cartList")) {
				JSONArray array = object.getJSONArray("cartList");
				for (int i = 0; i < array.length(); i++) {	
					JSONObject obj = array.getJSONObject(i);
					List<ShoppingGoods> list = new ArrayList<ShoppingGoods>();
					CustomsVo custom = new CustomsVo();
					if (obj.has("invCustoms"))
						custom.setInvCustoms(obj.getString("invCustoms"));
					if (obj.has("invArea"))
						custom.setInvArea(obj.getString("invArea"));
					if (obj.has("postalLimit"))
						custom.setPostalLimit(obj.getInt("postalLimit"));
					if (obj.has("freeShip"))
						custom.setPostFee(obj.getInt("freeShip"));
					if (obj.has("postalStandard"))
						custom.setPostalStandard(obj.getInt("postalStandard"));
					if (obj.has("invAreaNm"))
						custom.setInvAreaNm(obj.getString("invAreaNm"));
					custom.setState("");
					custom.setTax("0");
					JSONArray cartsArray = obj.getJSONArray("carts");
					for (int j = 0; j < cartsArray.length(); j++) {
						JSONObject goodsObject = cartsArray.getJSONObject(j);
						ShoppingGoods goods = new ShoppingGoods();
						if(goodsObject.has("orCheck"))
							goods.setOrCheck(goodsObject.getString("orCheck"));
						if (goodsObject.has("cartId"))
							goods.setCartId(goodsObject.getString("cartId"));
						if (goodsObject.has("skuId"))
							goods.setGoodsId(goodsObject.getString("skuId"));
						if (goodsObject.has("amount"))
							goods.setGoodsNums(goodsObject.getInt("amount"));
						if (goodsObject.has("itemColor"))
							goods.setItemColor(goodsObject
									.getString("itemColor"));
						if (goodsObject.has("itemSize"))
							goods.setItemSize(goodsObject.getString("itemSize"));
						if (goodsObject.has("itemPrice"))
							goods.setGoodsPrice(goodsObject
									.getDouble("itemPrice"));
						if (goodsObject.has("state")) {
//							if (goodsObject.getString("state").equals("G")) {
//								goods.setState("I");
//							} else {
								goods.setState(goodsObject.getString("state"));
//							}
						}
						if (goodsObject.has("shipFee"))
							goods.setShipFee(goodsObject.getString("shipFee"));
						if (goodsObject.has("invArea"))
							goods.setInvArea(goodsObject.getString("invArea"));
						if (goodsObject.has("restrictAmount"))
							goods.setRestrictAmount(goodsObject
									.getInt("restrictAmount"));
						if (goodsObject.has("restAmount"))
							goods.setRestAmount(goodsObject
									.getInt("restAmount"));
						if (goodsObject.has("invImg")) {
							JSONObject imgObj = new JSONObject(
									goodsObject.getString("invImg"));
							if (imgObj.has("url"))
								goods.setGoodsImg(imgObj.getString("url"));
						}
						if (goodsObject.has("invUrl"))
							goods.setGoodsUrl(goodsObject.getString("invUrl"));
						if (goodsObject.has("invTitle"))
							goods.setGoodsName(goodsObject
									.getString("invTitle"));
						if (goodsObject.has("cartDelUrl"))
							goods.setDelUrl(goodsObject.getString("cartDelUrl"));
						if (goodsObject.has("invCustoms"))
							goods.setInvCustoms(goodsObject
									.getString("invCustoms"));
						if (goodsObject.has("postalTaxRate"))
							// {
							// if(goodsObject.getInt("postalTaxRate") >= 0){
							// goods.setPostalTaxRate(goodsObject.getInt("postalTaxRate"));
							// }else{
							// goods.setPostalTaxRate(0);
							// }
							// }
							goods.setPostalTaxRate(goodsObject
									.getString("postalTaxRate"));
						// if(goodsObject.has("postalStandard"))
						// goods.setPostalStandard(goodsObject.getInt("postalStandard"));
						// if(goodsObject.has("postalLimit"))
						// goods.setPostalLimit(goodsObject.getInt("postalLimit"));
						if (goodsObject.has("skuType"))
							goods.setSkuType(goodsObject.getString("skuType"));
						if (goodsObject.has("skuTypeId"))
							goods.setSkuTypeId(goodsObject
									.getString("skuTypeId"));
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

	public static HMessage paserResultMsg(String result) {
		HMessage msg = new HMessage();
		try {
			JSONObject object = new JSONObject(result);
			if (object.has("message")) {
				JSONObject obj = object.getJSONObject("message");
				if (obj.has("message"))
					msg.setMessage(obj.getString("message"));
				if (obj.has("code"))
					msg.setCode(obj.getInt("code"));
			}
			if (object.has("result")) {
				JSONObject userObject = object.getJSONObject("result");
				if (userObject.has("token"))
					msg.setTag(userObject.getString("token"));
				if (userObject.has("expired"))
					msg.setTime(userObject.getInt("expired"));
				if (userObject.has("id"))
					msg.setUserId(userObject.getInt("id") + "");
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
			if (obj.has("name"))
				user.setUserName(obj.getString("name"));
			if (obj.has("photo"))
				user.setUserImg(obj.getString("photo"));
			if (obj.has("phoneNum"))
				user.setPhone(obj.getString("phoneNum"));
			if (obj.has("couponsCount"))
				user.setCouponCount(obj.getInt("couponsCount"));
			if (obj.has("gender"))
				user.setSex(obj.getString("gender"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public static CollectionVo parserCollect(String result) {
		CollectionVo collectionInfo = new CollectionVo();
		try {
			JSONObject object = new JSONObject(result);
			if (object.has("message")) {
				HMessage hMessage = new HMessage();
				JSONObject mObject = object.getJSONObject("message");
				if (mObject.has("message"))
					hMessage.setMessage(mObject.getString("message"));
				if (mObject.has("code"))
					hMessage.setCode(mObject.getInt("code"));
				collectionInfo.setMessage(hMessage);
			}
			if (object.has("collectList")) {
				List<Collection> list = new ArrayList<Collection>();
				JSONArray array = object.getJSONArray("collectList");
				for (int i = 0; i < array.length(); i++) {
					Collection collection = new Collection();
					JSONObject obj = array.getJSONObject(i);
					if (obj.has("collectId"))
						collection.setCollectId(obj.getString("collectId"));
					if (obj.has("createAt"))
						collection.setCreateAt(obj.getLong("collectId"));
					if (obj.has("skuType"))
						collection.setSkuType(obj.getString("skuType"));
					if (obj.has("skuTypeId"))
						collection.setSkuTypeId(obj.getString("skuTypeId"));
					if (obj.has("cartSkuDto")) {
						Sku sku = new Sku();
						JSONObject skuObject = obj.getJSONObject("cartSkuDto");
						if (skuObject.has("skuId"))
							sku.setSkuId(skuObject.getString("skuId"));
						if (skuObject.has("price"))
							sku.setPrice_(skuObject.getDouble("price"));
						if (skuObject.has("skuTitle"))
							sku.setSkuTitle(skuObject.getString("skuTitle"));
						if (skuObject.has("invUrl"))
							sku.setInvUrl(skuObject.getString("invUrl"));
						if (skuObject.has("invImg")) {
							JSONObject imgObj = new JSONObject(
									skuObject.getString("invImg"));
							if (imgObj.has("url"))
								sku.setInvImg(imgObj.getString("url"));
						}
						if (skuObject.has("itemColor"))
							sku.setItemColor(skuObject.getString("itemColor"));
						if (skuObject.has("itemSize"))
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

	public static int parserCollectId(String result) {
		int collectionId = 0;
		try {
			JSONObject object = new JSONObject(result);
			if (object.has("collectId")) {
				collectionId = object.getInt("collectId");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return collectionId;
	}

	public static Notify parserJPush(String result) {
		Notify notify = new Notify();
		try {
			JSONObject object = new JSONObject(result);
			if (object.has("url"))
				notify.setUrl(object.getString("url"));
			if (object.has("targetType"))
				notify.setTargetType(object.getString("targetType"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return notify;
	}

	public static PushMessageTypeInfo parseMsgType(String result) {
		PushMessageTypeInfo typeInfo = new PushMessageTypeInfo();
		JSONObject object;
		try {
			object = new JSONObject(result);

			if (object.has("message")) {
				HMessage message = new HMessage();
				JSONObject msgObject = object.getJSONObject("message");
				if (msgObject.has("code"))
					message.setCode(msgObject.getInt("code"));
				if (msgObject.has("message"))
					message.setMessage(msgObject.getString("message"));
				typeInfo.setMessage(message);
			}
			if (object.has("msgTypeDTOList")) {
				List<PushMessageType> list = new ArrayList<PushMessageType>();
				JSONArray array = object.getJSONArray("msgTypeDTOList");
				for (int i = 0; i < array.length(); i++) {
					PushMessageType type = new PushMessageType();
					JSONObject obj = array.getJSONObject(i);
					if (obj.has("msgType"))
						type.setType(obj.getString("msgType"));
					if (obj.has("num"))
						type.setNum(obj.getInt("num"));
					if (obj.has("content"))
						type.setContent(obj.getString("content"));
					if (obj.has("createAt"))
						type.setTime(obj.getLong("createAt"));
					list.add(type);

				}
				typeInfo.setList(list);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return typeInfo;
	}

	public static PushMessageResult parseMsgInfo(String result) {
		PushMessageResult msgResult = new PushMessageResult();
		try {
			JSONObject object = new JSONObject(result);
			if (object.has("msgList")) {
				List<PushMessageVo> list = new ArrayList<PushMessageVo>();
				JSONArray array = object.getJSONArray("msgList");
				for (int i = 0; i < array.length(); i++) {
					PushMessageVo info = new PushMessageVo();
					JSONObject obj = array.getJSONObject(i);
					if (obj.has("id"))
						info.setMsgId(obj.getString("id"));
					if (obj.has("msgTitle"))
						info.setMsgTitle(obj.getString("msgTitle"));
					if (obj.has("msgContent"))
						info.setMsgContent(obj.getString("msgContent"));
					if (obj.has("msgImg"))
						info.setMsgImg(obj.getString("msgImg"));
					if (obj.has("msgUrl"))
						info.setMsgUrl(obj.getString("msgUrl"));
					if (obj.has("msgType"))
						info.setMsgType(obj.getString("msgType"));
					if (obj.has("createAt"))
						info.setCreateAt(obj.getLong("createAt"));
					if (obj.has("targetType"))
						info.setTargetType(obj.getString("targetType"));
					list.add(info);
				}
				msgResult.setList(list);
			}
			HMessage message = new HMessage();
			if (object.has("message")) {
				JSONObject msgObject = object.getJSONObject("message");
				if (msgObject.has("code"))
					message.setCode(msgObject.getInt("code"));
				if (msgObject.has("message"))
					message.setMessage(msgObject.getString("message"));
				msgResult.setMessage(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msgResult;
	}

	public static Logistics parserLogistics(String result) {
		Logistics logistics = new Logistics();
		try {
			JSONObject object = new JSONObject(result);
			if (object.has("expressNum"))
				logistics.setNu(object.getString("expressNum"));
			// if(object.has("comcontact"))
			// logistics.setComcontact(object.getString("comcontact"));
			// if(object.has("companytype"))
			// logistics.setCompanytype(object.getString("companytype"));
			if (object.has("expressName"))
				logistics.setCom(object.getString("expressName"));
			// if(object.has("signname"))
			// logistics.setSignname(object.getString("signname"));
			if (object.has("condition"))
				logistics.setCondition(object.getString("condition"));
			if (object.has("status"))
				logistics.setStatus(object.getString("status"));
			// if(object.has("codenumber"))
			// logistics.setCodenumber(object.getString("codenumber"));
			// if(object.has("signedtime"))
			// logistics.setSignedtime(object.getString("signedtime"));
			if (object.has("state"))
				logistics.setState(object.getInt("state"));
			// if(object.has("addressee"))
			// logistics.setAddressee(object.getString("addressee"));
			// if(object.has("departure"))
			// logistics.setDeparture(object.getString("departure"));
			// if(object.has("destination"))
			// logistics.setDestination(object.getString("destination"));
			if (object.has("message"))
				logistics.setMessage(object.getString("message"));
			if (object.has("ischeck"))
				logistics.setIscheck(object.getString("ischeck"));
			// if(object.has("pickuptime"))
			// logistics.setPickuptime(object.getString("pickuptime"));
			// if(object.has("comurl"))
			// logistics.setComurl(object.getString("comurl"));
			if (object.has("data")) {
				JSONArray array = object.getJSONArray("data");
				List<LogisticsData> list = new ArrayList<LogisticsData>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					LogisticsData logisticsData = new LogisticsData();
					if (obj.has("time"))
						logisticsData.setTime(obj.getString("time"));
					if (obj.has("context"))
						logisticsData.setContent(obj.getString("context"));
					list.add(logisticsData);
				}
				logistics.setList(list);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return logistics;
	}

//	public static CommentCenter parserCommentCenter(String result) {
//		CommentCenter center = new CommentCenter();
//		try {
//			JSONObject object = new JSONObject(result);
//			if (object.has("message")) {
//				HMessage message = new HMessage();
//				JSONObject msgObject = object.getJSONObject("message");
//				if (msgObject.has("message"))
//					message.setMessage(msgObject.getString("message"));
//				if (msgObject.has("code"))
//					message.setCode(msgObject.getInt("code"));
//				center.setMessage(message);
//			}
//			if (object.has("orderRemark")) {
//				List<OrderRemark> list = new ArrayList<OrderRemark>();
//				JSONArray array = object.getJSONArray("orderRemark");
//				for (int i = 0; i < array.length(); i++) {
//					OrderRemark remark = new OrderRemark();
//					JSONObject obj = array.getJSONObject(i);
//					if (obj.has("orderLine")) {
//						Sku sku = new Sku();
//						JSONObject skuObject = obj.getJSONObject("orderLine");
//						if (skuObject.has("orderId"))
//							sku.setOrderId(skuObject.getString("orderId"));
//						if (skuObject.has("skuId"))
//							sku.setSkuId(skuObject.getString("skuId"));
//						if (skuObject.has("amount"))
//							sku.setAmount(skuObject.getInt("amount"));
//						if (skuObject.has("price"))
//							sku.setPrice(skuObject.getInt("price"));
//						if (skuObject.has("skuTitle"))
//							sku.setSkuTitle(decode2(skuObject
//									.getString("skuTitle")));
//						if (skuObject.has("invImg")) {
//							JSONObject imgObj = new JSONObject(
//									skuObject.getString("invImg"));
//							if (imgObj.has("url"))
//								sku.setInvImg(imgObj.getString("url"));
//						}
//						if (skuObject.has("invUrl"))
//							sku.setInvUrl(skuObject.getString("invUrl"));
//						if (skuObject.has("itemColor"))
//							sku.setItemColor(skuObject.getString("itemColor"));
//						if (skuObject.has("itemSize"))
//							sku.setItemSize(skuObject.getString("itemSize"));
//						if (skuObject.has("skuType"))
//							sku.setSkuType(skuObject.getString("skuType"));
//						if (skuObject.has("skuTypeId"))
//							sku.setSkuTypeId(skuObject.getString("skuTypeId"));
//						remark.setSku(sku);
//					}
//					if (obj.has("comment")) {
//						Comment comment = new Comment();
//						JSONObject commentObject = obj.getJSONObject("comment");
//						if (commentObject.has("createAt"))
//							comment.setCreateAt(commentObject
//									.getString("createAt"));
//						if (commentObject.has("content"))
//							comment.setComment(commentObject
//									.getString("content"));
//						if (commentObject.has("picture")) {
//							if (commentObject.getString("picture").equals("")
//									&& !commentObject.getString("picture")
//											.equals("null")) {
//								// String path =
//								// commentObject.getString("picture");
//								if (commentObject.getString("picture") != null) {
//									ArrayList<String> arrayList = new ArrayList<>();
//									JSONArray jsonArray = new JSONArray(
//											commentObject.getString("picture"));
//									for (int j = 0; j < jsonArray.length(); j++) {
//										arrayList.add(jsonArray.getString(j));
//									}
//									comment.setPhotoList(arrayList);
//								}
//							}
//							if (commentObject.has("grade"))
//								comment.setScore(commentObject.getInt("grade"));
//							remark.setComment(comment);
//						}
//						list.add(remark);
//					}
//					center.setList(list);
//				}
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return center;
//
//	}

}
