package com.lzy.speedweibo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.adapter.WeiboAdapter;
import com.lzy.speedweibo.core.MyApplication;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

public class WeiboListActivity extends BaseActivity {

	private String action;
	private ListView listView;
	private WeiboAdapter adapter;
	private RelativeLayout footerView;
	private TextView loadMore;
	private StatusesAPI mStatusesAPI;
	private RequestListener mListener;
	private List<Status> statusList;
	private long maxStatusID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_list);

		listView = (ListView) findViewById(R.id.weiboLv);
		footerView = (RelativeLayout) View.inflate(this, R.layout.view_footer,
				null);
		loadMore = (TextView) footerView.findViewById(R.id.loadMore);

		mStatusesAPI = MyApplication.getStatusesAPI(this);
		statusList = new ArrayList<Status>();
		adapter = new WeiboAdapter(this);

		loadMore.setText("暂时没有微博");
		listView.addFooterView(footerView);
		listView.setAdapter(adapter);

		mListener = new RequestListener() {
			@Override
			public void onComplete(String response) {
				if (!TextUtils.isEmpty(response)) {
					if (response.substring(2).startsWith("statuses")) {
						StatusList statuses = StatusList.parse(response);
						if (statuses != null && statuses.total_number > 0) {
							handleStatuses(statuses.statusList);
						}
					}
				}
			}

			@Override
			public void onWeiboException(WeiboException e) {
				e.printStackTrace();
			}
		};

		Intent intent = getIntent();
		action = intent.getStringExtra("action");

		loadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mStatusesAPI.userTimeline(0, maxStatusID, 50, 1, false, 0,
						false, mListener);
			}
		});

		mStatusesAPI.userTimeline(0, 0, 50, 1, false, 0, false, mListener);

		initActionBar();
	}

	private void handleStatuses(List<Status> list) {
		if (null == list) {
			return;
		}
		boolean isDataChanged = false;
		if (statusList.size() == 0) {
			statusList = list;
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (Long.valueOf(list.get(i).id) < maxStatusID) {
					statusList.add(list.get(i));
					isDataChanged = true;
				}
			}
			if (!isDataChanged) {
				Toast.makeText(this, "没有更多微博", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		maxStatusID = Long.parseLong(statusList.get(statusList.size() - 1).id);
		adapter.setData(statusList);
		adapter.notifyDataSetChanged();

		loadMore.setText("加载更多");
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
