package com.lzy.speedweibo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.activity.EditActivity;
import com.lzy.speedweibo.core.MyApplication;
import com.lzy.speedweibo.core.SmartTextView;
import com.lzy.speedweibo.core.Utils;
import com.sina.weibo.sdk.openapi.models.Comment;

public class CommentAdapter extends BaseAdapter {
	private List<Comment> list;
	private Context context;
	private Holder holder;

	public CommentAdapter(Context context) {
		super();
		this.context = context;
		this.list = new ArrayList<Comment>();
	}

	public void setData(List<Comment> list) {
		this.list = list;
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
			convertView = View.inflate(context, R.layout.item_comment, null);
			holder.wholeLayout = (RelativeLayout) convertView
					.findViewById(R.id.wholeLayout);
			holder.head = (ImageView) convertView.findViewById(R.id.head);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.text = (SmartTextView) convertView.findViewById(R.id.text);
			holder.source = (TextView) convertView.findViewById(R.id.source);
			holder.time = (TextView) convertView.findViewById(R.id.time);

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

		MyApplication.displayImage(
				list.get(position).user.profile_image_url, holder.head);

		holder.wholeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setItems(new String[] { "回复", "转发" },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(context,
										EditActivity.class);
								intent.putExtra("id",
										list.get(position).id);
								intent.putExtra("statusID",
										list.get(position).status.id);
								switch (which) {
								case 0:
									intent.putExtra("action", "回复评论");
									context.startActivity(intent);
									break;
								case 1:
									intent.putExtra("action", "转发评论");
									intent.putExtra(
											"text",
											"//@"
													+ list.get(position).user.screen_name
													+ "："
													+ list.get(position).text);
									context.startActivity(intent);
									break;
								default:
									break;
								}
							}

						});
				builder.show();
			}
		});

		return convertView;
	}

	class Holder {
		RelativeLayout wholeLayout;
		ImageView head;
		TextView name;
		SmartTextView text;
		TextView source;
		TextView time;
	}
}
