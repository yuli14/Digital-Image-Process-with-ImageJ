
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

/*
 * CS/ECE545 - WPI, Spring 2016
 * Name:Likun Qin
 * Email:lqin@wpi.edu
 * Date:2016-05-03
 * Overview Description of Plugin: 
 * HW#5 Q3. 
 * Create an ImageJ plugin called Color_Transfer that implements the Reinhard et al. 2001 paper (IEEE Computer Graphics and Applications). Please read the paper thoroughly before attempting the implement it. Test your plugin using the images on the class site of “Vincent van Gogh’s Cafe Terrace on the Place du Forum” as the color profile to apply to the fuller labs photo, which obviously needs some color.
 */

public class DFT_2D implements PlugInFilter {
    
	static boolean center = true;
    
	public int setup(String arg, ImagePlus imp) {
		return DOES_ALL + NO_CHANGES;
	}
    
	public void run(ImageProcessor ip) {
		FloatProcessor ipf = (FloatProcessor) ip.convertToFloat();
		DFT2D dft = new DFT2D(ipf,false);
        DFT2D indft = (DFT2D)dft.clone();
        dft.doDFT2D();
        dft.makePowerSpectrum();
		ImageProcessor ipP = dft.makePowerImage();
        dft.swapQuadrants(ipP);
		ImagePlus win = new ImagePlus("DFT Image",ipP);
		win.show();
        indft.inverse();
        indft.makePowerSpectrum();
        ImageProcessor ipP1 = indft.makePowerImage2();
		ImagePlus win1 = new ImagePlus("Inverse DFT Image",ipP1);
		win1.show();
	}
}

class DFT2D implements Cloneable{
	int width;
	int height;
	float [] Real;	//original image data
	float [] Imag;
	float [] Power;
	float PowerMax;
	boolean swapQu = true;
	int scaleValue = 255;
	boolean forward = true;
    
	public DFT2D(FloatProcessor ip){
		width = ip.getWidth();
		height = ip.getHeight();
		Real = (float[]) ip.getPixels();
		Imag = new float[width*height];  // values are zero
	}
    
	public DFT2D(FloatProcessor ip, boolean center){
		this(ip);
		swapQu = center;
	}
    
    public Object clone() {
        DFT2D xx = null;
        try{
            xx = (DFT2D)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return xx;
    }
	
	public void setForward(){
		forward = true;
	}
    
	public void setInverse(){
		forward = false;
	}
    
	public float[] getReal(){
		return Real;
	}
    
	public float[] getImag(){
		return Imag;
	}
    
	public float[] getPower(){
		return Power;
	}
    
	
	public void doDFT2D () {
		//rows:
		Complex[] row = Complex.makeComplexVector(width);
		Dft1d dftR = new Dft1d(width);
		for (int v=0; v<height; v++){
			getRow(v,row);
			Complex[] rowDft = dftR.DFT(row,forward);
			putRow(v,rowDft);
		}
		//columns:
		Complex[] col = Complex.makeComplexVector(height);
		Dft1d dftC = new Dft1d(height);
		for (int u=0; u<width; u++){
			getCol(u,col);
			Complex[] colDft = dftC.DFT(col,forward);
			putCol(u,colDft);
		}
	}
    public void inverse(){
        setInverse();
        //rows:
		Complex[] row = Complex.makeComplexVector(width);
		Dft1d dftR = new Dft1d(width);
		for (int v=0; v<height; v++){
			getRow(v,row);
			Complex[] rowDft = dftR.DFT(row,forward);
			putRow(v,rowDft);
		}
		//columns:
		Complex[] col = Complex.makeComplexVector(height);
		Dft1d dftC = new Dft1d(height);
		for (int u=0; u<width; u++){
			getCol(u,col);
			Complex[] colDft = dftC.DFT(col,forward);
			putCol(u,colDft);
		}
    }
    
	void getRow(int v, Complex[] rowC){
		int i = v*width; //index of row v
		for (int u=0; u<width; u++){
			rowC[u].re = Real[i+u];
			rowC[u].im = Imag[i+u];
		}
	}
    
	void putRow(int v, Complex[] rowC){
		int i = v*width; //index of row v
		for (int u=0; u<width; u++){
			Real[i+u] = (float) rowC[u].re;
			Imag[i+u] = (float) rowC[u].im;
		}
	}
    
	void getCol(int u, Complex[] rowC){
		for (int v=0; v<height; v++){
			rowC[v].re = Real[v*width+u];
			rowC[v].im = Imag[v*width+u];
		}
	}
    
	void putCol(int u, Complex[] rowC){
		for (int v=0; v<height; v++){
			Real[v*width+u] = (float) rowC[v].re;
			Imag[v*width+u] = (float) rowC[v].im;
		}
	}
    
    
	public void makePowerSpectrum(){
		//computes the power spectrum
		Power = new float[Real.length];
		PowerMax = 0.0f;
		for (int i=0; i<Real.length; i++){
			double a = Real[i];
			double b = Imag[i];
			float p = (float) Math.sqrt(a*a + b*b);
			Power[i] = p;
			if (p>PowerMax)
				PowerMax = p;
		}
	}
    
	public ByteProcessor makePowerImage(){
		ByteProcessor ip = new ByteProcessor(width,height);
		byte[] pixels = (byte[]) ip.getPixels();

		double max = Math.log(PowerMax+1.0);
		double scale = 1.0;
		if (scaleValue > 0)
			scale = scaleValue/max;
		for (int i=0; i<pixels.length; i++){
			double p = Power[i];
			if (p<0) p = -p;
			double plog = Math.log(p+1.0);
			int pint = (int)(plog * scale);
			pixels[i] = (byte) (0xFF & pint);
		}

		return ip;
	}
    
    public ByteProcessor makePowerImage2(){
		ByteProcessor ip = new ByteProcessor(width,height);
		byte[] pixels = (byte[]) ip.getPixels();
		
		for (int i=0; i<pixels.length; i++){
			double p = Power[i];
			if (p<0) p = -p;
			
			int pint = (int)(p);
			pixels[i] = (byte) (0xFF & pint);
		}

		return ip;
	}

    
	public void swapQuadrants (ImageProcessor ip) {

		ImageProcessor t1, t2;
		int w = ip.getWidth();
		int h = ip.getHeight();
		int w2 = w/2;
		int h2 = h/2;
        
		ip.setRoi(w2,0,w-w2,h2);
		t1 = ip.crop();
        
		ip.setRoi(0,h2,w2,h-h2);
		t2 = ip.crop();
        
		ip.insert(t1,0,h2);
		ip.insert(t2,w2,0);
        
		ip.setRoi(0,0,w2,h2);
		t1 = ip.crop();
        
		ip.setRoi(w2,h2,w-w2,h-h2);
		t2 = ip.crop();
        
		ip.insert(t1,w2,h2);
		ip.insert(t2,0,0);
	}
    
}


class Dft1d {
    
	double[] cosTable;
	double[] sinTable;
	Complex[] G;
    
	Dft1d(int M){
		makeCosTable(M);
		makeSinTable(M);
		G = new Complex[M];
		for (int i = 0; i < M; i++) {
			G[i] = new Complex(0,0);
		}
	}
    
	void makeCosTable(int M){
		cosTable = new double[M];
		for (int i=0; i<M; i++){
			cosTable[i]= Math.cos(2*Math.PI*i/M);
		}
	}
    
	void makeSinTable(int M){
		sinTable = new double[M];
		for (int i=0; i<M; i++){
			sinTable[i]= Math.sin(2*Math.PI*i/M);
		}
	}
    
	public Complex[] DFT(Complex[] g, boolean forward) {
		int M = g.length;
		double s = 1 / Math.sqrt(M);
		for (int u = 0; u < M; u++) {
			double sumRe = 0;
			double sumIm = 0;
			for (int m = 0; m < M; m++) {
				double gRe = g[m].re;
				double gIm = g[m].im;
				int k = (u * m) % M;
				double cosPhi = cosTable[k];
				double sinPhi = sinTable[k];
				if (forward)
					sinPhi = -sinPhi;

				sumRe += gRe * cosPhi - gIm * sinPhi;
				sumIm += gRe * sinPhi + gIm * cosPhi;
			}
			G[u].re = s * sumRe;
			G[u].im = s * sumIm;
		}
		return G;
	}
}


class Complex {
	public double re;
	public double im;
    
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}
    
	public static Complex[] makeComplexVector(int M) {
		Complex[] g = new Complex[M];
		for (int i = 0; i < M; i++) {
			g[i] = new Complex(0,0);
		}
		return g;
	}
    
	public static Complex[] makeComplexVector(double[] signal) {
		int M = signal.length;
		Complex[] g = new Complex[M];
		for (int i = 0; i < M; i++) {
			g[i] = new Complex(signal[i], 0);
		}
		return g;
	}
    
	static Complex[] makeComplexVector(double[] real, double[] imag) {
		int M = real.length;
		Complex[] g = new Complex[M];
		for (int i = 0; i < M; i++) {
			g[i] = new Complex(real[i], imag[i]);
		}
		return g;
	}
    
	public static void printComplexVector(Complex[] g, String title) {
		System.out.println("Printing " + title);
		for (int i = 0; i < g.length; i++) {
			if (g[i] == null)
				System.out.println(i + ": ******");
			else {
				double gr = g[i].re;
				double gi = g[i].im;
				gr = (Math.rint(gr * 1000) / 1000);
				gi = (Math.rint(gi * 1000) / 1000);
				if (gi >= 0)
					System.out.println(i + ": " + gr + " + " + Math.abs(gi) + "i");
				else
					System.out.println(i + ": " + gr + " - " + Math.abs(gi) + "i");
			}
		}
	}
}

class invDFT2D {
	int width;
	int height;
	float[] Real;
	float[] Imag;
	float[] Power;
	float PowerMax;
	boolean swapQu = false;
	int scaleValue = 255;	
	boolean forward = false;
	Complex [][] G ;
	
	public invDFT2D(FloatProcessor ip, Complex[][] Gl){
		width = ip.getWidth();
		height = ip.getHeight();
		G = Gl;
		Real = (float[]) ip.getPixels();
		Imag = new float[width*height];
		doDFT2D();
		makePowerSpectrum();
	}
	

	
	public void setForward(){
		forward = true;
	}
	
	public void setInverse(){
		forward = false;
	}
	
	public float[] getReal(){
		return Real;
	}
	
	public float[] getImag(){
		return Imag;
	}
	
	public float[] getPower(){
		return Power;
	}
	

	
	public void doDFT2D () {
		//rows:
		Complex[] row = Complex.makeComplexVector(width);
		Dft1d dftR = new Dft1d(width);
		for (int v=0; v<height; v++){
			getRow(v,row);
			Complex[] rowDft = dftR.DFT(row, forward);			
			putRow(v,rowDft);
		}
		//columns:
		Complex[] col = Complex.makeComplexVector(height);
		Dft1d dftC = new Dft1d(height);
		for (int u=0; u<width; u++){
			getCol(u,col);
			Complex[] colDft = dftC.DFT(col,forward);
			putCol(u,colDft);
		}
	}
	
	void getRow(int v, Complex[] rowC){
        //index of row v
		for (int u=0; u<width; u++){
			rowC[u].re = G[v][u].re;
			rowC[u].im = G[v][u].im;
		}
	}
	
	void putRow(int v, Complex[] rowC){
         //index of row v
		int i = v*width;
		for (int u=0; u<width; u++){
			Real[i+u] = (float) rowC[u].re;
			Imag[i+u] = (float) rowC[u].im;
		}
	}
	
	void getCol(int u, Complex[] rowC){
		for (int v=0; v<height; v++){
			rowC[v].re = G[v][u].re;
			rowC[v].im = G[v][u].im;
		}
	}
	
	void putCol(int u, Complex[] rowC){
		for (int v=0; v<height; v++){
			Real[v*width+u] = (float) rowC[v].re;
			Imag[v*width+u] = (float) rowC[v].im;
		}
	}
	
	
	void makePowerSpectrum(){
		Power = new float[Real.length];
		PowerMax = 0.0f;
		for (int i=0; i<Real.length; i++){
			double a = Real[i];
			double b = Imag[i];
			float p = (float) Math.sqrt(a*a + b*b);
			Power[i] = p;
			if (p>PowerMax)
				PowerMax = p;
		}
	}
	
	public ByteProcessor makePowerImage(){
		ByteProcessor ip = new ByteProcessor(width,height);
		byte[] pixels = (byte[]) ip.getPixels();

		double max = Math.log(PowerMax+1.0);
		double scale = 1.0;
		if (scaleValue > 0)
			scale = scaleValue/max;
		for (int i=0; i<pixels.length; i++){
			double p = Power[i];
			if (p<0) p = -p;
			double plog = Math.log(p+1.0);
			int pint = (int)(plog * scale);
			pixels[i] = (byte) (0xFF & pint);
		}
		if (swapQu) swapQuadrants(ip);
		return ip;
	}

	public void swapQuadrants (ImageProcessor ip) {

		ImageProcessor t1, t2;
		int w = ip.getWidth();
		int h = ip.getHeight();
		int w2 = w/2;
		int h2 = h/2;
		
		ip.setRoi(w2,0,w-w2,h2);
		t1 = ip.crop();
		
		ip.setRoi(0,h2,w2,h-h2);
		t2 = ip.crop();
		
		ip.insert(t1,0,h2);
		ip.insert(t2,w2,0);
		
		ip.setRoi(0,0,w2,h2);
		t1 = ip.crop();
		
		ip.setRoi(w2,h2,w-w2,h-h2); 
		t2 = ip.crop();
		
		ip.insert(t1,w2,h2);
		ip.insert(t2,0,0);
	}

}
