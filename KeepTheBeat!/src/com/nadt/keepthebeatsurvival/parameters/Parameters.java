package com.nadt.keepthebeatsurvival.parameters;

import com.nadt.keepthebeatsurvival.CustomActivity;
import com.nadt.keepthebeatsurvival.R;
import com.nadt.keepthebeatsurvival.utils.Constants;
import com.nadt.keepthebeatsurvival.utils.FileAccess;
import com.nadt.keepthebeatsurvival.utils.Tools;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;


public class Parameters extends CustomActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String level = FileAccess.readFileAsString(Constants.keepTheBeatFolder, "level");
		if( level.equals("") ) {
			level = Constants.NORMAL;
		}
		
		setContentView(R.layout.parameters);
		
		RadioButton radioEasy = (RadioButton) findViewById(R.id.radioEasy);
		RadioButton radioNormal = (RadioButton) findViewById(R.id.radioNormal);
		RadioButton radioHard = (RadioButton) findViewById(R.id.radioHard);

		if( level.equals( Constants.EASY ) ) {
			radioEasy.setChecked(true);
		}
		else if ( level.equals( Constants.NORMAL ) ) {
			radioNormal.setChecked(true);
		}
		else if ( level.equals( Constants.HARD ) ) {
			radioHard.setChecked(true);
		}
		
		radioEasy.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if( isChecked ) {
					FileAccess.deleteFile(Constants.keepTheBeatFolder, "level");
					FileAccess.writeToFile(Constants.keepTheBeatFolder, "level", Constants.EASY);
					Tools.log(this, "level set to easy");
				}
			}
		});
		radioNormal.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if( isChecked ) {
					FileAccess.deleteFile(Constants.keepTheBeatFolder, "level");
					FileAccess.writeToFile(Constants.keepTheBeatFolder, "level", Constants.NORMAL);
					Tools.log(this, "level set to normal");
				}
			}
		});
		radioHard.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if( isChecked ) {
					FileAccess.deleteFile(Constants.keepTheBeatFolder, "level");
					FileAccess.writeToFile(Constants.keepTheBeatFolder, "level", Constants.HARD);
					Tools.log(this, "level set to hard");
				}
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        backToTitle();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
}
