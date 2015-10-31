package com.lzy.speedweibo.core;

import android.content.Context;
import android.text.TextUtils;

import com.lzy.speedweibo.fragment.HomeFragment;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.StatusList;

public class RequestListenerHelper {
	private RequestListener mListener;
	private HomeFragment fragment;
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;

	public RequestListenerHelper(Context context, HomeFragment fragment) {
		super();
		this.fragment = fragment;

		mAccessToken = AccessTokenKeeper.readAccessToken(context);
		mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, mAccessToken);

		init();
	}

	private void init() {

		mListener = new RequestListener() {

			@Override
			public void onWeiboException(WeiboException arg0) {
				arg0.printStackTrace();
			}

			@Override
			public void onComplete(String response) {
				if (!TextUtils.isEmpty(response)) {
					if (response.startsWith("{\"statuses\"")) {
						// 回复为微博列表
						// 调用 StatusList#parse 解析字符串成微博列表对象
						StatusList responseStatuses = StatusList
								.parse(response);
						if (responseStatuses != null
								&& responseStatuses.total_number > 0) {
							// 把微博列表传回fragment
							// fragment.handleResponse(responseStatuses);
						}
					}
				}
			}
		};
	}

	public void requestWeibo(long since_id, long max_id) {
		mStatusesAPI.friendsTimeline(since_id, max_id, 20, 1, false, 0, false,
				mListener);
	}
}
