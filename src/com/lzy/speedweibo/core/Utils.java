package com.lzy.speedweibo.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class Utils {

	/**
	 * 转化微博来源
	 * 
	 * @param source
	 * @return
	 */
	public static String transformSource(String source) {
		String[] sourceArray = source.split(">");
		return sourceArray[1].substring(0, sourceArray[1].length() - 3);
	}

	/**
	 * 转化微博发表时间
	 * 
	 * @param originalTime
	 * @return
	 */
	public static String transformTime(String originalTime) {
		// sample：Tue May 31 17:46:55 +0800 2011
		// E：周 MMM：字符串形式的月，如果只有两个M，表示数值形式的月 Z表示时区（＋0800）
		String dispalyTime;
		SimpleDateFormat defaultDateFormat = new SimpleDateFormat(
				"E MMM dd HH:mm:ss Z yyyy", Locale.US);
		Date originalDate = null;
		try {
			originalDate = defaultDateFormat.parse(originalTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy MM dd HH mm");
		String transformedTime = myDateFormat.format(originalDate);
		String[] transformedTimeArray = transformedTime.split(" ");
		int transformedYear = Integer.valueOf(transformedTimeArray[0]);
		int transformedMonth = Integer.valueOf(transformedTimeArray[1]);
		int transformedDay = Integer.valueOf(transformedTimeArray[2]);
		int transformedHour = Integer.valueOf(transformedTimeArray[3]);
		int transformedMin = Integer.valueOf(transformedTimeArray[4]);

		Date curDate = new Date(System.currentTimeMillis());
		String curTime = myDateFormat.format(curDate);
		String[] curTimeArray = curTime.split(" ");
		int curYear = Integer.valueOf(curTimeArray[0]);
		int curMonth = Integer.valueOf(curTimeArray[1]);
		int curDay = Integer.valueOf(curTimeArray[2]);

		if (curYear > transformedYear) {
			dispalyTime = transformedYear + "年" + transformedMonth + "月"
					+ transformedDay + "日";
		} else if (curYear < transformedYear) {
			dispalyTime = "来自未来";
		} else {
			if (transformedMonth == curMonth && transformedDay == curDay) {
				if (transformedMin < 10) {
					dispalyTime = "今天 " + transformedHour + ":0"
							+ transformedMin;
				} else {
					dispalyTime = "今天 " + transformedHour + ":"
							+ transformedMin;
				}
			} else if (transformedMonth == curMonth
					&& transformedDay == curDay - 1) {
				if (transformedMin < 10) {
					dispalyTime = "昨天 " + transformedHour + ":0"
							+ transformedMin;
				} else {
					dispalyTime = "昨天 " + transformedHour + ":"
							+ transformedMin;
				}
			} else if (transformedMonth == curMonth
					&& transformedDay == curDay - 2) {
				if (transformedMin < 10) {
					dispalyTime = "前天 " + transformedHour + ":"
							+ transformedMin;
				} else {
					dispalyTime = "前天 " + transformedHour + ":0"
							+ transformedMin;
				}
			} else {
				if (transformedMin < 10) {
					dispalyTime = transformedMonth + "-" + transformedDay + " "
							+ transformedHour + ":0" + transformedMin;
				} else {
					dispalyTime = transformedMonth + "-" + transformedDay + " "
							+ transformedHour + ":" + transformedMin;
				}
			}
		}
		return dispalyTime;
	}

	/**
	 * 转化转发评论数
	 * 
	 * @param repostsCount
	 * @param commentsCount
	 * @return
	 */
	public static String transformRepostsCount(int repostsCount,
			int commentsCount) {
		String displayCount = "";
		if (repostsCount == 0 && commentsCount != 0) {
			displayCount = commentsCount + "条评论";
		} else if (repostsCount != 0 && commentsCount == 0) {
			displayCount = repostsCount + "条转发";
		} else if (repostsCount != 0 && commentsCount != 0) {
			displayCount = repostsCount + "条转发 & " + commentsCount + "条评论";
		}
		return displayCount;
	}

}
