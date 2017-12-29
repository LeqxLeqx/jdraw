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


package jdraw.util;

import jdraw.types.Sprite;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public /*static*/ class ImageBase { private ImageBase() {}

  private static Hashtable<String, Sprite> entries = new Hashtable<>();


  public static void load(String name, String path) throws IOException {
    if (name == null)
      throw new IllegalArgumentException("Name cannot be null");
    if (has(name))
      throw new IllegalArgumentException(String.format("Name '%s' already in use", name));

    BufferedImage image = ImageIO.read(new File(path));
    entries.put(name, Sprite.get(image));
  }

  public static void loadRange(String name, String path, int start, int end) throws IOException {
    if (start >= end)
      throw new IllegalArgumentException(String.format("start value cannot be greater than or equal to end (%d // %d)", start, end));

    for (int k = start; k < end; k++) {
      load(
        String.format(name, k),
        String.format(path, k)
        );
    }

  }


  public static boolean has(String name) {
    return entries.get(name) != null;
  }

  public static Sprite get(String name) {
    Sprite ret = entries.get(name);
    if (ret == null)
      throw new IllegalArgumentException(String.format("No such sprite: '%s'", name));
    return ret;
  }



}
