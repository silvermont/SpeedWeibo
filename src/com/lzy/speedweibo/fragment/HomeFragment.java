package com.lzy.speedweibo.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
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

public class HomeFragment extends Fragment {
	private ListView weiboLv;
	private SwipeRefreshLayout refreshLayout;
	private WeiboAdapter lvAdapter;
	private List<Status> statusList;
	private long maxWeiboID;
	private long minWeiboID;
	private boolean isFirstRefresh = true;
	private boolean isRequestLatestWeibo = true;
	private RequestListener mListener;
	private StatusesAPI mStatusesAPI;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		statusList = new ArrayList<Status>();
		lvAdapter = new com.lzy.speedweibo.adapter.WeiboAdapter(getActivity(),
				statusList);
		mStatusesAPI = MyApplication.getStatusesAPI(getActivity());
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		weiboLv = (ListView) view.findViewById(R.id.weiboLv);
		refreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.refreshLayout);
		ImageButton scrollToTop = (ImageButton) view
				.findViewById(R.id.scrollToTop);

		View emptyView = View.inflate(getActivity(), R.layout.view_empty, null);
		View footerView = View.inflate(getActivity(), R.layout.view_footer,
				null);
		TextView loadMore = (TextView) footerView.findViewById(R.id.loadMore);
		loadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestPreviousWeibo();
			}
		});

		progressDialog = new ProgressDialog(getActivity());
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
				weiboLv.smoothScrollToPosition(0);
			}
		});
		weiboLv.addFooterView(footerView);
		weiboLv.setEmptyView(emptyView);
		weiboLv.setAdapter(lvAdapter);

		weiboLv.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));

		requestLatestWeibo();

		return view;
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
			Toast.makeText(getActivity(), "来了" + comingNumber + "条新微博",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(), "没有新微博", Toast.LENGTH_SHORT).show();
		}

		refreshWeiboID(statusList);

		lvAdapter.setStatusList(statusList);
		lvAdapter.notifyDataSetChanged();

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

}
