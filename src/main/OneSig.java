package main;
import java.util.ArrayList;

/**
 * 
 * @author 
 *
 */
public class OneSig {

	ArrayList<OneFrame> sigFrame;	
	ArrayList<OneFrameFeature> sigFeature;
	float[][] sigFeatureNormed;
	
	public OneSig(){
		//Initialize 
		sigFrame = new ArrayList<OneFrame>();
		sigFeature = new ArrayList<OneFrameFeature>();
		
		
		//add frame data into the sigFrame	
		
	}
	
	public float[][] getNormedFeature(){
		calFeature();
		return this.sigFeatureNormed;
	}
	
	
	
	
	public ArrayList<OneFrame> preProcess(ArrayList<OneFrame> sig){
		//to-do:
		//preprocessing		
		return sig;
		
	}

	/**
	 * calFeature from OneFrameFeaure 
	 * store the calculated and normed signature into sigFeatureNormed
	 */
	public void calFeature(){
		
		// careful about the out of bound 
		for (int i = 5; i<this.sigFrame.size() - 5; i++){
			OneFrameFeature feature = new OneFrameFeature(); // a temp feature at a single point.

			
			 feature.xAxisF = sigFrame.get(i).xAxis;
			 //System.out.println( feature.xAxisF +"__if equals to frame___"+ sigFrame.get(i).xAxis );
			 feature.yAxisF = sigFrame.get(i).yAxis;
			 feature.zAxisF = sigFrame.get(i).zAxis;
			 
			 feature.xVelF = sigFrame.get(i).xVel;
			 feature.yVelF = sigFrame.get(i).yVel;
			 feature.zVelF = sigFrame.get(i).zVel;
			 
			 feature.xDirF = sigFrame.get(i).xDir;
			 feature.yDirF = sigFrame.get(i).yDir;
			 feature.zDirF = sigFrame.get(i).zDir;
			 
			 feature.FingerLenth = sigFrame.get(i).FingerLenth;
			 feature.FingerWidth = sigFrame.get(i).FingerWidth;
			
			float disX = sigFrame.get(i).xAxis -  sigFrame.get(i-1).xAxis;
			float disY = sigFrame.get(i).yAxis -  sigFrame.get(i-1).yAxis;
			float disZ = sigFrame.get(i).zAxis -  sigFrame.get(i-1).zAxis;
			float disXPrevious = sigFrame.get(i-1).xAxis -  sigFrame.get(i-2).xAxis;
			float disYPrevious = sigFrame.get(i-1).yAxis -  sigFrame.get(i-2).yAxis;
			float disZPrevious = sigFrame.get(i-1).zAxis -  sigFrame.get(i-2).zAxis;
			
			float dis3D = (float) Math.sqrt(disX * disX + disY * disY + disZ * disZ);
			
			float dis3DPrevious = (float) Math.sqrt(disXPrevious * disXPrevious
					+ disYPrevious * disYPrevious + disZPrevious * disZPrevious);

			float dis3D2step = (float) Math.sqrt((disX+disXPrevious) * (disX + disXPrevious) + 
					(disY + disYPrevious) * (disY + disYPrevious) + (disZ + disZPrevious) * (disZ + disZPrevious));
			
			
			feature.dis3D = dis3D;
			feature.xAccF = sigFrame.get(i).xVel -  sigFrame.get(i-1).xVel;
			feature.yAccF = sigFrame.get(i).yVel -  sigFrame.get(i-1).yVel;
			feature.zAccF = sigFrame.get(i).zVel -  sigFrame.get(i-1).zVel;
			
			
			// calculate the arctan(y'/x'), arctan(z'/x')
			if (disX != 0) {
			feature.yxSlopeAngleF = (float) Math.atan(disY / disX);
			feature.zxSlopeAngleF = (float) Math.atan(disZ / disX);
			} else {
				feature.yxSlopeAngleF = 0;
				feature.zxSlopeAngleF = 0;
			}
			
			if (feature.yxSlopeAngleF == Double.NaN){
				feature.yxSlopeAngleF = 0;
			}
			if (feature.zxSlopeAngleF == Double.NaN){
				feature.zxSlopeAngleF = 0;
			}
			
			
			//calculate the angle in 3D
			//d(1,k)^2+d(1,k+1)^2-d2(1,k+1)^2)/(2*d(1,k)*d(1,k+1)
			float temp;
				temp = (dis3DPrevious * dis3DPrevious + dis3D * dis3D - dis3D2step * dis3D2step) / (2 * dis3DPrevious * dis3D);
			
			if (temp > 1){
				feature.threeDimAngleF = 1;
			} else if (temp < -1){
				feature.threeDimAngleF = -1;
			} else {
			feature.threeDimAngleF = (float) Math.acos(temp);
			}
			
			if (feature.threeDimAngleF == Double.NaN){
				feature.threeDimAngleF = 0;
			}
			
			float curvTemp = (float) (Math.sqrt(Math.pow((feature.zAccF * feature.yVelF - feature.yAccF * feature.zVelF),2) +
							  Math.pow((feature.xAccF * feature.zVelF - feature.zAccF * feature.xVelF),2) +
							 Math.pow((feature.yAccF * feature.xVelF - feature.xAccF * feature.yVelF),2))/
							 Math.pow((feature.xVelF * feature.xVelF + feature.yVelF * feature.yVelF + feature.zVelF * feature.zVelF), 1.5));
			if  (curvTemp < 0 || curvTemp == 0)
				curvTemp = (float) 0.0001;
			
			feature.logCurv = (float) Math.log10(1/curvTemp);
			
			if (feature.logCurv == Double.NaN){
				feature.logCurv = 0;
			}
			
			
			this.sigFeature.add(feature); // add feature;
			
		} // end for loop --a whole signature
		
		this.sigFeatureNormed  = dataNorm(sigFeature); // normalize feature;
		
		
	}// end method calFeature
	
	/**
	 * Feature Normalization
	 * @param sigFeature
	 * @return an two dimensional array with #Frames X #features dimension
	 */
	
	private float[][] dataNorm(ArrayList<OneFrameFeature> sigFeature){
		
		int sizeSig = sigFeature.size();
		int featureDim = sigFeature.get(0).featureDim;
		
		float[][] tempArray = new float[sizeSig][featureDim]; 
		
		for (int i = 0; i < sizeSig; i++){
			tempArray[i][0] = sigFeature.get(i).xAxisF;
			tempArray[i][1] = sigFeature.get(i).yAxisF;
			tempArray[i][2] = sigFeature.get(i).zAxisF;
			tempArray[i][3] = sigFeature.get(i).dis3D;

			tempArray[i][4] = sigFeature.get(i).xVelF;
			tempArray[i][5] = sigFeature.get(i).yVelF;
			tempArray[i][6] = sigFeature.get(i).zVelF;
			tempArray[i][7] = sigFeature.get(i).xAccF;
			tempArray[i][8] = sigFeature.get(i).yAccF;
			tempArray[i][9] = sigFeature.get(i).zAccF;
			tempArray[i][10] = sigFeature.get(i).xDirF;
			tempArray[i][11] = sigFeature.get(i).yDirF;
			tempArray[i][12] = sigFeature.get(i).zDirF;
			
			tempArray[i][13] = sigFeature.get(i).yxSlopeAngleF;
			tempArray[i][14] = sigFeature.get(i).zxSlopeAngleF;
			tempArray[i][15] = sigFeature.get(i).threeDimAngleF;
			tempArray[i][16] = sigFeature.get(i).logCurv;
			
			tempArray[i][17] = sigFeature.get(i).FingerLenth;
			tempArray[i][18] = sigFeature.get(i).FingerWidth;
		}
		
		float[] total = new float[featureDim];  // average
		float[] totalSTD = new float[featureDim];  //std
		
		for (int j = 0; j<featureDim; j++){			
			total[j] = 0;
			for (int i = 0;i<sizeSig; i++){
				total[j] =total[j] + tempArray[i][j];

			}
			total[j] = total[j] / sizeSig;	
			
			for (int i = 0;i<sizeSig; i++){
				float temp = (float) Math.pow(tempArray[i][j] - total[j], 2);
				totalSTD[j] = totalSTD[j] + temp;
			}
			
			totalSTD[j] = (float) Math.sqrt(totalSTD[j] / sizeSig);
			
			for (int i = 0;i<sizeSig; i++){

				tempArray[i][j] = (tempArray[i][j] - total[j]) / totalSTD[j];
			}
			
		} // end j: featureDim
		return tempArray;
		
	}// end method calFeature()
		
	/**
	 * 	
	 * @param anotherSig
	 * @return DTW distance of this signature and another signature
	 */
	public float calDTW(float[][] anotherSig){
		//to-do:
		//calculate DTW disthance between this signature and another signature
		
		DTW dtw = new DTW(sigFeatureNormed, anotherSig);
		return dtw.costValue;
		
	}
	
	/**
	 * 
	 * @return raw data in String format
	 */
	public String toStringFrame(){
		String s = "";
		for (int i  = 0; i < this.sigFrame.size(); i++){
		//for (OneFrame frames: this.sigFrame) {
			s += this.sigFrame.get(i).toString();
			s += "\n";
		}
		
		return s;
	}
	
	/**
	 * 
	 * @return feature in String format
	 */
	public String toStringFeature(){
		String s = "";
		for (OneFrameFeature features: this.sigFeature) {
			s += features.toString();
			s += "\n";
		}
		
		return s;
	}
	
	/**
	 * 
	 * @return Normalized feature in String format
	 */
	public String toStringNormedFeature(){
	
		sigFeatureNormed = getNormedFeature();
		
		String s = "";
		// to-do:
		//sigFeatureNormed
		for (int i = 0; i < sigFeatureNormed.length; i++){
			String sTemp = "";
			for (int j = 0; j< sigFeatureNormed[i].length; j++){
				
				sTemp +=  sigFeatureNormed[i][j];
				sTemp += ", ";
			}
			s += sTemp;
			s += "\n";
		}		
		
		return s;
	}
		
}// class
