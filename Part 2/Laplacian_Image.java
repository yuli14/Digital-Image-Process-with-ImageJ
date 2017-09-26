import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
 public class Laplacian_Image implements PlugInFilter{
	public int setup(String arg, ImagePlus imp) {
	
		return DOES_8G;
}
	public void run(ImageProcessor ip){
		ImageProcessor R = ip.duplicate();
		
		int w = R.getWidth();
		int h = R.getHeight();
		
		int p1 = 0;
		
	float [][]H = {
			{0,1,0},
			{1,-4,1},
			{0,1,0},
	};
	int ct1 = (H.length-1)/2;
	for(int v = 0; v < h; v++){
		for(int u = 0; u < w; u++){
			
			double sum2 = 0;
		//wrap
			for(int j = -ct1; j <= ct1 ; j++){
				for(int i= -ct1; i <= ct1 ; i++){
					if(u+j<0 && v+i<0){
				         p1 = R.getPixel(w+u+j,v+h+i);
					  }
					  else if (u+j>w-1 && v+i<0){
						 p1 = R.getPixel(u+j-w,v+h+i); 
					  }
				        else if (u+j<w && u+j>=0 && v+i<0){
				         p1 = R.getPixel(u+j, h+v+i);
					    }
						else if (v+i<h && v+i>=0 && u+j<0){
				         p1 = R.getPixel(w+u+j, v+i);
					  }
					    else if (v+i<h && v+i>=0 && u+j>w-1){
				         p1 = R.getPixel(u+j-w, v+i);
					  }
				        else if (v+i>h-1 && u+j<0){
				         p1 = R.getPixel(u+j+w, v+i-h);
					  }
					    else if (v+i>h-1 && u+j<w && u+j>=0){
				         p1 = R.getPixel(u+j, v+i-h);
					  }
					  else if (v+i>h-1 &&  u+j>w-1){
				         p1 = R.getPixel(u+j-w, v+i-h);
					  }
					  else
						  p1 = R.getPixel(u+j, v+i);
					
					float c = H[ct1+i][ct1+j];
					
					sum2 = sum2 + c* p1;
				
				}
			}
			int y = (int)Math.round(sum2);
			ip.putPixel(u, v, y);
		}
	}
	}
	}

