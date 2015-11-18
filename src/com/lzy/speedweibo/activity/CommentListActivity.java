package com.lzy.speedweibo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.adapter.CommentToMeAdapter;
import com.lzy.speedweibo.core.MyApplication;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;

public class CommentListActivity extends BaseActivity {

	private ListView listView;
	private CommentToMeAdapter adapter;
	private RelativeLayout footerView;
	private TextView loadMore;
	private CommentsAPI mCommentsAPI;
	private RequestListener mListener;
	private List<Comment> commentList;
	private long maxCommentID;
	private String action;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_list);

		listView = (ListView) findViewById(R.id.listView);
		footerView = (RelativeLayout) View.inflate(this, R.layout.view_footer,
				null);
		loadMore = (TextView) footerView.findViewById(R.id.loadMore);

		mCommentsAPI = MyApplication.getCommentsAPI(this);
		adapter = new CommentToMeAdapter(this);
		commentList = new ArrayList<Comment>();

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
				}
			}

			@Override
			public void onWeiboException(WeiboException e) {
				e.printStackTrace();
			}
		};

		loadMore.setText("暂时没有数据");
		listView.addFooterView(footerView);
		listView.setAdapter(adapter);

		loadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (action.equals("comments")) {
					mCommentsAPI.toME(0, maxCommentID, 50, 1, 0, 0, mListener);
				} else if (action.equals("at")) {
					mCommentsAPI.mentions(0, maxCommentID, 50, 1, 0, 0,
							mListener);
				}
			}
		});

		action = getIntent().getStringExtra("action");

		initActionBar();

		if (action.equals("comments")) {
			mCommentsAPI.toME(0, 0, 50, 1, 0, 0, mListener);
		} else if (action.equals("at")) {
			mCommentsAPI.mentions(0, 0, 50, 1, 0, 0, mListener);
		}
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
				Toast.makeText(this, "没有更多", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		maxCommentID = Long
				.parseLong(commentList.get(commentList.size() - 1).id);
		adapter.setData(commentList);
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
		if (action.equals("comments")) {
			title.setText("给我的评论");
		} else if (action.equals("at")) {
			title.setText("@我的评论");
		}
	}
}
