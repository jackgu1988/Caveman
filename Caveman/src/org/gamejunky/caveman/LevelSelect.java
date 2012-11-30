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
import android.view.View;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

/**
 * LevelSelect.java
 * 
 * The Activity that asks the user to select a level to begin with.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class LevelSelect extends Activity {

	/** Creates a new Intent object */
	private Intent Game;

	/** Creates a new Bundle object */
	private Bundle bundle;

	/** Creates an array to hold the buttons */
	private ImageButton[] btns;

	/**
	 * Fills an array with ImageButtons and copies that to the btns array. This
	 * is done because the btns array can not be filled otherwise without
	 * throuing exceptions.
	 */
	private void buttons() {
		ImageButton[] buttons = {
				(ImageButton) findViewById(R.id.imageButton1),
				(ImageButton) findViewById(R.id.imageButton2),
				(ImageButton) findViewById(R.id.imageButton3),
				(ImageButton) findViewById(R.id.imageButton4),
				(ImageButton) findViewById(R.id.imageButton5),
				(ImageButton) findViewById(R.id.imageButton6) };
		btns = buttons;
	}

	/**
	 * Retrieves the button that was clicked and calls the method that starts
	 * the corresponding level.
	 */
	private void levelSelection() {
		bundle = new Bundle();
		for (int i = 0; i < btns.length; i++)
			btns[i].setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					ImageButton clickedButton = (ImageButton) v;

					switch (clickedButton.getId()) {
					case R.id.imageButton1:
						bundle.putString("level", "1");
						startLevel();
						break;

					case R.id.imageButton2:
						bundle.putString("level", "2");
						startLevel();
						break;

					case R.id.imageButton3:
						bundle.putString("level", "3");
						startLevel();
						break;

					case R.id.imageButton4:
						bundle.putString("level", "4");
						startLevel();
						break;

					case R.id.imageButton5:
						bundle.putString("level", "5");
						startLevel();
						break;

					case R.id.imageButton6:
						bundle.putString("level", "6");
						startLevel();
						break;
					}
				}
			});
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_select_layout);
		buttons();
		levelSelection();
	}

	/**
	 * Key listener that when the user presses the back button, he returns to
	 * the menu.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent Menu = new Intent(LevelSelect.this, MenuActivity.class);

			startActivity(Menu);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * A level starts. This method sends to the GameActivity the information
	 * stating which level it should be and then it finishes the current
	 * Activity (LevelSelect)
	 */
	private void startLevel() {
		Game = new Intent(LevelSelect.this, GameActivity.class);
		Game.putExtras(bundle);
		startActivity(Game);
		finish();
	}

}
