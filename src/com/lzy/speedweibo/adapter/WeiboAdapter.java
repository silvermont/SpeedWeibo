package com.lzy.speedweibo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.activity.EditActivity;
import com.lzy.speedweibo.activity.WeiboActivity;
import com.lzy.speedweibo.core.MyApplication;
import com.lzy.speedweibo.core.SmartTextView;
import com.lzy.speedweibo.core.Utils;
import com.sina.weibo.sdk.openapi.models.Status;

public class WeiboAdapter extends BaseAdapter {
	private List<Status> list;
	private int imageWidth;
	private Context context;
	private Holder holder;

	public WeiboAdapter(Context context) {
		super();
		this.context = context;
		this.list = new ArrayList<Status>();
		this.imageWidth = MyApplication.getImageWidth();
	}

	public void setData(List<Status> statusList) {
		this.list = statusList;
	}

	@Override
	public int getCount() {
		return list.size();
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
			convertView = View.inflate(context, R.layout.item_weibo, null);
			holder.wholeLayout = (RelativeLayout) convertView
					.findViewById(R.id.wholeLayout);
			holder.headLayout = (RelativeLayout) convertView
					.findViewById(R.id.headLayout);
			holder.pictureLayout = (RelativeLayout) convertView
					.findViewById(R.id.pictureLayout);
			holder.retweetedPictureLayout = (RelativeLayout) convertView
					.findViewById(R.id.retweetedPictureLayout);
			holder.head = (ImageView) holder.headLayout.findViewById(R.id.head);
			holder.name = (TextView) holder.headLayout.findViewById(R.id.name);
			holder.text = (SmartTextView) holder.headLayout
					.findViewById(R.id.text);
			holder.source = (TextView) holder.headLayout
					.findViewById(R.id.source);
			holder.time = (TextView) holder.headLayout.findViewById(R.id.time);
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
			holder.picture = (ImageView) convertView.findViewById(R.id.picture);
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
			holder.retweetedPicture1 = (ImageView) convertView
					.findViewById(R.id.retweetedPicture1);
			holder.retweetedPicture2 = (ImageView) convertView
					.findViewById(R.id.retweetedPicture2);
			holder.retweetedPicture3 = (ImageView) convertView
					.findViewById(R.id.retweetedPicture3);
			holder.retweetedPicture4 = (ImageView) convertView
					.findViewById(R.id.retweetedPicture4);
			holder.retweetedPicture5 = (ImageView) convertView
					.findViewById(R.id.retweetedPicture5);
			holder.retweetedPicture6 = (ImageView) convertView
					.findViewById(R.id.retweetedPicture6);
			holder.retweetedPicture7 = (ImageView) convertView
					.findViewById(R.id.retweetedPicture7);
			holder.retweetedPicture8 = (ImageView) convertView
					.findViewById(R.id.retweetedPicture8);
			holder.retweetedPicture9 = (ImageView) convertView
					.findViewById(R.id.retweetedPicture9);
			holder.retweetedPictureArray = new ImageView[] {
					holder.retweetedPicture1, holder.retweetedPicture2,
					holder.retweetedPicture3, holder.retweetedPicture4,
					holder.retweetedPicture5, holder.retweetedPicture6,
					holder.retweetedPicture7, holder.retweetedPicture8,
					holder.retweetedPicture9 };

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.text.setMText(list.get(position).text);
		holder.text.setTextColor(context.getResources().getColor(
				R.color.text_black));
		holder.text.invalidate();

		holder.name.setText(list.get(position).user.screen_name);
		holder.source
				.setText(Utils.transformSource(list.get(position).source));
		holder.time
				.setText(Utils.transformTime(list.get(position).created_at));
		holder.repostsCount.setText(Utils.transformRepostsCount(
				list.get(position).reposts_count,
				list.get(position).comments_count));

		MyApplication.asyncLoadImage(
				list.get(position).user.profile_image_url, holder.head);

		if (null == list.get(position).pic_urls) {
			holder.pictureLayout.setVisibility(View.GONE);
		} else if (list.get(position).pic_urls.size() == 1) {
			holder.pictureLayout.setVisibility(View.VISIBLE);
			holder.picture.setVisibility(View.VISIBLE);
			for (int i = 0; i < 9; i++) {
				holder.pictureArray[i].setVisibility(View.GONE);
			}
			MyApplication.asyncLoadImage(list.get(position).bmiddle_pic,
					holder.picture);
		} else {
			holder.pictureLayout.setVisibility(View.VISIBLE);
			holder.picture.setVisibility(View.GONE);
			int imageCount = list.get(position).pic_urls.size();
			for (int i = 0; i < 9; i++) {
				if (i < imageCount) {
					holder.pictureArray[i].setVisibility(View.VISIBLE);
					LayoutParams params = (LayoutParams) holder.pictureArray[i]
							.getLayoutParams();
					params.width = imageWidth;
					params.height = imageWidth;
					holder.pictureArray[i].setLayoutParams(params);
					holder.pictureArray[i]
							.setScaleType(ImageView.ScaleType.CENTER_CROP);
					MyApplication.asyncLoadImage(
							list.get(position).pic_urls.get(i),
							holder.pictureArray[i]);
				} else {
					holder.pictureArray[i].setVisibility(View.GONE);
				}
			}
		}

		holder.wholeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyApplication.setStatus(list.get(position));
				Intent intent = new Intent(context, WeiboActivity.class);
				context.startActivity(intent);
			}
		});

		holder.wholeLayout.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setItems(new String[] { "转发", "评论" },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(context,
										EditActivity.class);
								intent.putExtra("id",
										list.get(position).id);
								switch (which) {
								case 0:
									if (null != list.get(position).retweeted_status) {
										intent.putExtra(
												"text",
												"//@"
														+ list
																.get(position).user.screen_name
														+ "："
														+ list
																.get(position).text);
									}
									intent.putExtra("action", "转发");
									context.startActivity(intent);
									break;
								case 1:
									intent.putExtra("action", "评论");
									context.startActivity(intent);
									break;
								default:
									break;
								}
							}

						});
				builder.show();
				return true;
			}
		});

		if (null == list.get(position).retweeted_status) {
			holder.retweetedLayout.setVisibility(View.GONE);
		} else {
			holder.retweetedLayout.setVisibility(View.VISIBLE);
			if (null == list.get(position).retweeted_status.user) {
				// 针对转发的原微博已被删除的情况
				holder.retweetedText.setMText("该微博已被删除");
				holder.retweetedText.setTextColor(context.getResources()
						.getColor(R.color.text_black_light));
				holder.retweetedText.invalidate();

				holder.retweetedRepostsCount.setVisibility(View.GONE);
				holder.retweetedPictureLayout.setVisibility(View.GONE);
			} else {
				holder.retweetedLayout
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								MyApplication.setStatus(list
										.get(position).retweeted_status);
								Intent intent = new Intent(context,
										WeiboActivity.class);
								context.startActivity(intent);
							}
						});

				holder.retweetedLayout
						.setOnLongClickListener(new OnLongClickListener() {

							@Override
							public boolean onLongClick(View v) {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										context);
								builder.setItems(new String[] { "转发原微博",
										"评论原微博" },
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												Intent intent = new Intent(
														context,
														EditActivity.class);
												intent.putExtra(
														"id",
														list
																.get(position).retweeted_status.id);

												switch (which) {
												case 0:
													intent.putExtra("action",
															"转发");
													context.startActivity(intent);
													break;
												case 1:
													intent.putExtra("action",
															"评论");
													context.startActivity(intent);
													break;
												default:
													break;
												}
											}

										});
								builder.show();
								return true;
							}
						});

				holder.retweetedText
						.setMText("@"
								+ list.get(position).retweeted_status.user.screen_name
								+ ":"
								+ list.get(position).retweeted_status.text);
				holder.retweetedText.setTextColor(context.getResources()
						.getColor(R.color.text_black_light));
				holder.retweetedText.invalidate();

				holder.retweetedRepostsCount
						.setText(Utils.transformRepostsCount(
								list.get(position).retweeted_status.reposts_count,
								list.get(position).retweeted_status.comments_count));

				if (null == list.get(position).retweeted_status.pic_urls) {
					holder.retweetedPictureLayout.setVisibility(View.GONE);
				} else if (list.get(position).retweeted_status.pic_urls
						.size() == 1) {
					holder.retweetedPictureLayout.setVisibility(View.VISIBLE);
					holder.retweetedPicture.setVisibility(View.VISIBLE);
					for (int i = 0; i < 9; i++) {
						holder.retweetedPictureArray[i]
								.setVisibility(View.GONE);
					}
					MyApplication
							.asyncLoadImage(
									list.get(position).retweeted_status.bmiddle_pic,
									holder.retweetedPicture);
				} else {
					holder.retweetedPictureLayout.setVisibility(View.VISIBLE);
					holder.retweetedPicture.setVisibility(View.GONE);
					int imageCount = list.get(position).retweeted_status.pic_urls
							.size();
					for (int i = 0; i < 9; i++) {
						if (i < imageCount) {
							holder.retweetedPictureArray[i]
									.setVisibility(View.VISIBLE);
							LayoutParams params = (LayoutParams) holder.retweetedPictureArray[i]
									.getLayoutParams();
							params.width = imageWidth;
							params.height = imageWidth;
							holder.retweetedPictureArray[i]
									.setLayoutParams(params);
							holder.retweetedPictureArray[i]
									.setScaleType(ImageView.ScaleType.CENTER_CROP);
							MyApplication
									.asyncLoadImage(
											list.get(position).retweeted_status.pic_urls
													.get(i),
											holder.retweetedPictureArray[i]);
						} else {
							holder.retweetedPictureArray[i]
									.setVisibility(View.GONE);
						}
					}
				}
			}
		}

		return convertView;
	}

	class Holder {
		RelativeLayout wholeLayout;
		RelativeLayout headLayout;
		RelativeLayout pictureLayout;
		RelativeLayout retweetedPictureLayout;
		ImageView head;
		ImageView picture;
		TextView name;
		SmartTextView text;
		TextView source;
		TextView time;
		SmartTextView retweetedText;
		TextView repostsCount;
		RelativeLayout retweetedLayout;
		ImageView retweetedPicture;
		TextView retweetedRepostsCount;
		ImageView picture1;
		ImageView picture2;
		ImageView picture3;
		ImageView picture4;
		ImageView picture5;
		ImageView picture6;
		ImageView picture7;
		ImageView picture8;
		ImageView picture9;
		ImageView retweetedPicture1;
		ImageView retweetedPicture2;
		ImageView retweetedPicture3;
		ImageView retweetedPicture4;
		ImageView retweetedPicture5;
		ImageView retweetedPicture6;
		ImageView retweetedPicture7;
		ImageView retweetedPicture8;
		ImageView retweetedPicture9;
		ImageView[] pictureArray;
		ImageView[] retweetedPictureArray;
	}
}
