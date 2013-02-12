package ru.telepuzinator.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class Tab {
	private View mTabView;
	
	private int mLayout;
	private int mTabId;
	
	private boolean mIsSelected;

	public Tab(LayoutInflater inflater, ViewGroup tabBar, Bundle state, int tab) {
		mLayout = state.getInt("fragments_tab_" + tab);
		mIsSelected = state.getBoolean("fragments_tab_state_" + tab);
		mTabId = tab;
		attachTab(inflater, tabBar);
	}
	
	public Tab(LayoutInflater inflater, ViewGroup tabBar, int tabContent, int tab) {
		mLayout = tabContent;
		mTabId = tab;
		attachTab(inflater, tabBar);
	}

	public int getLayout() {
		return mLayout;
	}

	public void setLayout(int tabContent) {
		mLayout = tabContent;
	}
	
	public void saveState(int tab, Bundle state) {
		state.putInt("fragments_tab_" + tab, mLayout);
		state.putBoolean("fragments_tab_state_" + tab, mIsSelected);
	}

	public void setId(int tabNumber) {
		mTabId = tabNumber;
	}

	public int getId() {
		return mTabId;
	}
	
	public void select() {
		mTabView.setSelected(true);
		mIsSelected = true;
	}
	
	public void deselect() {
		mTabView.setSelected(false);
		mIsSelected = false;
	}
	
	public boolean isSelected() {
		return mIsSelected;
	}
	
	private void attachTab(LayoutInflater inflater, ViewGroup tabBar) {
		mTabView = inflater.inflate(mLayout, null);
		tabBar.addView(mTabView);
		tabBar.invalidate();
		
		mTabView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((TabActivity) v.getContext()).switchTab(getId());
			}
		});
	}
}
