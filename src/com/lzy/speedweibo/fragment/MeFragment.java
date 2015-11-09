package com.lzy.speedweibo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.core.AccessTokenKeeper;
import com.lzy.speedweibo.core.MyApplication;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

public class MeFragment extends Fragment {
	private RelativeLayout user;
	private RelativeLayout weibo;
	private RelativeLayout follow;
	private RelativeLayout follower;
	private ImageView head;
	private TextView name;
	private TextView description;
	private TextView weiboNumber;
	private TextView followNumber;
	private TextView followerNumber;
	private User curUser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_me, null);
		user = (RelativeLayout) view.findViewById(R.id.user);
		weibo = (RelativeLayout) view.findViewById(R.id.weibo);
		follow = (RelativeLayout) view.findViewById(R.id.follow);
		follower = (RelativeLayout) view.findViewById(R.id.follower);
		head = (ImageView) view.findViewById(R.id.head);
		name = (TextView) view.findViewById(R.id.name);
		description = (TextView) view.findViewById(R.id.description);
		weiboNumber = (TextView) view.findViewById(R.id.weiboNumber);
		followNumber = (TextView) view.findViewById(R.id.followNumber);
		followerNumber = (TextView) view.findViewById(R.id.followerNumber);

		UsersAPI mUsersAPI = MyApplication.getUsersAPI(getActivity());
		RequestListener mListener = new RequestListener() {

			@Override
			public void onWeiboException(WeiboException arg0) {
				arg0.printStackTrace();
			}

			@Override
			public void onComplete(String response) {
				if (!TextUtils.isEmpty(response)) {
					curUser = User.parse(response);
					handleUser(curUser);
				}
			}
		};

		user.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		weibo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		follow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		follower.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		mUsersAPI.show(Long.parseLong(AccessTokenKeeper.readAccessToken(
				getActivity()).getUid()), mListener);

		return view;
	}

	private void handleUser(User user) {
		MyApplication.asyncLoadImage(user.avatar_large, head);
		name.setText(user.screen_name);
		weiboNumber.setText(user.statuses_count + "");
		followNumber.setText(user.friends_count + "");
		followerNumber.setText(user.followers_count + "");

		if (!user.description.equals("")) {
			description.setText("简介：" + user.description);
		} else {
			description.setText("简介：暂无介绍");
		}
	}
}
