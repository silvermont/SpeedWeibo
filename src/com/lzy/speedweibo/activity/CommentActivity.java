package com.lzy.speedweibo.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.speedweibo.R;

public class CommentActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

		initActionBar();
	}

	private void initActionBar() {
		ActionBar actionBar = this.getActionBar();
		actionBar.setCustomView(R.layout.action_bar_back);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		TextView title = (TextView) actionBar.getCustomView().findViewById(
				R.id.title);
		Intent intent = getIntent();
		title.setText(intent.getStringExtra("action"));

		RelativeLayout back = (RelativeLayout) actionBar.getCustomView()
				.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
