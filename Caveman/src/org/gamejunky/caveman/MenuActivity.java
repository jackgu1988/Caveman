/**
 * Copyright (C) 2011  Iakovos Gurulian
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gamejunky.caveman;

import org.gamejunky.caveman.R;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

/**
 * MenuActivity.java
 * 
 * The Activity that draws and handles the menu of the game.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class MenuActivity extends Activity implements AnimationListener {

	/** Creates a new button */
	private Button GameButton;

	/** Creates a new button */
	private Button OptionsButton;

	/** Creates a new button */
	private Button HighscoresButton;

	/** Creates a new button */
	private Button HelpButton;

	/** Creates a new button */
	private Button CreditsButton;

	/** Creates a new MediaPlayer */
	private MediaPlayer mp;

	/** An array with the buttons */
	private boolean[] selected = new boolean[5];

	/**
	 * Plays a sound when a button is clicked.
	 */
	private void clickSound() {
		if (mp != null) {
			mp.release();
		}
		mp = MediaPlayer.create(getApplicationContext(), R.raw.click);
		mp.start();
	}

	/**
	 * Creates the menu buttons and sets the animations and sounds to be played
	 * when clicked.
	 */
	private void createMenuButtons() {

		final Animation animation1 = AnimationUtils.loadAnimation(this,
				R.anim.clicked);

		GameButton = (Button) findViewById(R.id.Game);
		GameButton.startAnimation(AnimationUtils.loadAnimation(
				MenuActivity.this, R.anim.slide_down));
		GameButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				clickSound();
				selected[0] = true;
				GameButton.startAnimation(animation1);
				animation1.setAnimationListener(MenuActivity.this);
			}
		});

		OptionsButton = (Button) findViewById(R.id.Options);
		OptionsButton.startAnimation(AnimationUtils.loadAnimation(
				MenuActivity.this, R.anim.slide_down));
		OptionsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				clickSound();
				selected[1] = true;
				OptionsButton.startAnimation(animation1);
				animation1.setAnimationListener(MenuActivity.this);
			}
		});

		HighscoresButton = (Button) findViewById(R.id.Highscores);
		HighscoresButton.startAnimation(AnimationUtils.loadAnimation(
				MenuActivity.this, R.anim.slide_down));
		HighscoresButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				clickSound();
				selected[2] = true;
				HighscoresButton.startAnimation(animation1);
				animation1.setAnimationListener(MenuActivity.this);
			}
		});

		HelpButton = (Button) findViewById(R.id.Help);
		HelpButton.startAnimation(AnimationUtils.loadAnimation(
				MenuActivity.this, R.anim.slide_down));
		HelpButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				clickSound();
				selected[3] = true;
				HelpButton.startAnimation(animation1);
				animation1.setAnimationListener(MenuActivity.this);
			}
		});

		CreditsButton = (Button) findViewById(R.id.Credits);
		CreditsButton.startAnimation(AnimationUtils.loadAnimation(
				MenuActivity.this, R.anim.slide_down));
		CreditsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				clickSound();
				selected[4] = true;
				CreditsButton.startAnimation(animation1);
				animation1.setAnimationListener(MenuActivity.this);
			}
		});
	}

	/**
	 * Waits for a small animation to end before proceeding to the next
	 * Activity.
	 */
	public void onAnimationEnd(Animation animation) {
		switchView();
		finish();
	}

	public void onAnimationRepeat(Animation animation) {

	}

	public void onAnimationStart(Animation animation) {

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		for (int i = 0; i < 5; i++)
			selected[i] = false;
		createMenuButtons();
	}

	/**
	 * When the user hits the back button, the application ends.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Detects the button that was clicked and redirects the user to the
	 * corresponding Activity.
	 */
	private void switchView() {
		for (int i = 0; i < 5; i++) {
			if (selected[i] == true) {
				switch (i) {
				case 0:
					Intent Game = new Intent(MenuActivity.this,
							LevelSelect.class);
					startActivity(Game);
					break;

				case 1:
					Intent Options = new Intent(MenuActivity.this,
							OptionsActivity.class);
					startActivity(Options);
					break;

				case 2:
					Intent Highscores = new Intent(MenuActivity.this,
							HighscoresActivity.class);
					startActivity(Highscores);
					break;

				case 3:
					Intent Help = new Intent(MenuActivity.this,
							HelpActivity.class);
					startActivity(Help);
					break;

				case 4:
					Intent Credits = new Intent(MenuActivity.this,
							CreditsActivity.class);
					startActivity(Credits);
					break;

				}
			}

			selected[i] = false;
		}
	}
}
