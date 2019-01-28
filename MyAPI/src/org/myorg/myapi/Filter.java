package org.myorg.myapi;

public class Filter{
	private boolean bandpass;//true if bandpass, false if bandcut
	private double low,
		lowCorner,
		high,
		highCorner;
		Filter(){
			bandpass = true;
			low = 0;
			lowCorner = 0;
			high = Double.POSITIVE_INFINITY;
			highCorner = Double.POSITIVE_INFINITY;
		}
		Filter(double l, double lc, double hc, double h, boolean bp){
			
			low = l;
			lowCorner = lc;
			highCorner = hc;
			high = h;
			bandpass = bp;
		}
		public boolean isBandpass(){
			return bandpass;
		}
		public void setBandpass(boolean b){
			bandpass = b;
		}
		
		public double getLow(){
			return low;
		}
		public double getLowCorner(){
			return lowCorner;
		}
		public double getHighCorner(){
			return highCorner;
		}
		public double getHigh(){
			return high;
		}
		public double[] getFilterArray(double sampleRate, int length){
			double[] fa = new double[length];
			//make sure values are sane
			if(low > sampleRate) low = sampleRate;
			if(lowCorner > sampleRate) lowCorner = sampleRate;
			if(highCorner > sampleRate) highCorner = sampleRate;
			if(high > sampleRate) high = sampleRate;
			if(low < 0) low = 0;
			if(lowCorner < 0) lowCorner = 0;
			if(highCorner < 0) highCorner = 0;
			if(high < 0) high = 0;
			if(low > lowCorner) low = lowCorner;
			if(highCorner < lowCorner) highCorner = lowCorner;
			if(high < highCorner) high = highCorner;
			//first convert frequencies to indices
			int lowIndex = (int) (low/sampleRate*length);
			int lowCornerIndex = (int) (lowCorner/sampleRate*length);
			int highCornerIndex = (int) (highCorner/sampleRate*length);
			int highIndex = (int) (high/sampleRate*length);
			//System.out.println(lowIndex+"    "+lowCornerIndex+"    "+highCornerIndex+"    "+highIndex);
			//now construct below-band portion
			for (int i = 0; i < lowIndex; i++){
				if(bandpass) fa[i] = 0;
				else fa[i] = 1;
			}
			//lower ramp
			if (lowCornerIndex != lowIndex) {
				double slope = 1.0/(lowCornerIndex - lowIndex);
				//System.out.println(slope);
				for (int i = lowIndex; i < lowCornerIndex; i++){
					if(bandpass) fa[i] = slope*(i-lowIndex);
					else fa[i] = 1-(slope*(i-lowIndex));
				}
			}
			//in-band portion
			for(int i = lowCornerIndex; i < highCornerIndex; i++){
				if(bandpass) fa[i] = 1;
				else fa[i]=0;
			}
			//high ramp
			if (highCornerIndex != highIndex) {
				double slope = 1.0/(highIndex - highCornerIndex);
				//System.out.println(slope);
				for (int i = highCornerIndex; i < highIndex; i++){
					if(bandpass) fa[i] = 1-(slope*(i-highCornerIndex));
					else fa[i] = slope*(i-highCornerIndex);
				}
			}
			//above-band portion
			for (int i = highIndex; i < length; i++){
				if(bandpass) fa[i] = 0;
				else fa[i] = 1;
			}
			//for(int i = 0; i < length; i++){
			//	System.out.println(i+"    "+fa[i]);
			//}
			return fa;
		}
				
		public void setLow(double d){
			low = d;
		}
		public void setLowCorner(double d){
			lowCorner = d;
		}
		public void setHighCorner(double d){
			highCorner = d;
		}
		public void setHigh(double d){
			highCorner = d;
		}
}
