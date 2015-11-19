package com.lzy.speedweibo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.adapter.WeiboAdapter;
import com.lzy.speedweibo.core.MyApplication;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

public class HomeActivity extends BaseActivity {

	private ListView listView;
	private SwipeRefreshLayout refreshLayout;
	private ImageButton scrollToTop;
	private WeiboAdapter adapter;
	private List<Status> statusList;
	private long maxWeiboID;
	private long minWeiboID;
	private boolean isFirstRefresh = true;
	private boolean isRequestLatestWeibo = true;
	private long pressTime = 0;
	private RequestListener mListener;
	private StatusesAPI mStatusesAPI;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		listView = (ListView) findViewById(R.id.weiboLv);
		refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
		scrollToTop = (ImageButton) findViewById(R.id.scrollToTop);

		View emptyView = View.inflate(this, R.layout.view_empty, null);
		View footerView = View.inflate(this, R.layout.view_footer, null);
		TextView loadMore = (TextView) footerView.findViewById(R.id.loadMore);
		loadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestPreviousWeibo();
			}
		});

		statusList = new ArrayList<Status>();
		adapter = new com.lzy.speedweibo.adapter.WeiboAdapter(this);
		mStatusesAPI = MyApplication.mStatusesAPI;
		mListener = new RequestListener() {

			@Override
			public void onWeiboException(WeiboException arg0) {
				arg0.printStackTrace();
			}

			@Override
			public void onComplete(String response) {
				if (!TextUtils.isEmpty(response)) {
					if (response.startsWith("{\"statuses\"")) {
						// 调用 StatusList#parse 解析字符串成微博列表对象
						StatusList responseStatuses = StatusList
								.parse(response);
						if (responseStatuses != null
								&& responseStatuses.total_number > 0) {
							handleStatuses(responseStatuses.statusList);
						}
					}
				}
			}
		};

		initActionBar();

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在加载");

		refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		refreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				requestLatestWeibo();
			}
		});

		scrollToTop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listView.smoothScrollToPosition(0);
			}
		});
		listView.addFooterView(footerView);
		listView.setEmptyView(emptyView);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));

		requestLatestWeibo();
	}

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - pressTime < 2000) {
			super.onBackPressed();
		} else {
			pressTime = System.currentTimeMillis();
			Toast.makeText(HomeActivity.this, "再按一次退出", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void initActionBar() {
		ActionBar actionBar = this.getActionBar();
		actionBar.setCustomView(R.layout.action_bar_home);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		RelativeLayout operate = (RelativeLayout) actionBar.getCustomView()
				.findViewById(R.id.operate);
		operate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, MeActivity.class);
				startActivity(intent);
			}
		});

		RelativeLayout create = (RelativeLayout) actionBar.getCustomView()
				.findViewById(R.id.create);
		create.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,
						EditActivity.class);
				intent.putExtra("action", "发表新微博");
				startActivity(intent);
			}
		});
	}

	/**
	 * 请求最新微博
	 */
	private void requestLatestWeibo() {
		progressDialog.show();
		isRequestLatestWeibo = true;
		mStatusesAPI.friendsTimeline(0, 0, 50, 1, false, 0, false, mListener);
	}

	/**
	 * 请求之前微博
	 */
	private void requestPreviousWeibo() {
		progressDialog.show();
		isRequestLatestWeibo = false;
		mStatusesAPI.friendsTimeline(0, minWeiboID, 50, 1, false, 0, false,
				mListener);
	}

	/**
	 * 提示加载的微博数目，并将其加入list，刷新listview
	 * 
	 * @param statuses
	 */
	private void handleStatuses(ArrayList<Status> list) {
		int comingNumber = 0;
		if (isRequestLatestWeibo) {
			statusList = list;
			for (int i = 0; i < statusList.size(); i++) {
				if (Long.parseLong(statusList.get(i).id) > maxWeiboID) {
					comingNumber++;
				} else {
					break;
				}
			}

		} else {
			comingNumber = list.size() - 1;
			for (int i = 1; i < list.size(); i++) {
				statusList.add(list.get(i));
			}
		}

		if (comingNumber > 0) {
			Toast.makeText(this, "来了" + comingNumber + "条新微博",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "没有新微博", Toast.LENGTH_SHORT).show();
		}

		refreshWeiboID(statusList);

		adapter.setData(statusList);
		adapter.notifyDataSetChanged();

		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		if (refreshLayout.isRefreshing()) {
			refreshLayout.setRefreshing(false);
		}
	}

	/**
	 * 记录微博ID的最大最小值
	 * 
	 * @param statuses
	 */
	private void refreshWeiboID(List<Status> statusList) {
		if (isFirstRefresh) {
			maxWeiboID = Long.parseLong(statusList.get(0).id);
			minWeiboID = Long
					.parseLong(statusList.get(statusList.size() - 1).id);

			isFirstRefresh = false;
			return;
		}

		if (Long.parseLong(statusList.get(0).id) > maxWeiboID) {
			maxWeiboID = Long.parseLong(statusList.get(0).id);
		}

		if (Long.parseLong(statusList.get(statusList.size() - 1).id) < minWeiboID) {
			minWeiboID = Long
					.parseLong(statusList.get(statusList.size() - 1).id);
		}
	}

	// class MyScrollListener extends PauseOnScrollListener {
	// private int lastVisibleItem;
	//
	// public MyScrollListener(ImageLoader imageLoader, boolean pauseOnScroll,
	// boolean pauseOnFling) {
	// super(imageLoader, pauseOnScroll, pauseOnFling);
	// }
	//
	// @Override
	// public void onScroll(AbsListView view, int firstVisibleItem,
	// int visibleItemCount, int totalItemCount) {
	// super.onScroll(view, firstVisibleItem, visibleItemCount,
	// totalItemCount);
	//
	// if (lastVisibleItem == 0) {
	// lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
	// } else if (firstVisibleItem + visibleItemCount - 1 > lastVisibleItem) {
	// lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
	// if (getActionBar().isShowing()) {
	// getActionBar().hide();
	// }
	// } else if (firstVisibleItem + visibleItemCount - 1 < lastVisibleItem) {
	// lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
	// if (!getActionBar().isShowing()) {
	// getActionBar().show();
	// }
	// }
	// }
	//
	// @Override
	// public void onScrollStateChanged(AbsListView view, int scrollState) {
	// super.onScrollStateChanged(view, scrollState);
	// }
	//
	// }

}
