package com.lzy.speedweibo.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.core.Constants;
import com.lzy.speedweibo.core.MyApplication;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;

public class CommentActivity extends BaseActivity {
	private EditText text;
	private CommentsAPI mCommentsAPI;
	private StatusesAPI mStatusesAPI;
	private long id;
	private String action;
	private RequestListener mListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

		text = (EditText) findViewById(R.id.text);

		mCommentsAPI = new CommentsAPI(this, Constants.APP_KEY,
				MyApplication.getmAccessToken());
		mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY,
				MyApplication.getmAccessToken());
		mListener = new RequestListener() {

			@Override
			public void onWeiboException(WeiboException arg0) {
				arg0.printStackTrace();
				Toast.makeText(CommentActivity.this, "操作失敗", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onComplete(String arg0) {
				Toast.makeText(CommentActivity.this, "操作成功", Toast.LENGTH_SHORT)
						.show();
				finish();
			}
		};

		Intent intent = getIntent();
		action = intent.getStringExtra("action");
		if (!action.equals("发表新微博")) {
			id = Long.parseLong(intent.getStringExtra("id"));
		}

		initActionBar();
	}

	private void initActionBar() {
		ActionBar actionBar = this.getActionBar();
		actionBar.setCustomView(R.layout.action_bar_send);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		TextView title = (TextView) actionBar.getCustomView().findViewById(
				R.id.title);
		title.setText(action);

		RelativeLayout back = (RelativeLayout) actionBar.getCustomView()
				.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		RelativeLayout send = (RelativeLayout) actionBar.getCustomView()
				.findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = text.getText().toString();
				if (content.equals("")) {
					Toast.makeText(CommentActivity.this, "内容不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (action.equals("评论")) {
					mCommentsAPI.create(content, id, false, null);
				} else if (action.equals("转发")) {
					mStatusesAPI.repost(id, content, 0, null);
				} else if (action.equals("发表新微博")) {
					mStatusesAPI.update(content, "0", "0", null);
				}
			}
		});
	}
}
