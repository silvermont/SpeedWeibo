package com.lzy.speedweibo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.speedweibo.R;

public class MessageFragment extends Fragment {
	private TextView atMe;
	private TextView commentToMe;
	private boolean isShowAtMe = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, null);
		atMe = (TextView) view.findViewById(R.id.atMe);
		commentToMe = (TextView) view.findViewById(R.id.commentToMe);

		atMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isShowAtMe) {
					isShowAtMe = true;
					atMe.setTextColor(Color.WHITE);
					commentToMe.setTextColor(getResources().getColor(
							R.color.text_black));
					atMe.setBackground(getResources().getDrawable(
							R.drawable.button_at_me_press));
					commentToMe.setBackground(getResources().getDrawable(
							R.drawable.button_comment_to_me_normal));
				}
			}
		});

		commentToMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShowAtMe) {
					isShowAtMe = false;
					atMe.setTextColor(getResources().getColor(
							R.color.text_black));
					commentToMe.setTextColor(Color.WHITE);
					atMe.setBackground(getResources().getDrawable(
							R.drawable.button_at_me_normal));
					commentToMe.setBackground(getResources().getDrawable(
							R.drawable.button_comment_to_me_press));
				}
			}
		});

		return view;
	}
}
