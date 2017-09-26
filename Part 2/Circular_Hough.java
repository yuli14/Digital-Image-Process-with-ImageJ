import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.plugin.filter.Convolver;
import ij.plugin.filter.GaussianBlur;

import ij.plugin.filter.PlugInFilter;

public class Circular_Hough implements PlugInFilter{
	public int setup(String arg, ImagePlus im){
		return DOES_8G;
	}

	public void run(ImageProcessor ip){
		
					ImageProcessor J = ip.duplicate();
					GaussianBlur gb = new GaussianBlur();
					
					double radius = 1.4;
					
					
					gb.blur(J,radius);//noise reducing
					J.findEdges();   
					
					int xCtr=J.getWidth()/2;
					int yCtr=J.getHeight()/2;
					
					int rMax = (int)Math.sqrt(xCtr*xCtr+yCtr*yCtr);//max radius
					int h=J.getHeight();
					int w = J.getWidth();
					int houghArray[][][] = new int[w][h][rMax];
					for(int v=0;v<h;v++){
						for(int u=0;u<w;u++){
							
							if(J.get(u,v)>230){//edge points
								for(int y=0;y<h;y++){
									for(int x=0;x<w;x++){
										for(int r =25;r<33;r++){
								
								double xx = Math.abs(u-x);
								double yy = Math.abs(v-y);
								double x1 = Math.pow(xx,2);
								double y1 = Math.pow(yy,2);
								double rad = Math.sqrt(x1+y1);
								if(r == rad){
									houghArray[x][y][r]++;//central points count
											}
										}
									}
								}
								
							}
							else if(J.get(u,v)<=230){
								ip.putPixel(u, v, 0);
							}
								
						}
					}
					
							
							
					for(int y=0;y<h;y++){
						for(int x=0;x<w;x++){
							for(int r =25;r<33;r++){
								if(houghArray[x][y][r]>9){//central point of each circle
									for(int yy=0;yy<h;yy++){
											for(int xx=0;xx<w;xx++){
												for(int rr =0;rr<r;rr++){
									if((x-xx)*(x-xx)+(y-yy)*(y-yy)<rr*rr){ 
										
										ip.putPixel(xx, yy, 255);//if point is inside cicle,make it white
													}
												}
										}
									}
					
								}		
							}
						}
					}
	
			IJ.showMessage("finish");
}
}








		
		
	

	
	
	

		


	


/*class CircleHT{
ImageProcessor ip;
int xCtr,yCtr;
int nAng;
int nRad;
int cRad;
int rad;
double dAng;
double dRad;
int [][][]houghArray;



CircleHT(ImageProcessor ip,int nAng,int nRad,int rad){
	
	this.ip=ip;
	this.xCtr=ip.getWidth()/2;
	this.yCtr=ip.getHeight()/2;
	this.rad = rad;
	this.nAng = nAng;
	this.dAng= Math.PI/nAng;
	this.nRad =nRad;
	this.cRad = nRad/2;
	double rMax = Math.sqrt(xCtr*xCtr+yCtr*yCtr);
	this.dRad = (2.0*rMax)/nRad;
	this.houghArray = new int[nAng][nAng][nAng];
	fillHoughAccumulator();
}*/

		/*float []Hx = {
				-1,0,1,
				-2,0,2,
				-1,0,1};
		float []Hy = {
				1, 2, 1,
				0, 0, 0,
			   -1,-2,-1,};
		Convolver cv1 = new Convolver();
		Convolver cv2 = new Convolver();
		cv1.setNormalize(false);
		cv2.setNormalize(false);
		cv1.convolve(ip,Hx,3,3);
		cv2.convolve(ip,Hy,3,3);
	}
}*/
//edge detection
		/*int h=J.getHeight();
		int w = J.getWidth();
		for(int v=0;v<h;v++){
			for(int u=0;u<w;u++){
				if(J.get(u,v)<230){//edge detection
					doPixel(u,v);
				}
			}
		}
	}
	void doPixel(int x,int y){
		int xCtr=ip.getWidth()/2;
		int yCtr=ip.getHeight()/2;
		int x = u-xCtr, y = v-yCtr;//distance from image median point
		int h=ip.getHeight();
		int rou = 20;
		int w = ip.getWidth();
		int x1 = (int)Math.pow(x,2);
		int y1 = (int)Math.pow(y,2);
		for( u=0;u<w;u++){
			for( v=0;v<h;v++){
			int rad = (int)Math.sqrt(x1+y1);
		if(rad >= rou){
			houghArray[xCtr][yCtr][rad]++;
			
		}
		}
	}
}
		
}
}*/
