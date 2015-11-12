package com.lzy.speedweibo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.adapter.UserAdapter;
import com.lzy.speedweibo.core.MyApplication;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.openapi.models.UserList;

public class UserListActivity extends BaseActivity {

	private String action;
	private ListView listView;
	private UserAdapter adapter;
	private FriendshipsAPI mFriendshipsAPI;
	private RequestListener mListener;
	private List<User> list;
	private List<String> uidList;
	private RelativeLayout footerView;
	private TextView loadMore;
	private int cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);

		listView = (ListView) findViewById(R.id.listView);
		footerView = (RelativeLayout) View.inflate(this, R.layout.view_footer,
				null);
		loadMore = (TextView) footerView.findViewById(R.id.loadMore);

		adapter = new UserAdapter(this);
		mFriendshipsAPI = MyApplication.getFriendshipsAPI(this);
		list = new ArrayList<User>();
		uidList = new ArrayList<String>();
		mListener = new RequestListener() {

			@Override
			public void onWeiboException(WeiboException arg0) {
				arg0.printStackTrace();
			}

			@Override
			public void onComplete(String response) {
				if (!TextUtils.isEmpty(response)) {
					if (response.substring(2).startsWith("users")) {
						UserList list = UserList.parse(response);
						if (list != null && list.total_number > 0) {
							handleUsers(list.userList);
							cursor = Integer.valueOf(list.next_cursor);
						}
					}
				}
			}
		};

		listView.addFooterView(footerView);
		listView.setAdapter(adapter);

		Intent intent = getIntent();
		action = intent.getStringExtra("action");

		loadMore.setText("暂时没有数据");
		loadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (action.equals("关注")) {
					mFriendshipsAPI.friends(MyApplication.getUid(), 200,
							cursor, true, mListener);
				} else if (action.equals("粉丝")) {
					mFriendshipsAPI.followers(MyApplication.getUid(), 200,
							cursor, false, mListener);
				}
			}
		});

		if (action.equals("关注")) {
			mFriendshipsAPI.friends(MyApplication.getUid(), 200, 0, true,
					mListener);
		} else if (action.equals("粉丝")) {
			mFriendshipsAPI.followers(MyApplication.getUid(), 200, 0, false,
					mListener);
		}

		initActionBar();
	}

	private void handleUsers(List<User> list) {
		boolean isDataChanged = false;
		for (int i = 0; i < list.size(); i++) {
			if (!uidList.contains(list.get(i).id)) {
				isDataChanged = true;
				uidList.add(list.get(i).id);
				this.list.add(list.get(i));
			}
		}
		if (isDataChanged) {
			adapter.setData(this.list);
			adapter.notifyDataSetChanged();

			loadMore.setText("加载更多");
		} else {
			Toast.makeText(UserListActivity.this, "没有更多", Toast.LENGTH_SHORT)
					.show();
			loadMore.setText("加载更多");
		}
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

		TextView title = (TextView) actionBar.getCustomView().findViewById(
				R.id.title);
		title.setText(action);
	}
}
