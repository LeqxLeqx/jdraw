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


package jdraw.overlay;

import jdraw.types.Coordinate;

import java.awt.*;

public abstract class OComponent {

  boolean focused = false;

  public final OType type;
  protected Coordinate position, size;
  protected Color backColor;

  protected Color foreColor;

  OComponent(OType type) {
    this.type = type;
    this.position = new Coordinate(0, 0);
    this.size = new Coordinate(100, 100);

    this.backColor = Color.WHITE;
    this.foreColor = Color.BLACK;
  }


  protected abstract void render(Graphics2D g);
  protected abstract void acceptClickImp(Coordinate c);



  public void setPosition(Coordinate coordinate) {
    if (coordinate == null)
      throw new IllegalArgumentException("position cannot be set to null");

    position = coordinate;
  }
  public void setSize(Coordinate coordinate) {
    if (coordinate == null)
      throw new IllegalArgumentException("size cannot be set to null");

    size = coordinate;
  }

  public Coordinate getPosition() {
    return position;
  }

  public Coordinate getSize() {
    return size;
  }


  public Color getBackColor() {
    return backColor;
  }

  public void setBackColor(Color backColor) {
    if (backColor == null)
      throw new IllegalArgumentException("backColor cannot be set to null");

    this.backColor = backColor;
  }

  public Color getForeColor() {
    return foreColor;
  }

  public void setForeColor(Color foreColor) {
    if (foreColor == null)
      throw new IllegalArgumentException("foreColor cannot be set to null");
    this.foreColor = foreColor;
  }


  public void acceptClick(Coordinate c) {
    if (
            c.x >= position.x && c.y >= position.y &&
            c.x < position.x + size.x && c.y < position.y + size.y
            )
      acceptClickImp(c.sub(position));
  }

  public void draw(Graphics2D g) {

    g.translate(position.x, position.y);
    Rectangle oldClip = g.getClipBounds();
    g.setClip(0, 0, size.x, size.y);

    g.setColor(backColor);
    g.fillRect(0, 0, size.x, size.y);

    render(g);

    g.setClip(oldClip);
    g.translate(-position.x, -position.y);

  }


}
