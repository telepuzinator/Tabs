package ru.telepuzinator.tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class TabLayout extends LinearLayout {
	private float mWidth = 1;
	private float mHeight = 1;

	public TabLayout(Context context) {
		super(context);
	}

	public TabLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public TabLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(changed) {
			mHeight = b - t;
			mWidth = r - l;
			resizeChildViews();
		}
		super.onLayout(changed, l, t, r, b);
	}
	
	public void addView(View child) {
		super.addView(child);
	}

	private void resizeChildViews() {
		int childrenNumber = getChildCount();
		float childWidth = mWidth / childrenNumber;
		
		LayoutParams params;
		for(int i = 0; i < childrenNumber; i++) {
			View view = getChildAt(i);
			params = (LayoutParams) view.getLayoutParams();
			params.width = (int) childWidth;
			params.height = (int) mHeight;
			view.setLayoutParams(params);
		}
	}
}
