package ru.telepuzinator.tabs;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class State implements Serializable {
	private static final long serialVersionUID = -1567421758021469517L;

	//Tab list -> fragment list
	private ArrayList<ArrayList<FragmentsState>> mFragmentsStateList;
	//Tab list
	private ArrayList<Tab> mTabsList;

	private int mCurrent = -1;
	private int mCurrentTab = -1;
	
	public State() {
		mTabsList = new ArrayList<Tab>();
		mFragmentsStateList = new ArrayList<ArrayList<FragmentsState>>();
	}
	
	public State(Bundle state, LayoutInflater layoutInflater, ViewGroup mTabLayout) {
		mTabsList = new ArrayList<Tab>();
		mFragmentsStateList = new ArrayList<ArrayList<FragmentsState>>();
		
		mCurrent = state.getInt("tab_fragment_current");
		mCurrentTab = state.getInt("tab_fragment_current_tab");
		
		int tabsSize = state.getInt("tab_fragment_total_tab_count");
		int fragmentsSize;
		for(int i = 0; i < tabsSize; i++) {
			ArrayList<FragmentsState> tabFragments = new ArrayList<FragmentsState>();
			mFragmentsStateList.add(tabFragments);
			fragmentsSize = state.getInt("tab_" + i + "_fragment_total_count");
			mTabsList.add(new Tab(layoutInflater, mTabLayout, state, i));
			for(int j = 0; j < fragmentsSize; j++) {
				tabFragments.add(new FragmentsState(state, "tab_" + i + "_fragment_" + j));
			}
		}
	}
	
	public void saveState(Bundle state, FragmentManager fm) {
		state.putInt("tab_fragment_current_tab", mCurrentTab);
		state.putInt("tab_fragment_current", mCurrent);
		if(mCurrent >= 0) {
			mFragmentsStateList.get(mCurrentTab).get(mCurrent).rememberFragmentState(fm); 
		}
		state.putInt("tab_fragment_total_tab_count", mTabsList.size());
		for(int i = 0; i < mTabsList.size(); i++) {
			ArrayList<FragmentsState> tabFragments = mFragmentsStateList.get(i);
			state.putInt("tab_" + i + "_fragment_total_count", tabFragments.size());
			mTabsList.get(i).saveState(i, state);
			for(int j = 0; j < tabFragments.size(); j++) {
				tabFragments.get(j).saveFragmentState(state, "tab_" + i + "_fragment_" + j);
			}
		}
	}
	
	public int addFragment(Fragment fragment, int tab) {
		mFragmentsStateList.get(tab).add(new FragmentsState(fragment));
		return mFragmentsStateList.get(tab).size() - 1;
	}
	
	public Fragment getCurrentFragment() {
		return mFragmentsStateList.get(mCurrentTab).get(mCurrent).getFragment();
	}
	
	public Fragment getFragment(int number) {
		return mFragmentsStateList.get(mCurrentTab).get(number).getFragment();
	}
	
	//numbers
	public int getCurrent() {
		return mCurrent;
	}
	
	public int getCurrentTab() {
		return mCurrentTab;
	}
	
	public int getPrevious() {
		mFragmentsStateList.get(mCurrentTab).remove(mCurrent);
		return --mCurrent;
	}
	
	public int getLast(int tab) {
		return mFragmentsStateList.get(tab).size() - 1;
	}
	
	public void setCurrentTab(int tab) {
		if(mCurrentTab >= 0) mTabsList.get(mCurrentTab).deselect();
		mTabsList.get(tab).select();
		mCurrentTab = tab;
		mCurrent = mFragmentsStateList.get(mCurrentTab).size() - 1;
	}
	
	public void onFragmentChange(FragmentManager fm, int tab) {
		if(mCurrentTab >= 0 && mCurrent >= 0) {
			try {
				mFragmentsStateList.get(mCurrentTab).get(mCurrent).rememberFragmentState(fm);
			} catch(IllegalStateException stateException) {
			}
		}
		setCurrentTab(tab);
	}

	public int createTab(int tabLayout, LayoutInflater layoutInflater, ViewGroup mTabLayout) {
		int nextTab = mTabsList.size();
		Tab tab = new Tab(layoutInflater, mTabLayout, tabLayout, nextTab);
		mTabsList.add(tab);
		mFragmentsStateList.add(new ArrayList<FragmentsState>());
		
		return nextTab;
	}
}
