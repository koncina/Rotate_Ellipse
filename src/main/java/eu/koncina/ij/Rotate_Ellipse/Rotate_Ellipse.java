package eu.koncina.ij.Rotate_Ellipse;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.EllipseRoi;
import ij.gui.GenericDialog;
import ij.gui.Line;
import ij.gui.Roi;
import ij.plugin.RoiRotator;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Rotate_Ellipse implements PlugInFilter {
	protected ImagePlus imp;

	@Override
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_8G | DOES_16 | DOES_32 | DOES_RGB;
	}

	@Override
	public void run(ImageProcessor ip) {
		Roi roi = imp.getRoi();
		if (roi == null) {
			IJ.error("This plugin requires a ROI");
			return;
		}
		if (!(roi instanceof EllipseRoi)) {
			IJ.error("This plugin works only with an ellipse ROI");
			return;
		}
		GenericDialog gd = new GenericDialog("Rotate Ellipse");
		gd.addNumericField("Angle", 5, 0);
		//gd.addRadioButtonGroup("Orientation", new String[]{"left", "right"}, 1, 2, "right");
		gd.showDialog();
		if (gd.wasCanceled()) return;

		int angle = (int) gd.getNextNumber();
		//String orientation = gd.getNextRadioButton();

		EllipseRoi ellipse = (EllipseRoi) roi;
		double[] p = ellipse.getParams();
		Line line = new Line(p[0], p[1], p[2], p[3]);
		line = (Line) RoiRotator.rotate(line, angle);
		//IJ.log("length = " + Math.sqrt(Math.pow(p[2] - p[0], 2) + Math.pow(p[3] - p[1], 2)));
		imp.setRoi(new EllipseRoi(line.x1d, line.y1d, line.x2d, line.y2d, p[4]));
	}
}
