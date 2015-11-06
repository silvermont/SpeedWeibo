package com.lzy.speedweibo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.adapter.CommentAdapter;
import com.lzy.speedweibo.adapter.RepostAdapter;
import com.lzy.speedweibo.core.Constants;
import com.lzy.speedweibo.core.MyApplication;
import com.lzy.speedweibo.core.SmartTextView;
import com.lzy.speedweibo.core.Utils;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.Repost;
import com.sina.weibo.sdk.openapi.models.RepostList;
import com.sina.weibo.sdk.openapi.models.Status;

public class WeiboActivity extends BaseActivity {
	private Status status;
	private RelativeLayout weiboLayout;
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
	private ListView listView;
	private RelativeLayout retweetedLayout;
	private ImageView retweetedPicture;
	private TextView retweetedRepostsCount;
	private RelativeLayout footerView;
	private TextView loadMore;
	private int imageWidth;
	private CommentsAPI mCommentsAPI;
	private StatusesAPI mStatusesAPI;
	private RequestListener mListener;
	private List<Comment> commentList;
	private List<Repost> repostList;
	private CommentAdapter commentAdapter;
	private RepostAdapter repostAdapter;
	private long maxCommentID;
	private long maxRepostID;
	private boolean isShowComments = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo);

		footerView = (RelativeLayout) View.inflate(this, R.layout.view_footer,
				null);
		loadMore = (TextView) footerView.findViewById(R.id.loadMore);

		weiboLayout = (RelativeLayout) View.inflate(this,
				R.layout.layout_weibo_for_comment_activity, null);
		userHead = (ImageView) weiboLayout.findViewById(R.id.userHead);
		picture = (ImageView) weiboLayout.findViewById(R.id.picture);
		userName = (TextView) weiboLayout.findViewById(R.id.userName);
		text = (SmartTextView) weiboLayout.findViewById(R.id.text);
		source = (TextView) weiboLayout.findViewById(R.id.source);
		time = (TextView) weiboLayout.findViewById(R.id.time);
		retweetedText = (SmartTextView) weiboLayout
				.findViewById(R.id.retweetedText);
		retweetedLayout = (RelativeLayout) weiboLayout
				.findViewById(R.id.retweetedLayout);
		retweetedPicture = (ImageView) weiboLayout
				.findViewById(R.id.retweetedPicture);
		retweetedRepostsCount = (TextView) weiboLayout
				.findViewById(R.id.retweetedRepostsCount);

		picture1 = (ImageView) weiboLayout.findViewById(R.id.picture1);
		picture2 = (ImageView) weiboLayout.findViewById(R.id.picture2);
		picture3 = (ImageView) weiboLayout.findViewById(R.id.picture3);
		picture4 = (ImageView) weiboLayout.findViewById(R.id.picture4);
		picture5 = (ImageView) weiboLayout.findViewById(R.id.picture5);
		picture6 = (ImageView) weiboLayout.findViewById(R.id.picture6);
		picture7 = (ImageView) weiboLayout.findViewById(R.id.picture7);
		picture8 = (ImageView) weiboLayout.findViewById(R.id.picture8);
		picture9 = (ImageView) weiboLayout.findViewById(R.id.picture9);
		pictureArray = new ImageView[] { picture1, picture2, picture3,
				picture4, picture5, picture6, picture7, picture8, picture9 };
		retweetedPicture1 = (ImageView) weiboLayout
				.findViewById(R.id.retweetedPicture1);
		retweetedPicture2 = (ImageView) weiboLayout
				.findViewById(R.id.retweetedPicture2);
		retweetedPicture3 = (ImageView) weiboLayout
				.findViewById(R.id.retweetedPicture3);
		retweetedPicture4 = (ImageView) weiboLayout
				.findViewById(R.id.retweetedPicture4);
		retweetedPicture5 = (ImageView) weiboLayout
				.findViewById(R.id.retweetedPicture5);
		retweetedPicture6 = (ImageView) weiboLayout
				.findViewById(R.id.retweetedPicture6);
		retweetedPicture7 = (ImageView) weiboLayout
				.findViewById(R.id.retweetedPicture7);
		retweetedPicture8 = (ImageView) weiboLayout
				.findViewById(R.id.retweetedPicture8);
		retweetedPicture9 = (ImageView) weiboLayout
				.findViewById(R.id.retweetedPicture9);
		retweetedPictureArray = new ImageView[] { retweetedPicture1,
				retweetedPicture2, retweetedPicture3, retweetedPicture4,
				retweetedPicture5, retweetedPicture6, retweetedPicture7,
				retweetedPicture8, retweetedPicture9 };

		retweetedTv = (TextView) weiboLayout.findViewById(R.id.retweetedTv);
		commentTv = (TextView) weiboLayout.findViewById(R.id.commentTv);
		retweetedLine = weiboLayout.findViewById(R.id.retweetedLine);
		commentLine = weiboLayout.findViewById(R.id.commentLine);

		listView = (ListView) findViewById(R.id.retweetedLv);

		status = MyApplication.getStatus();
		imageWidth = MyApplication.getImageWidth();
		mCommentsAPI = MyApplication.getCommentsAPI();
		mStatusesAPI = MyApplication.getStatusesAPI();
		commentList = new ArrayList<Comment>();
		repostList = new ArrayList<Repost>();
		commentAdapter = new CommentAdapter(this);
		repostAdapter = new RepostAdapter(this);

		mListener = new RequestListener() {
			@Override
			public void onComplete(String response) {
				if (!TextUtils.isEmpty(response)) {
					if (response.contains("comments")) {
						CommentList comments = CommentList.parse(response);
						if (comments != null && comments.total_number > 0) {
							handleComments(comments.commentList);
						}
					}
					if (response.contains("reposts")) {
						RepostList reposts = RepostList.parse(response);
						if (reposts != null && reposts.total_number > 0) {
							handleReposts(reposts.repostList);
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
		listView.addHeaderView(weiboLayout);
		listView.addFooterView(footerView);
		listView.setAdapter(commentAdapter);

		loadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShowComments) {
					mCommentsAPI.show(Long.parseLong(status.id), 0,
							maxCommentID, 5, 1, 0, mListener);
				} else {
					mStatusesAPI.repostTimeline(Long.parseLong(status.id), 0,
							maxRepostID, 5, 1, 0, mListener);
				}
			}
		});

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

		if (status.pic_urls.size() == 0) {
			picture.setVisibility(View.GONE);
			for (int i = 0; i < 9; i++) {
				pictureArray[i].setVisibility(View.GONE);
			}
		} else if (status.pic_urls.size() == 1) {
			picture.setVisibility(View.VISIBLE);
			for (int i = 0; i < 9; i++) {
				pictureArray[i].setVisibility(View.GONE);
			}
			MyApplication.asyncLoadImage(status.bmiddle_pic, picture);
		} else {
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
		}

		if (status.retweeted_status.id.equals("0")) {
			retweetedLayout.setVisibility(View.GONE);
		} else {
			retweetedLayout.setVisibility(View.VISIBLE);
			retweetedLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MyApplication.setStatus(status.retweeted_status);
					Intent intent = new Intent(WeiboActivity.this,
							WeiboActivity.class);
					startActivity(intent);
				}
			});

			retweetedText.setMText("@"
					+ status.retweeted_status.user.screen_name + ":"
					+ status.retweeted_status.text);
			retweetedText.setTextColor(getResources().getColor(
					R.color.text_black));
			retweetedText.invalidate();

			retweetedRepostsCount.setText(Utils.transformRepostsCount(
					status.retweeted_status.reposts_count,
					status.retweeted_status.comments_count));

			if (status.retweeted_status.pic_urls.size() == 0) {
				retweetedPicture.setVisibility(View.GONE);
				for (int i = 0; i < 9; i++) {
					retweetedPictureArray[i].setVisibility(View.GONE);
				}
			} else if (status.retweeted_status.pic_urls.size() == 1) {
				retweetedPicture.setVisibility(View.VISIBLE);
				for (int i = 0; i < 9; i++) {
					retweetedPictureArray[i].setVisibility(View.GONE);
				}
				MyApplication.asyncLoadImage(
						status.retweeted_status.bmiddle_pic, retweetedPicture);
			} else {
				retweetedPicture.setVisibility(View.GONE);
				int imageCount = status.retweeted_status.pic_urls.size();
				for (int i = 0; i < 9; i++) {
					if (i < imageCount) {
						retweetedPictureArray[i].setVisibility(View.VISIBLE);
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
			}
		}

		mCommentsAPI.show(Long.parseLong(status.id), 0, 0, 50, 1, 0, mListener);
		mStatusesAPI.repostTimeline(Long.parseLong(status.id), 0, 0, 50, 1, 0,
				mListener);
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

	private void handleComments(List<Comment> list) {
		boolean isNewlyAdded = false;

		if (commentList.size() == 0) {
			commentList = list;
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (Long.valueOf(list.get(i).id) < maxCommentID) {
					commentList.add(list.get(i));
					isNewlyAdded = true;
				}
			}
			if (!isNewlyAdded) {
				Toast.makeText(this, "没有更多评论", Toast.LENGTH_SHORT).show();
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

	private void handleReposts(List<Repost> responseList) {
		boolean isNewlyAdded = false;

		if (repostList.size() == 0) {
			repostList = responseList;
		} else {
			for (int i = 0; i < responseList.size(); i++) {
				if (Long.valueOf(responseList.get(i).id) < maxRepostID) {
					repostList.add(responseList.get(i));
					isNewlyAdded = true;
				}
			}
			if (!isNewlyAdded) {
				Toast.makeText(this, "没有更多转发", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		maxRepostID = Long.parseLong(repostList.get(repostList.size() - 1).id);
		repostAdapter.setData(repostList);
		repostAdapter.notifyDataSetChanged();

		if (!isShowComments) {
			loadMore.setText("加载更多");
		}
	}

	public void showReposts(View view) {
		if (isShowComments) {
			retweetedTv.setTextColor(getResources().getColor(R.color.blue));
			commentTv.setTextColor(getResources().getColor(R.color.text_gray));
			retweetedLine.setVisibility(View.VISIBLE);
			commentLine.setVisibility(View.GONE);

			listView.setAdapter(repostAdapter);
			isShowComments = false;

			if (repostList.size() == 0) {
				loadMore.setText("暂时没有转发");
			} else {
				loadMore.setText("加载更多");
			}
		} else {
			Intent intent = new Intent(WeiboActivity.this, EditActivity.class);
			intent.putExtra("action", "转发");
			intent.putExtra("id", status.id);
			startActivity(intent);
		}
	}

	public void showComments(View view) {
		if (!isShowComments) {
			commentTv.setTextColor(getResources().getColor(R.color.blue));
			retweetedTv
					.setTextColor(getResources().getColor(R.color.text_gray));
			commentLine.setVisibility(View.VISIBLE);
			retweetedLine.setVisibility(View.GONE);

			listView.setAdapter(commentAdapter);
			isShowComments = true;

			if (commentList.size() == 0) {
				loadMore.setText("暂时没有评论");
			} else {
				loadMore.setText("加载更多");
			}
		} else {
			Intent intent = new Intent(WeiboActivity.this, EditActivity.class);
			intent.putExtra("action", "评论");
			intent.putExtra("id", status.id);
			startActivity(intent);
		}
	}
}
