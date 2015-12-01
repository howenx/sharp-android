package com.hanbimei.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.hanbimei.entity.Adress;
import com.hanbimei.entity.GoodsDetail;
import com.hanbimei.entity.GoodsDetail.ItemFeature;
import com.hanbimei.entity.HMessage;
import com.hanbimei.entity.Order;
import com.hanbimei.entity.Result;
import com.hanbimei.entity.Sku;
import com.hanbimei.entity.Slider;
import com.hanbimei.entity.Theme;
import com.hanbimei.entity.ThemeDetail;
import com.hanbimei.entity.ThemeItem;

public class DataParser {
	public static List<Theme> parserHome(String result) {
		List<Theme> list = new ArrayList<Theme>();
		try {
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("theme");
			for (int i = 0; i < array.length(); i++) {
				Theme theme = new Theme();
				JSONObject obj = array.getJSONObject(i);
				if (obj.has("id"))
					theme.setId(obj.getLong("id"));
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
				list.add(slider);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;

	}

	public static ThemeDetail parserThemeItem(String result) {
		ThemeDetail detail = new ThemeDetail();
		JSONObject obj = null;
		HMessage msg = new HMessage();
		List<ThemeItem> themeList = new ArrayList<ThemeItem>();
		ThemeItem t = null;
		try {
			obj = new JSONObject(result);
			JSONObject json = obj.getJSONObject("message");
			msg.setMessage(json.getString("message"));
			msg.setCode(json.getInt("code"));
			detail.setMessage(msg);

			JSONArray array = new JSONArray(obj.getString("themeList"));

			for (int i = 0; i < array.length(); i++) {
				JSONObject jjson = array.getJSONObject(i);
				t = new ThemeItem();
				t.setItemTitle(jjson.getString("itemTitle"));
				t.setItemMasterImg(jjson.getString("itemMasterImg"));
				t.setCollectCount(jjson.getInt("collectCount"));
				t.setThemeId(jjson.getInt("themeId"));
				t.setItemDiscount(jjson.getInt("itemDiscount"));
				t.setItemSoldAmount(jjson.getInt("itemSoldAmount"));
				t.setItemId(jjson.getInt("itemId"));
				t.setItemImg(jjson.getString("itemImg"));
				t.setItemSrcPrice(jjson.getInt("itemSrcPrice"));
				t.setMasterItemTag(jjson.getString("masterItemTag"));
				t.setOrMasterItem(jjson.getBoolean("orMasterItem"));
				t.setItemPrice(jjson.getInt("itemPrice"));
				t.setState(jjson.getString("state"));
				t.setItemUrl(jjson.getString("itemUrl"));

				if (t.getOrMasterItem()) {
					detail.setMasterItem(t);
				} else {
					themeList.add(t);
				}
			}

			detail.setThemeList(themeList);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return detail;
	}

	public static List<Adress> parserAddressList(String result) {
		List<Adress> list = new ArrayList<Adress>();
		try {
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("address");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Adress adress = new Adress();
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

<<<<<<< HEAD
	// 商品详情页 －－ 商品详情数据解析
	public static GoodsDetail parserGoodsDetail(String result) {
=======
	
	//商品详情页 －－ 商品详情数据解析
	public static GoodsDetail parserGoodsDetail(String result){
>>>>>>> ae51f586ce0cd0635d732ad47610e40e4d9f6050
		GoodsDetail detail = new GoodsDetail();
		try {
			JSONObject obj = new JSONObject(result);
			JSONObject objMain = obj.getJSONObject("main");
			GoodsDetail.Main main = detail.new Main();

			main.setId(objMain.getInt("id"));
			main.setItemTitle(objMain.getString("itemTitle"));
			main.setItemMasterImg(objMain.getString("itemMasterImg"));
			main.setOnShelvesAt(objMain.getString("onShelvesAt"));
			main.setOffShelvesAt(objMain.getString("offShelvesAt"));
			JSONArray array = new JSONArray(objMain.getString("itemDetailImgs"));
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < array.length(); i++) {
				list.add(array.getString(i));
			}
			main.setItemDetailImgs(list);
			JSONObject json = new JSONObject(objMain.getString("itemFeatures"));
			Iterator<String> it = json.keys();
			List<ItemFeature> itemFeatures = new ArrayList<ItemFeature>();
			ItemFeature itemFeature = null;
			while (it.hasNext()) {
				String key = it.next();
				String value = json.get(key).toString();
				itemFeature = detail.new ItemFeature(key,value);
				itemFeatures.add(itemFeature);
			}
			main.setItemFeatures(itemFeatures);
			main.setThemeId(objMain.getInt("themeId"));
			main.setState(objMain.getString("state"));
			main.setShareUrl(objMain.getString("shareUrl"));
			main.setCollectCount(objMain.getInt("collectCount"));
			main.setItemNotice(objMain.getString("itemNotice"));
			array = new JSONArray(objMain.getString("publicity"));
			list = new ArrayList<String>();
			for (int i = 0; i < array.length(); i++) {
				list.add(array.getString(i));
			}
			main.setPublicity(list);
			main.setMasterInvId(objMain.getInt("masterInvId"));

			detail.setMain(main);

			GoodsDetail.Stock stock = null;
			List<GoodsDetail.Stock> listt = new ArrayList<GoodsDetail.Stock>();
			JSONArray arrayStock = obj.getJSONArray("stock");
			for (int j = 0; j < arrayStock.length(); j++) {
				JSONObject objStock = arrayStock.getJSONObject(j);
				stock = detail.new Stock();
				stock.setId(objStock.getInt("id"));
				stock.setItemColor(objStock.getString("itemColor"));
				stock.setItemSize(objStock.getString("itemSize"));
				stock.setItemSrcPrice(objStock.getDouble("itemSrcPrice"));
				stock.setItemPrice(objStock.getDouble("itemPrice"));
				stock.setItemDiscount(objStock.getDouble("itemDiscount"));
				stock.setOrMasterInv(objStock.getBoolean("orMasterInv"));
				stock.setState(objStock.getString("state"));
				stock.setShipFee(objStock.getInt("shipFee"));
				stock.setInvArea(objStock.getString("invArea"));
				stock.setRestrictAmount(objStock.getInt("restrictAmount"));
				stock.setRestAmount(objStock.getInt("restAmount"));
				stock.setInvImg(objStock.getString("invImg"));
				array = new JSONArray(objStock.getString("itemPreviewImgs"));
				list = new ArrayList<String>();
				for (int i = 0; i < array.length(); i++) {
					list.add(array.getString(i));
				}
				stock.setItemPreviewImgs(list);
				stock.setInvImg(objStock.getString("invImg"));
				stock.setInvTitle(objStock.getString("invTitle"));
				listt.add(stock);
			}
			detail.setStocks(listt);
		} catch (JSONException e) {
		}

		return detail;
	}
<<<<<<< HEAD

	public static Result parserLoginResult(String str) {
=======
	public static Result parserLoginResult(String str){
>>>>>>> ae51f586ce0cd0635d732ad47610e40e4d9f6050
		Result result = new Result();
		try {
			JSONObject object = new JSONObject(str);
			if (object.has("result"))
				result.setSuccess(object.getBoolean("result"));
			if (object.has("message"))
				result.setMessage(object.getString("message"));

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
					Adress adress = new Adress();
					if(addObject.has("addId"))
						adress.setAdress_id(addObject.getInt("addId"));
					if(addObject.has("tel"))
						adress.setPhone(addObject.getString("tel"));
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
						order.setPayTotal(orderObject.getInt("payTotal"));
					if(orderObject.has("payMethod"))
						order.setPayMethod(orderObject.getString("payMethod"));
					if(orderObject.has("orderCreateAt"))
						order.setOrderCreateAt(orderObject.getString("orderCreateAt"));
					if(orderObject.has("orderStatus"))
						order.setOrderStatus(orderObject.getString("orderStatus"));
					if(orderObject.has("discount"))
						order.setDiscount(orderObject.getInt("discount"));
					if(orderObject.has("orderDesc"))
						order.setOrderDesc(orderObject.getString("orderDesc"));
					if(orderObject.has("addId"))
						order.setAddId(orderObject.getInt("addId"));
					if(orderObject.has("shipFee"))
						order.setShipFee(orderObject.getInt("shipFee"));
					if(orderObject.has("orderDetailUrl"))
						order.setOrderDetailUrl(orderObject.getString("orderDetailUrl"));
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
}
