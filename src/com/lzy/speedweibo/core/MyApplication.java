package com.lzy.speedweibo.core;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.lzy.speedweibo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
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
	private static Oauth2AccessToken mAccessToken;
	private static CommentsAPI mCommentsAPI;
	private static StatusesAPI mStatusesAPI;
	private static UsersAPI mUsersAPI;
	private static FriendshipsAPI mFriendshipsAPI;
	private static int imageWidth;
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

		DisplayMetrics metric = getResources().getDisplayMetrics();
		int widthPX = metric.widthPixels;// 屏幕宽度（像素）
		float density = metric.density;// 屏幕密度（0.75 / 1.0 / 1.5）
		imageWidth = (int) ((widthPX - 30 * density) / 3);
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

	public static int getImageWidth() {
		return imageWidth;
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
