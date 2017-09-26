import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.plugin.filter.GaussianBlur;
public class Pupil_Measure implements PlugInFilter{
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
						for(int r =1;r<15;r++){
				
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
				J.putPixel(u, v, 0);
			}
				
		}
	}
	
			
			
	for(int y=0;y<h;y++){
		for(int x=0;x<w;x++){
			for(int r =1;r<3;r++){
				if(houghArray[x][y][r]>3){//central point of each circle
					for(int yy=0;yy<h;yy++){
							for(int xx=0;xx<w;xx++){
								for(int rr =0;rr<r;rr++){
					if((x-xx)*(x-xx)+(y-yy)*(y-yy)<rr*rr){ 
						
						ip.putPixel(xx, yy, 255);//make it white
									}
								}
						}
					}
	
				}		
			}
			for(int r =4;r<10;r++){
				if(houghArray[x][y][r]>9){//central point of each circle
					for(int yy=0;yy<h;yy++){
							for(int xx=0;xx<w;xx++){
								for(int rr =0;rr<r;rr++){
					if((x-xx)*(x-xx)+(y-yy)*(y-yy)<rr*rr){ 
						
						ip.putPixel(xx, yy, 255);//make it white
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
