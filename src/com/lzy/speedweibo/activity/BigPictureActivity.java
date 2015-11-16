package com.lzy.speedweibo.activity;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
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
	private ImageView defalutImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_picture);

		layout = (LinearLayout) findViewById(R.id.bigPictureLayout);
		defalutImageView = (ImageView) findViewById(R.id.defaultImageView);

		String url = getIntent().getStringExtra("url");
		final int width = MyApplication.getDisplayWidth();
		final int height = MyApplication.getDisplayHeight();

		ImageLoader.getInstance().loadImage(url,
				MyApplication.optionsBigPicture,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						int imageHeight = loadedImage.getHeight();
						int imageWidth = loadedImage.getWidth();

						if (imageHeight > GL10.GL_MAX_TEXTURE_SIZE) {
							defalutImageView.setVisibility(View.GONE);
							List<Bitmap> bitmaps = Utils
									.slideBitmap(loadedImage);
							for (Bitmap b : bitmaps) {
								ImageView imageView = new ImageView(
										BigPictureActivity.this);
								imageView.setScaleType(ScaleType.FIT_XY);
								imageView
										.setLayoutParams(new LinearLayout.LayoutParams(
												width,
												(int) (width * ((float) b
														.getHeight() / (float) b
														.getWidth()))));
								imageView.setImageBitmap(b);
								layout.addView(imageView);
							}
						} else {
							defalutImageView.setImageBitmap(loadedImage);

							final int statusBarHeight = MyApplication
									.getStatusBarHeight(BigPictureActivity.this);

							int realHeight = (int) ((float) imageHeight
									/ (float) imageWidth * width);
							if (realHeight < height - statusBarHeight) {
								// 让imageview居中
								defalutImageView.setPadding(0, (height
										- statusBarHeight - realHeight) / 2, 0,
										0);
							}
						}
					}
				});
	}
}