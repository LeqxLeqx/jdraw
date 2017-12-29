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

import jdraw.types.Alignment;
import jdraw.types.Coordinate;
import jdraw.util.Utilities;

import java.awt.*;

public class OButton extends OComponent {

  private String text;

  public OButton(String text) {
    super(OType.BUTTON);

    if (text == null)
      text = "";

    this.text = text;
  }

  public OButton() {
    this("");
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    if (text == null)
      text = "";

    this.text = text;
  }



  @Override
  protected void render(Graphics2D g) {
    Utilities.drawStringInBox(
            g,
            text,
            this.position,
            this.size,
            Alignment.CENTER
    );
  }

  @Override
  protected void acceptClickImp(Coordinate c) {

  }
}
