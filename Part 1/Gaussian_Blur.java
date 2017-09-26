import ij.ImagePlus;
import ij.plugin.filter.Convolver;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
public class Gaussian_Blur implements PlugInFilter{
	public int setup(String arg, ImagePlus im){
		return DOES_8G;
	}
	
	float[]makeGaussKernel1d(double sigma){
		int center = (int)(3.0*sigma);
		float[]Kernel = new float[2*center+1];
		
		double sigma2 = sigma*sigma;
		for(int i=0;i<Kernel.length;i++){
		double r = center - i;
		Kernel[i] = (float)Math.exp(-0.5*(r*r)/sigma2);
	}
		return Kernel;
	}

	public void run(ImageProcessor ip){
		
		
		ImageProcessor I = ip.convertToFloat();
		int sigma = 10;
		
		float[]H = makeGaussKernel1d(sigma);	
		int w = I.getWidth();
		int h = I.getHeight();
		int n = H.length;
		
		for(int v = 0;v<=h-1;v++){
			for(int u =0;u<=w-1;u++){
				//get original pixel value
				int p = ip.getPixel(u, v);
				
				//board define and set value of board
				if(u<=n/2 && v<=n/2) {
					p =ip.getPixel(n/2, n/2);
				
				
				}
				else if(u<=n/2 && v>=h-1-n/2) {
					p =ip.getPixel(n/2, h-1-n/2);
				}
				else if(u>=w-1-n/2 && v<=n/2) {
					p =ip.getPixel(w-1-n/2, n/2);
				}
				else if(u>=w-1-n/2 && v>=h-1-n/2) {
					p =ip.getPixel(w-1-n/2, h-1-n/2);
				}
				else if(u>=n/2 &&u<w-1-n/2&&v>=0&&v<2/n)
				{
						p = ip.getPixel(u, 2/n);
					}
				
		else if(u>=n/2&&u<w-1-n/2&&v>=h-1-2/n&&v<=h-1){
					
						p = ip.getPixel(u, h-1-2/n);
				}
		else if(u>=0&&u<=2/n&&v>=2/n&&v<h-1-n/2){
					
						p = ip.getPixel(2/n, v);
					}
				
			else if(u >= w-1-n/2&&u<=w-1&&v >=2/n&&v<h-1-n/2){
					
						p = ip.getPixel(w-1-n/2, v);
					}
				
					ip.putPixel(u, v, p);
				}
		}
					
		//convolve image
		Convolver cv = new Convolver();
		cv.setNormalize(true);
		cv.convolve(I, H, 1, H.length);
		cv.convolve(I, H, H.length, 1);
		
		
		ip.insert(I.convertToByte(false), 0, 0);
		
}
	}
				