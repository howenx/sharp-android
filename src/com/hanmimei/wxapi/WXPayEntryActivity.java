package com.hanmimei.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hanmimei.entity.PayEvent;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ypy.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	api = WXAPIFactory.createWXAPI(this, "wx578f993da4b29f97");
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		
	}

	@Override
	public void onResp(BaseResp resp) {
		
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			EventBus.getDefault().post(new PayEvent(resp.errCode));
			finish();
		}
	}
}