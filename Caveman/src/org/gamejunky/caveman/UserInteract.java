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

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Weapon.java
 * 
 * This is the Canvas layer of the game. It records the points that the user
 * touches while drawing the line, draws the line and also draws the score of
 * the level.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class UserInteract extends View implements OnTouchListener {

	/**
	 * A thread that checks whether the score has increased
	 * 
	 * @author Iakovos Gurulian
	 * 
	 */
	public class UpdateThread implements Runnable {

		/**
		 * Checks every 300ms for score updates
		 */
		public void run() {
			while (!stopThread) {
				if (score != pipe.getScore()) {
					score = pipe.getScore();
					postInvalidate();
				}
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/** An ArrayList to hold the points that were selected in type Path */
	private ArrayList<Path> line = new ArrayList<Path>();

	/** A new Path object */
	private Path path = new Path();

	/** A new Paint object */
	private Paint paint = new Paint();

	/** The Pipe singleton */
	private Pipe pipe;

	/** True if the user firstly touched on top of the caveman */
	private boolean correct = false;

	/** Holds the score of the level */
	private int score = 0;

	/** Thread for the timer */
	private Thread myThread = new Thread(new UpdateThread());

	/** Responsible to stop the Thread when needed */
	private boolean stopThread = false;

	/**
	 * Constructor. Initiates the line drawing action and starts the Thread.
	 * 
	 * @param context
	 */
	protected UserInteract(Context context) {
		super(context);
		setFocusable(true);
		this.setOnTouchListener(this);

		pipe = Pipe.getPipe();

		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(4);
		pipe.setWeaponCondition(false);
		pipe.setNextLevel(false);

		myThread.start();
	}

	/**
	 * Draws the score achieved so far
	 * 
	 * @param canvas
	 */
	private void drawScore(Canvas canvas) {
		paint.setAntiAlias(true);
		paint.setTextSize(30);
		canvas.drawText("Score: " + pipe.getScore(), 20, 40, paint);
	}

	/**
	 * Clears the values stored in the ArrayList of the Pipe
	 */
	public void initActions() {
		pipe.clearWeapon();
	}

	/**
	 * Clears the values stored in the ArrayList of the Pipe and stops the score
	 * checking Thread. Maynly used for back button presses
	 */
	public void onBackButton() {
		pipe.clearWeapon();
		stopThread = true;
	}

	/**
	 * Draws the line that is created by the points collected so far. as the
	 * user continues to draw the line, the number of points increases and the
	 * job of this method becomes bigger
	 */
	@Override
	public void onDraw(Canvas canvas) {
		this.drawScore(canvas);
		for (Path path : line) {
			canvas.drawPath(path, paint);
		}
	}

	/**
	 * Stores the values of the points touched into two ArrayLists, one to be
	 * drawn in this layer and one to get passed to the OpenGL layer in order to
	 * draw the axe on top. Also states whether the user has touched the screen
	 * in order to proceed to the next level or the LevelSelect Activity when he
	 * passes a level or gets a game over accordingly
	 */
	public boolean onTouch(View view, MotionEvent event) {
		if (pipe.getLevelComplete())
			pipe.setNextLevel(true);
		else
			switch (event.getAction()) {
			// Only allow drawing the line after the user touches on top of the
			// caveman
			case MotionEvent.ACTION_DOWN:
				if (event.getX() > 20 && event.getX() < 90
						&& event.getY() > (this.getHeight() / 2 - 40)
						&& event.getY() < (this.getHeight() / 2 + 40)
						&& pipe.weaponIsReady() == false) {
					path = new Path();
					path.moveTo(event.getX(), event.getY());
					path.lineTo(event.getX(), event.getY());
					pipe.addToList((int) event.getX());
					pipe.addToList((int) event.getY());
					line.add(path);
					correct = true;
					invalidate();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (correct && pipe.weaponIsReady() == false) {
					path.lineTo(event.getX(), event.getY());
					pipe.addToList((int) event.getX());
					pipe.addToList((int) event.getY());
					invalidate();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (correct && pipe.weaponIsReady() == false) {
					path.lineTo(event.getX(), event.getY());
					pipe.addToList((int) event.getX());
					pipe.addToList((int) event.getY());
					// return to the startpoint in the end of the line
					pipe.returnToInitPoint();
					pipe.setWeaponCondition(true);
					// I clear the ArrayList and prepare for the next round
					line.clear();
					correct = false;
					invalidate();
				}
				break;
			default:
				break;
			}
		return true;
	}
}