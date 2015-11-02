package com.lzy.speedweibo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.core.CommentLvAdapter;
import com.lzy.speedweibo.core.Constants;
import com.lzy.speedweibo.core.GridViewAdapter;
import com.lzy.speedweibo.core.MyApplication;
import com.lzy.speedweibo.core.SmartTextView;
import com.lzy.speedweibo.core.Utils;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.Status;

public class WeiboActivity extends BaseActivity {
	private Status status;
	private ImageView userHead;
	private ImageView picture;
	private TextView userName;
	private SmartTextView text;
	private TextView source;
	private TextView time;
	private SmartTextView retweetedText;
	// private TextView repostsCount;
	private TextView retweetedTv;
	private TextView commentTv;
	private View retweetedLine;
	private View commentLine;
	private ListView retweetedLv;
	private RelativeLayout retweetedLayout;
	private ImageView retweetedPicture;
	private TextView retweetedRepostsCount;
	private GridView pictureGridView;
	private GridView retweetedPictureGridView;
	private int imageWidth;
	/** 微博评论接口 */
	private CommentsAPI mCommentsAPI;
	private RequestListener mListener;
	private List<Comment> commentList;
	private CommentLvAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo);

		userHead = (ImageView) findViewById(R.id.userHead);
		picture = (ImageView) findViewById(R.id.picture);
		userName = (TextView) findViewById(R.id.userName);
		text = (SmartTextView) findViewById(R.id.text);
		source = (TextView) findViewById(R.id.source);
		time = (TextView) findViewById(R.id.time);
		retweetedText = (SmartTextView) findViewById(R.id.retweetedText);
		// repostsCount = (TextView) findViewById(R.id.repostsCount);
		retweetedTv = (TextView) findViewById(R.id.retweetedTv);
		commentTv = (TextView) findViewById(R.id.commentTv);
		retweetedLine = findViewById(R.id.retweetedLine);
		commentLine = findViewById(R.id.commentLine);
		retweetedLv = (ListView) findViewById(R.id.retweetedLv);
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

		status = MyApplication.getStatus();
		commentList = new ArrayList<Comment>();
		adapter = new CommentLvAdapter(this, commentList);
		retweetedLv.setAdapter(adapter);

		initActionBar();

		text.setMText(status.text);
		userName.setText(status.user.screen_name);
		source.setText("来源：" + Utils.transformSource(status.source));
		time.setText(Utils.transformTime(status.created_at));
		// repostsCount.setText(Utils.transformRepostsCount(status.reposts_count,
		// status.comments_count));
		retweetedTv.setText("转发 " + status.reposts_count);
		commentTv.setText("评论 " + status.comments_count);

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

		mListener = new RequestListener() {
			@Override
			public void onComplete(String response) {
				if (!TextUtils.isEmpty(response)) {
					CommentList comments = CommentList.parse(response);
					if (comments != null && comments.total_number > 0) {
						handleResponseComment(comments.commentList);
					}
				}
			}

			@Override
			public void onWeiboException(WeiboException e) {
				e.printStackTrace();
			}
		};

		mCommentsAPI = new CommentsAPI(this, Constants.APP_KEY,
				MyApplication.getmAccessToken());
		mCommentsAPI.show(Long.parseLong(status.id), 0, 0, 50, 1, 0, mListener);
	}

	private void initActionBar() {
		ActionBar actionBar = this.getActionBar();
		actionBar.setCustomView(R.layout.action_bar_back);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		TextView title = (TextView) actionBar.getCustomView().findViewById(
				R.id.title);
		title.setText("微博正文");

		RelativeLayout back = (RelativeLayout) actionBar.getCustomView()
				.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void handleResponseComment(List<Comment> newCommentList) {
		commentList = newCommentList;
		adapter.setCommentList(commentList);
		adapter.notifyDataSetChanged();
	}

	public void chooseRetweetedCountLayout(View view) {
		retweetedTv.setTextColor(getResources().getColor(R.color.blue));
		commentTv.setTextColor(getResources().getColor(R.color.text_gray));
		retweetedLine.setVisibility(View.VISIBLE);
		commentLine.setVisibility(View.GONE);
	}

	public void chooseCommentCountLayout(View view) {
		commentTv.setTextColor(getResources().getColor(R.color.blue));
		retweetedTv.setTextColor(getResources().getColor(R.color.text_gray));
		commentLine.setVisibility(View.VISIBLE);
		retweetedLine.setVisibility(View.GONE);
	}
}
