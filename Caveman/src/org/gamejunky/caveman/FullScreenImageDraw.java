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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * FullScreenImageDraw.java
 * 
 * This class is responsible for drawing fullscreen images (rectangle +
 * texture).
 * 
 * @author Iakovos Gurulian
 * 
 */
public class FullScreenImageDraw {

	/** The buffer that holds the vertices of the square that the enemy lives */
	private FloatBuffer vertexBuffer;

	/** The buffer that holds the indices for the texture */
	private ByteBuffer indexBuffer;

	/** The vertices of the square (pairs of 3) */
	private float vertices[];

	/** The buffer that holds the vertices of the texture */
	private FloatBuffer textureBuffer;

	/** The vertices of the texture (pairs of 2) */
	private float texture[] = { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f };

	/** An array to hold the textures to be used (1 in this occasion) */
	private int[] textures = new int[1];

	/** An array that contains the indices */
	private byte indices[] = { 0, 1, 3, 0, 3, 2, };

	/** The image id */
	private int img;

	/**
	 * Creates the object according to the screen size.
	 * 
	 * @param img
	 *            the image to be used
	 * @param height
	 *            the height of the display
	 * @param width
	 *            the width of the display
	 */
	public FullScreenImageDraw(int img, int height, int width) {
		this.img = img;

		float vert[] = { -height / 2, width / 2, 0.0f, height / 2, width / 2,
				0.0f, -height / 2, -width / 2, 0.0f, height / 2, -width / 2,
				0.0f };

		vertices = vert;

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuffer.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		// create a byte buffer with the UV coordinates
		byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuffer.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	/**
	 * The draw method that draws the object and sets its properties.
	 * 
	 * @param gl
	 */
	public void draw(GL10 gl) {
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		// Bind the texture according to the set texture filter
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		// Enable the vertex, texture and normal state
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

		// Set the face rotation
		gl.glFrontFace(GL10.GL_CCW);

		// Point to our buffers
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		// Draw the vertices as triangles, based on the Index Buffer information
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_BYTE, indexBuffer);

		// Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

	/**
	 * The method needed to load the texture and assign a texture to an object.
	 * 
	 * @param gl
	 * @param context
	 */
	public void textureLoader(GL10 gl, Context context) {
		// Get the texture from the Android resource directory
		InputStream is = context.getResources().openRawResource(img);
		Bitmap bitmap = null;
		try {
			// BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			// Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}

		// Generate there texture pointer
		gl.glGenTextures(1, textures, 0);

		// Create Nearest Filtered Texture and bind it to texture 0
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		// Setting min/mag to linear to solve issue with waving texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);

		// This fixes an issue that a line above the textures would appear on
		// Samsung Galaxy S
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_CLAMP_TO_EDGE);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		// Clean up
		bitmap.recycle();
	}
}
