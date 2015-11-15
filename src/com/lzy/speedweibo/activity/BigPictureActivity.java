package com.lzy.speedweibo.activity;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.core.MyApplication;
import com.lzy.speedweibo.core.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class BigPictureActivity extends Activity {
	private Bitmap bitmap;
	private int width;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_picture);

		layout = (LinearLayout) findViewById(R.id.bigPictureLayout);

		String url = getIntent().getStringExtra("url");

		width = MyApplication.getDisplayWidth();

		ImageLoader.getInstance().loadImage(url,
				MyApplication.optionsBigPicture,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						bitmap = loadedImage;

						List<Bitmap> bitmaps = Utils.slideBitmap(bitmap);
						for (Bitmap b : bitmaps) {
							ImageView imageView = new ImageView(
									BigPictureActivity.this);
							imageView.setImageBitmap(b);
							imageView
									.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT, width
													* (b.getHeight() / b
															.getWidth())));
							layout.addView(imageView);
						}
					}
				});
	}
}
