package com.hanmimei.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

import com.hanmimei.R;

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
	
	//同一textview  不同字体设置 mid截止到 end 
	public static void setDifrentFontStyle(Context mContext, TextView textView, String font, int mid, int end){
		SpannableString styledText = new SpannableString(font);  
        styledText.setSpan(new TextAppearanceSpan(mContext, R.style.youhui1), 0, mid, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
        styledText.setSpan(new TextAppearanceSpan(mContext, R.style.youhui2), 14, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
        textView.setText(styledText, TextView.BufferType.SPANNABLE);
	}
	//同一textview  不同字体颜色设置 mid截止到 end 
		public static void setDifrentFontColor(Context mContext, TextView textView, String font, int mid, int end){
			SpannableString styledText = new SpannableString(font);  
	        styledText.setSpan(new TextAppearanceSpan(mContext, R.style.tax01), 0, mid, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
	        styledText.setSpan(new TextAppearanceSpan(mContext, R.style.tax02), mid+1, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
	        textView.setText(styledText, TextView.BufferType.SPANNABLE);
		}
}
