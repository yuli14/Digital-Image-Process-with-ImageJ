import ij.*;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.process.*;
import java.util.Arrays;


public class Motion_Deblur   implements PlugInFilter {
	
	

	@Override
	public int setup(String arg, ImagePlus imp) {
		// TODO Auto-generated method stub
		return DOES_8G;
	}
	
	
	float[]makeGaussKernel(double sigma){
		int center = (int)(3.0*sigma);
		float[]Kernel = new float[2*center+1];
		
		double sigma2 = sigma*sigma;
		double SumK = 0;
		for(int i=0;i<Kernel.length;i++){
			double r = center - i;
			double Value = (double)Math.exp(-0.5*(r*r)/sigma2);
			
			SumK = SumK + Value;
		}
		
		for(int i=0;i<Kernel.length;i++){
			double r = center - i;
			double Value = (double)Math.exp(-0.5*(r*r)/sigma2);
			Kernel[i] = (float) (Value / SumK);
			//Normalize
		}
		return Kernel;
	}
	
	
	@Override
	public void run(ImageProcessor blur) {
		
		
		ImageProcessor copy = blur.duplicate();

		
		int w = blur.getWidth();
		int h = blur.getHeight();
		double sigma = 13/3;
		int center = (int)(3.0*sigma);
		float[]filter = makeGaussKernel(sigma);
		float[]filterre = makeGaussKernel(sigma);
		
		
		
		int sum = 0;
		
		for (int v = 0; v <= h-1; v++){
			for (int u = 0; u <= w-1; u++){
				int p = blur.getPixel(u, v);
				sum = sum + p;
			}
		}
		int average = sum / (w * h);
		for (int v = 0; v <= h-1; v++){
			for (int u = 0; u <= w-1; u++){
				copy.putPixel(u, v, average);				
			}
		}
		
		double[][]MatrixA = new double[h][w];
		double[][]MatrixB = new double[h][w];
		double[][]MatrixC = new double[h][w];
		
		
		
		int N = 100;
		for (int k = 0; k <= N; k++){
			
			
			for (int u = 0; u <= h-1; u++){
				for (int v = 0; v <= w-1; v++){
					double s = 0;
					for (int j = -center; j <= center; j++){
						int p;
						if (u+j <= 0){
							p = copy.getPixel(0, v);
						}
							//extend edge of the image
						else if (u+j >= h-1){
							p = copy.getPixel(h-1, v);
						}
							//extend edge of the image
						else{
							p = copy.getPixel(u+j, v);
						}
						float c = filter[j+center];
						s = s + c * p;
					}
					MatrixB[u][v]=s;
								
				}
			}
			
			for (int v = 0; v <= h-1; v++){
				for (int u = 0; u <= w-1; u++){
					double p = MatrixB[u][v];
					int q = blur.getPixel(u, v);
					if (p == 0){
						p = 0.01;
					}
					p = q / p;
					MatrixB[u][v]=p;
				}
			}

			for (int u = 0; u <= h-1; u++){
				for (int v = 0; v <= w-1; v++){
					double s = 0;
					for (int j = -center; j <= center; j++){
						double p;
						if (u+j <= 0){
							p = MatrixB[0][v];
						}
							//extend edge of the image
						else if (u+j >= h-1){
							p = MatrixB[h-1][v];
						}
							//extend edge of the image
						else{
							p = MatrixB[u+j][v];
						}
						float c = filterre[j+center];
						s = s + c * p;
					}
					MatrixC[u][v]=s;
								
				}
			}
			
			for (int v = 0; v <= h-1; v++){
				for (int u = 0; u <= w-1; u++){
					double p = MatrixC[u][v];
					int q = copy.getPixel(u, v);
					p = q * p;
					MatrixA[u][v]=p;
				}
			}
			for (int v = 0; v <= h-1; v++){
				for (int u = 0; u <= w-1; u++){
					int p = (int)MatrixA[u][v];	
					copy.putPixel(u, v, p);
				}
			}
		}
		
		for (int v = 0; v <= h-1; v++){
			for (int u = 0; u <= w-1; u++){
				int p = copy.getPixel(u, v);	
				blur.putPixel(u, v, p);
			}
		}			
	}
}
	