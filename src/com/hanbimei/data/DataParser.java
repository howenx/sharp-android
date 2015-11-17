package com.hanbimei.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hanbimei.entity.Slider;
import com.hanbimei.entity.Theme;

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

}
