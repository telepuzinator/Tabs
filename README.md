Tabs
====

Usage:
===========
tab_layout.xml example:

	<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="wrap_content"
	    android:padding="10px">
		<ImageView
		    android:id="@+id/tab_icon"
		    android:layout_width="wrap_content"
		    android:layout_height="wrap_content"
		    android:scaleType="centerInside"
		    android:layout_gravity="center"
		    android:src="@drawable/tab_selector"/>
	</FrameLayout>
	
activity example:

<pre>
public class MainActivity extends TabActivity{
	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);
		
		if(state == null) {
			Fragment f = new TestFragment1();
			addTab(f, R.layout.tab_layout);
			
			f = new TestFragment1();
			addFragment(f);
			
			f = new TestFragment1();
			addTab(f, R.layout.tab_layout);
		}

		setOnTabChangeListener(new OnTabChangeListener() {
			@Override
			public void onTabChange(int newTab) {
				// TODO smth
			}
		});
	}
}
</pre>
