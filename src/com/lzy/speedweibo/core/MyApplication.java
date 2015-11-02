package com.lzy.speedweibo.core;

import android.app.Application;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.lzy.speedweibo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.Status;

public class MyApplication extends Application {

	private static ImageLoaderConfiguration configuration;
	private static Status status;
	private static DisplayImageOptions options;
	private static Oauth2AccessToken mAccessToken;

	@Override
	public void onCreate() {
		super.onCreate();

		configuration = ImageLoaderConfiguration.createDefault(this);
		ImageLoader.getInstance().init(configuration);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.blank)
				.showImageOnFail(R.drawable.blank).cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	// public static MyApplication getInstance() {
	// if (application == null) {
	// application = new MyApplication();
	// }
	// return application;
	// }

	/**
	 * 异步加载图片
	 * 
	 * @param imageUrl
	 * @param imageView
	 */
	public static void asyncLoadImage(String imageUrl, ImageView imageView) {
		ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
	}

	public static void setStatus(Status status) {
		MyApplication.status = status;
	}

	public static Status getStatus() {
		return status;
	}

	public static Oauth2AccessToken getmAccessToken() {
		return mAccessToken;
	}

	public static void setmAccessToken(Oauth2AccessToken mAccessToken) {
		MyApplication.mAccessToken = mAccessToken;
	}
}
