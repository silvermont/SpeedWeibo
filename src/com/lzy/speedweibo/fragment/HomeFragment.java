package com.lzy.speedweibo.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.activity.WeiboActivity;
import com.lzy.speedweibo.core.AccessTokenKeeper;
import com.lzy.speedweibo.core.Constants;
import com.lzy.speedweibo.core.GridViewAdapter;
import com.lzy.speedweibo.core.MyApplication;
import com.lzy.speedweibo.core.SmartTextView;
import com.lzy.speedweibo.core.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

public class HomeFragment extends Fragment {
	private ListView weiboLv;
	private SwipeRefreshLayout refreshLayout;
	private ListViewAdapter myAdapter;
	private List<Status> statusList;
	private List<Status> copyStatusList;
	// private DisplayImageOptions options;
	private long maxWeiboID = 0;
	private long minWeiboID = 0;
	private boolean isFirstRefresh = true;
	private boolean isRequestLatestWeibo = true;
	private RequestListener mListener;
	private StatusesAPI mStatusesAPI;
	private int imageWidth;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		statusList = new ArrayList<Status>();
		myAdapter = new ListViewAdapter();

		Oauth2AccessToken mAccessToken = AccessTokenKeeper
				.readAccessToken(getActivity());
		mStatusesAPI = new StatusesAPI(getActivity(), Constants.APP_KEY,
				mAccessToken);

		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		int widthPX = metric.widthPixels;// 屏幕宽度（像素）
		float density = metric.density;// 屏幕密度（0.75 / 1.0 / 1.5）
		imageWidth = (int) ((widthPX - 30 * density) / 3);

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
							handleResponse(responseStatuses);
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

		View footerView = View.inflate(getActivity(), R.layout.view_footer,
				null);
		TextView loadMore = (TextView) footerView.findViewById(R.id.loadMore);
		loadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!progressDialog.isShowing()) {
					progressDialog.show();
					requestPreviousWeibo();
				}
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
		weiboLv.setAdapter(myAdapter);

		weiboLv.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));

		progressDialog.show();
		requestLatestWeibo();

		return view;
	}

	/**
	 * 请求最新微博
	 */
	private void requestLatestWeibo() {
		isRequestLatestWeibo = true;
		mStatusesAPI.friendsTimeline(maxWeiboID, 0, 20, 1, false, 0, false,
				mListener);
		refreshLayout.setRefreshing(false);
	}

	/**
	 * 请求之前微博
	 */
	private void requestPreviousWeibo() {
		isRequestLatestWeibo = false;
		mStatusesAPI.friendsTimeline(0, minWeiboID, 20, 1, false, 0, false,
				mListener);
	}

	/**
	 * 提示加载的微博数目，并将其加入list，刷新listview
	 * 
	 * @param statuses
	 */
	private void handleResponse(StatusList statuses) {
		try {
			if (statuses.statusList.size() > 0) {
				Toast.makeText(getActivity(),
						"来了" + statuses.statusList.size() + "条新微博",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			Toast.makeText(getActivity(), "啊哦 没有新微博", Toast.LENGTH_SHORT)
					.show();
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			return;
		}

		if (isRequestLatestWeibo) {
			copyStatusList = statusList;
			statusList = statuses.statusList;
			for (int i = 0; i < copyStatusList.size(); i++) {
				statusList.add(copyStatusList.get(i));
			}
		} else {
			for (int i = 1; i < statuses.statusList.size(); i++) {
				statusList.add(statuses.statusList.get(i));
			}
		}

		refreshExtremum(statuses);
		myAdapter.notifyDataSetChanged();

		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 记录微博ID的最大最小值
	 * 
	 * @param statuses
	 */
	private void refreshExtremum(StatusList statuses) {
		if (isFirstRefresh) {
			maxWeiboID = Long.parseLong(statuses.statusList.get(0).id);
			minWeiboID = maxWeiboID;
			isFirstRefresh = false;
		}

		if (Long.parseLong(statuses.statusList.get(0).id) > maxWeiboID) {
			maxWeiboID = Long.parseLong(statuses.statusList.get(0).id);
		}

		if (Long.parseLong(statuses.statusList.get(statuses.statusList.size() - 1).id) < minWeiboID) {
			minWeiboID = Long.parseLong(statuses.statusList
					.get(statuses.statusList.size() - 1).id);
		}
	}

	class ListViewAdapter extends BaseAdapter {
		Holder holder;

		@Override
		public int getCount() {
			return statusList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(getActivity(), R.layout.item_weibo,
						null);
				holder.wholeLayout = (RelativeLayout) convertView
						.findViewById(R.id.wholeLayout);
				holder.userHead = (ImageView) convertView
						.findViewById(R.id.userHead);
				holder.picture = (ImageView) convertView
						.findViewById(R.id.picture);
				holder.userName = (TextView) convertView
						.findViewById(R.id.userName);
				holder.text = (SmartTextView) convertView
						.findViewById(R.id.text);
				holder.source = (TextView) convertView
						.findViewById(R.id.source);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				holder.retweetedText = (SmartTextView) convertView
						.findViewById(R.id.retweetedText);
				holder.repostsCount = (TextView) convertView
						.findViewById(R.id.repostsCount);
				holder.retweetedLayout = (RelativeLayout) convertView
						.findViewById(R.id.retweetedLayout);
				holder.retweetedPicture = (ImageView) convertView
						.findViewById(R.id.retweetedPicture);
				holder.retweetedRepostsCount = (TextView) convertView
						.findViewById(R.id.retweetedRepostsCount);
				holder.pictureGridView = (GridView) convertView
						.findViewById(R.id.pictureGridView);
				holder.retweetedPictureGridView = (GridView) convertView
						.findViewById(R.id.retweetedPictureGridView);

				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			holder.text.setMText(statusList.get(position).text);
			holder.userName.setText(statusList.get(position).user.screen_name);
			holder.source.setText("来源："
					+ Utils.transformSource(statusList.get(position).source));
			holder.time
					.setText(Utils.transformTime(statusList.get(position).created_at));
			holder.repostsCount.setText(Utils.transformRepostsCount(
					statusList.get(position).reposts_count,
					statusList.get(position).comments_count));

			holder.text.setTextColor(getResources()
					.getColor(R.color.text_black));
			holder.text.invalidate();

			MyApplication.asyncLoadImage(
					statusList.get(position).user.profile_image_url,
					holder.userHead);

			try {
				if (statusList.get(position).pic_urls.size() > 1) {
					holder.picture.setVisibility(View.GONE);
					holder.pictureGridView.setVisibility(View.VISIBLE);
					holder.pictureGridView.setAdapter(new GridViewAdapter(
							getContext(), statusList.get(position).pic_urls,
							imageWidth));
				} else if (statusList.get(position).pic_urls.size() == 1) {
					holder.picture.setVisibility(View.VISIBLE);
					holder.pictureGridView.setVisibility(View.GONE);
					MyApplication.asyncLoadImage(
							statusList.get(position).bmiddle_pic,
							holder.picture);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				holder.picture.setVisibility(View.GONE);
				holder.pictureGridView.setVisibility(View.GONE);
			}

			try {
				// 表明该微博为转发
				holder.retweetedText
						.setMText("@"
								+ statusList.get(position).retweeted_status.user.screen_name
								+ ":"
								+ statusList.get(position).retweeted_status.text);
				holder.retweetedLayout.setVisibility(View.VISIBLE);

				holder.retweetedText.setTextColor(getResources().getColor(
						R.color.text_black));
				holder.retweetedText.invalidate();

				holder.retweetedRepostsCount
						.setText(Utils.transformRepostsCount(
								statusList.get(position).retweeted_status.reposts_count,
								statusList.get(position).retweeted_status.comments_count));

				try {

					if (statusList.get(position).retweeted_status.pic_urls
							.size() > 1) {
						holder.retweetedPicture.setVisibility(View.GONE);
						holder.retweetedPictureGridView
								.setVisibility(View.VISIBLE);
						holder.retweetedPictureGridView
								.setAdapter(new GridViewAdapter(
										getContext(),
										statusList.get(position).retweeted_status.pic_urls,
										imageWidth));
					} else if (statusList.get(position).retweeted_status.pic_urls
							.size() == 1) {
						holder.retweetedPicture.setVisibility(View.VISIBLE);
						holder.retweetedPictureGridView
								.setVisibility(View.GONE);
						MyApplication

								.asyncLoadImage(
										statusList.get(position).retweeted_status.bmiddle_pic,
										holder.retweetedPicture);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					holder.retweetedPicture.setVisibility(View.GONE);
					holder.retweetedPictureGridView.setVisibility(View.GONE);
				}

			} catch (Exception e) {
				e.printStackTrace();
				holder.retweetedLayout.setVisibility(View.GONE);
			}

			holder.wholeLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MyApplication.getInstance().setStatus(
							statusList.get(position));
					Intent intent = new Intent(getActivity(),
							WeiboActivity.class);
					startActivity(intent);
				}
			});

			return convertView;
		}

	}

	class Holder {
		RelativeLayout wholeLayout;
		ImageView userHead;
		ImageView picture;
		TextView userName;
		SmartTextView text;
		TextView source;
		TextView time;
		SmartTextView retweetedText;
		TextView repostsCount;
		RelativeLayout retweetedLayout;
		ImageView retweetedPicture;
		TextView retweetedRepostsCount;
		GridView pictureGridView;
		GridView retweetedPictureGridView;
	}
}
