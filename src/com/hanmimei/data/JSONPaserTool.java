package com.hanmimei.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hanmimei.entity.Customs;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;

public class JSONPaserTool {

	public static JSONArray ClientSettlePaser(ShoppingCar car) {
		JSONArray array = new JSONArray();
		try {
			for (Customs cs : car.getList()) {
				JSONObject json = new JSONObject();

				json.put("invCustoms", cs.getInvCustoms());
				json.put("invArea", cs.getInvArea());
				JSONArray arrayy = new JSONArray();
				for(ShoppingGoods sg : cs.getList()){
					JSONObject jsonn = new JSONObject();
					jsonn.put("cartId", null);
					jsonn.put("skuId", sg.getGoodsId());
					jsonn.put("amount", sg.getGoodsNums());
					jsonn.put("state", null);
					arrayy.put(jsonn);
				}
				json.put("cartDtos", arrayy);
//				json.put("addressId", value);
				array.put(json);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;
	}
}
