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

import org.gamejunky.caveman.R;

/**
 * JumpingEnemy.java
 * 
 * Creates an enemy of type JumpingEnemy. JumpingEnemies require a hit with the
 * weapon after it has firstly passed through fire in order to be killed.
 * Otherwise they just stop for a while.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class JumpingEnemy extends BasicEnemy {

	/**
	 * Sends to the superclass the icon to be drawn, the height of the display
	 * and a number that indicates that this is a JumpingEnemy.
	 * 
	 * @param height
	 *            The height of the display
	 */
	public JumpingEnemy(int height) {
		super(R.drawable.jump_enemy, height, 1);
	}

}
