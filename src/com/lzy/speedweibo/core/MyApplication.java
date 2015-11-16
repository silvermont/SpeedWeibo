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
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;

public class MyApplication extends Application {

	private static ImageLoaderConfiguration configuration;
	private static Status status;
	private static DisplayImageOptions options;
	public static DisplayImageOptions optionsBigPicture;
	private static Oauth2AccessToken mAccessToken;
	private static CommentsAPI mCommentsAPI;
	private static StatusesAPI mStatusesAPI;
	private static UsersAPI mUsersAPI;
	private static FriendshipsAPI mFriendshipsAPI;
	private static int imageWidth;
	private static int displayWidth;
	private static int displayHeight;
	private static int statusBarHeight;
	private static long uid;

	@Override
	public void onCreate() {
		super.onCreate();

		configuration = ImageLoaderConfiguration.createDefault(this);
		ImageLoader.getInstance().init(configuration);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.blank)
				.showImageOnFail(R.drawable.blank).cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		optionsBigPicture = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.blank)
				.showImageOnFail(R.drawable.blank).cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.NONE).build();

		DisplayMetrics metric = getResources().getDisplayMetrics();
		displayWidth = metric.widthPixels;// 屏幕宽度（像素）
		displayHeight = metric.heightPixels;
		float density = metric.density;// 屏幕密度（0.75 / 1.0 / 1.5）
		imageWidth = (int) ((displayWidth - 30 * density) / 3);
	}

	/**
	 * 异步加载图片
	 * 
	 * @param imageUrl
	 * @param imageView
	 */
	public static void asyncLoadImage(String imageUrl, ImageView imageView) {
		ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
	}

	public static int getImageWidth() {
		return imageWidth;
	}

	public static int getDisplayWidth() {
		return displayWidth;
	}

	public static int getDisplayHeight() {
		return displayHeight;
	}

	public static int getStatusBarHeight(Context context) {
		if (statusBarHeight == 0) {
			Class<?> c = null;
			Object obj = null;
			Field field = null;
			int x = 0;

			try {
				c = Class.forName("com.android.internal.R$dimen");
				obj = c.newInstance();
				field = c.getField("status_bar_height");
				x = Integer.parseInt(field.get(obj).toString());
				statusBarHeight = context.getResources().getDimensionPixelSize(
						x);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return statusBarHeight;
	}

	public static CommentsAPI getCommentsAPI(Context context) {
		if (mCommentsAPI == null) {
			mCommentsAPI = new CommentsAPI(context, Constants.APP_KEY,
					getmAccessToken());
		}
		return mCommentsAPI;
	}

	public static StatusesAPI getStatusesAPI(Context context) {
		if (mStatusesAPI == null) {
			mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY,
					getmAccessToken());
		}
		return mStatusesAPI;
	}

	public static UsersAPI getUsersAPI(Context context) {
		if (mUsersAPI == null) {
			mUsersAPI = new UsersAPI(context, Constants.APP_KEY,
					getmAccessToken());
		}
		return mUsersAPI;
	}

	public static FriendshipsAPI getFriendshipsAPI(Context context) {
		if (mFriendshipsAPI == null) {
			mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY,
					getmAccessToken());
		}
		return mFriendshipsAPI;
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

	public static long getUid() {
		if (uid == 0L) {
			uid = Long.parseLong(getmAccessToken().getUid());
		}
		return uid;
	}
}
