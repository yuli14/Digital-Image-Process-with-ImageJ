import ij.*;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.filter.*;
import ij.process.*;



public class histogram_spec_averages implements PlugInFilter{

	public int setup(String arg, ImagePlus imp) {
		// TODO Auto-generated method stub
		return DOES_8G;
	}
	int Hlength = 256;
	int average[] = new int [Hlength];	
	public void run(ImageProcessor Ip) {
		// TODO Auto-generated method stub
		if(runDialog()){
			int[] hA = Ip.getHistogram();
			int[]F = matchHistograms(hA,average);
			Ip.applyTable(F);
		}	
	}
	int[]matchHistograms(int[]hA,int[]hR){
		int K = hA.length;
		double[]PA = Cdf(hA);
		double[]PR = Cdf(hR);
		int [] F = new int[K];
		for (int a = 0;a<K;a++){
			int j = K-1;
			do{
				F[a] = j;
				j--;
			}while(j>=0&&PA[a]<=PR[j]);
		}
		return F;
	}
	
	double[]Cdf(int[]h){
		int K = h.length;
		int n = 0;
		for(int i = 0;i<K;i++){
			n += h[i];
		}
		double []P = new double[K];
		int c = h[0];
		P[0] = (double)c/n;
		for(int i=1;i<K;i++){
			c +=h[i];
			P[i]= (double)c/n;
		}
		return P;
	}
	
	boolean runDialog(){
		int[]windowList = WindowManager.getIDList();
		if(windowList == null){
			IJ.noImage();
			return false;
		}
		for(int i=0;i<windowList.length-1;i++){
			ImagePlus im = WindowManager.getImage(windowList[i]);
			ImageProcessor imp = im.getProcessor().convertToByte(false);
			int[]imH = imp.getHistogram();
			for(int j = 0;j<256;j++){
				average[j] += imH[j]/(windowList.length-1);
			}
		}
		return true;
	}

}
