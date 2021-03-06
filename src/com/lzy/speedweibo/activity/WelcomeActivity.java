package com.lzy.speedweibo.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.lzy.speedweibo.R;
import com.lzy.speedweibo.core.AccessTokenKeeper;
import com.lzy.speedweibo.core.Constants;
import com.lzy.speedweibo.core.MyApplication;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class WelcomeActivity extends BaseActivity {
	// private AuthInfo mAuthInfo;
	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	// private SsoHandler mSsoHandler;
	private Oauth2AccessToken mAccessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		// initActionBar();

		// 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
		AuthInfo mAuthInfo = new AuthInfo(this, Constants.APP_KEY,
				Constants.REDIRECT_URL, Constants.SCOPE);
		SsoHandler mSsoHandler = new SsoHandler(WelcomeActivity.this, mAuthInfo);

		// 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
		// 第一次启动本应用，AccessToken 不可用
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		if (mAccessToken.isSessionValid()) {
			// updateTokenView(true);
			MyApplication
					.setmAccessToken(getApplicationContext(), mAccessToken);
			// 已认证则跳转到主界面
			Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		} else {
			mSsoHandler.authorizeWeb(new AuthListener());
			finish();
		}
	}

	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			// 从这里获取用户输入的 电话号码信息
			// String phoneNum = mAccessToken.getPhoneNum();
			if (mAccessToken.isSessionValid()) {
				// 显示 Token
				// updateTokenView(false);

				// 保存 Token 到 SharedPreferences
				AccessTokenKeeper.writeAccessToken(WelcomeActivity.this,
						mAccessToken);
				MyApplication.setmAccessToken(getApplicationContext(),
						mAccessToken);
				Toast.makeText(WelcomeActivity.this,
						R.string.weibosdk_demo_toast_auth_success,
						Toast.LENGTH_SHORT).show();

				// 已认证则跳转到主界面
				Intent intent = new Intent(WelcomeActivity.this,
						HomeActivity.class);
				startActivity(intent);
				finish();
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = getString(R.string.weibosdk_demo_toast_auth_failed);
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				Toast.makeText(WelcomeActivity.this, message, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(WelcomeActivity.this,
					R.string.weibosdk_demo_toast_auth_canceled,
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(WelcomeActivity.this,
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * 显示当前 Token 信息。
	 * 
	 * @param hasExisted
	 *            配置文件中是否已存在 token 信息并且合法
	 */
	// private void updateTokenView(boolean hasExisted) {
	// String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
	// .format(new java.util.Date(mAccessToken.getExpiresTime()));
	// String format =
	// getString(R.string.weibosdk_demo_token_to_string_format_1);
	// // tv.setText(String.format(format, mAccessToken.getToken(), date));
	//
	// String message = String.format(format, mAccessToken.getToken(), date);
	// if (hasExisted) {
	// message = getString(R.string.weibosdk_demo_token_has_existed)
	// + "\n" + message;
	// }
	// // authInfoTv.setText(message);
	// }
}
