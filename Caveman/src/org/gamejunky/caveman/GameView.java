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

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * GameView.java
 * 
 * Class that just sets the Renderer that will control the scene of the OpenGL
 * layer.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class GameView extends GLSurfaceView {

	/** The Renderer of the game */
	private GameRenderer gameRen;

	/**
	 * Set the Renderer and let the fun begin :)
	 * 
	 * @param context
	 *            The Context of the GameActivity
	 * @param height
	 *            The height of the display
	 * @param width
	 *            The width of the display
	 * @param level
	 *            The initial level
	 * 
	 */
	public GameView(Context context, int height, int width, int level) {
		super(context);
		gameRen = new GameRenderer(context, height, width, level);
		this.setRenderer(gameRen);
	}
}
