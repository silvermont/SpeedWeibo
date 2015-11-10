package com.lzy.speedweibo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
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

public class MeActivity extends BaseActivity {
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me);

		user = (RelativeLayout) findViewById(R.id.user);
		weibo = (RelativeLayout) findViewById(R.id.weibo);
		follow = (RelativeLayout) findViewById(R.id.follow);
		follower = (RelativeLayout) findViewById(R.id.follower);
		head = (ImageView) findViewById(R.id.head);
		name = (TextView) findViewById(R.id.name);
		description = (TextView) findViewById(R.id.description);
		weiboNumber = (TextView) findViewById(R.id.weiboNumber);
		followNumber = (TextView) findViewById(R.id.followNumber);
		followerNumber = (TextView) findViewById(R.id.followerNumber);

		initActionBar();

		UsersAPI mUsersAPI = MyApplication.getUsersAPI(this);
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

		mUsersAPI.show(Long.parseLong(AccessTokenKeeper.readAccessToken(this)
				.getUid()), mListener);

	}

	private void initActionBar() {
		ActionBar actionBar = this.getActionBar();
		actionBar.setCustomView(R.layout.action_bar_back);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		RelativeLayout back = (RelativeLayout) actionBar.getCustomView()
				.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
