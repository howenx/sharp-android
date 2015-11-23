package com.hanbimei.utils;

import android.content.Context;
import android.content.Intent;

public class DoJumpUtils {
	public static void doJump(Context mcContext, Class clazz){
		Intent intent = new Intent(mcContext,clazz);
		mcContext.startActivity(intent);
	}
}
