package com.example.keepthebeat;

import com.example.keepthebeat.game.Game;
import com.example.keepthebeat.title.Title;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;

public class CustomActivity extends Activity {
	public void backToTitle() {
		Intent myIntent = new Intent(CustomActivity.this, Title.class);
		startActivityForResult(myIntent, 0);
		onDestroy();
	}

	public void backToTitle(final String message) {
		backToTitle("Retour a l'accueil", message);
	}
	
	public void backToTitle(final String titre, final String message) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder alert = new AlertDialog.Builder(CustomActivity.this);

				alert.setTitle(titre);
				alert.setMessage(message);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						backToTitle();
					}
				});

				alert.show();
			}
		});
	}
}
