package com.hanmimei.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	public ToastUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}
	
	
	private static String oldMsg;  
    protected static Toast toast   = null;  
    private static long oneTime=0;  
    private static long twoTime=0;  
	/**
	 * toast 工具类 自定义toast
	 * 
	 * @param context
	 * @param s
	 */
	public static void Toast(Context context, String s){      
        if(toast==null){   
            toast =Toast.makeText(context, s, Toast.LENGTH_SHORT);  
            toast.show();  
            oneTime=System.currentTimeMillis();  
        }else{  
            twoTime=System.currentTimeMillis();  
            if(s.equals(oldMsg)){  
                if(twoTime-oneTime>Toast.LENGTH_SHORT){  
                    toast.show();  
                }  
            }else{  
                oldMsg = s;  
                toast.setText(s);  
                toast.show();  
            }         
        }  
        oneTime=twoTime;  
    }  
      
	/**
	 * toast 工具类 自定义toast
	 * 
	 * @param context
	 * @param s
	 */
    public static void Toast(Context context, int resId){     
        Toast(context, context.getString(resId));  
    }  
}
