package ru.telepuzinator.tabs;

import java.util.ArrayList;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

public abstract class TabActivity extends FragmentActivity implements ViewTreeObserver.OnGlobalLayoutListener{

	public boolean mTabsOnTop = false;
	private LinearLayout mTabLayout;
	private State mState;
	
	private ArrayList<OnTabChangeListener> mTabListener;
	
	private Fragment mCurrent;
	
	private View activityRootView;
	
	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);
		if(mTabsOnTop) {
			setContentView(R.layout.tabs_activity_top);
		} else {
			setContentView(R.layout.tabs_activity);
		}
		mTabListener = new ArrayList<OnTabChangeListener>();
		mTabLayout = (LinearLayout) findViewById(R.id.tab_activity_tabs);
		
		activityRootView = findViewById(R.id.tabs_root_view);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
		
		if(state == null) {
			mState = new State();
		} else {
			mState = new State(state, getLayoutInflater(), mTabLayout);
			displayFragment(mState.getCurrentTab(), mState.getCurrent());
		}
		showTabs();
	}
	
	@Override
	public void onGlobalLayout() {
		Rect r = new Rect();
		activityRootView.getWindowVisibleDisplayFrame(r);
	
		int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
		if (heightDiff > 100) {
			hideTabs();
		} else if(heightDiff < 100) {
			showTabs();
		}
	}
	
	public Fragment getCurrentFragment() {
		return mCurrent;
	}
	
//	final View activityRootView = findViewById(R.id.loginRootView);
//
//	activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//	@Override
//	public void onGlobalLayout() {
//	Rect r = new Rect();
//	activityRootView.getWindowVisibleDisplayFrame(r);
//
//	int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
//	if (heightDiff > 100) {
//	hideMenu();
//	mRegText.setVisibility(View.GONE);
//	mReg.setVisibility(View.GONE);
//
//	} else if(heightDiff < 100) {
//	showMenu();
//	mRegText.setVisibility(View.VISIBLE);
//	mReg.setVisibility(View.VISIBLE);
//	}
//	}
//	});
	
	public void addHeader(int drawable) {
		ImageView header = (ImageView) findViewById(R.id.tab_activity_header);
		header.setImageResource(drawable);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mState.saveState(outState, getSupportFragmentManager());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onBackPressed() {
		replaceBack();
	}
	
	protected void showTabs() {
		 if(mTabLayout != null) {
			 mTabLayout.setVisibility(View.VISIBLE);
		 }
	}
	
	protected void hideTabs() {
		 if(mTabLayout != null) {
			 mTabLayout.setVisibility(View.GONE);
		 }
	}
	
	public Fragment getCurrentFragment() {
		return mCurrent;
	}
	
	public void setOnTabChangeListener(OnTabChangeListener tabListener) {
		if(!mTabListener.contains(tabListener)) {
			mTabListener.add(tabListener);
		}
	}
	
	public void switchTab(int tab) {
		if(mState.getCurrentTab() == tab) return;
		displayFragment(tab, mState.getLast(tab));
		mState.setCurrentTab(tab);
		
		for(OnTabChangeListener listener: mTabListener) {
			listener.onTabChange(tab);
		}
	}
	
	public void addTab(Fragment frag, int tabLayout) {
		int tab = mState.createTab(tabLayout, getLayoutInflater(), mTabLayout);
		addFragment(frag, tab, mState.getCurrent() < 0);
	}
	
	public void addFragment(Fragment frag) {
		addFragment(frag, mState.getCurrentTab(), true);
	}
	
	public void addFragment(Fragment frag, int tab, boolean display) {
		int i = mState.addFragment(frag, tab);
		if(display) {
			displayFragment(tab, i);
		}
	}
	
	public void replaceBack() {
		int num = mState.getPrevious();
		if(num >= 0) {
			displayFragment(mState.getCurrentTab(), num);
		} else {
			finish();
		}
	}
	
	private Fragment getFragment(int number) {
		return mState.getFragment(number);
	}
	
	private void displayFragment(int tab, int number) {
		FragmentManager fm = getSupportFragmentManager();
		mState.onFragmentChange(fm, tab);
		FragmentTransaction ft = fm.beginTransaction();
		if(mCurrent != null) ft.remove(mCurrent);
		mCurrent = getFragment(number);
		ft.replace(R.id.tab_activity_tab_content, mCurrent);
		ft.commit();
	}
}