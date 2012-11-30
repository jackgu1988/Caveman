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

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;

/**
 * GameActivity.java
 * 
 * The Activity on top of which the whole game will be built.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class GameActivity extends Activity {

	/** The Canvas layer of the game */
	private UserInteract userInter;

	/** The OpenGL ES layer of the game */
	private GameView surface;

	/** A bundle to pass data from an Activity to a View */
	private Bundle extras;

	/**
	 * The initial level that the user will play. Set to 1 just in case anything
	 * goes wrong and no level is set.
	 */
	private String level = "1";

	/**
	 * Called when the activity is first created. Creates the layers, passes the
	 * level value and basically starts the game.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// Get the height and width of the display
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		userInter = new UserInteract(this);
		userInter.initActions();

		extras = this.getIntent().getExtras();
		level = extras.getString("level");
		surface = new GameView(this, height, width, Integer.parseInt(level));
		setContentView(surface);
		addContentView(userInter, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
	}

	/**
	 * Handles the occasion that this Activity finishes (when finish() is called
	 * actually).
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		userInter.onBackButton();
	}

	/**
	 * When the back button is pressed, this Activity stops and the game returns
	 * to the MenuActivity.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent Menu = new Intent(GameActivity.this, MenuActivity.class);

			// clear the pipe, as it is static and may not clear itself
			userInter.onBackButton();

			// by calling the following I free up some memory and I avoid
			// program crashes
			surface.destroyDrawingCache();
			surface.clearFocus();
			userInter.destroyDrawingCache();
			userInter.clearFocus();
			surface.onPause();

			startActivity(Menu);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Handles the occasion that the game pauses for some reason (call, low
	 * battery etc).
	 */
	@Override
	protected void onPause() {
		super.onPause();
		surface.onPause();
	}

	/**
	 * Handles the occasion that the game resumes after pausing.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		surface.onResume();
	}

}