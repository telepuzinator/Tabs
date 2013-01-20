package ru.telepuzinator.tabs;

import java.io.Serializable;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentManager;

public class FragmentsState implements Serializable {
	private static final long serialVersionUID = 2646146489318053134L;

	private Fragment mFragment;
	
	private String mClassName;
	private Bundle mStartArgs;
	private Parcelable mState;
	
	public FragmentsState(Fragment fragment) {
		this.mFragment = fragment;
		mClassName = fragment.getClass().getCanonicalName();
		mStartArgs = fragment.getArguments();
	}
	
	public FragmentsState(Bundle state, String fieldName) {
		this.mClassName = state.getString(fieldName + "_class");
		this.mStartArgs = state.getBundle(fieldName + "_args");
		this.mState = state.getParcelable(fieldName + "_state");
		try {
			Class<?> c = Class.forName(mClassName);
			Fragment fragment = (Fragment) c.newInstance();
			fragment.setArguments(mStartArgs);
			fragment.setInitialSavedState((SavedState) mState);
			this.mFragment = fragment;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Fragment getFragment() {
		mFragment.setInitialSavedState((SavedState) mState);
		return mFragment;
	}
	
	public void rememberFragmentState(FragmentManager fm) {
		mState = fm.saveFragmentInstanceState(mFragment);
	}
	
	public void saveFragmentState(Bundle state, String fieldName) {
		state.putString(fieldName + "_class", mClassName);
		state.putBundle(fieldName + "_args", mStartArgs);
		state.putParcelable(fieldName + "_state", mState);
	}
	
	public Parcelable getFragmentState() {
		return mState;
	}
}
