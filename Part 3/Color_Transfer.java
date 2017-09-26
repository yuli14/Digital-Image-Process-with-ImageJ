import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.gui.GenericDialog;

public class Color_Transfer implements PlugInFilter {
	ImagePlus atIm=null;
	public int setup(String arg, ImagePlus imp){
		return DOES_RGB;
	}
	public void run(ImageProcessor ip){
		if(runDialog()){
		ImageProcessor atIp = atIm.getProcessor().convertToRGB();
		int ah=atIp.getHeight();
		int aw=atIp.getWidth();
		int n=0;
		double a1=1/Math.sqrt(3);
    	double b1=1/Math.sqrt(6);
    	double c1=1/Math.sqrt(2);
    	double []la=new double[ah*aw];
		double []ala=new double[ah*aw];
		double []bea=new double[ah*aw];
    	double al_s=0;
    	double aa_s=0;
    	double ab_s=0;
		for(int u=0;u<aw;u++){
			for(int v=0;v<ah;v++){
				int c=atIp.get(u,v);
				int r=(c & 0xff0000) >>16;
		    	int g=(c & 0x00ff00) >>8;
		    	int b=(c & 0x0000ff);
		    	
		    	double L=0.3811*r+0.5783*g+0.0402*b;
		    	double M=0.1967*r+0.7244*g+0.0782*b;
		    	double S=0.0241*r+0.1288*g+0.8444*b;
		    	
		    	L=Math.log10(L);
		    	M=Math.log10(M);
		    	S=Math.log10(S);
		    	 la[n]=a1*(L+M+S);
		    	 ala[n]=b1*(L+M-2*S);
		    	 bea[n]=c1*(L-M);
		    	 
		    	 al_s=la[n]+al_s;
		    	 aa_s=ala[n]+aa_s;
		    	 ab_s=bea[n]+ab_s;
		    	 n++;
			}
		}
		
		double al_a=al_s/n;
		double aa_a=aa_s/n;
		double ab_a=ab_s/n;
		double sl=0;
		double sa=0;
		double sb=0;
		for(int i=0;i<n;i++) {
			 sl=(la[i]-al_a)*(la[i]-al_a)+sl;
			 sa=(ala[i]-aa_a)*(ala[i]-aa_a)+sa;
			 sb=(bea[i]-ab_a)*(bea[i]-ab_a)+sb;
		}
		sl=Math.sqrt(sl/n);
		sa=Math.sqrt(sa/n);
		sb=Math.sqrt(sb/n);
		
		int h=ip.getHeight();
		int w=ip.getWidth();
		double []l=new double[h*w];
		double []al=new double[h*w];
		double []be=new double[h*w];
    	double l_s=0;
    	double a_s=0;
    	double b_s=0;
    	n=0;
		for(int u=0;u<w;u++){
			for(int v=0;v<h;v++){
				int c=ip.get(u,v);
				int r=(c & 0xff0000) >>16;
		    	int g=(c & 0x00ff00) >>8;
		    	int b=(c & 0x0000ff);
		    	
		    	double L=0.3811*r+0.5783*g+0.0402*b;
		    	double M=0.1967*r+0.7244*g+0.0782*b;
		    	double S=0.0241*r+0.1288*g+0.8444*b;
		    	
		    	L=Math.log10(L);
		    	M=Math.log10(M);
		    	S=Math.log10(S);
		    	
		    	 l[n]=a1*(L+M+S);
		    	 al[n]=b1*(L+M-2*S);
		    	 be[n]=c1*(L-M);
		    	
		    	 l_s=l[n]+l_s;
		    	 a_s=al[n]+a_s;
		    	 b_s=be[n]+b_s;
		    	 n++;
			}
		}
		
		
		double l_a=l_s/n;
		double a_a=a_s/n;
		double b_a=b_s/n;
		double tl=0;
		double ta=0;
		double tb=0;
		for(int i=0;i<n;i++) {
			 tl=(l[i]-l_a)*(l[i]-l_a)+tl;
			 ta=(al[i]-a_a)*(al[i]-a_a)+ta;
			 tb=(be[i]-b_a)*(be[i]-b_a)+tb;
		}
		tl=Math.sqrt(tl/n);
		ta=Math.sqrt(ta/n);
		tb=Math.sqrt(tb/n);
		n=0;
		double K=1;
		double R=1;
		for(int u=0;u<w;u++){
			for(int v=0;v<h;v++){	
			    l[n]=l[n]-l_a;
			    al[n]=al[n]-a_a;
			    be[n]=be[n]-b_a;
			    
			    l[n]=l[n]*tl/sl;
			    al[n]=al[n]*ta/sa;
			    be[n]=be[n]*tb/sb;
			    
			    l[n]=l[n]+R*(K*l_a+(1-K)*al_a);
			    al[n]=al[n]+R*(K*a_a+(1-K)*aa_a);
			    be[n]=be[n]+R*(K*b_a+(1-K)*ab_a);
				n++;
			}	
		}
		
		n=0;
		for(int u=0;u<w;u++){
			for(int v=0;v<h;v++){	
				double L=a1*l[n]+b1*al[n]+c1*be[n];
				double M=a1*l[n]+b1*al[n]-c1*be[n];
				double S=a1*l[n]-2*b1*al[n];
		    	
		    	L=Math.pow(10, L);
		    	M=Math.pow(10, M);
		    	S=Math.pow(10, S);
		    	
		    	double r=4.4679*L-3.5873*M+0.1193*S;
		    	double g=-1.2186*L+2.3809*M-0.1624*S;
		    	double b=0.0497*L-0.2439*M+1.2045*S;
		    	
		    	int c=(((int)r & 0xff)<<16) |(((int)g & 0xff)<<8)|(((int)b & 0xff));
		    	ip.putPixel(u, v, c);
		    	n++;
			}
		}
	}
	}
	 boolean runDialog(){
	      int[] windowList=WindowManager.getIDList();
	      if (windowList==null){
	        IJ.noImage();
	        return false;
	        }
	      String[] windowTitles=new String[windowList.length];
	      for (int i=0; i<windowList.length; i++){
	      ImagePlus im=WindowManager.getImage(windowList[i]);
	      if (im==null)
	            windowTitles[i]="untitled";
	      else
	            windowTitles[i]=im.getShortTitle();
	      }
	      GenericDialog gd=new GenericDialog("choose at image");
	      gd.addChoice("atmosphare image:",windowTitles,windowTitles[0]);
	      gd.showDialog();
	      if (gd.wasCanceled())
	           return false;
	      else {
	           int fgIdx=gd.getNextChoiceIndex();
	           atIm=WindowManager.getImage(windowList[fgIdx]);
	           return true;
	           }
	      }
	
}
