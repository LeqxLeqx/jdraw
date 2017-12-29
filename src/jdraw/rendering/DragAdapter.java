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
import jdraw.util.Utilities;
import jmath.types.Vector;

public class DragAdapter implements DrawMouseListener, UpdateCallback {

  private Coordinate lastDragCoordinate = null;
  private Vector velocity = Vector.ZERO;
  private double velocityAblation;

  private boolean stopClick = false;

  public final DrawContext drawContext;

  public DragAdapter(DrawContext drawContext) {
    this(drawContext, 0);
  }

  public DragAdapter(DrawContext drawContext, double velocityAblation) {
    if (drawContext == null)
      throw new IllegalArgumentException("Draw context cannot be null");
    if (velocityAblation >= 1 || velocityAblation < 0)
      throw new IllegalArgumentException("Velocity ablation must be in [0, 1)");


    this.drawContext = drawContext;
    this.velocityAblation = velocityAblation;
  }

  @Override
  public void event(DrawMouseEvent e) {

    switch (e.type) {

      case DRAG:
        stopClick = false;
        if (lastDragCoordinate != null) {
          velocity = Utilities.asVector(lastDragCoordinate,
                  drawContext.canvas.getWidth(),
                  drawContext.canvas.getHeight(),
                  drawContext.getCamera()
              ).sub(e.position);
          drawContext.getCamera().augmentPosition(velocity);
          lastDragCoordinate = e.coordinate;
        }
        else
          lastDragCoordinate = e.coordinate;
        break;
      case PRESSED:
        lastDragCoordinate = e.coordinate;
        stopClick = true;
        break;
      case RELEASE:
        lastDragCoordinate = null;
        if (stopClick) {
          velocity = Vector.ZERO;
          stopClick = false;
        }
        break;
    }


  }


  @Override
  public void update() {
    if (lastDragCoordinate == null) {
      drawContext.getCamera().augmentPosition(velocity);
      velocity = velocity.scale(velocityAblation);
    }
  }
}
