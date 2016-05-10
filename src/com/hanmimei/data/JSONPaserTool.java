package com.hanmimei.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hanmimei.entity.CustomsVo;
import com.hanmimei.entity.OrderSubmitVo;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;

public class JSONPaserTool {

	public static JSONArray ClientSettlePaser(ShoppingCar car) {
		JSONArray array = new JSONArray();
		try {
			for (CustomsVo cs : car.getList()) {
				JSONObject json = new JSONObject();

				json.put("invCustoms", cs.getInvCustoms());
				json.put("invArea", cs.getInvArea());
				json.put("invAreaNm", cs.getInvAreaNm());
				JSONArray arrayy = new JSONArray();
				for(ShoppingGoods sg : cs.getList()){
					JSONObject jsonn = new JSONObject();
					jsonn.put("cartId", sg.getCartId());
					jsonn.put("skuId", sg.getGoodsId());
					jsonn.put("amount", sg.getGoodsNums());
					jsonn.put("state", sg.getState());
					jsonn.put("skuType", sg.getSkuType());
					jsonn.put("skuTypeId", sg.getSkuTypeId());
					jsonn.put("pinTieredPriceId", sg.getPinTieredPriceId());
					arrayy.put(jsonn);
				}
				json.put("cartDtos", arrayy);
				array.put(json);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	public static JSONObject OrderSubmitPaser(OrderSubmitVo os){
		JSONObject json = new JSONObject();
		try {
			json.put("settleDTOs", os.getSettleDtos());
			json.put("addressId", os.getAddressId());
			json.put("couponId", os.getCouponId());
			json.put("clientIp", os.getClientIp());
			json.put("clientType", os.getClientType());
			json.put("shipTime", os.getShipTime());
			json.put("payMethod", os.getPayMethod());
			json.put("buyNow", os.getBuyNow());
			json.put("pinActiveId", os.getPinActiveId());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
		
	}
}
