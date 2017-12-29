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


package jdraw.rendering;

import jdraw.types.*;
import jmath.JMath;
import jmath.types.Matrix;
import jmath.types.Vector;

import java.awt.*;

class DrawRequest {


  DrawRequestTask task;
  boolean absolute;
  DrawRequestTime time;

  DrawRequest() { }


  void perform(Canvas canvas, Camera camera, Graphics2D graphics) {

    if (absolute)
      ((AbsoluteDrawRequestTask) task).draw(graphics);
    else {

      final double
              canvasProportion = (double) canvas.getWidth() / (double) canvas.getHeight();


      Matrix cameraRotationMatrix = Matrix.zRotation(-camera.rotation);
      Vector position = cameraRotationMatrix.transform(camera.getPosition().negative()).scale(camera.scale);

      graphics.scale(canvas.getHeight(), canvas.getHeight());
      graphics.translate(canvasProportion / 2.0, 0.5);

      graphics.translate(position.x, position.y);
      graphics.scale(camera.scale, camera.scale);
      graphics.rotate(-camera.rotation);

      ((RelativeDrawRequestTask) task).draw(camera, graphics);

      graphics.rotate(camera.rotation);
      graphics.scale(1.0 / camera.scale, 1.0 / camera.scale);
      graphics.translate(-position.x, -position.y);

      graphics.translate(-canvasProportion / 2.0, -0.5);
      graphics.scale(1.0 / canvas.getHeight(), 1.0 / canvas.getHeight());

    }


  }


}
