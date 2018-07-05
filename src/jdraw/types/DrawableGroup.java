/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  jdraw: A pure Java 2d rendering library                                *
 *  Copyright (C) 2017 LeqxLeqx                                            *
 *                                                                         *
 *  This program is free software: you can redistribute it and/or modify   *
 *  it under the terms of the GNU General Public License as published by   *
 *  the Free Software Foundation, either version 3 of the License, or      *
 *  (at your option) any later version.                                    *
 *                                                                         *
 *  This program is distributed in the hope that it will be useful,        *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of         *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the          *
 *  GNU General Public License for more details.                           *
 *                                                                         *
 *  You should have received a copy of the GNU General Public License      *
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


package jdraw.types;

import java.util.List;
import java.util.Arrays;

public class DrawableGroup {

  public static DrawableGroup create(Drawable... drawableArray) {
    if (drawableArray == null)
      throw new IllegalArgumentException("Drawables array cannot be null");
    if (Arrays.asList(drawableArray).contains(null))
      throw new IllegalArgumentException("Drawables list cannot contain nulls");

    return new DrawableGroup(drawableArray);
  }

  public static DrawableGroup create(List<Drawable> drawableList) {
    if (drawableList == null)
      throw new IllegalArgumentException("Drawables list cannot be null");
    if (drawableList.contains(null))
      throw new IllegalArgumentException("Drawables list cannot contain nulls");

    return new DrawableGroup(drawableList.toArray(new Drawable[drawableList.size()]));
  }

  private Drawable[] drawables;

  private DrawableGroup(Drawable[] drawables) {
    this.drawables = drawables.clone();
  }


  public Drawable[] getDrawables() {
    return drawables.clone();
  }

}
