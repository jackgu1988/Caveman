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
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * SplashScreenActivity.java
 * 
 * The Activity that is responsible for the splash screen, when someone opens
 * the game.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class SplashScreenActivity extends Activity {

	/** Holds the state of the splash screen */
	protected volatile boolean active = true;

	/** True if the user hits the back key */
	protected boolean backKey = false;

	/** Time to display the splash screen in ms */
	protected final int TIMER = 5000;

	/** The Thread in which the splash screen time is measured */
	private Thread splashThread;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		// thread for displaying the splash screen
		splashThread = new Thread() {
			@Override
			public void run() {
				try {
					// could have just done sleep, but then hit screen to
					// continue to next Activity would not work.
					int time = 0;
					while (active && (time < TIMER)) {
						sleep(100);
						if (active) {
							time += 100;
						}
					}
				} catch (InterruptedException e) {

				} finally {
					finish();
					if (backKey == false) {
						Intent intent = new Intent();
						// switch to the next screen/Activity
						intent.setClass(SplashScreenActivity.this,
								MenuActivity.class);
						startActivity(intent);
					}
					interrupt();
				}
			}
		};
		splashThread.start();
	}

	/**
	 * Reacts to button presses. Indicates what happens when the back and home
	 * buttons are pressed.
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			this.splashThread.interrupt();
			backKey = true;
			finish();
			break;

		case KeyEvent.KEYCODE_HOME:
			this.splashThread.interrupt();
			backKey = true;
			finish();
			break;
		}

		return super.onKeyDown(keyCode, event);

	}

	/**
	 * When the user touches the screen, the splash screen goes off.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			active = false;
		}
		return true;
	}

}
