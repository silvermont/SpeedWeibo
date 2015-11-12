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
import com.sina.weibo.sdk.openapi.models.User;

public class UserAdapter extends BaseAdapter {
	private List<User> list;
	private Context context;
	private Holder holder;

	public UserAdapter(Context context) {
		super();
		this.context = context;
		this.list = new ArrayList<User>();
	}

	public void setData(List<User> list) {
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
			convertView = View.inflate(context, R.layout.item_user, null);
			holder.user = (RelativeLayout) convertView.findViewById(R.id.user);
			holder.head = (ImageView) convertView.findViewById(R.id.head);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.description = (TextView) convertView
					.findViewById(R.id.description);
			holder.operate = (ImageView) convertView.findViewById(R.id.operate);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		MyApplication.asyncLoadImage(list.get(position).profile_image_url,
				holder.head);

		holder.name.setText(list.get(position).screen_name);
		if (!list.get(position).description.equals("")) {
			holder.description.setText("简介：" + list.get(position).description);
		} else {
			holder.description.setText("简介：暂无介绍");
		}

		holder.user.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return convertView;
	}

	class Holder {
		RelativeLayout user;
		ImageView head;
		TextView name;
		TextView description;
		ImageView operate;
	}
}
