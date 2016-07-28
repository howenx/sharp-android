/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-26 上午9:48:11 
**/
package com.kakao.kakaogift.activity.mine.comment.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.mine.comment.PubCommentActivity;
import com.kakao.kakaogift.entity.CommentVo;
import com.kakao.kakaogift.entity.OrderRemark;
import com.kakao.kakaogift.entity.Sku;
import com.kakao.kakaogift.utils.GlideLoaderTools;

/**
 * @author eric
 *
 */
public class CommentGoodsAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<OrderRemark> list;
	private Context mContext;
	private String orderId;
	public CommentGoodsAdapter(Context mContext, List<OrderRemark> list){
		inflater = LayoutInflater.from(mContext);
		this.list = list;
		this.mContext = mContext;
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return list.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
//		final Sku sku = list.get(position).getSku();
		final Sku sku = list.get(position).getOrderLine();
		final CommentVo comment = list.get(position).getComment();
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.comment_goods_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.comment = (TextView) convertView.findViewById(R.id.comment);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(sku.getSkuTitle());
		GlideLoaderTools.loadSquareImage(mContext, sku.getInvImg_(), holder.img);
		if(comment != null){
			holder.comment.setText("查看评价");
		}else{
			holder.comment.setText("评价晒单");
		}
		holder.comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext,PubCommentActivity.class);
				intent.putExtra("sku", sku);
				intent.putExtra("orderId", orderId);
				if(comment != null){
					intent.putExtra("comment", comment);
				}
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}
	private class ViewHolder{
		private ImageView img;
		private TextView title;
		private TextView comment;
	}

}
