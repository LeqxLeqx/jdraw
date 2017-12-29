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

import jmath.types.Vector;

public class Camera {

  private Vector position;
  public double rotation;
  public double scale;

  public Camera() {
    position = Vector.ZERO;
    rotation = 0;
    scale = 1.0;
  }

  public Camera clone() {
    Camera ret = new Camera();
    ret.position = position;
    ret.rotation = rotation;
    ret.scale = scale;
    return ret;
  }


  public Vector getPosition() {
    return position;
  }

  public void setPosition(Vector position) {
    if (position == null)
      throw new IllegalArgumentException("Position cannot be set to null");
    this.position = position;
  }

  public void augmentPosition(Vector positionAugment) {
    if (positionAugment == null)
      positionAugment = Vector.ZERO;
    this.position = Vector.add(this.position, positionAugment);
  }


}
