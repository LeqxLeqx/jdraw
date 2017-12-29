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

public class Coordinate {

  public final int x, y;

  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Coordinate sub(Coordinate c) {
    return new Coordinate(x - c.x, y - c.y);
  }

  public Coordinate augmentX(int x) {
    return new Coordinate(this.x + x, this.y);
  }

  public Coordinate augmentY(int y) {
    return new Coordinate(this.x, this.y + y);
  }

}
