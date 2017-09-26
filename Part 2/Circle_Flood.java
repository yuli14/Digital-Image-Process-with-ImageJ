import java.util.Stack;
import java.util.*;
import ij.IJ;

import ij.ImagePlus;

import ij.plugin.filter.GaussianBlur;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
public class Circle_Flood implements PlugInFilter{
	
	int i =1;
	boolean runDialog(){
		if(i==1){
			return false;
		}
			
				return true;
	}
	
		
	

		public int setup(String arg, ImagePlus imp) {
			// TODO Auto-generated method stub
			return DOES_8G;
		}
		class Node{
			int x,y;
			
			Node(int x,int y){
				this.x = x;
				this.y = y;
			}
		}
		public void run(ImageProcessor ip){
			ImageProcessor J = ip.duplicate();
			GaussianBlur gb = new GaussianBlur();
			int m =2;
			double radius = 1.4;
			
			
			gb.blur(J,radius);//noise reducing
			J.findEdges();   
			
			int xCtr=J.getWidth()/2;
			int yCtr=J.getHeight()/2;
			
			int rMax = (int)Math.sqrt(xCtr*xCtr+yCtr*yCtr);
			int h=J.getHeight();
			int w = J.getWidth();
			int houghArray[][][] = new int[w][h][rMax];//hough Array
			for(int v=0;v<h;v++){
				for(int u=0;u<w;u++){
					
					if(J.get(u,v)>230){
						for(int y=0;y<h;y++){
							for(int x=0;x<w;x++){
								for(int r =25;r<33;r++){
						
						double xx = Math.abs(u-x);
						double yy = Math.abs(v-y);
						double x1 = Math.pow(xx,2);
						double y1 = Math.pow(yy,2);
						double rad = Math.sqrt(x1+y1);//characteristic of circle
						if(r == rad){
							houghArray[x][y][r]++;//count
									}
								}
							}
						}
						
					}
						
				}
			}
			
					
			for(int y=0;y<h;y++){
				for(int x=0;x<w;x++){
					for(int r =25;r<33;r++){
						if(houghArray[x][y][r]>9){
							for(int yy=0;yy<h;yy++){
									for(int xx=0;xx<w;xx++){
										for(int rr =0;rr<r;rr++){
							if((x-xx)*(x-xx)+(y-yy)*(y-yy)<rr*rr){ //if the point inside the cirlce,make it white
								
								ip.putPixel(xx, yy, 255);
										}
							
									}
								}
							}
						}
						
					}
				}
			}
			for(int y=0;y<h;y++){
				for(int x=0;x<w;x++){
					if(ip.getPixel(x, y)<255){
						ip.putPixel(x, y,0);//back ground
						}
					if(ip.getPixel(x,y)==255){
						ip.putPixel(x, y, 1);//fore ground
					
					
						}
				}
			}
					for(int y=0;y<h;y++){
						for(int x=0;x<w;x++){
					if(ip.getPixel(x,y)==1){
						if(runDialog()){
						
						floodFillspan(ip,x,y,m);//choose from boolean
						 m=m+1;
						}
					else if(!runDialog()){
							floodFill(ip,x,y,m);
							m = m+1;
								}
							}
						}
		
					}
	
		
	IJ.showMessage("finish");
		}
		
				
	
		
	
	
	
		void floodFill(ImageProcessor ip, int x, int y, int label) {
			int h = ip.getHeight();
			int w = ip.getWidth();
			if ((x>=0) && (x<w) && (y>=0) && (y<h)&& ip.getPixel(x,y)==1) {
				ip.putPixel(x, y, 255-20*label);
				floodFill(ip,x+1,y,label);
				floodFill(ip,x,y+1,label);
				floodFill(ip,x,y-1,label);
				floodFill(ip,x-1,y,label);
			}
		}
		  void floodFillspan(ImageProcessor ip, int x, int y, int label) {
	            int h = ip.getHeight();
	            int w = ip.getWidth();
	            LinkedList<Node> q = new LinkedList<Node>(); 
	            q.addFirst(new Node(x,y));
	            while (!q.isEmpty()) {
	            Node n = q.removeLast();
	            if (n.x >= 0 && n.x < w && n.y >= 0 && n.y < h && ip.getPixel(n.x,n.y)==1) {
	            ip.putPixel(n.x,n.y,255-20*label);
	            q.addFirst(new Node(n.x+1,n.y));
	            q.addFirst(new Node(n.x,n.y+1));
	            q.addFirst(new Node(n.x,n.y-1));
	            q.addFirst(new Node(n.x-1,n.y));
	       }
	    }
	    }
		
}
		
