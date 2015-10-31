package com.lzy.speedweibo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.core.GridViewAdapter;
import com.lzy.speedweibo.core.MyApplication;
import com.lzy.speedweibo.core.SmartTextView;
import com.lzy.speedweibo.core.Utils;
import com.sina.weibo.sdk.openapi.models.Status;

public class WeiboActivity extends Activity {
	private Status status;
	private ImageView userHead;
	private ImageView picture;
	private TextView userName;
	private SmartTextView text;
	private TextView source;
	private TextView time;
	private SmartTextView retweetedText;
	private TextView repostsCount;
	private RelativeLayout retweetedLayout;
	private ImageView retweetedPicture;
	private TextView retweetedRepostsCount;
	private GridView pictureGridView;
	private GridView retweetedPictureGridView;
	private int imageWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo);

		status = MyApplication.getStatus();

		userHead = (ImageView) findViewById(R.id.userHead);
		picture = (ImageView) findViewById(R.id.picture);
		userName = (TextView) findViewById(R.id.userName);
		text = (SmartTextView) findViewById(R.id.text);
		source = (TextView) findViewById(R.id.source);
		time = (TextView) findViewById(R.id.time);
		retweetedText = (SmartTextView) findViewById(R.id.retweetedText);
		repostsCount = (TextView) findViewById(R.id.repostsCount);
		retweetedLayout = (RelativeLayout) findViewById(R.id.retweetedLayout);
		retweetedPicture = (ImageView) findViewById(R.id.retweetedPicture);
		retweetedRepostsCount = (TextView) findViewById(R.id.retweetedRepostsCount);
		pictureGridView = (GridView) findViewById(R.id.pictureGridView);
		retweetedPictureGridView = (GridView) findViewById(R.id.retweetedPictureGridView);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int widthPX = metric.widthPixels;// 屏幕宽度（像素）
		float density = metric.density;// 屏幕密度（0.75 / 1.0 / 1.5）
		imageWidth = (int) ((widthPX - 30 * density) / 3);

		ActionBar actionBar = this.getActionBar();
		actionBar.setCustomView(R.layout.action_bar_back);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		TextView title = (TextView) actionBar.getCustomView().findViewById(
				R.id.title);
		title.setText("微博正文");

		RelativeLayout back = (RelativeLayout) actionBar.getCustomView().findViewById(
				R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		text.setMText(status.text);
		userName.setText(status.user.screen_name);
		source.setText("来源：" + Utils.transformSource(status.source));
		time.setText(Utils.transformTime(status.created_at));
		repostsCount.setText(Utils.transformRepostsCount(status.reposts_count,
				status.comments_count));

		text.setTextColor(getResources().getColor(R.color.text_black));
		text.invalidate();

		MyApplication.asyncLoadImage(status.user.profile_image_url, userHead);

		try {
			if (status.pic_urls.size() > 1) {
				picture.setVisibility(View.GONE);
				pictureGridView.setVisibility(View.VISIBLE);
				pictureGridView.setAdapter(new GridViewAdapter(this,
						status.pic_urls, imageWidth));
			} else if (status.pic_urls.size() == 1) {
				picture.setVisibility(View.VISIBLE);
				pictureGridView.setVisibility(View.GONE);
				MyApplication.asyncLoadImage(status.bmiddle_pic, picture);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			picture.setVisibility(View.GONE);
			pictureGridView.setVisibility(View.GONE);
		}

		try {
			// 表明该微博为转发
			retweetedText.setMText("@"
					+ status.retweeted_status.user.screen_name + ":"
					+ status.retweeted_status.text);
			retweetedLayout.setVisibility(View.VISIBLE);

			retweetedText.setTextColor(getResources().getColor(
					R.color.text_black));
			retweetedText.invalidate();

			retweetedRepostsCount.setText(Utils.transformRepostsCount(
					status.retweeted_status.reposts_count,
					status.retweeted_status.comments_count));

			try {

				if (status.retweeted_status.pic_urls.size() > 1) {
					retweetedPicture.setVisibility(View.GONE);
					retweetedPictureGridView.setVisibility(View.VISIBLE);
					retweetedPictureGridView
							.setAdapter(new GridViewAdapter(this,
									status.retweeted_status.pic_urls,
									imageWidth));
				} else if (status.retweeted_status.pic_urls.size() == 1) {
					retweetedPicture.setVisibility(View.VISIBLE);
					retweetedPictureGridView.setVisibility(View.GONE);
					MyApplication.asyncLoadImage(
							status.retweeted_status.bmiddle_pic,
							retweetedPicture);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				retweetedPicture.setVisibility(View.GONE);
				retweetedPictureGridView.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			e.printStackTrace();
			retweetedLayout.setVisibility(View.GONE);
		}
	}
}
