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

import jdraw.types.Alignment;
import jdraw.types.Camera;
import jdraw.types.Coordinate;
import jmath.JMath;
import jmath.types.Matrix;
import jmath.types.Vector;

import java.awt.*;
import java.awt.event.MouseEvent;

public /*static*/ class Utilities { private Utilities() {}


  public static Coordinate asCoordinate(MouseEvent e) {
    return new Coordinate(e.getX(), e.getY());
  }

  public static Vector asVector(Coordinate coordinate, int width, int height, Camera camera) {

    final double
            canvasProportion = (double) width / (double) height;

    final Matrix
            cameraRotation = Matrix.zRotation(camera.rotation);

    Vector position = new Vector(coordinate.x, coordinate.y);
    position = position.scale(1.0 / height);
    position = position.sub(new Vector(canvasProportion / 2.0, 0.5));

    position = position.scale(1.0 / camera.scale);
    position = cameraRotation.transform(position);
    position = Vector.add(position, camera.getPosition());

    return position;
  }

  public static String toSpacedString(String[] words, int count) {
    count = JMath.min(words.length, count);
    if (count == 0)
      return "";
    else if (count == 1)
      return words[0];
    else {
      StringBuilder sb = new StringBuilder();
      sb.append(words[0]);

      for (int k = 1; k < count; k++) {
        sb.append(" ");
        sb.append(words[1]);
      }

      return sb.toString();
    }
  }

  public static int widthOfString(Graphics2D g, String string) {
    return g.getFontMetrics().stringWidth(string);
  }

  public static void drawLine(Graphics2D g, String string, Coordinate position, int width, Alignment alignment) {

    int stringWidth = widthOfString(g, string);
    switch (alignment) {

      case LEFT:
        g.drawString(string, position.x, position.y);
        break;
      case RIGHT:
        g.drawString(string, position.x + width - stringWidth, position.y);
        break;
      case CENTER:
        g.drawString(string, position.x + (width - stringWidth) / 2, position.y);
        break;

      default:
        throw new IllegalArgumentException("No such programmed alignment: " + alignment);

    }

  }

  public static void drawStringInBox(Graphics2D g, String string, Coordinate position, Coordinate size, Alignment alignment) {

    Rectangle previousClip = g.getClipBounds();
    g.setClip(position.x, position.y, size.x, size.y);

    String[] lines = string.split("\\n");
    int lineCount = 1, fontHeight = g.getFontMetrics().getHeight();

    for (String line : lines) {

      if (widthOfString(g, line) < size.x) {
        drawLine(g, string, position.augmentY(fontHeight * lineCount), size.x, alignment);
      }
      else {
        String reserve;
        String last = line;
        while (!last.isEmpty()) {

          reserve = "";
          while (widthOfString(g, last) > size.x) {
            reserve = last.charAt(last.length() - 1) + reserve;
            last = last.substring(0, last.length() - 1);
          }

          drawLine(g, last, position.augmentY(fontHeight * lineCount), size.x, alignment);
          lineCount++;

          last = reserve;
        }
      }
    }

    g.setClip(previousClip);

  }


}
