package com.lzy.speedweibo.core;

import java.lang.reflect.Field;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.lzy.speedweibo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;

public class MyApplication extends Application {

	public static Status status;
	public static DisplayImageOptions options;
	public static DisplayImageOptions lossyOptions;
	public static Oauth2AccessToken mAccessToken;
	public static CommentsAPI mCommentsAPI;
	public static StatusesAPI mStatusesAPI;
	public static UsersAPI mUsersAPI;
	public static FriendshipsAPI mFriendshipsAPI;
	public static int imageWidth;
	public static int displayWidth;
	public static int displayHeight;
	public static int statusBarHeight;
	public static long uid;

	@Override
	public void onCreate() {
		super.onCreate();

		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);
		ImageLoader.getInstance().init(configuration);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.blank)
				.showImageOnFail(R.drawable.blank).cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.NONE).build();

		lossyOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.blank)
				.showImageOnFail(R.drawable.blank).cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

		DisplayMetrics metric = getResources().getDisplayMetrics();
		displayWidth = metric.widthPixels;// 屏幕宽度（像素）
		displayHeight = metric.heightPixels;
		float density = metric.density;// 屏幕密度（0.75 / 1.0 / 1.5）
		imageWidth = (int) ((displayWidth - 30 * density) / 3);

		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0;

		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 异步显示图片,无损
	 * 
	 * @param url
	 * @param imageView
	 */
	public static void displayImage(String url, ImageView imageView) {
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}

	/**
	 * 异步显示图片,有损
	 * 
	 * @param url
	 * @param imageView
	 */
	public static void lossyDisplayImage(String url, ImageView imageView) {
		ImageLoader.getInstance().displayImage(url, imageView, lossyOptions);
	}

	/**
	 * 异步加载图片,无损
	 * 
	 * @param imageUrl
	 * @param imageView
	 */
	public static void loadImage(String url, SimpleImageLoadingListener listener) {
		ImageLoader.getInstance().loadImage(url, options, listener);
	}

	public static void setmAccessToken(Context context,
			Oauth2AccessToken mAccessToken) {
		MyApplication.mAccessToken = mAccessToken;

		mCommentsAPI = new CommentsAPI(context, Constants.APP_KEY, mAccessToken);
		mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, mAccessToken);
		mUsersAPI = new UsersAPI(context, Constants.APP_KEY, mAccessToken);
		mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY,
				mAccessToken);

		uid = Long.parseLong(mAccessToken.getUid());
	}
}
