import ij.ImagePlus;

import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
public class Zero_Crossing implements PlugInFilter{
	public int setup(String arg, ImagePlus orig){
		return DOES_8G;
	}

public void run(ImageProcessor ip){
	int w = ip.getWidth();
	int h = ip.getHeight();
	ImageProcessor A = ip.duplicate();
	ImageProcessor B = ip.duplicate();
	Xgradient(A);
	Xgradient(A);

	for(int v = 0; v < h; v++){
		for(int u = 0; u < w; u++){
			int p = A.getPixel(u,v);
			int q = B.getPixel(u,v);
			double T = Math.sqrt(p*p+q*q);//second derivative value
			int y = (int)Math.round(T);
			ip.putPixel(u, v,y);
	}
	}
}
	
	
	
	void Xgradient(ImageProcessor ip){
		float [][]Hx = {
				{-1,0,1},
				{-1,0,1},
				{-1,0,1},
		};
		ImageProcessor R = ip.duplicate();
		int w = R.getWidth();
		int h = R.getHeight();
		int p1 = 0;
		float t1 = 0;
		float t2 = 0;
		
	int ct1 = (Hx.length-1)/2;
	for(int v = 0; v < h; v++){
		for(int u = 0; u < w; u++){
			
			
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
				
					 t1 = Hx[ct1+i][ct1+j];
					t2 = t2 + t1* p1;
					int y = (int)Math.round(t2);
					ip.putPixel(u, v, y);
					
					
					
				}
			}
		}
	}
	}
	
			void Ygradient(ImageProcessor ip){
				float [][]Hy = {
						{-1,-1,-1},
						{0,0,0},
						{1,1,1},
				};
				ImageProcessor R = ip.duplicate();
				int w = R.getWidth();
				int h = R.getHeight();
				
				float t1 = 0;
				float t2 = 0;
					
				int p1 = 0;
			int ct1 = (Hy.length-1)/2;
			for(int v = 0; v < h; v++){
				for(int u = 0; u < w; u++){
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
					
					 t1 = Hy[ct1+i][ct1+j];
					
					t2 = t2 + t1* p1;
					int y = (int)Math.round(t2);
					ip.putPixel(u, v, y);
			
						}
					}
				}
			}
				
			}	
	}
			
		
		