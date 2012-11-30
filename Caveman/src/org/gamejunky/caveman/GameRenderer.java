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
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.gamejunky.caveman.enemies.BasicEnemy;
import org.gamejunky.caveman.enemies.JumpingEnemy;
import org.gamejunky.caveman.enemies.SimpleEnemy;
import org.gamejunky.caveman.hero.Hero;
import org.gamejunky.caveman.hero.WeaponHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

/**
 * GameRenderer.java
 * 
 * This class is the heart of the OpenGL layer of the game. It is responsible to
 * draw the whole OpenGL scene and take actions to move enemies, axe, check for
 * collisions, request the next level to be generated, check if the player has
 * lost and more.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class GameRenderer implements Renderer {

	/** Creates a new Hero object */
	private Hero hero;

	/** Creates a new Fire object */
	private Fire fire;

	/** Creates a new WeaponHandler object */
	private WeaponHandler weapon;

	/** Creates a new GameBackground object */
	private GameBackground bg;

	/** Creates a new LevelFinishImage object */
	private LevelFinishImage nextLvl;

	/** Creates a new LevelFinishImage object */
	private LevelFinishImage gameOver;

	/** Context object to store the Activity's context */
	private Context context;

	/** The display width */
	private int width;

	/** The display height */
	private int height;

	/**
	 * A List to hold the data captured through the XML files that build the
	 * level
	 */
	private List<String> lvlStr;

	/** A List that holds all the enemies */
	private List<BasicEnemy> enemies;

	/** GL object responsible for drawing */
	private GL10 gl;

	/** The size of each row of the game (height / 10) */
	private int rowsize;

	/** States whether the axe is on the first point of its path */
	private boolean firstpoint = true;

	/** Creates a new MediaPlayer object for music and sound playback */
	private MediaPlayer mp;

	/** The enemies left in the game */
	private int enemiesLeft;

	/** Creates a new LevelParser object */
	private LevelParser lvl;

	/** The current level */
	private int level;

	/** Creates a new Pipe object */
	private Pipe pipe;

	/** The angle of the axe in the current frame */
	private int rot;

	/** True if the axe has passed through the fireplace in this throwing round */
	private boolean setOnFire = false;

	private Intent Game;

	/**
	 * True if an enemy makes it to the edge of the display without being killed
	 */
	private boolean lost = false;

	/**
	 * True when an enemy touches the edge of the display. Used in order to
	 * avoid having a new game-over image when the rest of the enemies touch the
	 * edge.
	 */
	private boolean hasLost = false;

	/**
	 * Takes the appropriate actions required by the OpenGL layer in order to
	 * start a new game. Initializes the characters, populates the List of the
	 * enemies for the current level etc.
	 * 
	 * @param context
	 *            The GameActivity's Context
	 * @param height
	 *            The display's height
	 * @param width
	 *            The display's width
	 * @param level
	 *            The initial level
	 */
	public GameRenderer(Context context, int height, int width, int level) {
		this.height = height;
		this.width = width;

		rowsize = height / 10;

		this.level = level;

		pipe = Pipe.getPipe();

		lvl = new LevelParser(context);
		lvlStr = lvl.parser(level);

		enemies = new ArrayList<BasicEnemy>();

		this.hero = new Hero(height);
		this.fire = new Fire(height);
		this.weapon = new WeaponHandler(height, width);
		this.buildLevel();
		this.bg = new GameBackground(R.drawable.bg, height, width);
		this.nextLvl = new LevelFinishImage(R.drawable.next_level, height, width);
		this.gameOver = new LevelFinishImage(R.drawable.game_over, height, width);
		this.context = context;
		pipe.setLevelComplete(false);
		pipe.setScore(0);
	}

	/**
	 * Method responsible to assign the textures to all the components of the
	 * game.
	 */
	private void assignTextures() {
		for (int i = 0; i < enemies.size(); i++)
			enemies.get(i).textureLoader(gl, context);
		hero.textureLoader(gl, context);
		fire.textureLoader(gl, context);
		weapon.textureLoader(gl, context);
		bg.textureLoader(gl, context);
		nextLvl.textureLoader(gl, context);
		gameOver.textureLoader(gl, context);
	}

	/**
	 * Method responsible to build the current level (populate the List with the
	 * enemies and set their initial location). It gets called each time the
	 * next level starts.
	 */
	private void buildLevel() {
		enemies.clear();
		for (int i = 0; i < lvlStr.size(); i += 3) {
			if (lvlStr.get(i).equals("SimpleEnemy"))
				enemies.add(new SimpleEnemy(height));
			else if (lvlStr.get(i).equals("JumpingEnemy"))
				enemies.add(new JumpingEnemy(height));
			else
				// In case of typo add a SimpleEnemy
				enemies.add(new SimpleEnemy(height));
			enemies.get(enemies.size() - 1).setX(
					width + rowsize * Integer.parseInt(lvlStr.get(i + 1)));
			enemies.get(enemies.size() - 1).setPosition(
					Integer.parseInt(lvlStr.get(i + 2)));
		}
		enemiesLeft = enemies.size();
		lvlStr.clear();
	}

	/**
	 * Method responsible to draw the game-over image.
	 */
	private void drawGameOverScreen() {
		gl.glPushMatrix();
		gl.glTranslatef(width / 2, height / 2, 0.0f);
		gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
		gameOver.draw(gl);
		gl.glPopMatrix();
	}

	/**
	 * Method responsible to draw the weapon. On each frame it receives the
	 * location where it should be in and draws it appropriatelly.
	 */
	private void drawWeapon() {
		// if the weapon just started following the path, the user stops being
		// able to draw a new line until it gets back
		if (firstpoint) {
			setOnFire = false;
			weapon.throwWeapon();
			firstpoint = false;
			rot = 90;
		}

		weapon.nextSpot();
		if (weapon.allowThrowing()) {
			gl.glPushMatrix();
			gl.glTranslatef(0.0f, weapon.weaponY(), 0.0f);
			gl.glTranslatef(weapon.weaponX(), 0.0f, 0.0f);

			gl.glRotatef(rot, 0.0f, 0.0f, 1.0f);
			gl.glRotatef(180, 1.0f, 0.0f, 0.0f);

			weapon.draw(gl);

			gl.glPopMatrix();
			rot += 5;
		}
	}

	/**
	 * Method responsible to draw the fire in the game.
	 */
	private void fire() {
		gl.glPushMatrix();
		gl.glTranslatef(width / 3, height - height / 10, 0.0f);
		gl.glRotatef(90, 0.0f, 0.0f, 1.0f);

		fire.draw(gl);

		gl.glPopMatrix();
	}

	/**
	 * When the user loses and touches the display, the GameActivity stops and
	 * the application returns back to the LevelSelect Activity.
	 */
	private void gameOver() {
		Game = new Intent(((Activity) context), LevelSelect.class);
		((Activity) context).startActivity(Game);
		((Activity) context).finish();
	}

	/**
	 * Method responsible to play a sound each time an enemy gets killed.
	 */
	private void killSound() {
		if (mp != null) {
			mp.release();
		}
		mp = MediaPlayer.create(context.getApplicationContext(), R.raw.hrach);
		mp.start();
	}

	/**
	 * Method responsible to move all the enemies on each frame. It also checks
	 * for collisions between the axe and the enemies and if they do collide it
	 * kills (or stops for a while) the correct enemy and reduces the amount of
	 * enemies left.
	 */
	private void moveEnemies() {
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getX() < width + rowsize
					&& !enemies.get(i).isDead()) {
				gl.glPushMatrix();
				gl.glTranslatef(0.0f, enemies.get(i).getPosition() * rowsize,
						0.0f);
				gl.glTranslatef(enemies.get(i).getX(), 0.0f, 0.0f);
				gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
				gl.glRotatef(180, 1.0f, 0.0f, 0.0f);
				enemies.get(i).draw(gl);
				gl.glPopMatrix();
				if (weapon.ready())
					if (weapon.collidesWith(enemies.get(i).getX(),
							enemies.get(i).getPosition() * rowsize)) {
						if (enemies.get(i).getType() == 1 && setOnFire == false)
							enemies.get(i).stopForAWhile();
						else {
							killSound();
							enemies.get(i).die();
							enemiesLeft--;
							pipe.setScore(pipe.getScore() + 10);
						}
					}
			}
			if (enemies.get(i).getX() >= -20 && !enemies.get(i).isDead())
				enemies.get(i).setX(
						enemies.get(i).getX() - enemies.get(i).getSpeed());
			else
				onGameOver(i);
		}
	}

	/**
	 * Resets the values that are needed in order for the next level to begin
	 * and performs actions to populate List etc and start the level.
	 */
	private void nextLevel() {
		pipe.setLevelComplete(false);
		pipe.setNextLevel(false);
		if (lvl.hasNext(level))
			lvlStr = lvl.parser(++level);
		else
			gameOver();
		buildLevel();
		assignTextures();
		weapon.reset();
		setOnFire = false;
		pipe.setScore(0);
		hasLost = false;
	}

	/**
	 * The actions that are going to be performed on each frame. Calls the
	 * methods needed in order to draw the frames and handles what is going to
	 * be drawn when the level is complete or the player loses.
	 */
	public void onDrawFrame(GL10 gl) {
		// Clear the screen
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glPushMatrix();
		gl.glTranslatef(width / 2, height / 2, 0.0f);
		gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
		bg.draw(gl);
		gl.glPopMatrix();

		if (weapon.ready())
			this.drawWeapon();
		else
			firstpoint = true;

		weaponOnFire();
		moveEnemies();

		gl.glPushMatrix();
		gl.glTranslatef(50.0f, height / 2, 0.0f);
		gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
		gl.glRotatef(180, 1.0f, 0.0f, 0.0f);
		hero.draw(gl);
		gl.glPopMatrix();

		fire();

		if (enemiesLeft <= 0) {
			pipe.setLevelComplete(true);
			if (pipe.getNextLevel()) {
				nextLevel();
			} else {
				gl.glPushMatrix();
				gl.glTranslatef(width / 2, height / 2, 0.0f);
				gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
				nextLvl.draw(gl);
				gl.glPopMatrix();
			}
		}

		if (hasLost)
			drawGameOverScreen();

	}

	/**
	 * Performs the actions needed when the player loses. It informs the Canvas
	 * layer about the situation, draws an image to indicate that the game is
	 * over and waits for a touch from the Canvas layer in order to return to
	 * the LevelSelect Activity.
	 * 
	 * @param i
	 *            The number of the enemy to be checked whether he has touched
	 *            the edge.
	 */
	private void onGameOver(int i) {
		if (!lost && !enemies.get(i).isDead()) {
			hasLost = true;
			pipe.setLevelComplete(true);
			if (pipe.getNextLevel()) {
				lost = true;
				gameOver();
			}
		}
	}

	/**
	 * Actions performed when the surface changes.
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) {
			height = 1;
		}
		this.width = width;
		this.height = height;

		gl.glMatrixMode(GL10.GL_PROJECTION);

		gl.glViewport(0, 0, width, height);
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, 0, width, height, 0);
	}

	/**
	 * Actions to be performed when the surface is created. It basically sets
	 * the projection, enebles OpenGL features such as the Alpha value for
	 * transparency and assigns the textures.
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		this.gl = gl;

		assignTextures();

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		GLU.gluOrtho2D(gl, 0, width, height, 0);
	}

	/**
	 * Checks for collisions between the weapon and the fire and if they collide
	 * it sets the weapon to be on fire, thus able to kill JumpingEnemies.
	 */
	private void weaponOnFire() {
		if (weapon.collidesWith(width / 3, height - height / 10)) {
			setOnFire = true;
		}
	}
}
