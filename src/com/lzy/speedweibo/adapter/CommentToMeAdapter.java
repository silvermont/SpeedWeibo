package com.lzy.speedweibo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.core.MyApplication;
import com.lzy.speedweibo.core.SmartTextView;
import com.lzy.speedweibo.core.Utils;
import com.sina.weibo.sdk.openapi.models.Comment;

public class CommentToMeAdapter extends BaseAdapter {
	private List<Comment> commentList;
	private Context context;
	private Holder holder;

	public CommentToMeAdapter(Context context) {
		super();
		this.context = context;
		this.commentList = new ArrayList<Comment>();
	}

	public void setData(List<Comment> list) {
		this.commentList = list;
	}

	@Override
	public int getCount() {
		return commentList.size();
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
			convertView = View.inflate(context, R.layout.item_comment_to_me,
					null);
			holder.wholeLayout = (RelativeLayout) convertView
					.findViewById(R.id.wholeLayout);
			holder.userHead = (ImageView) convertView
					.findViewById(R.id.userHead);
			holder.userName = (TextView) convertView
					.findViewById(R.id.userName);
			holder.text = (SmartTextView) convertView.findViewById(R.id.text);
			holder.source = (TextView) convertView.findViewById(R.id.source);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.retweetedLayout = (RelativeLayout) convertView
					.findViewById(R.id.retweetedLayout);
			holder.retweetedText = (SmartTextView) convertView
					.findViewById(R.id.retweetedText);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.text.setMText(commentList.get(position).text);
		holder.text.setTextColor(context.getResources().getColor(
				R.color.text_black));
		holder.text.invalidate();

		holder.userName.setText(commentList.get(position).user.screen_name);
		holder.source
				.setText(Utils.transformSource(commentList.get(position).source));
		holder.time
				.setText(Utils.transformTime(commentList.get(position).created_at));

		if (null != commentList.get(position).reply_comment) {
			holder.retweetedText.setMText("回复我的评论："
					+ commentList.get(position).reply_comment.text);
		} else {
			holder.retweetedText.setMText("评论我的微博："
					+ commentList.get(position).status.text);
		}
		holder.retweetedText.setTextColor(context.getResources().getColor(
				R.color.text_black));
		holder.retweetedText.invalidate();

		MyApplication.asyncLoadImage(
				commentList.get(position).user.profile_image_url,
				holder.userHead);

		holder.wholeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return convertView;
	}

	class Holder {
		RelativeLayout wholeLayout;
		ImageView userHead;
		TextView userName;
		SmartTextView text;
		TextView source;
		TextView time;
		RelativeLayout retweetedLayout;
		SmartTextView retweetedText;
	}
}
