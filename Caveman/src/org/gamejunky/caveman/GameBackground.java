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

/**
 * GameBackground.java
 * 
 * Creates the game background and draws it.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class GameBackground extends FullScreenImageDraw {

	/**
	 * Sends the image to be drawn along with the display width and height to
	 * the superclass, that will draw them
	 * 
	 * @param img
	 *            the image to be presented
	 * @param height
	 *            the height of the screen
	 * @param width
	 *            the width of the screen
	 */
	public GameBackground(int img, int height, int width) {
		super(img, height, width);
	}
}
