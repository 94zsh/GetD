package com.future.getd.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class ScreenUtil
	{
		private ScreenUtil()
			{
				/* cannot be instantiated */
				throw new UnsupportedOperationException("cannot be instantiated");
			}

		/**
		 * 获得屏幕高度
		 * 
		 * @param context
		 * @return
		 */
		public static int getScreenWidth(Context context)
			{
				WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				DisplayMetrics outMetrics = new DisplayMetrics();
				wm.getDefaultDisplay().getMetrics(outMetrics);
				return outMetrics.widthPixels;
			}

		/**
		 * 获得屏幕宽度
		 * 
		 * @param context
		 * @return
		 */
		public static int getScreenHeight(Context context)
			{
				WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				DisplayMetrics outMetrics = new DisplayMetrics();
				wm.getDefaultDisplay().getMetrics(outMetrics);
				return outMetrics.heightPixels;
			}
//		/**
//		 * 获得状�?�栏的高�?
//		 * 
//		 * @param context
//		 * @return
//		 */
//		public static int getStatusHeight1(Activity activity)
//			{
//				int statusHeight = 0;
//				Rect localRect = new Rect();
//				activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
//				statusHeight = localRect.top;
//				if (0 == statusHeight)
//					{
//						Class<?> localClass;
//						try
//							{
//								localClass = Class.forName("com.android.internal.R$dimen");
//								Object localObject = localClass.newInstance();
//								int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
//								statusHeight = activity.getResources().getDimensionPixelSize(i5);
//							}
//						catch (Exception e)
//							{
//								e.printStackTrace();
//							}
//					}
//				return statusHeight;
//			}

		/**
		 * 获取状态栏高度
		 * 
		 * @param context
		 * @return
		 */
		public static int getStatusHeight(Context context)
			{

				int statusHeight = 0;
				try
					{
						int result = 0;
						int resourceId = context.getResources()
								.getIdentifier("status_bar_height", "dimen", "android");
						if (resourceId > 0) {
							result = context.getResources().getDimensionPixelSize(resourceId);
						}
						return result;
					}
				catch (Exception e)
					{
						e.printStackTrace();
					}
				return statusHeight;
			}

		/**
		 * 获取当前屏幕截图，包含状态栏
		 * 
		 * @param activity
		 * @return
		 */
		public static Bitmap snapShotWithStatusBar(Activity activity)
			{
				View view = activity.getWindow().getDecorView();
				view.setDrawingCacheEnabled(true);
				view.buildDrawingCache();
				Bitmap bmp = view.getDrawingCache();
				int width = getScreenWidth(activity);
				int height = getScreenHeight(activity);
				Bitmap bp = null;
				bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
				view.destroyDrawingCache();
				return bp;

			}

		/**
		 * 获取当前屏幕截图，不包含状态栏
		 * 
		 * @param activity
		 * @return
		 */
		public static Bitmap snapShotWithoutStatusBar(Activity activity)
			{
				View view = activity.getWindow().getDecorView();
				view.setDrawingCacheEnabled(true);
				view.buildDrawingCache();
				Bitmap bmp = view.getDrawingCache();
				Rect frame = new Rect();
				activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
				int statusBarHeight = frame.top;
				int width = getScreenWidth(activity);
				int height = getScreenHeight(activity);
				Bitmap bp = null;
				bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
				view.destroyDrawingCache();
				return bp;

			}

	}
