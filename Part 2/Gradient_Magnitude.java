import ij.ImagePlus;
import ij.plugin.filter.Convolver;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
public class Gradient_Magnitude implements PlugInFilter{
	public int setup(String arg, ImagePlus imp) {
	
		return DOES_8G;
}
	public void run(ImageProcessor ip){
		ImageProcessor R = ip.duplicate();
		ImageProcessor G = ip.duplicate();
		int w = R.getWidth();
		int h = R.getHeight();
		double sum = 0;
		int p = 0;
		int q = 0;
		
		
		float [][]Hx = {
				{-1,0,1},
				{-1,0,1},
				{-1,0,1},
		};
				
		
		float [][]Hy = {
				{-1,-1,-1},
				{0,0,0},
				{1,1,1},
		};
		
	
		int ct1 = (Hy.length-1)/2;
		for(int v = 0; v < h; v++){
			for(int u = 0; u < w; u++){
				double sum1 = 0;
				double sum2 = 0;
			//x direction gradient
				for(int j = -ct1; j <= ct1 ; j++){
					for(int i= -ct1; i <= ct1 ; i++){
						if(u+j<0){
						         p = R.getPixel(w-u+j,v);
							  }
							  else if(u+j>w-1){
								 p = R.getPixel(-j,v); 
							  }
						        else  {
						         p = R.getPixel(u+j, v);
							    }
						float t1 = Hx[ct1+i][ct1+j];
						sum1 = sum1 + t1* p;
					// y direction gradient		
								
						if(v+i<0){
					         q = G.getPixel(u,h-v+i);
						  }
						  else if(v+i>h-1){
							 q = G.getPixel(u,-i); 
						  }
					        else  {
					        q = G.getPixel(u, v+i);
						    }
						
						float t2 = Hy[ct1+i][ct1+j];
						
						sum2 = sum2 + t2* q;
						sum = Math.sqrt((sum1*sum1)+(sum2*sum2));//gradient of image
					}
				}
				int y = (int)Math.round(sum);
				ip.putPixel(u, v, y);
			}
		}
	
	}
	}
	

