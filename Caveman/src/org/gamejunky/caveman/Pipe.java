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

/**
 * Pipe.java
 * 
 * A singleton class to pass values and pipe events through the different Views.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class Pipe {

	/** A new static Pipe to be used for making this class singleton */
	private static Pipe pipe = null;

	/**
	 * Initiates the singleton class and returns it after that.
	 * 
	 * @return the class itself as a singleton class
	 */
	public static Pipe getPipe() {
		if (pipe == null) {
			pipe = new Pipe();
		}
		return pipe;
	}

	/** An ArrayList of points selected in the UserInteract class */
	private ArrayList<Integer> points;

	/**
	 * Indicates whether the axe is ready to be thrown. That means that the user
	 * has firstly drawn the line in order for it to become true
	 */
	private boolean ok = false;

	/**
	 * Indicates whether the user has tapped the display while having finished a
	 * level and wishes to proceed to the next
	 */
	private boolean clicked = false;

	/** Indicates whether the level is complete or not */
	private boolean complete = false;

	/** The score achieved so far in the level */
	private int score = 0;

	/**
	 * Initiates the ArrayList
	 */
	protected Pipe() {
		points = new ArrayList<Integer>();
	}

	/**
	 * 
	 * @param coord
	 */
	public void addToList(int coord) {
		points.add(coord);
	}

	/**
	 * Resets all the values of the pipe to the default ones.
	 */
	public void clearPipe() {
		clicked = false;
		ok = false;
		this.clearWeapon();
		score = 0;
	}

	/**
	 * Clears the coordinates that the weapon should follow from the ArrayList.
	 */
	public void clearWeapon() {
		points.clear();
	}

	/**
	 * Returns true when a level is complete, so that the user will not be able
	 * to draw a line any more.
	 * 
	 * @return true if the user achieves to complete a level or get a game over
	 */
	public boolean getLevelComplete() {
		return complete;
	}

	/**
	 * Returns whether the user has touched the screen in order to proceed to
	 * the next level.
	 * 
	 * @return true if the user touched the screen
	 */
	public boolean getNextLevel() {
		return clicked;
	}

	/**
	 * Returns an ArrayList with the points that were touched.
	 * 
	 * @return an ArrayList of points touched
	 */
	public ArrayList<Integer> getPoints() {
		return points;
	}

	/**
	 * Returns the score achieved so far.
	 * 
	 * @return the score achieved
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Sets the final point that the axe should go to to be the same as the
	 * initial in order to return to the caveman in the end of its path.
	 */
	public void returnToInitPoint() {
		points.add(points.get(0));
		points.add(points.get(1));
	}

	/**
	 * Sets complete to true. Of course this may mean that the user has lost as
	 * well :)
	 * 
	 * @param complete
	 *            True if the level will not continue
	 */
	public void setLevelComplete(boolean complete) {
		this.complete = complete;
	}

	/**
	 * Sets whether the user has touched the display.
	 * 
	 * @param clicked
	 *            true if the user touched the display while waiting for the
	 *            next level
	 */
	public void setNextLevel(boolean clicked) {
		this.clicked = clicked;
	}

	/**
	 * Sets the score achieved so far in the level.
	 * 
	 * @param score
	 *            the score achieved so far.
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Sets the condition of the weapon. If true, that means that the user has
	 * drawn a line with his finger and lifted it, so the weapon is ready to set
	 * fire!
	 * 
	 * @param ok
	 *            true when the user lifts his finger after drawing a line
	 */
	public void setWeaponCondition(boolean ok) {
		this.ok = ok;
	}

	/**
	 * Returns true if the user has drawn a line and lifted finger. False when
	 * the weapon has done its loop and returned to its initial place
	 * 
	 * @return true if the user has drawn a line and lifted finger
	 */
	public boolean weaponIsReady() {
		return ok;
	}

}
