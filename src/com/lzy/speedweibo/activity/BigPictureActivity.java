package com.lzy.speedweibo.activity;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.core.MyApplication;
import com.lzy.speedweibo.core.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class BigPictureActivity extends Activity {
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_picture);

		layout = (LinearLayout) findViewById(R.id.bigPictureLayout);

		String url = getIntent().getStringExtra("url");
		Log.e("", "url "+url);
		final int width = MyApplication.getDisplayWidth();

		ImageLoader.getInstance().loadImage(url,
				MyApplication.optionsBigPicture,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						List<Bitmap> bitmaps = Utils.slideBitmap(loadedImage);
						for (Bitmap b : bitmaps) {
							Log.e("", "for");
							ImageView imageView = new ImageView(
									BigPictureActivity.this);
							imageView.setScaleType(ScaleType.FIT_XY);
							imageView
									.setLayoutParams(new LinearLayout.LayoutParams(
											width, width
													* (b.getHeight() / b
															.getWidth())));
							imageView.setImageBitmap(b);
							layout.addView(imageView);
						}

					}
				});
	}
}
