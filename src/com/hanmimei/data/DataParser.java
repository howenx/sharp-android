package com.hanmimei.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hanmimei.entity.HMMAddress;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.GoodsDetail;
import com.hanmimei.entity.HMMThemeGoods;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.Order;
import com.hanmimei.entity.Result;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.Sku;
import com.hanmimei.entity.Slider;
import com.hanmimei.entity.Theme;
import com.hanmimei.entity.User;

public class DataParser {
	public static List<Theme> parserHome(String result) {
		List<Theme> list = new ArrayList<Theme>();
		try {
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("theme");
			for (int i = 0; i < array.length(); i++) {
				Theme theme = new Theme();
				JSONObject obj = array.getJSONObject(i);
				if(obj.has("id"))
					theme.setItem_id(obj.getInt("id"));
				if (obj.has("themeImg"))
					theme.setThemeImg(obj.getString("themeImg"));
				if (obj.has("themeUrl"))
					theme.setThemeUrl(obj.getString("themeUrl"));
				list.add(theme);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;

	}

	public static List<Slider> parserSlider(String result) {
		List<Slider> list = new ArrayList<Slider>();
		try {
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("slider");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Slider slider = new Slider();
				if (obj.has("url"))
					slider.setImgUrl(obj.getString("url"));
				if (obj.has("itemTarget"))
					slider.setUrl(obj.getString("itemTarget"));
				if (obj.has("itemTargetAndroid"))
					slider.setUrl(obj.getString("itemTargetAndroid"));
				if(obj.has("targetType"))
					slider.setType(obj.getString("targetType"));
				list.add(slider);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;

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
	public static Result parserUpImg(String str){
		Result result = new Result();
		try {
			JSONObject object = new JSONObject(str);
			if(object.has("message"))
				result.setMessage(object.getString("message"));
			if(object.has("code"))
				result.setCode(object.getInt("code"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}


	
	//商品详情页 －－ 商品详情数据解析
	public static GoodsDetail parserGoodsDetail(String result){
		return new Gson().fromJson(result, GoodsDetail.class);
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
		List<Order> list = new ArrayList<Order>();
		try {
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("orderList");
			for(int i = 0; i < array.length(); i ++){
				Order order = new Order();
				JSONObject obj = array.getJSONObject(i);
				if(obj.has("address")){
					JSONObject addObject = obj.getJSONObject("address");
					HMMAddress adress = new HMMAddress();
//					if(addObject.has("addId")){
//						if(!addObject.getString("addId").equals("null"))
//						adress.setAdress_id(addObject.getInt("addId"));
//					}
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
						order.setOrderId(orderObject.getInt("orderId")+"");
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
					if(orderObject.has("countDown"))
						order.setCountDown(orderObject.getInt("countDown"));
				}
				if(obj.has("sku")){
					JSONArray skuArray = obj.getJSONArray("sku");
					List<Sku> skuList = new ArrayList<Sku>();
					for(int j = 0; j < skuArray.length(); j ++){
						JSONObject skuObject = skuArray.getJSONObject(j);
						Sku sku = new Sku();
						if(skuObject.has("skuId"))
							sku.setSkuId(skuObject.getInt("skuId"));
						if(skuObject.has("amount"))
							sku.setAmount(skuObject.getInt("amount"));
						if(skuObject.has("price"))
							sku.setPrice(skuObject.getInt("price"));
						if(skuObject.has("skuTitle"))
							sku.setSkuTitle(skuObject.getString("skuTitle"));
						if(skuObject.has("invImg"))
							sku.setInvImg(skuObject.getString("invImg"));
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
	public static ShoppingCar parserShoppingCar(String result){
		ShoppingCar car = new ShoppingCar();
		List<Customs> customs = new ArrayList<Customs>();
		HMessage msg = new HMessage();
		try {
			JSONObject object = new JSONObject(result);
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
				custom.setState("");
				JSONArray cartsArray = obj.getJSONArray("carts");
				for(int j = 0; j < cartsArray.length(); j ++){
					JSONObject goodsObject = cartsArray.getJSONObject(j);
					ShoppingGoods goods = new ShoppingGoods();
					if(goodsObject.has("cartId"))
						goods.setCartId(goodsObject.getInt("cartId"));
					if(goodsObject.has("skuId"))
						goods.setGoodsId(goodsObject.getInt("skuId"));
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
					if(goodsObject.has("invImg"))
						goods.setGoodsImg(goodsObject.getString("invImg"));
					if(goodsObject.has("invUrlAndroid"))
						goods.setGoodsUrl(goodsObject.getString("invUrlAndroid"));
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
					list.add(goods);
				}
				custom.setList(list);
				customs.add(custom);
			}
			JSONObject msgObject = object.getJSONObject("message");
			if(msgObject.has("message"))
				msg.setMessage(msgObject.getString("message"));
			if(msgObject.has("code"))
				msg.setCode(msgObject.getInt("code"));
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
			JSONObject obj = object.getJSONObject("message");
			if(obj.has("message"))
				msg.setMessage(obj.getString("message"));
			if(obj.has("code"))
				msg.setCode(obj.getInt("code"));
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
	
}
