import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
public class Laplacian_Filter_with_c implements PlugInFilter {
	public int setup(String arg, ImagePlus orig){
		return DOES_8G;
	}
public void run(ImageProcessor orig){
	int w = orig.getWidth();
	int h = orig.getHeight();
	double c = 0;
	
	double a = c/4;
	double b = 1-c;
	//Laplacian filter
	double [][]filter = {
			{0,a,0},
			{a,b,a},
			{0,a,0},
			
	};
	ImageProcessor copy = orig.duplicate();
	for(int v=1;v<=h-2;v++){
		for(int u=1;u<=w-2;u++){
			double sum = 0;
			for(int j=-1;j<=1;j++){
				for(int i =-1;i<=1;i++){
					int p = copy.getPixel(u+i,v+j);
					double f = filter[j+1][i+1];
					sum = sum +f*p;
				}
					
				}
			int q = (int)Math.round(sum);
			
				
			orig.putPixel(u, v, q);
			}
		}
	}
}
