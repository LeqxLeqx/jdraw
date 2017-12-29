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
import jdraw.types.DrawMouseEvent;
import jdraw.types.DrawMouseListener;
import jmath.tools.ArrayTools;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

public class OWindow extends OComponent implements DrawMouseListener {

  private final LinkedList<OComponent> children;

  public OWindow() {
    super(OType.WINDOW);
    children = new LinkedList<>();
  }

  public OComponent[] getChildren() {
    return children.toArray(new OComponent[children.size()]);
  }

  public OComponent[] getDescendants() {

    final LinkedList<OComponent> ret = new LinkedList<>();

    ret.addAll(children);
    children.forEach(child -> {
      if (child.type == OType.WINDOW)
        Collections.addAll(ret, ((OWindow) child).getDescendants());
    });

    return ret.toArray(new OComponent[ret.size()]);
  }

  public void add(OComponent component) {
    if (component == null)
      throw new IllegalArgumentException("Component cannot be null");
    if (this == component)
      throw new IllegalArgumentException("Cannot add window to itself");
    if (component.type == OType.WINDOW && ArrayTools.contains(((OWindow) component).getDescendants(), this))
      throw new IllegalArgumentException("Component is ancestor of window");
    if (ArrayTools.contains(getDescendants(), component))
      throw new IllegalArgumentException("Component is already a descendant of window");

    children.add(component);
  }

  public boolean remove(OComponent component) {
    return children.remove(component);
  }



  @Override
  protected void render(Graphics2D g) {

    for (OComponent child : children) {
      child.draw(g);
    }

  }

  @Override
  protected void acceptClickImp(Coordinate c) {

    for (OComponent child : children) {
      child.acceptClick(c);
    }

  }


  @Override
  public void event(DrawMouseEvent e) {
    acceptClick(e.coordinate);
  }
}
