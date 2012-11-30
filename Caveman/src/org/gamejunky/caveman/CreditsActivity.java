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
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ViewFlipper;

/**
 * CreditsActivity.java
 * 
 * The game credits Activity where people who helped in the project are
 * credited.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class CreditsActivity extends Activity {

	/** A flipper to switch between the credits */
	private ViewFlipper mFlipper;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credits_layout);
		mFlipper = ((ViewFlipper) this.findViewById(R.id.flipper));
		mFlipper.startFlipping();
	}

	/**
	 * When the back key is pressed, this Activity finishes and the MenuActivity
	 * starts.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent Menu = new Intent(CreditsActivity.this, MenuActivity.class);
			startActivity(Menu);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
