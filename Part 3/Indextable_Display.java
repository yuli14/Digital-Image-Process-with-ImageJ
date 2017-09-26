// File Brighten_Index_Image.java 2
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import java.awt.image.IndexColorModel;
import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import java.awt.*;
import ij.gui.*;
import ij.plugin.filter.Convolver;
import ij.IJ;
import ij.gui.GenericDialog;
import ij.process.*;
import ij.process.ImageConverter;
import java.util.*;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import java.awt.image.IndexColorModel;


public class Indextable_Display implements PlugInFilter { 
	static final int R = 0, G = 1, B = 2;
	
	public int setup(String arg, ImagePlus im) {
		ImagePlus imp = IJ.getImage();
		ImageConverter ic = new ImageConverter(imp);
		ic.convertRGBtoIndexedColor(256);
		imp.updateAndDraw();
		return DOES_8C; // this plugin works on indexed color images
	}
	
	public void run(ImageProcessor ip) {
		IndexColorModel icm = (IndexColorModel) ip.getColorModel();
		int pixBits = icm.getPixelSize();
		int mapSize = icm.getMapSize();
		int[] indextable = new int[mapSize];
		int w = ip.getWidth();
		int h = ip.getHeight();
		
		//retrieve the current lookup tables (maps) for R,G,B
		byte[] Rmap = new byte[mapSize]; 
		byte[] Gmap = new byte[mapSize]; 
		byte[] Bmap = new byte[mapSize]; 
		icm.getReds(Rmap);
		icm.getGreens(Gmap);
		icm.getBlues(Bmap);


		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				int index = ip.getPixel(u, v);
				indextable[index]+=1;
			}
		}

		ColorProcessor cip = new ColorProcessor(528,528);
		
		int[] white=new int[3];
		white[R]=white[G]=white[B]=255;
		for (int v = 0; v < 528; v++) {
			for (int u = 0; u < 528; u++) {
				cip.putPixel(u, v, white);
			}
		}
		
		int[] RGB = new int[3];
		int dx=15;
		int dy=15;
		for (int idx = 0; idx < mapSize; idx++){
			RGB[R] = Rmap[idx];
			RGB[G] = Gmap[idx];
			RGB[B] = Bmap[idx];
			for (int i=0; i<16; i++){
				for (int j=0; j<16; j++){
					cip.putPixel(dx+i, dy+j, RGB);
				}
			}
			int number=idx+1;
//			String str = "(" + number + ":" + indextable[idx] + ")" ;
			String str = "("+ indextable[idx] + ")" ;
			cip.drawString (str, (dx), (dy+28) );
//			
			dx=dx+32;
			if (dx>=512){
				dx=15;
				dy=dy+32;
			}
		}

		ImagePlus cimg = new ImagePlus("RGB Image",cip);
		cimg.show();

	}
} 