package com.hanmimei.utils;

import android.content.Context;
import android.content.Intent;

public class DoJumpUtils {
	@Deprecated
	public static void doJump(Context mContext, Class<?> clazz) {
		Intent intent = new Intent(mContext, clazz);
		mContext.startActivity(intent);
	}
}
