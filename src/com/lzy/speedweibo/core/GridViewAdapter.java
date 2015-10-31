package com.lzy.speedweibo.core;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {
	private List<String> urls;
	private int imageWidth;
	private Context context;

	public GridViewAdapter(Context context, List<String> urls, int imageWidth) {
		super();
		this.context = context;
		this.urls = urls;
		this.imageWidth = imageWidth;
	}

	@Override
	public int getCount() {
		return urls.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(context);
			imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
					imageWidth));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		} else {
			imageView = (ImageView) convertView;
		}
		MyApplication.asyncLoadImage(urls.get(position), imageView);
		return imageView;
	}

}
