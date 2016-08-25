/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-8-25 上午10:58:21 
**/
package com.kakao.kakaogift.activity.goods.category.presenter;

import java.util.List;

import com.kakao.kakaogift.activity.goods.category.CategoryGoodsView;
import com.kakao.kakaogift.activity.goods.category.model.CategoryGoodsModel;
import com.kakao.kakaogift.activity.goods.category.model.CategoryGoodsModelImpl;
import com.kakao.kakaogift.entity.HGoodsVo;

/**
 * @author vince
 *
 */
public class CategoryGoodsPresenterImpl implements CategoryGoodsPresenter {
	
	public interface CallBack{
		void onSuccess(List<HGoodsVo> data,String title);
		void onFailed(String msg);
	}
	
	private CategoryGoodsModel model;
	private CategoryGoodsView view;
	
	

	public CategoryGoodsPresenterImpl(CategoryGoodsView view) {
		super();
		this.view = view;
		this.model = new CategoryGoodsModelImpl();
	}



	@Override
	public void getCategoryGoodsList(String url , int pageNo) {
		view.showLoading();
		model.loadCategoryGoodsList( url , pageNo, new CallBack() {
			
			@Override
			public void onSuccess(List<HGoodsVo> data,String title) {
				view.CategoryGoodsData(data);
				if(title !=null){
					view.title(title);
				}
				view.hideLoading();
			}
			
			@Override
			public void onFailed(String msg) {
				view.showLoadFaild(msg);
				view.hideLoading();
			}
		});
	}

}
