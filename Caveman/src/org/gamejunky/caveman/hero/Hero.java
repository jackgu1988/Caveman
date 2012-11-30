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

import org.gamejunky.caveman.R;
import org.gamejunky.caveman.TextureLoader;

/**
 * Creates the Hero of the game.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class Hero extends TextureLoader {

	/**
	 * Creates a Hero and sends the image to be used to the TextureLoader class.
	 * 
	 * @param height
	 *            The height of the display
	 */
	public Hero(int height) {
		super(R.drawable.caveman, height);
	}

}
