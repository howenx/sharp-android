package com.hanmimei.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {

private Context context ;
private SharedPreferences preferences ;

public SharedPreferencesUtil(Context context,String name) {
	super();
	this.context = context;
	preferences =this.context.getSharedPreferences(name, Context.MODE_PRIVATE) ;
} ;


public void putString(String key ,String value){
	Editor editor = preferences.edit();
	editor.putString(key, value);
	editor.commit();
}

public String getString(String key){
 return 	preferences.getString(key, null);
}
	
}
