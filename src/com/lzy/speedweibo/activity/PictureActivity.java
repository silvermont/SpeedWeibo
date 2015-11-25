package com.lzy.speedweibo.activity;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.core.MyApplication;
import com.lzy.speedweibo.core.Utils;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class PictureActivity extends Activity {
	private LinearLayout layout;
	private ImageView defalutImageView;
	private ImageView forward;
	private ImageView backward;
	private TextView indexText;
	private String url;
	private SimpleImageLoadingListener listener;
	private int index;
	private int size;
	private ArrayList<String> urls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);

		layout = (LinearLayout) findViewById(R.id.pictureLayout);
		defalutImageView = (ImageView) findViewById(R.id.defaultImageView);
		forward = (ImageView) findViewById(R.id.forward);
		backward = (ImageView) findViewById(R.id.backward);
		indexText = (TextView) findViewById(R.id.indexText);

		final int height = MyApplication.displayHeight;
		listener = new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String uri, View view,
					Bitmap loadedImage) {
				Bitmap bitmap = Utils.zoomImage(loadedImage);

				int imageHeight = bitmap.getHeight();

				if (imageHeight > GL10.GL_MAX_TEXTURE_SIZE) {
					defalutImageView.setVisibility(View.GONE);

					List<Bitmap> bitmaps = Utils.slideBitmap(bitmap);
					for (Bitmap b : bitmaps) {
						ImageView imageView = new ImageView(
								PictureActivity.this);
						imageView
								.setLayoutParams(new LinearLayout.LayoutParams(
										LayoutParams.WRAP_CONTENT,
										LayoutParams.WRAP_CONTENT));
						imageView.setImageBitmap(b);

						layout.addView(imageView);
					}
				} else {
					defalutImageView.setImageBitmap(bitmap);

					final int statusBarHeight = MyApplication.statusBarHeight;

					if (imageHeight < height - statusBarHeight) {
						// 让imageview居中
						defalutImageView.setPadding(0, (height
								- statusBarHeight - imageHeight) / 2, 0, 0);
					}
				}
			}
		};

		urls = (ArrayList<String>) getIntent().getSerializableExtra("urls");
		index = getIntent().getIntExtra("index", -1);
		size = urls.size();

		if (index == -1) {
			indexText.setVisibility(View.GONE);
			forward.setVisibility(View.GONE);
			backward.setVisibility(View.GONE);
			url = Utils.transformThumbnailToBmiddle(urls.get(0));
		} else {
			if (index == 0) {
				backward.setVisibility(View.GONE);
			} else if (index == size - 1) {
				forward.setVisibility(View.GONE);
			}
			indexText.setText((index + 1) + "/" + size);
			url = Utils.transformThumbnailToBmiddle(urls.get(index));
		}

		forward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				index++;
				backward.setVisibility(View.VISIBLE);
				indexText.setText((index + 1) + "/" + size);
				MyApplication.loadImage(
						Utils.transformThumbnailToBmiddle(urls.get(index)),
						listener);

				if (index == size - 1) {
					forward.setVisibility(View.GONE);
				}
			}
		});

		backward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				index--;
				forward.setVisibility(View.VISIBLE);
				indexText.setText((index + 1) + "/" + size);
				MyApplication.loadImage(
						Utils.transformThumbnailToBmiddle(urls.get(index)),
						listener);

				if (index == 0) {
					backward.setVisibility(View.GONE);
				}
			}
		});

		MyApplication.loadImage(url, listener);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.activity_close);
	}
}
