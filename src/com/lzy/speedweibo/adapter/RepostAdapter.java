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
import com.sina.weibo.sdk.openapi.models.Repost;

public class RepostAdapter extends BaseAdapter {
	private List<Repost> repostList;
	private Context context;
	private Holder holder;

	public RepostAdapter(Context context) {
		super();
		this.context = context;
		this.repostList = new ArrayList<Repost>();
	}

	public void setData(List<Repost> list) {
		this.repostList = list;
	}

	@Override
	public int getCount() {
		return repostList.size();
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

		holder.text.setMText(repostList.get(position).text);
		holder.text.setTextColor(context.getResources().getColor(
				R.color.text_black));
		holder.text.invalidate();

		holder.name.setText(repostList.get(position).user.screen_name);
		holder.source
				.setText(Utils.transformSource(repostList.get(position).source));
		holder.time
				.setText(Utils.transformTime(repostList.get(position).created_at));

		MyApplication.asyncLoadImage(
				repostList.get(position).user.profile_image_url, holder.head);

		holder.wholeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

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
