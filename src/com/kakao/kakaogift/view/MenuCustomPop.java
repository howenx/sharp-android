/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-9-6 下午3:02:09 
**/
package com.kakao.kakaogift.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.flyco.dialog.widget.popup.base.BasePopup;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.utils.ToastUtils;

/**
 * @author vince
 *
 */
public class MenuCustomPop extends BasePopup<MenuCustomPop> {
	
	TextView mTvItem1;
    TextView mTvItem2;
    TextView mTvItem3;
   TextView mTvItem4;

	/**
	 * @param context
	 */
	public MenuCustomPop(Context context) {
		super(context);
	}

	/* (non-Javadoc)
	 * @see com.flyco.dialog.widget.popup.base.BasePopup#onCreatePopupView()
	 */
	@Override
	public View onCreatePopupView() {
		View inflate = View.inflate(mContext, R.layout.menu_pop_panel, null);
		mTvItem1 = (TextView) inflate.findViewById(R.id.tv_item_1);
		mTvItem2 = (TextView) inflate.findViewById(R.id.tv_item_2);
		mTvItem3 = (TextView) inflate.findViewById(R.id.tv_item_3);
		mTvItem4 = (TextView) inflate.findViewById(R.id.tv_item_4);
		return inflate;
	}

	/* (non-Javadoc)
	 * @see com.flyco.dialog.widget.base.BaseDialog#setUiBeforShow()
	 */
	@Override
	public void setUiBeforShow() {
		mTvItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.Toast(mContext, mTvItem1.getText().toString());
            }
        });
        mTvItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	 ToastUtils.Toast(mContext, mTvItem2.getText().toString());
            }
        });
        mTvItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	 ToastUtils.Toast(mContext, mTvItem3.getText().toString());
            }
        });
        mTvItem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	 ToastUtils.Toast(mContext, mTvItem4.getText().toString());
            }
        });
	}

}
