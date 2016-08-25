/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-26 上午9:41:09 
 **/
package com.kakao.kakaogift.activity.goods.pin.model;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.CollectionVo;
import com.kakao.kakaogift.entity.PinDetail;
import com.kakao.kakaogift.entity.StockVo;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;

/**
 * @author vince
 * 
 */
public class PinDetailModelImpl implements PinDetailModel {

	public interface OnPinDetailListener {
		void onSuccess(PinDetail detail);

		void cancelCollectionSuccess();

		void addCollectionSuccess(long collectId);

		void onFailed(String msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.pdetail.PinDetailModel#getPinDetail(java.
	 * util.Map, java.lang.String, java.lang.String,
	 * com.kakao.kakaogift.activity.model.
	 * pdetail.PinDetailModelImpl.OnPinDetailListener)
	 */
	@Override
	public void getPinDetail(Map<String, String> headers, String url,
			String tag, final OnPinDetailListener listener) {
		// TODO Auto-generated method stub
		VolleyHttp.doGetRequestTask(headers, url, new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				PinDetail detail = new Gson().fromJson(result, PinDetail.class);
				if (detail.getMessage().getCode() == 200) {
					listener.onSuccess(detail);
				} else {
					listener.onFailed(detail.getMessage().getMessage());
				}
			}

			@Override
			public void onError() {
				listener.onFailed(AppConstant.HTTP_ERROR);
			}
		}, tag);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.pdetail.PinDetailModel#cancelCollection(java
	 * .util.Map, long, java.lang.String,
	 * com.kakao.kakaogift.activity.model.pdetail.PinDetailModelImpl
	 * .OnPinDetailListener)
	 */
	@Override
	public void cancelCollection(Map<String, String> headers, long collectId,
			String tag, final OnPinDetailListener listener) {
		// TODO Auto-generated method stub
		VolleyHttp.doGetRequestTask(headers,
				UrlUtil.DEL_COLLECTION + collectId, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						CollectionVo vo = new Gson().fromJson(result,
								CollectionVo.class);
						if (vo.getMessage().getCode() == 200) {
							listener.cancelCollectionSuccess();
						} else {
							listener.onFailed(vo.getMessage().getMessage());
						}
					}

					@Override
					public void onError() {
						listener.onFailed(AppConstant.HTTP_ERROR);
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.pdetail.PinDetailModel#addCollection(java
	 * .util.Map, java.lang.String, java.lang.String, java.lang.String,
	 * com.kakao.kakaogift
	 * .activity.model.pdetail.PinDetailModelImpl.OnPinDetailListener)
	 */
	@Override
	public void addCollection(Map<String, String> headers, StockVo stock,
			String tag, final OnPinDetailListener listener) {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		try {
			params.put("skuId", stock.getId());
			params.put("skuType", stock.getSkuType());
			params.put("skuTypeId", stock.getSkuTypeId());
		} catch (JSONException e) {
		}
		VolleyHttp.doPostRequestTask2(headers, UrlUtil.ADD_COLLECTION,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						CollectionVo vo = new Gson().fromJson(result,
								CollectionVo.class);
						if (vo.getMessage().getCode() == 200) {
							listener.addCollectionSuccess(vo.getCollectId());
						} else {
							listener.onFailed(vo.getMessage().getMessage());
						}
					}

					@Override
					public void onError() {
						listener.onFailed(AppConstant.HTTP_ERROR);
					}
				}, params.toString());

	}
}
