import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.Blitter;
import ij.process.ImageProcessor;
public class Laplacian_Edge_Detection implements PlugInFilter{

	public int setup(String arg, ImagePlus imp) {
		
		return DOES_8G;
}
	public void run(ImageProcessor ip){
		ImageProcessor A = ip.duplicate();
		ImageProcessor B = ip.duplicate();
		ImageProcessor C = ip.duplicate();
		//image processing
		Laplacian(A);
		zerocrossing(B);
		Gradient(C);
		int w =ip.getWidth();
		int h = ip.getHeight();
		
		  A.multiply(1);
		     B.multiply(1);
		     B.copyBits(A,0,0,Blitter.SUBTRACT);//overlay A with B
			 ip.insert(B.convertToByte(false), 0, 0);
			  for(int v = 0; v < h; v++){
					for(int u = 0; u < w; u++){
						int p = C.getPixel(u, v);
					
						if(ip.getPixel(u,v)>p){
							ip.putPixel(u, v, p);
						}
					}
			 
			  }
		
	}
	
	void Laplacian(ImageProcessor ip){
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

void Gradient(ImageProcessor ip){
	ImageProcessor R = ip.duplicate();
	ImageProcessor G = ip.duplicate();
	int w = R.getWidth();
	int h = R.getHeight();
	double sum = 0;
	int p = 0;
	int q = 0;
	/*for(int v = 0;v<=h-1;v++){
		for(int u =0;u<=w-1;u++){
	*/	
	
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
					sum = Math.sqrt((sum1*sum1)+(sum2*sum2));
				}
			}
			int y = (int)Math.round(sum);
			ip.putPixel(u, v, y);
			
		}
	}
}

void zerocrossing(ImageProcessor ip){
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
			double T = Math.sqrt(p*p+q*q);
		
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
					/*if(u+j<0){
					         p = R.getPixel(w-u+j,v);
						  }
						  else if(u+j>w-1){
							 p = R.getPixel(-j,v); 
						  }
					        else  {
					         p = R.getPixel(u+j, v);
						    } */
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
