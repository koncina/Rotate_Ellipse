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
		return DOES_ALL + NO_CHANGES;
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
		gd.showDialog();
		if (gd.wasCanceled()) return;

		int angle = (int) gd.getNextNumber();

		EllipseRoi ellipse = (EllipseRoi) roi;
		double[] p = ellipse.getParams();
		Line line = new Line(p[0], p[1], p[2], p[3]);
		line = (Line) RoiRotator.rotate(line, angle);
		imp.setRoi(new EllipseRoi(line.x1d, line.y1d, line.x2d, line.y2d, p[4]));
	}
}
