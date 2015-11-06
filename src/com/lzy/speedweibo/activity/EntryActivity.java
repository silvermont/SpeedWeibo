package com.lzy.speedweibo.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.core.SystemBarTintManager;
import com.lzy.speedweibo.fragment.HomeFragment;
import com.lzy.speedweibo.fragment.MeFragment;
import com.lzy.speedweibo.fragment.MessageFragment;

public class EntryActivity extends FragmentActivity {

	// private ViewPager viewPager;
	// private HomeFragment homeFragment;
	// private MessageFragment messageFragment;
	// private MeFragment meFragment;
	// private ArrayList<Fragment> fragmentList;
	private long pressTime = 0;
	// private LinearLayout topBar;
	private TextView homeTv, messageTv, meTv;
	private View homeLine, messageLine, meLine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.blue);
		}
		setContentView(R.layout.activity_entry);

		initViewPager();
		initActionBar();
	}

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - pressTime < 2000) {
			super.onBackPressed();
		} else {
			pressTime = System.currentTimeMillis();
			Toast.makeText(EntryActivity.this, "再按一次退出", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void initActionBar() {
		ActionBar actionBar = this.getActionBar();
		actionBar.setCustomView(R.layout.action_bar_entry);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		RelativeLayout operate = (RelativeLayout) actionBar.getCustomView()
				.findViewById(R.id.operate);
		operate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						EntryActivity.this);
				builder.setItems(new String[] { "设置" },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									Intent intent = new Intent(
											EntryActivity.this,
											SettingActivity.class);
									startActivity(intent);
									break;

								default:
									break;
								}
							}
						});
				builder.show();
			}
		});

		RelativeLayout create = (RelativeLayout) actionBar.getCustomView()
				.findViewById(R.id.create);
		create.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EntryActivity.this,
						EditActivity.class);
				intent.putExtra("action", "发表新微博");
				startActivity(intent);
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void initViewPager() {
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
		LinearLayout topBar = (LinearLayout) findViewById(R.id.topBar);
		homeTv = (TextView) topBar.findViewById(R.id.homeTv);
		messageTv = (TextView) topBar.findViewById(R.id.messageTv);
		meTv = (TextView) topBar.findViewById(R.id.meTv);
		homeLine = topBar.findViewById(R.id.homeLine);
		messageLine = topBar.findViewById(R.id.messageLine);
		meLine = topBar.findViewById(R.id.meLine);

		ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();

		HomeFragment homeFragment = new HomeFragment();
		MessageFragment messageFragment = new MessageFragment();
		MeFragment meFragment = new MeFragment();

		fragmentList.add(homeFragment);
		fragmentList.add(messageFragment);
		fragmentList.add(meFragment);

		viewPager.setOffscreenPageLimit(2);
		viewPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentList));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				initTopBar();
				switch (arg0) {
				case 0:
					homeTv.setTextColor(getResources().getColor(R.color.blue));
					homeLine.setVisibility(View.VISIBLE);
					break;
				case 1:
					messageTv.setTextColor(getResources()
							.getColor(R.color.blue));
					messageLine.setVisibility(View.VISIBLE);
					break;
				case 2:
					meTv.setTextColor(getResources().getColor(R.color.blue));
					meLine.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void initTopBar() {
		homeTv.setTextColor(getResources().getColor(R.color.text_gray));
		messageTv.setTextColor(getResources().getColor(R.color.text_gray));
		meTv.setTextColor(getResources().getColor(R.color.text_gray));
		homeLine.setVisibility(View.GONE);
		messageLine.setVisibility(View.GONE);
		meLine.setVisibility(View.GONE);
	}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> list;

		public MyFragmentPagerAdapter(FragmentManager fm,
				ArrayList<Fragment> list) {
			super(fm);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}
	}

}
