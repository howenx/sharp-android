package com.kakao.kakaogift.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

import com.kakao.kakaogift.R
;

public class KeyWordUtil {
	
	/**
	 * 关键字高亮变色
	 * 
	 * @param color
	 *            变化的色值
	 * @param text
	 *            文字
	 * @param keyword
	 *            文字中的关键字
	 * @return
	 */
	public static SpannableString matcherSearchTitle(int color, String text,
			String keyword) {
		SpannableString s = new SpannableString(text);
		Pattern p = Pattern.compile(keyword);
		Matcher m = p.matcher(s);
		while (m.find()) {
			int start = m.start();
			int end = m.end();
			s.setSpan(new ForegroundColorSpan(color), start, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return s;
	}

	/**
	 * 多个关键字高亮变色
	 * 
	 * @param color
	 *            变化的色值
	 * @param text
	 *            文字
	 * @param keyword
	 *            文字中的关键字数组
	 * @return
	 */
	public static SpannableString matcherSearchTitle(int color, String text,
			String[] keyword) {
		SpannableString s = new SpannableString(text);
		for (int i = 0; i < keyword.length; i++) {
			Pattern p = Pattern.compile(keyword[i]);
			Matcher m = p.matcher(s);
			while (m.find()) {
				int start = m.start();
				int end = m.end();
				s.setSpan(new ForegroundColorSpan(color), start, end,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return s;
	}
	
	/**
	 * 设置不同文字style 同一textview  不同字体颜色设置 mid截止到 end 
	 * 正常文字： 12sp 灰色
	 * 高亮文字：14sp 主题色
	 * 
	 * @param mContext
	 * @param textView
	 * @param font
	 * @param mid
	 * @param end
	 */
		public static void setDifrentFontColor(Context mContext, TextView textView, String font, int mid, int end){
			SpannableString styledText = new SpannableString(font);  
	        styledText.setSpan(new TextAppearanceSpan(mContext, R.style.tax01), 0, mid, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
	        styledText.setSpan(new TextAppearanceSpan(mContext, R.style.tax02), mid+1, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
	        textView.setText(styledText, TextView.BufferType.SPANNABLE);
		}
		/**
		 * 设置不同文字style 同一textview  不同字体颜色设置 mid截止到 end 
		 * 正常文字： 12sp 灰色
		 * 高亮文字：12sp 主题色
		 * 
		 * @param mContext
		 * @param textView
		 * @param font
		 * @param mid
		 * @param end
		 */
		public static void setDifrentFontColor12(Context mContext, TextView textView, String font, int mid, int end){
			SpannableString styledText = new SpannableString(font);  
			styledText.setSpan(new TextAppearanceSpan(mContext, R.style.tax05), 0, mid, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
			styledText.setSpan(new TextAppearanceSpan(mContext, R.style.tax04), mid+1, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
			textView.setText(styledText, TextView.BufferType.SPANNABLE);
		}
		/**
		 * 设置不同文字style   同一textview  不同字体颜色设置 mid截止到 end 
		 * 正常文字： 原设定文字属性
		 * 高亮文字：14sp 主题色
		 * 
		 * @param mContext
		 * @param textView
		 * @param font
		 * @param mid
		 * @param end
		 */
		public static void setDifferentFontColor(Context mContext, TextView textView, String font, int start, int end){
			SpannableString styledText = new SpannableString(font);  
			styledText.setSpan(new TextAppearanceSpan(mContext, R.style.tax02), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
			textView.setText(styledText, TextView.BufferType.SPANNABLE);
		}
		/**
		 * 设置不同文字style   同一textview  不同字体颜色设置 mid截止到 end 
		 * 正常文字： 原设定文字属性
		 * 高亮文字：18sp 主题色
		 * 
		 * @param mContext
		 * @param textView
		 * @param font
		 * @param mid
		 * @param end
		 */
		public static void setDifferentFontColor18(Context mContext, TextView textView, String font, int start, int end){
			SpannableString styledText = new SpannableString(font);  
			styledText.setSpan(new TextAppearanceSpan(mContext, R.style.tax03), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
			textView.setText(styledText, TextView.BufferType.SPANNABLE);
		}
}
