package com.lzy.speedweibo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.core.CommentLvAdapter;
import com.lzy.speedweibo.core.Constants;
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
	private RelativeLayout singleWeibo;
	private ImageView userHead;
	private ImageView picture;
	private TextView userName;
	private SmartTextView text;
	private TextView source;
	private TextView time;
	private SmartTextView retweetedText;
	private ImageView picture1;
	private ImageView picture2;
	private ImageView picture3;
	private ImageView picture4;
	private ImageView picture5;
	private ImageView picture6;
	private ImageView picture7;
	private ImageView picture8;
	private ImageView picture9;
	private ImageView retweetedPicture1;
	private ImageView retweetedPicture2;
	private ImageView retweetedPicture3;
	private ImageView retweetedPicture4;
	private ImageView retweetedPicture5;
	private ImageView retweetedPicture6;
	private ImageView retweetedPicture7;
	private ImageView retweetedPicture8;
	private ImageView retweetedPicture9;
	private ImageView[] pictureArray;
	private ImageView[] retweetedPictureArray;
	private TextView retweetedTv;
	private TextView commentTv;
	private View retweetedLine;
	private View commentLine;
	private ListView retweetedLv;
	private RelativeLayout retweetedLayout;
	private ImageView retweetedPicture;
	private TextView retweetedRepostsCount;
	private RelativeLayout footerView;
	private TextView loadMore;
	private int imageWidth;
	/** 微博评论接口 */
	private CommentsAPI mCommentsAPI;
	private RequestListener mListener;
	private List<Comment> commentList;
	private CommentLvAdapter adapter;
	private long maxID;
	private boolean isFirstRequest = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo);

		footerView = (RelativeLayout) View.inflate(this, R.layout.view_footer,
				null);
		loadMore = (TextView) footerView.findViewById(R.id.loadMore);
		singleWeibo = (RelativeLayout) View.inflate(this,
				R.layout.layout_weibo_for_comment_activity, null);
		userHead = (ImageView) singleWeibo.findViewById(R.id.userHead);
		picture = (ImageView) singleWeibo.findViewById(R.id.picture);
		userName = (TextView) singleWeibo.findViewById(R.id.userName);
		text = (SmartTextView) singleWeibo.findViewById(R.id.text);
		source = (TextView) singleWeibo.findViewById(R.id.source);
		time = (TextView) singleWeibo.findViewById(R.id.time);
		retweetedText = (SmartTextView) singleWeibo
				.findViewById(R.id.retweetedText);
		retweetedTv = (TextView) singleWeibo.findViewById(R.id.retweetedTv);
		commentTv = (TextView) singleWeibo.findViewById(R.id.commentTv);
		retweetedLine = singleWeibo.findViewById(R.id.retweetedLine);
		commentLine = singleWeibo.findViewById(R.id.commentLine);
		retweetedLv = (ListView) findViewById(R.id.retweetedLv);
		retweetedLayout = (RelativeLayout) singleWeibo
				.findViewById(R.id.retweetedLayout);
		retweetedPicture = (ImageView) singleWeibo
				.findViewById(R.id.retweetedPicture);
		retweetedRepostsCount = (TextView) singleWeibo
				.findViewById(R.id.retweetedRepostsCount);
		picture1 = (ImageView) singleWeibo.findViewById(R.id.picture1);
		picture2 = (ImageView) singleWeibo.findViewById(R.id.picture2);
		picture3 = (ImageView) singleWeibo.findViewById(R.id.picture3);
		picture4 = (ImageView) singleWeibo.findViewById(R.id.picture4);
		picture5 = (ImageView) singleWeibo.findViewById(R.id.picture5);
		picture6 = (ImageView) singleWeibo.findViewById(R.id.picture6);
		picture7 = (ImageView) singleWeibo.findViewById(R.id.picture7);
		picture8 = (ImageView) singleWeibo.findViewById(R.id.picture8);
		picture9 = (ImageView) singleWeibo.findViewById(R.id.picture9);
		pictureArray = new ImageView[] { picture1, picture2, picture3,
				picture4, picture5, picture6, picture7, picture8, picture9 };
		retweetedPicture1 = (ImageView) singleWeibo
				.findViewById(R.id.retweetedPicture1);
		retweetedPicture2 = (ImageView) singleWeibo
				.findViewById(R.id.retweetedPicture2);
		retweetedPicture3 = (ImageView) singleWeibo
				.findViewById(R.id.retweetedPicture3);
		retweetedPicture4 = (ImageView) singleWeibo
				.findViewById(R.id.retweetedPicture4);
		retweetedPicture5 = (ImageView) singleWeibo
				.findViewById(R.id.retweetedPicture5);
		retweetedPicture6 = (ImageView) singleWeibo
				.findViewById(R.id.retweetedPicture6);
		retweetedPicture7 = (ImageView) singleWeibo
				.findViewById(R.id.retweetedPicture7);
		retweetedPicture8 = (ImageView) singleWeibo
				.findViewById(R.id.retweetedPicture8);
		retweetedPicture9 = (ImageView) singleWeibo
				.findViewById(R.id.retweetedPicture9);
		retweetedPictureArray = new ImageView[] { retweetedPicture1,
				retweetedPicture2, retweetedPicture3, retweetedPicture4,
				retweetedPicture5, retweetedPicture6, retweetedPicture7,
				retweetedPicture8, retweetedPicture9 };

		status = MyApplication.getStatus();
		imageWidth = MyApplication.getImageWidth();
		commentList = new ArrayList<Comment>();
		retweetedLv.addHeaderView(singleWeibo);
		retweetedLv.addFooterView(footerView);
		adapter = new CommentLvAdapter(this);
		retweetedLv.setAdapter(adapter);

		initActionBar();

		text.setMText(status.text);
		userName.setText(status.user.screen_name);
		source.setText(Utils.transformSource(status.source));
		time.setText(Utils.transformTime(status.created_at));
		retweetedTv.setText("转发 " + status.reposts_count);
		commentTv.setText("评论 " + status.comments_count);

		text.setTextColor(getResources().getColor(R.color.text_black));
		text.invalidate();

		MyApplication.asyncLoadImage(status.user.profile_image_url, userHead);

		try {
			if (status.pic_urls.size() > 1) {
				picture.setVisibility(View.GONE);
				int imageCount = status.pic_urls.size();
				for (int i = 0; i < 9; i++) {
					if (i < imageCount) {
						pictureArray[i].setVisibility(View.VISIBLE);
						LayoutParams params = (LayoutParams) pictureArray[i]
								.getLayoutParams();
						params.width = imageWidth;
						params.height = imageWidth;
						pictureArray[i].setLayoutParams(params);
						pictureArray[i]
								.setScaleType(ImageView.ScaleType.CENTER_CROP);
						MyApplication.asyncLoadImage(status.pic_urls.get(i),
								pictureArray[i]);
					} else {
						pictureArray[i].setVisibility(View.GONE);
					}
				}
			} else if (status.pic_urls.size() == 1) {
				picture.setVisibility(View.VISIBLE);
				for (int i = 0; i < 9; i++) {
					pictureArray[i].setVisibility(View.GONE);
				}
				MyApplication.asyncLoadImage(status.bmiddle_pic, picture);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			picture.setVisibility(View.GONE);
			for (int i = 0; i < 9; i++) {
				pictureArray[i].setVisibility(View.GONE);
			}
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
					int imageCount = status.retweeted_status.pic_urls.size();
					for (int i = 0; i < 9; i++) {
						if (i < imageCount) {
							retweetedPictureArray[i]
									.setVisibility(View.VISIBLE);
							LayoutParams params = (LayoutParams) retweetedPictureArray[i]
									.getLayoutParams();
							params.width = imageWidth;
							params.height = imageWidth;
							retweetedPictureArray[i].setLayoutParams(params);
							retweetedPictureArray[i]
									.setScaleType(ImageView.ScaleType.CENTER_CROP);
							MyApplication.asyncLoadImage(
									status.retweeted_status.pic_urls.get(i),
									retweetedPictureArray[i]);
						} else {
							retweetedPictureArray[i].setVisibility(View.GONE);
						}
					}
				} else if (status.retweeted_status.pic_urls.size() == 1) {
					retweetedPicture.setVisibility(View.VISIBLE);
					for (int i = 0; i < 9; i++) {
						retweetedPictureArray[i].setVisibility(View.GONE);
					}
					MyApplication.asyncLoadImage(
							status.retweeted_status.bmiddle_pic,
							retweetedPicture);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				retweetedPicture.setVisibility(View.GONE);
				for (int i = 0; i < 9; i++) {
					retweetedPictureArray[i].setVisibility(View.GONE);
				}
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

		loadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCommentsAPI.show(Long.parseLong(status.id), 0, maxID, 5, 1, 0,
						mListener);
			}
		});

		mCommentsAPI = new CommentsAPI(this, Constants.APP_KEY,
				MyApplication.getmAccessToken());
		mCommentsAPI.show(Long.parseLong(status.id), 0, 0, 5, 1, 0, mListener);
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
		boolean isNewlyAdded = false;

		if (isFirstRequest) {
			commentList = newCommentList;
			isFirstRequest = false;
		} else {
			// commentList.addAll(commentList.size() - 1, newCommentList);
			for (int i = 0; i < newCommentList.size(); i++) {
				if (Long.valueOf(newCommentList.get(i).id) < maxID) {
					commentList.add(newCommentList.get(i));
					isNewlyAdded = true;
				}
			}
			if (!isNewlyAdded) {
				Toast.makeText(this, "没有更多评论", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		maxID = Long.parseLong(commentList.get(commentList.size() - 1).id);
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
