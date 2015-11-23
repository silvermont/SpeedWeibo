package com.lzy.speedweibo.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.core.MyApplication;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

public class SettingActivity extends BaseActivity {
	private RelativeLayout user;
	private RelativeLayout weibo;
	private RelativeLayout follow;
	private RelativeLayout follower;
	private RelativeLayout showComments;
	private RelativeLayout showAt;
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
		showComments = (RelativeLayout) findViewById(R.id.showComments);
		showAt = (RelativeLayout) findViewById(R.id.showAt);
		head = (ImageView) findViewById(R.id.head);
		name = (TextView) findViewById(R.id.name);
		description = (TextView) findViewById(R.id.description);
		weiboNumber = (TextView) findViewById(R.id.weiboNumber);
		followNumber = (TextView) findViewById(R.id.followNumber);
		followerNumber = (TextView) findViewById(R.id.followerNumber);

		initActionBar();

		UsersAPI mUsersAPI = MyApplication.mUsersAPI;
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
				Intent intent = new Intent(SettingActivity.this,
						WeiboListActivity.class);
				intent.putExtra("action", "微博");
				startActivity(intent);
			}
		});

		follow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,
						UserListActivity.class);
				intent.putExtra("action", "关注");
				startActivity(intent);
			}
		});

		follower.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,
						UserListActivity.class);
				intent.putExtra("action", "粉丝");
				startActivity(intent);
			}
		});

		showComments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,
						CommentListActivity.class);
				intent.putExtra("action", "comments");
				startActivity(intent);
			}
		});

		showAt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,
						CommentListActivity.class);
				intent.putExtra("action", "at");
				startActivity(intent);
			}
		});

		mUsersAPI.show(MyApplication.uid, mListener);
	}

	private void initActionBar() {
		ActionBar actionBar = this.getActionBar();
		actionBar.setCustomView(R.layout.action_bar_back);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setBackgroundDrawable(getResources()
				.getDrawable(R.color.blue));

		TextView title = (TextView) actionBar.getCustomView().findViewById(
				R.id.title);
		title.setText("个人设置");

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
		MyApplication.displayImageLossless(user.avatar_large, head);
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
