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


import java.awt.*;

public abstract class Sprite {

  public static Sprite get(Image image) {
    if (image == null)
      throw new IllegalArgumentException("Image cannot be null");

    return new ImageSprite(image);
  }

  public static Sprite get(Color color) {
    if (color == null)
      throw new IllegalArgumentException("Color cannot be null");

    return new SolidColorSprite(color);
  }

  public static Sprite get(Color color, Shape shape) {
    if (color == null)
      throw new IllegalArgumentException("Color cannot be null");
    if (shape == null)
      throw new IllegalArgumentException("Shape cannot be null");

    return new ShapeSprite(color, shape);
  }


  public final SpriteType type;

  Sprite(SpriteType type) {
    this.type = type;
  }

  public abstract void render(Graphics2D g);


}
