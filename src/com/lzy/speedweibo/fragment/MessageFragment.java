package com.lzy.speedweibo.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.adapter.AtMeAdapter;
import com.lzy.speedweibo.adapter.CommentToMeAdapter;
import com.lzy.speedweibo.core.MyApplication;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

public class MessageFragment extends Fragment {
	private TextView atMe;
	private TextView commentToMe;
	private ListView listView;
	private RelativeLayout footerView;
	private TextView loadMore;
	private CommentsAPI mCommentsAPI;
	private StatusesAPI mStatusesAPI;
	private RequestListener mListener;
	private CommentToMeAdapter commentAdapter;
	private AtMeAdapter atMeAdapter;
	private List<Comment> commentList;
	private List<Status> statusList;
	private long maxCommentID;
	private long maxStatusID;
	private boolean isShowComments = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, null);
		atMe = (TextView) view.findViewById(R.id.atMe);
		commentToMe = (TextView) view.findViewById(R.id.commentToMe);
		listView = (ListView) view.findViewById(R.id.listView);
		footerView = (RelativeLayout) View.inflate(getActivity(),
				R.layout.view_footer, null);
		loadMore = (TextView) footerView.findViewById(R.id.loadMore);

		mCommentsAPI = MyApplication.getCommentsAPI(getActivity());
		mStatusesAPI = MyApplication.getStatusesAPI(getActivity());

		commentAdapter = new CommentToMeAdapter(getActivity());
		atMeAdapter = new AtMeAdapter(getActivity());
		commentList = new ArrayList<Comment>();
		statusList = new ArrayList<Status>();

		mListener = new RequestListener() {
			@Override
			public void onComplete(String response) {
				if (!TextUtils.isEmpty(response)) {
					if (response.substring(2).startsWith("comments")) {
						CommentList comments = CommentList.parse(response);
						if (comments != null && comments.total_number > 0) {
							handleComments(comments.commentList);
						}
					}
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

		loadMore.setText("暂时没有评论");
		listView.addFooterView(footerView);
		listView.setAdapter(commentAdapter);

		loadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShowComments) {
					mCommentsAPI.toME(0, maxCommentID, 50, 1, 0, 0, mListener);
				} else {
					mStatusesAPI.mentions(0, maxStatusID, 50, 1, 0, 0, 0,
							false, mListener);
				}
			}
		});

		atMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShowComments) {
					isShowComments = false;

					atMe.setTextColor(Color.WHITE);
					atMe.setBackground(getResources().getDrawable(
							R.drawable.button_at_me_press));

					commentToMe.setTextColor(getResources().getColor(
							R.color.text_black));
					commentToMe.setBackground(getResources().getDrawable(
							R.drawable.button_comment_to_me_normal));

					listView.setAdapter(atMeAdapter);

					if (statusList.size() == 0) {
						loadMore.setText("暂时没有@");
					} else {
						loadMore.setText("加载更多");
					}
				}
			}
		});

		commentToMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isShowComments) {
					isShowComments = true;

					atMe.setTextColor(getResources().getColor(
							R.color.text_black));
					atMe.setBackground(getResources().getDrawable(
							R.drawable.button_at_me_normal));

					commentToMe.setTextColor(Color.WHITE);
					commentToMe.setBackground(getResources().getDrawable(
							R.drawable.button_comment_to_me_press));

					listView.setAdapter(commentAdapter);

					if (commentList.size() == 0) {
						loadMore.setText("暂时没有评论");
					} else {
						loadMore.setText("加载更多");
					}
				}
			}
		});

		mCommentsAPI.toME(0, 0, 50, 1, 0, 0, mListener);
		mStatusesAPI.mentions(0, 0, 50, 1, 0, 0, 0, false, mListener);

		return view;
	}

	private void handleComments(List<Comment> list) {
		boolean isDataChanged = false;

		if (commentList.size() == 0) {
			commentList = list;
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (Long.valueOf(list.get(i).id) < maxCommentID) {
					commentList.add(list.get(i));
					isDataChanged = true;
				}
			}
			if (!isDataChanged) {
				Toast.makeText(getActivity(), "没有更多评论", Toast.LENGTH_SHORT)
						.show();
				return;
			}
		}

		maxCommentID = Long
				.parseLong(commentList.get(commentList.size() - 1).id);
		commentAdapter.setData(commentList);
		commentAdapter.notifyDataSetChanged();

		if (isShowComments) {
			loadMore.setText("加载更多");
		}
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
				Toast.makeText(getActivity(), "没有更多转发", Toast.LENGTH_SHORT)
						.show();
				return;
			}
		}

		maxStatusID = Long.parseLong(statusList.get(statusList.size() - 1).id);
		atMeAdapter.setData(statusList);
		atMeAdapter.notifyDataSetChanged();

		if (!isShowComments) {
			loadMore.setText("加载更多");
		}
	}
}
