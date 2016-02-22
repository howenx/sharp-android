package com.hanmimei.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.utils.ImageLoaderUtils;

/**
 * 自定义dialog
 * 
 * @author Austriee
 * 
 */

@SuppressLint("InflateParams")
public class ListViewDialog extends Dialog {

	public ListViewDialog(Context context) {
		super(context);
	}

	public ListViewDialog(Context context, int theme) {
		super(context, theme);
	}


	public static class Builder {
		private ListViewDialog dialog;
		private Context mContext;
		private View view;
		private TextView titleView;
		private ImageView iconView;
		private ListView mListView;

		public Builder(Context context) {
			this.mContext = context;
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.pingou_detail_sel_layout, null);
				titleView = (TextView) view.findViewById(R.id.titleView);
				iconView = (ImageView) view.findViewById(R.id.iconView);
				mListView = (ListView) view.findViewById(R.id.mListView);
			}
		}

		public Builder setIconView(int id) {
			iconView.setImageResource(id);
			return this;
		}

		public Builder setIconView(Drawable drawable) {
			iconView.setImageDrawable(drawable);
			return this;
		}

		public Builder setIconView(String url) {
			ImageLoaderUtils.loadImage(url, iconView);
			return this;
		}

		public Builder setAdapter(BaseAdapter adapter) {
			mListView.setAdapter(adapter);
			return this;
		}

		public Builder setOnItemClickListener(OnItemClickListener listener) {
			mListView.setOnItemClickListener(listener);
			return this;
		}

		/**
		 * 设置dialog标题
		 */
		public Builder setTitle(int id) {
			titleView.setText(mContext.getText(id).toString());
			return this;
		}

		/**
		 * 设置dialog标题
		 */
		public Builder setTitle(String title) {
			titleView.setText(title);
			return this;
		}

		public ListViewDialog show() {
			if (dialog == null) {
				dialog = new ListViewDialog(mContext,R.style.CustomDialog);
				dialog.setContentView(view, new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			}
			dialog.show();
			return dialog;
		}

	}

}
