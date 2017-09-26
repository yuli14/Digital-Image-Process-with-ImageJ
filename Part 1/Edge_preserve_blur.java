//import ij.process.Blitter;
import ij.ImagePlus;
import ij.plugin.filter.Convolver;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Edge_preserve_blur implements PlugInFilter{
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
		//edge detect filter
		float[] filterx = {
				-1f,0f,1f,
				-2f,0f,2f,
				-1f,0f,1f};
		
		float[] filtery = {
				-1f,-2f,-1f,
				0f,0f,0f,
				1f,2f,1f};
		ImageProcessor I =ip.convertToFloat();
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
			
		
		
	
		ImageProcessor J = I.duplicate();
		// Image(N) store original pixel value
		ImageProcessor N = I.duplicate();
		//Image (J) as a reference image to detect edge
		Convolver cv = new Convolver();
		cv.setNormalize(false);
		cv.convolve(J, filterx, 3, 3);
		cv.convolve(J, filtery, 3, 3);
		
		int w1 = ip.getWidth();
		int h1 = ip.getHeight();
							
		
		//convolve original image first image(I)
		Convolver cv2 = new Convolver();
		cv2.setNormalize(true);
		cv2.convolve(N, H, 1, H.length);
		cv2.convolve(N, H, H.length, 1);
	//get pixel value in processed Image(J)
			for(int v=1;v<=h1-1;v++){
			for(int u=1;u<=w1-1;u++){
				int p = J.getPixel(u,v);
				//edge detect ,the edge point has value 255
				if(p<=230){

							//if it is not edge point, put original value
							int nn = N.getPixel(u, v);
							I.putPixel(u, v, nn);
						} 
			}
		}
		ip.insert(I.convertToByte(false), 0, 0);
	}
}
							
							