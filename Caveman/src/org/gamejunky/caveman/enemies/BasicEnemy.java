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

package org.gamejunky.caveman.enemies;

import java.util.Timer;
import java.util.TimerTask;

import org.gamejunky.caveman.TextureLoader;

/**
 * BasicEnemy.java
 * 
 * This class will provide the basic enemy features.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class BasicEnemy extends TextureLoader {

	private class Timeout extends TimerTask {
		/** Method called when class is started */
		public void run() {

			// Sets speed to 0 and when the timer is over it becomes 1 again
			setSpeed(0);
			try {
				// stops for 800ms
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setSpeed(1);
		}
	}

	/** The position of the enemy on the x axis */
	private int x;

	/** The position on the y axis */
	private int position;

	/** Indicates whether the enemy has been killed */
	private boolean dead = false;

	/** The speed that the enemy moves */
	private int speed;

	/** The type of the enemy. 1 for simple, 2 for jumping */
	private int type;

	/**
	 * Sets the image (according to the display height) and type of the enemy.
	 * 
	 * @param img
	 *            the image to be used
	 * @param height
	 *            the display height
	 * @param type
	 *            the type of the enemy (1 for simple, 2 for jumping)
	 */
	public BasicEnemy(int img, int height, int type) {
		super(img, height);
		this.type = type;
		speed = 1;
	}

	/**
	 * Sets the enemy to be dead.
	 */
	public void die() {
		dead = true;
	}

	/**
	 * Returns the position of the enemy on the y axis.
	 * 
	 * @return the position on the y axis
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Returns the speed by which the enemy moves.
	 * 
	 * @return the speed of the enemy
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Returns the type of the enemy (1 for simple, 2 for jumping).
	 * 
	 * @return the type of the enemy
	 */
	public int getType() {
		return type;
	}

	/**
	 * Returns the position of the enemy on the x axis.
	 * 
	 * @return the position on the x axis
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns whether the enemy is dead or not.
	 * 
	 * @return true if the enemy is dead
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * Sets the position of the enemy on the y axis.
	 * 
	 * @param position
	 *            the position of the enemy on the y axis
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Sets the speed of the enemy.
	 * 
	 * @param speed
	 *            the speed of the enemy
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Sets the position of the enemy on the x axis.
	 * 
	 * @param x
	 *            the position of the enemy on the x axis
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Stops the Jumping enemies for a while when they get hit by a non-flaming
	 * axe.
	 */
	public void stopForAWhile() {
		Timer timer = new Timer();
		Timeout timeout = new Timeout();

		timer.schedule(timeout, 500);
	}

}
