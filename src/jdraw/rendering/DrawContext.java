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
import jmath.JMath;
import jmath.types.Matrix;
import jmath.types.Vector;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

public class DrawContext implements MouseMotionListener, MouseListener {


  public final Canvas canvas;
  public final Window window;

  private Toolkit toolkit;
  private Camera camera;
  private BufferStrategy bufferStrategy;
  private Color backgroundColor = Color.BLACK;

  private LinkedList<Renderable> renderables;
  private LinkedList<DrawRequest> drawRequests;

  private LinkedList<DrawMouseListener> mouseListeners;
  private LinkedList<UpdateCallback> updateCallbacks;


  public DrawContext(Window window) {

    if (window == null)
      throw new IllegalArgumentException("OWindow cannot be null");

    this.window = window;
    this.canvas = new Canvas();
    this.camera = new Camera();

    this.toolkit = Toolkit.getDefaultToolkit();
    this.renderables = new LinkedList<>();
    this.drawRequests = new LinkedList<>();

    this.mouseListeners = new LinkedList<>();
    this.updateCallbacks = new LinkedList<>();

    this.canvas.addMouseListener(this);
    this.canvas.addMouseMotionListener(this);


  }

  public synchronized void setCamera(Camera camera) {
    if (camera == null)
      throw new IllegalArgumentException("Camera cannot be set to null");
    this.camera = camera;
  }

  public synchronized Camera getCamera() {
    return this.camera;
  }


  public synchronized Color getBackgroundColor() {
    return backgroundColor;
  }

  public synchronized void setBackgroundColor(Color color) {
    this.backgroundColor = color;
  }


  public synchronized void addUpdateCallback(UpdateCallback uc) {

    if (uc == null)
      throw new IllegalArgumentException("Update callback cannot be null");

    updateCallbacks.add(uc);
  }

  public synchronized boolean removeUpdateCallback(UpdateCallback uc) {

    if (uc == null)
      throw new IllegalArgumentException("Update callback cannot be null");

    return updateCallbacks.remove(uc);
  }


  public synchronized void addRenderable(Renderable renderable) {

    if (renderable == null)
      throw new IllegalArgumentException("Renderable cannot be null");

    renderables.add(renderable);
  }
  public synchronized boolean removeRenderable(Renderable renderable) {

    if (renderable == null)
      throw new IllegalArgumentException("Renderable cannot be null");

    return renderables.remove(renderable);
  }

  public synchronized void addMouseListener(DrawMouseListener ml) {
    if (ml == null)
      throw new IllegalArgumentException("DrawMouseListener cannot be null");

    if (!mouseListeners.contains(ml))
      mouseListeners.add(ml);
  }

  public synchronized boolean removeMouseListener(DrawMouseListener ml) {

    if (ml == null)
      throw new IllegalArgumentException("DrawMouseListener cannot be null");

    return mouseListeners.remove(ml);
  }

  public void addDrawRequest(AbsoluteDrawRequestTask task) {
    addDrawRequest(DrawRequestTime.AFTER_RENDERABLES, task);
  }
  public synchronized void addDrawRequest(DrawRequestTime time, AbsoluteDrawRequestTask task) {

    if (time == null)
      throw new IllegalArgumentException("Draw request time cannot be null");
    if (task == null)
      throw new IllegalArgumentException("Draw request task cannot be null");

    DrawRequest request = new DrawRequest();
    request.absolute = true;
    request.task = task;
    request.time = time;
    drawRequests.add(request);
  }

  public void addDrawRequest(RelativeDrawRequestTask task) {
    addDrawRequest(DrawRequestTime.AFTER_RENDERABLES, task);
  }
  public synchronized void addDrawRequest(DrawRequestTime time, RelativeDrawRequestTask task) {

    if (time == null)
      throw new IllegalArgumentException("Draw request time cannot be null");
    if (task == null)
      throw new IllegalArgumentException("Draw request task cannot be null");

    DrawRequest request = new DrawRequest();
    request.absolute = false;
    request.task = task;
    request.time = time;
    drawRequests.add(request);
  }




  public synchronized void init() {

    canvas.createBufferStrategy(2);
    bufferStrategy = canvas.getBufferStrategy();

  }

  public synchronized void update() {

    Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();

    renderDrawRequests(g, DrawRequestTime.FIRST);

    renderBackground(g);

    renderDrawRequests(g, DrawRequestTime.AFTER_BACKGROUND);

    renderRenderables(g);

    renderDrawRequests(g, DrawRequestTime.AFTER_RENDERABLES);
    renderDrawRequests(g, DrawRequestTime.LAST);

    g.dispose();
    bufferStrategy.show();

    window.repaint();
    toolkit.sync();

    for (UpdateCallback uc : updateCallbacks) {
      uc.update();
    }

  }


  private void renderDrawRequests(Graphics2D g, DrawRequestTime time) {

    for (DrawRequest request : drawRequests) {
      if (request.time == time)
        request.perform(canvas, camera, g);
    }

  }

  private void renderBackground(Graphics2D g) {
    g.setColor(backgroundColor);
    g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  private void renderRenderables(Graphics2D g) {

    final double
            canvasProportion = (double) canvas.getWidth() / (double) canvas.getHeight(),
            canvasDiagonalExtent = JMath.hypot(.5, canvasProportion / 2.0);

    g.scale(canvas.getHeight(), canvas.getHeight());
    g.translate(canvasProportion / 2.0, 0.5);

    for (Renderable renderable : renderables) {

      if (renderable.getVisibility() == 0)
        continue;

      Vector
        renderablePosition = renderable.getPosition().sub(camera.getPosition()),
        drawablePosition;
      double
        renderableRotation = renderable.getRotation() - camera.rotation,
        drawableRotation,
        drawableWidth,
        drawableHeight;
      Matrix
        renderableRotationMatrix = Matrix.zRotation(renderableRotation),
        cameraRotationMatrix = Matrix.zRotation(-camera.rotation);

      for (Drawable drawable : renderable.getDrawableGroup().getDrawables()) {

        drawablePosition = cameraRotationMatrix.transform(
                Vector.add(
                  renderablePosition,
                  renderableRotationMatrix.transform(drawable.relativePosition)
                )
             ).scale(camera.scale);

        if (drawablePosition.magnitude() - camera.scale * drawable.getDiagonalExtent() > canvasDiagonalExtent)
          continue;

        drawableRotation = renderableRotation + drawable.relativeRotation;
        drawableWidth = camera.scale * drawable.width / 2;
        drawableHeight = camera.scale * drawable.height / 2;


        g.translate(drawablePosition.x, drawablePosition.y);
        g.rotate(drawableRotation);
        g.scale(drawableWidth, drawableHeight);

        drawable.sprite.render(g);

        g.scale(1 / drawableWidth, 1 / drawableHeight);
        g.rotate(-drawableRotation);
        g.translate(-drawablePosition.x, -drawablePosition.y);

      }

    }

    g.translate(-canvasProportion / 2.0, -0.5);
    g.scale(1.0 / canvas.getHeight(), 1.0 / canvas.getHeight());

  }


  private Vector getPositionFromScreenPosition(int px, int py) {

    final double
            canvasProportion = (double) canvas.getWidth() / (double) canvas.getHeight();

    final Matrix
            cameraRotation = Matrix.zRotation(camera.rotation);

    Vector position = new Vector(px, py);
    position = position.scale(1.0 / canvas.getHeight());
    position = position.sub(new Vector(canvasProportion / 2.0, 0.5));

    position = position.scale(1.0 / camera.scale);
    position = cameraRotation.transform(position);
    position = Vector.add(position, camera.getPosition());

    return position;

  }

  private synchronized void fireMouseEvent(DrawMouseEventType type, MouseEvent e) {
    if (mouseListeners.size() == 0)
      return;

    Vector position = Utilities.asVector(
            Utilities.asCoordinate(e),
            canvas.getWidth(),
            canvas.getHeight(),
            this.camera
        );

    for (DrawMouseListener ml : mouseListeners) {
      ml.event(new DrawMouseEvent(type, position, e));
    }

  }


  @Override public void mouseClicked(MouseEvent mouseEvent) {}
  @Override public void mouseEntered(MouseEvent mouseEvent) {}
  @Override public void mouseExited(MouseEvent mouseEvent) {}


  @Override
  public void mousePressed(MouseEvent mouseEvent) {
    fireMouseEvent(DrawMouseEventType.PRESSED, mouseEvent);
  }

  @Override
  public void mouseReleased(MouseEvent mouseEvent) {
    fireMouseEvent(DrawMouseEventType.RELEASE, mouseEvent);
  }

  @Override
  public void mouseDragged(MouseEvent mouseEvent) {
    fireMouseEvent(DrawMouseEventType.DRAG, mouseEvent);
  }

  @Override
  public void mouseMoved(MouseEvent mouseEvent) {
    fireMouseEvent(DrawMouseEventType.HOVER, mouseEvent);
  }
}
