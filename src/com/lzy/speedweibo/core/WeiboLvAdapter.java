package com.lzy.speedweibo.core;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.activity.WeiboActivity;
import com.sina.weibo.sdk.openapi.models.Status;

public class WeiboLvAdapter extends BaseAdapter {
	private List<Status> statusList;
	private int imageWidth;
	private Context context;
	private Holder holder;
	private RelativeLayout.LayoutParams params;

	public WeiboLvAdapter(Context context, List<Status> statusList,
			int imageWidth) {
		super();
		this.context = context;
		this.statusList = statusList;
		this.imageWidth = imageWidth;
		params = new RelativeLayout.LayoutParams(imageWidth, imageWidth);
	}

	public void setStatusList(List<Status> statusList) {
		this.statusList = statusList;
	}

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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.item_weibo2, null);
			holder.wholeLayout = (RelativeLayout) convertView
					.findViewById(R.id.wholeLayout);
			holder.userHead = (ImageView) convertView
					.findViewById(R.id.userHead);
			holder.picture = (ImageView) convertView.findViewById(R.id.picture);
			holder.userName = (TextView) convertView
					.findViewById(R.id.userName);
			holder.text = (SmartTextView) convertView.findViewById(R.id.text);
			holder.source = (TextView) convertView.findViewById(R.id.source);
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
			/*
			 * holder.pictureGridView = (GridView) convertView
			 * .findViewById(R.id.pictureGridView);
			 */
			holder.picture1 = (ImageView) convertView
					.findViewById(R.id.picture1);
			holder.picture2 = (ImageView) convertView
					.findViewById(R.id.picture2);
			holder.picture3 = (ImageView) convertView
					.findViewById(R.id.picture3);
			holder.picture4 = (ImageView) convertView
					.findViewById(R.id.picture4);
			holder.picture5 = (ImageView) convertView
					.findViewById(R.id.picture5);
			holder.picture6 = (ImageView) convertView
					.findViewById(R.id.picture6);
			holder.picture7 = (ImageView) convertView
					.findViewById(R.id.picture7);
			holder.picture8 = (ImageView) convertView
					.findViewById(R.id.picture8);
			holder.picture9 = (ImageView) convertView
					.findViewById(R.id.picture9);
			holder.pictureArray = new ImageView[] { holder.picture1,
					holder.picture2, holder.picture3, holder.picture4,
					holder.picture5, holder.picture6, holder.picture7,
					holder.picture8, holder.picture9 };
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

		holder.text.setTextColor(context.getResources().getColor(
				R.color.text_black));
		holder.text.invalidate();

		MyApplication.asyncLoadImage(
				statusList.get(position).user.profile_image_url,
				holder.userHead);

		try {
			if (statusList.get(position).pic_urls.size() > 1) {
				holder.picture.setVisibility(View.GONE);
				int imageCount = statusList.get(position).pic_urls.size();
				Log.e("", "text " + statusList.get(position).text);
				for (int i = 0; i < 9; i++) {
					if (i < imageCount) {
						holder.pictureArray[i].setVisibility(View.VISIBLE);
						holder.pictureArray[i].setLayoutParams(params);
						MyApplication.asyncLoadImage(
								statusList.get(position).pic_urls.get(i),
								holder.pictureArray[i]);
					} else {
						holder.pictureArray[i].setVisibility(View.GONE);
					}

				}
				for (int i = 0; i < 9; i++) {
					if (holder.pictureArray[i].getVisibility() == View.VISIBLE) {
						Log.e("", i + "可见");
					}
				}

				// holder.pictureGridView.setVisibility(View.VISIBLE);
				// holder.pictureGridView.setAdapter(new
				// GridViewAdapter(context,
				// statusList.get(position).pic_urls, imageWidth));
			} else if (statusList.get(position).pic_urls.size() == 1) {
				holder.picture.setVisibility(View.VISIBLE);
				holder.picture1.setVisibility(View.GONE);
				holder.picture2.setVisibility(View.GONE);
				holder.picture3.setVisibility(View.GONE);
				holder.picture4.setVisibility(View.GONE);
				holder.picture5.setVisibility(View.GONE);
				holder.picture6.setVisibility(View.GONE);
				holder.picture7.setVisibility(View.GONE);
				holder.picture8.setVisibility(View.GONE);
				holder.picture9.setVisibility(View.GONE);
				// holder.pictureGridView.setVisibility(View.GONE);
				MyApplication.asyncLoadImage(
						statusList.get(position).bmiddle_pic, holder.picture);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			holder.picture.setVisibility(View.GONE);
			holder.picture1.setVisibility(View.GONE);
			holder.picture2.setVisibility(View.GONE);
			holder.picture3.setVisibility(View.GONE);
			holder.picture4.setVisibility(View.GONE);
			holder.picture5.setVisibility(View.GONE);
			holder.picture6.setVisibility(View.GONE);
			holder.picture7.setVisibility(View.GONE);
			holder.picture8.setVisibility(View.GONE);
			holder.picture9.setVisibility(View.GONE);
		}

		try {
			// 表明该微博为转发
			holder.retweetedText
					.setMText("@"
							+ statusList.get(position).retweeted_status.user.screen_name
							+ ":"
							+ statusList.get(position).retweeted_status.text);
			holder.retweetedLayout.setVisibility(View.VISIBLE);

			holder.retweetedText.setTextColor(context.getResources().getColor(
					R.color.text_black));
			holder.retweetedText.invalidate();

			holder.retweetedRepostsCount.setText(Utils.transformRepostsCount(
					statusList.get(position).retweeted_status.reposts_count,
					statusList.get(position).retweeted_status.comments_count));

			try {

				if (statusList.get(position).retweeted_status.pic_urls.size() > 1) {
					holder.retweetedPicture.setVisibility(View.GONE);
					holder.retweetedPictureGridView.setVisibility(View.VISIBLE);
					holder.retweetedPictureGridView
							.setAdapter(new GridViewAdapter(context, statusList
									.get(position).retweeted_status.pic_urls,
									imageWidth));
				} else if (statusList.get(position).retweeted_status.pic_urls
						.size() == 1) {
					holder.retweetedPicture.setVisibility(View.VISIBLE);
					holder.retweetedPictureGridView.setVisibility(View.GONE);
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
				MyApplication.setStatus(statusList.get(position));
				Intent intent = new Intent(context, WeiboActivity.class);
				context.startActivity(intent);
			}
		});

		return convertView;
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
		// GridView pictureGridView;
		GridView retweetedPictureGridView;
		ImageView picture1;
		ImageView picture2;
		ImageView picture3;
		ImageView picture4;
		ImageView picture5;
		ImageView picture6;
		ImageView picture7;
		ImageView picture8;
		ImageView picture9;
		ImageView[] pictureArray;
	}
}
