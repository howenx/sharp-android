package com.hanmimei.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hanmimei.entity.Customs;
import com.hanmimei.entity.OrderSubmit;
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
					jsonn.put("cartId", sg.getCartId());
					jsonn.put("skuId", sg.getGoodsId());
					jsonn.put("amount", sg.getGoodsNums());
					jsonn.put("state", sg.getState());
					arrayy.put(jsonn);
				}
				json.put("cartDtos", arrayy);
				array.put(json);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;
	}
	
	public static JSONObject OrderSubmitPaser(OrderSubmit os){
		JSONObject json = new JSONObject();
		try {
			json.put("settleDTOs", os.getSettleDtos());
			json.put("addressId", os.getAddressId());
			json.put("couponId", os.getCouponId());
			json.put("clientIp", os.getClientIp());
			json.put("clientType", os.getClientType());
			json.put("shipTime", os.getShipTime());
//			json.put("orderDesc", os.getOrderDesc());
			json.put("payMethod", os.getPayMethod());
			json.put("buyNow", os.getBuyNow());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
		
	}
}
