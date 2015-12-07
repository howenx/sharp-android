package com.hanmimei.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.hanmimei.entity.BitmapInfo;
import com.squareup.picasso.Picasso;

/**
 * 异步加载图片
 */
public class AsyncImageLoader {
	
	public class SoftReference<T>{
		T t ;

		public SoftReference(T t) {
			super();
			this.t = t;
		}
		public T get(){
			return t;
		}
	}
	
	private static class AsyncImageLoaderHolder{
		public static final AsyncImageLoader instance = new AsyncImageLoader();
	}
	 
	
	public static AsyncImageLoader getInstance(){
		return AsyncImageLoaderHolder.instance;
	}
	

    // 软引用，使用内存做临时缓存 （程序退出，或内存不够则清除软引用）
    private HashMap<String, SoftReference<Bitmap>> imageCache;

    public AsyncImageLoader() {
        imageCache = new HashMap<String, SoftReference<Bitmap>>();
    }

    /**
     * 定义回调接口
     */
    public interface ImageCallback {
        public void imageLoaded(Bitmap imgBitmap, ImageView mImageView,String imageUrl);
    }
    
    /**
     * 定义回调接口
     */
    public interface LoadedCallback {
        public void imageLoaded(BitmapInfo info);
    }



    /**
     * 创建子线程加载图片
     * 子线程加载完图片交给handler处理（子线程不能更新ui，而handler处在主线程，可以更新ui）
     * handler又交给imageCallback，imageCallback须要自己来实现，在这里可以对回调参数进行处理
     *
     * @param imageUrl ：须要加载的图片url
     * @param imageCallback：
     * @return
     */
    
    public Bitmap loadBitmap(final Context context,final int size,final String imageUrl,
            final LoadedCallback loadedCallback) {
         
        
        /**
         * 在主线程里执行回调，更新视图
         */
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
            	loadedCallback.imageLoaded((BitmapInfo)message.obj);
            }
        };

         
        /**
         * 创建子线程访问网络并加载图片 ，把结果交给handler处理
         */
        new Thread() {
            @Override
            public void run() {
            	Float scale = returnBitMapSize(context, imageUrl);
                // 下载完的图片放到缓存里
            	BitmapInfo info = new BitmapInfo(imageUrl, scale);
                Message message = handler.obtainMessage(0, info);
                handler.sendMessage(message);
               
               
            }
        }.start();
         
        return null;
    }
    
    
    public HashMap<String, SoftReference<Bitmap>> getImageCache(){
    	return this.imageCache;
    }
    

    //清除缓存
    public  void clearCache() {

        if (imageCache.size() > 0) {

            imageCache.clear();
        }

    }
    
    /**
     * 下载图片  （注意HttpClient 和httpUrlConnection的区别）
     */
    public Drawable loadImageFromUrl(String url) {

        try {
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000*15);
            HttpGet get = new HttpGet(url);
            HttpResponse response;

            response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                Drawable d = Drawable.createFromStream(entity.getContent(),
                        "src");
                
                return d;
            } else {
                return null;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    
	// 把一个url的网络图片变成一个本地的BitMap
	public static float returnBitMapSize(Context context,String url) {
		URL myFileUrl = null;
		float scale = 0f;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			if (is == null){  
			    throw new RuntimeException("stream is null");  
			}else{  
				BitmapFactory.Options options = new BitmapFactory.Options();  
				options.inJustDecodeBounds = true;
		        BitmapFactory.decodeStream(is,null, options);
		        scale = (float)options.outWidth/(float)options.outHeight;
			    is.close();  
			}  
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scale;
		
	}

}
