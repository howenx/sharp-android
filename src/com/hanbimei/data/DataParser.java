package com.hanbimei.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hanbimei.entity.Adress;
import com.hanbimei.entity.Result;
import com.hanbimei.entity.Slider;
import com.hanbimei.entity.Theme;
import com.hanbimei.entity.ThemeItem;

public class DataParser {
	public static List<Theme> parserHome(String result){
		List<Theme> list = new ArrayList<Theme>();
		try {
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("theme");
			for(int i = 0; i < array.length(); i ++){
				Theme theme = new Theme();
				JSONObject obj = array.getJSONObject(i);
				if(obj.has("id"))
					theme.setId(obj.getLong("id"));
				if(obj.has("sortNu"))
					theme.setSortNum(obj.getInt("sortNu"));
				if(obj.has("masterItemId"))
					theme.setItem_id(obj.getInt("masterItemId"));
				if(obj.has("themeImg"))
					theme.setThemeImg(obj.getString("themeImg"));
				if(obj.has("themeUrl"))
					theme.setThemeUrl(obj.getString("themeUrl"));
				list.add(theme);
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
		
	}
	public static List<Slider> parserSlider(String result){
		List<Slider> list = new ArrayList<Slider>();
		try {
			JSONObject obj = new JSONObject(result);
			JSONArray array = obj.getJSONArray("slider");
			for(int i = 0; i < array.length(); i ++){
				Slider slider = new Slider();
				slider.setImgUrl(array.get(i).toString());
				list.add(slider);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
		
	}
	public static List<ThemeItem> parserThemeItem(String result){
		List<ThemeItem> list = new ArrayList<ThemeItem>();
		try {
			JSONArray array = new JSONArray(result);
			for(int i = 0; i < array.length(); i ++){
				JSONObject obj = array.getJSONObject(i);
				ThemeItem item = new ThemeItem();
				if(obj.has("themeId"))
					item.setThemeId(obj.getInt("themeId"));
				if(obj.has("itemId"))
					item.setItemId(obj.getInt("itemId"));
				if(obj.has("itemImg"))
					item.setItemImg(obj.getString("itemImg"));
				if(obj.has("itemUrl"))
					item.setItemUrl(obj.getString("itemUrl"));
				if(obj.has("itemTitle"))
					item.setItemTitle(obj.getString("itemTitle"));
				if(obj.has("itemPrice"))
					item.setItemPrice(obj.getInt("itemPrice"));
				if(obj.has("itemCostPrice"))
					item.setItemCostPrice(obj.getInt("itemCostPrice"));
				if(obj.has("itemDiscount"))
					item.setItemDiscount(obj.getInt("itemDiscount"));
				if(obj.has("itemSoldAmount"))
					item.setItemSoldAmount(obj.getInt("itemSoldAmount"));
				if(obj.has("orMasterItem"))
					item.setOrMasterItem(obj.getBoolean("orMasterItem"));
				if(obj.has("masterItemTag"))
					item.setMasterItemTag(obj.getString("masterItemTag"));
				if(obj.has("collectCount"))
					item.setCollectCount(obj.getInt("collectCount"));
				if(obj.has("masterItemImg"))
					item.setMasterItemImg(obj.getString("masterItemImg"));
				if(obj.has("onShelvesAt"))
					item.setOnShelvesAt(obj.getString("onShelvesAt"));
				if(obj.has("offShelvesAt"))
					item.setOffShelvesAt(obj.getString("offShelvesAt"));
				list.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static List<Adress> parserAddressList(String result){
		List<Adress> list = new ArrayList<Adress>();
		try {
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("address");
			for(int i = 0; i < array.length(); i ++){
				JSONObject obj = array.getJSONObject(i);
				Adress adress = new Adress();
				if(obj.has("addId"))
					adress.setAdress_id(obj.getInt("addId"));
//				if(obj.has("userId"))
//					adress.setUser_id(obj.getInt("userId"));
				if(obj.has("tel"))
					adress.setPhone(obj.getString("tel"));
				if(obj.has("name"))
					adress.setName(obj.getString("name"));
				if(obj.has("deliveryCity"))
					adress.setCity(obj.getString("deliveryCity"));
				if(obj.has("deliveryDetail"))
					adress.setAdress(obj.getString("deliveryDetail"));
				list.add(adress);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;	
	}
	public static Result parserResult(String str){
		Result result = new Result();
		try {
			JSONObject object = new JSONObject(str);
			if(object.has("message")){
				JSONObject obj = new JSONObject(object.getString("message"));
				if(obj.has("code"))
					result.setCode(obj.getInt("code"));
				if(obj.has("message"))
					result.setMessage(obj.getString("message"));
			}
			if(object.has("address")){
				JSONObject obj = new JSONObject(object.getString("address"));
				if(obj.has("addId"))
					result.setResult_id(obj.getInt("addId"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

}
