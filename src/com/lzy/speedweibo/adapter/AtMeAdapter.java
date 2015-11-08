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
import com.sina.weibo.sdk.openapi.models.Status;

public class AtMeAdapter extends BaseAdapter {
	private List<Status> statusList;
	private Context context;
	private Holder holder;

	public AtMeAdapter(Context context) {
		super();
		this.context = context;
		this.statusList = new ArrayList<Status>();
	}

	public void setData(List<Status> list) {
		this.statusList = list;
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

		holder.text.setMText(statusList.get(position).text);
		holder.text.setTextColor(context.getResources().getColor(
				R.color.text_black));
		holder.text.invalidate();

		holder.userName.setText(statusList.get(position).user.screen_name);
		holder.source
				.setText(Utils.transformSource(statusList.get(position).source));
		holder.time
				.setText(Utils.transformTime(statusList.get(position).created_at));

		if (null != statusList.get(position).retweeted_status) {
			holder.retweetedLayout.setVisibility(View.VISIBLE);
			
			holder.retweetedText
					.setMText(statusList.get(position).retweeted_status.user.screen_name
							+ "："
							+ statusList.get(position).retweeted_status.text);
			holder.retweetedText.setTextColor(context.getResources().getColor(
					R.color.text_black));
			holder.retweetedText.invalidate();
		} else {
			holder.retweetedLayout.setVisibility(View.GONE);
		}

		MyApplication.asyncLoadImage(
				statusList.get(position).user.profile_image_url,
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