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

package org.gamejunky.caveman.hero;

import java.util.ArrayList;

import org.gamejunky.caveman.Pipe;
import org.gamejunky.caveman.R;
import org.gamejunky.caveman.TextureLoader;

/**
 * WeaponHandler.java
 * 
 * @author Iakovos Gurulian
 * 
 */
public class WeaponHandler extends TextureLoader {

	/** An object of type Pipe to hold the instance of the singleton class */
	private Pipe pipe;

	/**
	 * An ArrayList in which the points that the user touched will be retrieved
	 * and stored
	 */
	private ArrayList<Integer> points;

	/** The next point of the points ArrayList to be used as an x coordinate */
	private int x = 0;

	/** The next point of the points ArrayList to be used as an y coordinate */
	private int y = 1;

	/**
	 * The radius of a hypothetical cycle surrounding the axe. To be used for
	 * collision detection
	 */
	private int radius;

	/** The width of the display */
	private int width;

	/**
	 * The distance between the centre of the axe and the centre of an object
	 * (ex. enemy)
	 */
	private float dist;

	/**
	 * The AC side of a hypothetical orthogonal triangle created by the spots A
	 * and B which are two continuous coordinates of the axe
	 */
	private float ac;

	/**
	 * The BC side of a hypothetical orthogonal triangle created by the spots A
	 * and B which are two continuous coordinates of the axe
	 */
	private float bc;

	/**
	 * The pieces that the distance AB will be split into for a more smooth
	 * flying of the axe
	 */
	private int chunks;

	/** The chunk number between AB that the axe is currently in */
	private int currentChunk = 0;

	/** The y coordinate of the axe */
	private int yPoint;

	/** The x coordinate of the axe */
	private int xPoint;

	/**
	 * The AC side of a hypothetical triangle between the centres of the 2
	 * objects that are being checked for collisions
	 */
	private float acCol;

	/**
	 * The BC side of a hypothetical triangle between the centres of the 2
	 * objects that are being checked for collisions
	 */
	private float bcCol;

	/** True if the axe is allowed to be thrown */
	private boolean allow;

	/**
	 * Initiate the WeaponHandler by retrieving the Pipe instance, initializing
	 * the ArrayList of points and passing to the superclass the height and the
	 * image that will be used.
	 * 
	 * @param height
	 *            the display height
	 * @param width
	 *            the display width
	 */
	public WeaponHandler(int height, int width) {
		super(R.drawable.axe, height);
		points = new ArrayList<Integer>();
		pipe = Pipe.getPipe();
		this.width = width;
		radius = height / 20;
	}

	/**
	 * True if throwing the axe is allowed
	 * 
	 * @return true if allowed
	 */
	public boolean allowThrowing() {
		return allow;
	}

	/**
	 * Calculate the point on the x axis where the axe will be drawn next
	 */
	private void calculateX() {
		xPoint = (int) (points.get(x - 2) + (ac / chunks) * currentChunk);
	}

	/**
	 * Calculate the point on the y axis where the axe will be drawn next
	 */
	private void calculateY() {
		yPoint = (int) (points.get(y) - (bc / chunks) * (chunks - currentChunk));
		currentChunk++;
		if (chunks <= currentChunk) {
			currentChunk = 0;
			if (y == points.size() - 1)
				y++;
		}
	}

	/**
	 * Checks for collisions by checking if the distance between the centres of
	 * two object is smaller than the sum of their radiuses
	 * 
	 * @param coordX
	 *            the x coordinate of the other object
	 * @param coordY
	 *            the y coordinate of the other object
	 * @return true if they collide
	 */
	public boolean collidesWith(float coordX, float coordY) {
		if (coordX > width + 10)
			return false;
		else {
			acCol = Math.abs(coordX - weaponX());
			bcCol = Math.abs(coordY - weaponY());
			if (acCol != 0 && bcCol != 0)
				dist = (float) Math.sqrt(acCol * acCol + bcCol * bcCol);
			else if (acCol == 0)
				dist = bcCol;
			else if (bcCol == 0)
				dist = acCol;
			if (dist < 2 * radius)
				return true;
			else
				return false;
		}
	}

	/**
	 * Calculates the distance between the current point where the axe is and
	 * the next one, where it should get. It is called AB because it is the AB
	 * side of a hypothetical triangle with A being the 1st point and B the 2nd.
	 * For that, the Pythagorean theorem is used
	 * 
	 * @param firstX
	 *            the x coordinate of the 1st point
	 * @param firstY
	 *            the y coordinate of the 1st point
	 * @param secondX
	 *            the x coordinate of the 2nd point
	 * @param secondY
	 *            the y coordinate of the 2nd point
	 * @return the distance between the 2 points
	 */
	private int distanceAB(int firstX, int firstY, int secondX, int secondY) {
		if (firstX - secondX == 0) {
			ac = 0;
			bc = secondY - firstY;
			return Math.abs(secondY - firstY);
		} else if (secondY - firstY == 0) {
			bc = 0;
			ac = secondX - firstX;
			return Math.abs(firstX - secondX);
		} else {
			ac = secondX - firstX;
			bc = secondY - firstY;
			return (int) Math.sqrt(ac * ac + bc * bc);
		}
	}

	/**
	 * States in how many chunks of 4 pixels a path AB should be split into
	 * 
	 * @param dist
	 *            the distance of AB
	 * @return the number of chunks
	 */
	private int getChunks(int dist) {
		if (dist / 4 > 0)
			return dist / 4;
		else
			return 1;
	}

	/**
	 * Retrieves the next point from the ArrayList of points that will be used.
	 * The distance between points can not be less than 4 pixels. This is done
	 * in order to avoid weird and uneven movement of the axe.
	 */
	private void nextPoint() {
		for (int i = 2; i < points.size() - x; i += 2) {
			if (distanceAB(points.get(x), points.get(y), points.get(x + i),
					points.get(y + i)) > 4) {
				x += i;
				y += i;
				i = points.size();
			}
		}
		if (x >= 2)
			allow = true;
		else
			allow = false;
	}

	/**
	 * Calculates the coordinates of the next spot as it knows the number of
	 * chunks and where the axe currently is
	 */
	public void nextSpot() {
		if (y < points.size()) {
			if (currentChunk == 0) {
				nextPoint();
				if (allow)
					chunks = this.getChunks(distanceAB(points.get(x - 2),
							points.get(y - 2), points.get(x), points.get(y)));
			}
			if (allow) {
				calculateX();
				calculateY();
			} else
				this.reset();
		} else {
			this.reset();
		}
	}

	/**
	 * Contacts the UserInteract class through Pipe and informs it that the
	 * weapon has returned and the user can draw again
	 * 
	 * @return true if the axe is not flying
	 */
	public boolean ready() {
		return pipe.weaponIsReady();
	}

	/**
	 * Resets the weapon condition when the next level loads
	 */
	public void reset() {
		x = 0;
		y = 1;
		currentChunk = 0;
		pipe.setWeaponCondition(false);
		pipe.clearWeapon();
	}

	/**
	 * Retrieves the ArrayList with the points that the user touched when the
	 * weapon gets triggered
	 */
	public void throwWeapon() {
		this.points = pipe.getPoints();
	}

	/**
	 * The x coordinate of the weapon
	 * 
	 * @return the x coordinate of the weapon
	 */
	public int weaponX() {
		return xPoint;
	}

	/**
	 * The y coordinate of the weapon
	 * 
	 * @return the y coordinate of the weapon
	 */
	public int weaponY() {
		return yPoint;
	}
}
