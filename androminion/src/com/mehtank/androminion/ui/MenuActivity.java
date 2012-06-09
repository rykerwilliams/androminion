package com.mehtank.androminion.ui;

import com.mehtank.androminion.Androminion;
import com.mehtank.androminion.R;
import com.mehtank.androminion.SettingsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class MenuActivity extends FragmentActivity {
	boolean mTwoColums = false;
	int mState = R.id.but_about;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuactivity);
		
		if(findViewById(R.id.contentfragment) != null)	{
			mTwoColums = true;
			if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.contentfragment) == null) {
				getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.contentfragment, new AboutFragment())
					.commit();
			}
			if(savedInstanceState != null) {
				mState = savedInstanceState.getInt("mState");
			}
		}	
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt("mState", mState);
		super.onSaveInstanceState(savedInstanceState);
	}

	public void buttonClick(View view) {
		doAction(view.getId());
	}
		
	public void doAction(int id) {
		switch(id){
		case R.id.but_start:
			startActivity(new Intent(this, Androminion.class));
			break;
		case R.id.but_stats:
			if(mTwoColums){
				if(mState != R.id.but_stats) {
					mState = R.id.but_stats;
					changeFragment(new CombinedStatsFragment());
				}
			} else {
				startActivity(new Intent(this, CombinedStatsActivity.class));
			}
			break;
		case R.id.but_settings:
				startActivity(new Intent(this, SettingsActivity.class));
			break;
		case R.id.but_about:
			if(mTwoColums){
				if(mState != R.id.but_about) {
					mState = R.id.but_about;
					changeFragment(new AboutFragment());
				}
			} else {
				startActivity(new Intent(this, AboutActivity.class));
			}
			break;
		}
	}
	
	private void changeFragment(Fragment newFragment) {
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.contentfragment, newFragment)
			.commit();
	}
}
