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

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;

/**
 * LevelParser.java
 * 
 * Takes the XML files that hold the description of where the enemies should
 * appear and and what type should they be and constructs Lists with that data.
 * 
 * @author Iakovos Gurulian
 * 
 */
public class LevelParser {

	/** The level to be parsed */
	private int level;

	/** The GameActivity Context */
	private Context context;

	/** The ID of the XML file to be parsed. Default is the 1st level */
	private int resId = R.xml.level1;

	/** A list to hold the data extracted from the XML file */
	private List<String> lvlStruct;

	/** The number of levels that the game has */
	private final int TOTALLVLS = 6;

	/**
	 * Creates an ArrayList that will hold the level structure.
	 * 
	 * @param context
	 *            The GameActivity's Context
	 */
	public LevelParser(Context context) {
		this.context = context;

		lvlStruct = new ArrayList<String>();
	}

	/**
	 * Returns the current level
	 * 
	 * @return returns current level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Returns true if the current level's number is smaller than the number of
	 * the greatest level.
	 * 
	 * @param currentLvl
	 *            The level that is going to be loaded
	 * @return True if there are more levels available
	 */
	public boolean hasNext(int currentLvl) {
		if (currentLvl < TOTALLVLS)
			return true;
		else
			return false;
	}

	/**
	 * By using an XmlPullParser, this method extracts data from the XML files
	 * with the level structure, according to their XML tags and stores that
	 * data in a List.
	 * 
	 * @param level
	 *            The level to be parsed
	 * @return A List with all the collected attributes
	 * 
	 */
	public List<String> parser(int level) {
		this.level = level;
		resId = context.getResources().getIdentifier("level" + level, "xml",
				context.getPackageName());
		XmlPullParser parser = context.getResources().getXml(resId);
		try {
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				String type = null;
				String time = null;
				String position = null;
				if ((name != null) && name.equals("enemy")) {
					int size = parser.getAttributeCount();
					for (int i = 0; i < size; i++) {
						String attributeN = parser.getAttributeName(i);
						String attributeV = parser.getAttributeValue(i);
						if ((attributeN != null) && attributeN.equals("type")) {
							type = attributeV;
						} else if ((attributeN != null)
								&& attributeN.equals("time")) {
							time = attributeV;
						} else if ((attributeN != null)
								&& attributeN.equals("position")) {
							position = attributeV;
						}
					}
					if ((type != null) && (time != null) && (position != null)) {
						lvlStruct.add(type);
						lvlStruct.add(time);
						lvlStruct.add(position);
					}
				}
			}
		} catch (Exception e) {
		}
		return lvlStruct;
	}
}
