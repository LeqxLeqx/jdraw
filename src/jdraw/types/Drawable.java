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

import jmath.JMath;
import jmath.types.Vector;

public class Drawable {


  public static Drawable get(
          Sprite sprite,
          Vector relativePosition,
          double width,
          double height,
          double relativeRotation
          ) {
    return new Drawable(sprite, relativePosition, width, height, relativeRotation);
  }

  public static Drawable get(
          Sprite sprite,
          Vector relativePosition,
          double width,
          double height
          ) {
    return new Drawable(sprite, relativePosition, width, height, 0);
  }

  public static Drawable get(
          Sprite sprite,
          double width,
          double height,
          double relativeRotation
  ) {
    return new Drawable(sprite, Vector.ZERO, width, height, relativeRotation);
  }

  public static Drawable get(
          Sprite sprite,
          double width,
          double height
          ) {
    return new Drawable(sprite, Vector.ZERO, width, height, 0);
  }



  public final Sprite sprite;
  public final Vector relativePosition;
  public final double width, height;
  public final double relativeRotation;

  private final double diagonalExtent;

  private Drawable(
          Sprite sprite,
          Vector relativePosition,
          double width,
          double height,
          double relativeRotation
    ) {

    if (sprite == null)
      throw new IllegalArgumentException("Sprite cannot be null");
    if (relativePosition == null)
      throw new IllegalArgumentException("Relative position cannot be null");
    if (width <= 0)
      throw new IllegalArgumentException("Width cannot be less than zero");
    if (height <= 0)
      throw new IllegalArgumentException("Height cannot be less than zero");

    this.sprite = sprite;
    this.relativePosition = relativePosition;
    this.width = width;
    this.height = height;
    this.relativeRotation = relativeRotation;

    this.diagonalExtent = JMath.hypotenuse(width / 2, height / 2);

  }

  public double getDiagonalExtent() {
    return diagonalExtent;
  }

}
