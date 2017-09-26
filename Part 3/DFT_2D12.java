
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.gui.GenericDialog;
public class DFT_2D implements PlugInFilter{
	static double choose=1;

	public int setup(String arg, ImagePlus imp){
		return DOES_8G;
	}
	class Complex{
		double re,im;
		Complex(double re,double im){
			this.re=re;
			this.im=im;
		}
		
	}
	Complex[] DFT(Complex[] g, boolean forward){
		int M=g.length;
		double s=1/Math.sqrt(M);
		Complex[] G=new Complex[M];
		for(int m=0;m<M;m++){
			double sumRe=0;
			double sumIm=0;
			double phim=2*Math.PI*m/M;
			for(int u=0;u<M;u++){
				double gRe=g[u].re;
				double gIm=g[u].im;
				double cosw=Math.cos(phim*u);
				double sinw=Math.sin(phim*u);
				if(!forward)
					sinw=-sinw;
				sumRe+=gRe*cosw+gIm*sinw;
				sumIm+=gIm*cosw-gRe*sinw;
			}
			G[m]=new Complex(s*sumRe,s*sumIm);
		}
		return G;
	}

	public void run(ImageProcessor ip) {
		if(runDialog()){
          int w=ip.getWidth();
          int h=ip.getHeight();
          double [][]r=new double [w][h];
          double [][]i=new double [w][h];
          double [][]abs=new double [w][h];
          
          Complex[] g1=new Complex[h];
          Complex[] g2=new Complex[w];
          for(int u=0;u<w;u++){
        	  for(int v=0;v<h;v++){	  
        		  Complex z=new Complex(ip.get(u, v),0);
        		  g1[v]=z;
        	  }
        	  g1=DFT(g1,choose==1);
        	  for(int v=0;v<h;v++){	  
        		  r[u][v]=g1[v].re;
        		  i[u][v]=g1[v].im;
        	  }
        	  
          }
          
          
          for(int v=0;v<h;v++){
        	  for(int u=0;u<w;u++){
        		  Complex z=new Complex(r[u][v],i[u][v]);
        		  g2[u]= z;
        	  }
        	  g2=DFT(g2,choose==1);
        	  for(int u=0;u<w;u++){	  
        		  r[u][v]=g2[u].re;
        		  i[u][v]=g2[u].im;
        	  }
          }
          for(int v=0;v<h;v++){
        	  for(int u=0;u<w;u++){
        		  abs[u][v]=Math.sqrt(r[u][v]*r[u][v]+i[u][v]*i[u][v]);
        		  ip.putPixel(u, v, (int)abs[u][v]);
        	  }
          }
          if(choose!=1){
        	  for(int u=0;u<w;u++){
            	  for(int v=0;v<h;v++){	  
            		  Complex z=new Complex(abs[u][v],0);
            		  g1[v]=z;
            	  }
            	  g1=DFT(g1,choose==1);
            	  for(int v=0;v<h;v++){	  
            		  r[u][v]=g1[v].re;
            		  i[u][v]=g1[v].im;
            	  }
        	  }
        	  for(int v=0;v<h;v++){
            	  for(int u=0;u<w;u++){
            		  Complex z=new Complex(r[u][v],i[u][v]);
            		  g2[u]= z;
            	  }
            	  g2=DFT(g2,choose==1);
            	  for(int u=0;u<w;u++){	  
            		  r[u][v]=g2[u].re;
            		  i[u][v]=g2[u].im;
            	  }
              }
              for(int v=0;v<h;v++){
            	  for(int u=0;u<w;u++){
            		  abs[u][v]=Math.sqrt(r[u][v]*r[u][v]+i[u][v]*i[u][v]);
            		  
            	  }
              }  
              for(int v=0;v<h;v++){
            	  for(int u=0;u<w;u++){
            		  
            		  ip.putPixel(u, v, (int)abs[h-u-1][w-v-1]);
            	  }
              }  
              
              }
          }
          
          
		}
	
	boolean runDialog(){
	      GenericDialog gd=new GenericDialog("DFT or Inverse");
	      gd.addNumericField("DFT(input 1) or Inverse(input 2) :", choose, 1);
	      gd.showDialog();
	      if (gd.wasCanceled())
	           return false;
	      else {
	           choose=gd.getNextNumber();
	           return true;
	           }
	      }
}
