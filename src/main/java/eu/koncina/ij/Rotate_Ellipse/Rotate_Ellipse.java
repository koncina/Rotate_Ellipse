package eu.koncina.ij.Rotate_Ellipse;

import java.awt.Event;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.EllipseRoi;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.Line;
import ij.plugin.RoiRotator;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Rotate_Ellipse implements PlugInFilter, MouseListener, MouseMotionListener {

	protected ImagePlus imp;
	static Vector<Integer> images = new Vector<Integer>();
	ImageCanvas canvas;
	EllipseRoi ellipse;
	double[] params;
	
	double aspectRatio;
	int width;
	
	int oldX, oldY;
	int centerX;

	@Override
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		IJ.register(Rotate_Ellipse.class);
		return DOES_ALL+NO_CHANGES;
	}

	@Override
	public void run(ImageProcessor ip) {
		Integer id = new Integer(imp.getID());
		if (images.contains(id)) {
			IJ.log("Already listening to this image");
			return;
		}

		ImageWindow win = imp.getWindow();

		canvas = win.getCanvas();
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		images.addElement(id);

		width = 700;
		aspectRatio = 0.3;
		int x1 = imp.getWidth() / 2 - width/2;
		int x2 = imp.getWidth() / 2 + width/2;
		int y = imp.getHeight() / 2;
		
		ellipse = new EllipseRoi(x1, y, x2, y, aspectRatio);
		params = ellipse.getParams();
		imp.setRoi(ellipse);

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int angle = 0;
		int offscreenX = canvas.offScreenX(x);
		int offscreenY = canvas.offScreenY(y);
		
		double width = params[2] - params[0];
		double height = params[3] - params[1];
		
		
		
		if ((e.getModifiers() & Event.CTRL_MASK) != 0) {
			if ((offscreenY > oldY && offscreenX < centerX) || (offscreenY < oldY && offscreenX > centerX)) {
				angle = -1;
			} else {
				angle = 1;
			}
			oldY = offscreenY;
			Line line = new Line(params[0], params[1], params[2], params[3]);
			line = (Line) RoiRotator.rotate(line, angle);
			ellipse = new EllipseRoi(line.x1d, line.y1d, line.x2d, line.y2d, params[4]);
			params = ellipse.getParams();
			imp.setRoi(ellipse);
		} else {
			centerX = offscreenX;
			ellipse = new EllipseRoi(offscreenX - width/2, offscreenY - height/2, offscreenX + width/2, offscreenY + height/2, params[4]);
			params = ellipse.getParams();
			imp.setRoi(ellipse);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		imp.killRoi();
		imp.updateAndDraw();
		if ((e.getModifiers() & Event.SHIFT_MASK) != 0) {
			if (width == 700) {
				width = 600;
				aspectRatio = 0.5;
			}
			else {
				width = 700;
				aspectRatio = 0.3;
			}
		}
		int x1 = imp.getWidth() / 2 - width / 2;
		int x2 = imp.getWidth() / 2 + width / 2;
		int y = imp.getHeight() / 2;
		
		ellipse = new EllipseRoi(x1, y, x2, y, aspectRatio);
		params = ellipse.getParams();
		
		imp.setRoi(ellipse);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
