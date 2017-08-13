package main;
import java.util.ArrayList;
import java.util.List;

public class OneGesture {

	List<OneHandFrame> handFrameList;	
	List<OneHandFeature> handFeatureList;
	static float[][] handFeatureNormed;
	public OneGesture(List<OneHandFrame> handFrameList){
		this.handFrameList = handFrameList;
		handFeatureList = new ArrayList<OneHandFeature>();
	}
	public OneGesture(){
		//Initialize 
		handFrameList = new ArrayList<OneHandFrame>();
		handFeatureList = new ArrayList<OneHandFeature>();


		//add frame data into the handFrame	

	}

	public float[][] getNormedFeature(){
		return calFeature();
		//this.handFeatureNormed;
	}




	public List<OneFrame> preProcess(ArrayList<OneFrame> sig){
		//to-do:
		//preprocessing		
		return sig;

	}

	/**
	 * calFeature from OneFrameFeaure 
	 * store the calculated and normed signature into sigFeatureNormed
	 */
	public float[][] calFeature(){
		float disXPrevious = (float) 0.00001;
		float disYPrevious = (float) 0.00001;
		float disZPrevious = (float) 0.00001;
		float dis3DPrevious = (float) 0.00001;
		// careful about the out of bound exception
		for (int i = 5; i<this.handFrameList.size() - 5; i++){
			
			OneHandFeature feature = new OneHandFeature(); // a temp feature at a single point.
			//hand
			feature.isLeft = handFrameList.get(i).isLeft;			
			feature.grabStrength = handFrameList.get(i).grabStrength;
			feature.pinchStregth = handFrameList.get(i).pinchStregth;
			feature.pitch = handFrameList.get(i).pitch;
			feature.roll = handFrameList.get(i).roll;
			feature.yaw = handFrameList.get(i).yaw;
			feature.widthP = handFrameList.get(i).widthP;
			feature.xAxisP = handFrameList.get(i).xAxisP;
			feature.yAxisP = handFrameList.get(i).yAxisP;
			feature.zAxisP = handFrameList.get(i).zAxisP;
			feature.armX = handFrameList.get(i).armX;
			feature.armY = handFrameList.get(i).armY;
			feature.armZ = handFrameList.get(i).armZ;
			feature.wristX = handFrameList.get(i).wristX;
			feature.wristY = handFrameList.get(i).wristY;
			feature.wristZ = handFrameList.get(i).wristZ;
			//gesture
			feature.gestureType[0] = handFrameList.get(i).gestureType[0];
			feature.gestureType[1] = handFrameList.get(i).gestureType[1];
			feature.gestureType[2] = handFrameList.get(i).gestureType[2];
			feature.gestureType[3] = handFrameList.get(i).gestureType[3];
			//finger
			for (int j = 0; j < 5; j++){
				feature.xAxis[j] = handFrameList.get(i).xAxis[j];
				feature.yAxis[j] = handFrameList.get(i).yAxis[j];
				feature.zAxis[j] = handFrameList.get(i).zAxis[j];

				feature.xVel[j] = handFrameList.get(i).xVel[j];
				feature.yVel[j] = handFrameList.get(i).yVel[j];
				feature.zVel[j] = handFrameList.get(i).zVel[j];

				feature.xDir[j] = handFrameList.get(i).xDir[j];
				feature.yDir[j] = handFrameList.get(i).yDir[j];
				feature.zDir[j] = handFrameList.get(i).zDir[j];

				feature.FingerLenth[j] = handFrameList.get(i).FingerLenth[j];
				feature.FingerWidth[j] = handFrameList.get(i).FingerWidth[j];

				float disX = handFrameList.get(i).xAxis[j] -  handFrameList.get(i-1).xAxis[j];
				float disY = handFrameList.get(i).yAxis[j] -  handFrameList.get(i-1).yAxis[j];
				float disZ = handFrameList.get(i).zAxis[j] -  handFrameList.get(i-1).zAxis[j];
				
				float dis3D = (float) Math.sqrt(disX * disX + disY * disY + disZ * disZ);


				float dis3D2step = (float) Math.sqrt((disX+disXPrevious) * (disX + disXPrevious) + 
						(disY + disYPrevious) * (disY + disYPrevious) + (disZ + disZPrevious) * (disZ + disZPrevious));

				disXPrevious = disX;
				disYPrevious = disY;
				disZPrevious = disZ;
				feature.dis3D[j] = dis3D;
				feature.xAcc[j] = handFrameList.get(i).xVel[j] -  handFrameList.get(i-1).xVel[j];
				feature.yAcc[j] = handFrameList.get(i).yVel[j] -  handFrameList.get(i-1).yVel[j];
				feature.zAcc[j] = handFrameList.get(i).zVel[j] -  handFrameList.get(i-1).zVel[j];


				// calculate the arctan(y'/x'), arctan(z'/x')
				if (disX != 0) {
					feature.yxSlopeAngle[j] = (float) Math.atan(disY / disX);
					feature.zxSlopeAngle[j] = (float) Math.atan(disZ / disX);
				} else {
					feature.yxSlopeAngle[j] = 0;
					feature.zxSlopeAngle[j] = 0;

				}
				if (Float.isNaN(feature.yxSlopeAngle[j])){
					feature.yxSlopeAngle[j] = (float) 0.00001;
				}
				if (Float.isNaN(feature.zxSlopeAngle[j])){
					feature.zxSlopeAngle[j] = (float) 0.00001;
				}


				//calculate the angle in 3D
				//d(1,k)^2+d(1,k+1)^2-d2(1,k+1)^2)/(2*d(1,k)*d(1,k+1)
				float temp;
				temp = (dis3DPrevious * dis3DPrevious + dis3D * dis3D - dis3D2step * dis3D2step) / (2 * dis3DPrevious * dis3D);

				if (temp > 1){
					feature.threeDimAngle[j] = 1;
				} else if (temp < -1){
					feature.threeDimAngle[j] = -1;
				} else {
					feature.threeDimAngle[j] = (float) Math.acos(temp);
				}
				if (Float.isNaN(feature.threeDimAngle[j])){
					feature.threeDimAngle[j] = (float) 0.00001;
				}
				//	 			calculate the curvature in 3D
				float curvTemp = (float) (Math.sqrt(Math.pow((feature.zAcc[j] * feature.yVel[j] - feature.yAcc[j] * feature.zVel[j]),2) +
						Math.pow((feature.xAcc[j] * feature.zVel[j] - feature.zAcc[j] * feature.xVel[j]),2) +
						Math.pow((feature.yAcc[j] * feature.xVel[j] - feature.xAcc[j] * feature.yVel[j]),2))/
						Math.pow((feature.xVel[j] * feature.xVel[j] + feature.yVel[j] * feature.yVel[j] + feature.zVel[j] * feature.zVel[j]), 1.5));
				if  (curvTemp < 0 || curvTemp - 0 < 0.000001)
					curvTemp = (float) 0.0001;

				feature.logCurv[j] = (float) Math.log10(1/curvTemp);
				if (Float.isNaN(feature.logCurv[j])){
					feature.logCurv[j] = (float) 0.00001;
				}
				dis3DPrevious = dis3D;

				this.handFeatureList.add(feature); // add feature;
			}

		} // end for loop --a whole signature
		

		return dataNorm(handFeatureList); // normalize feature;


	}// end method calFeature

	/**
	 * Feature Normalization
	 * @param sigFeature
	 * @return an two dimensional array with #Frames X #features dimension
	 */

	private float[][] dataNorm(List<OneHandFeature> handFeature){

		int sizeSig = handFeature.size();
		int featureDim = handFeature.get(0).handFeatureDim;

		float[][] tempArray = new float[sizeSig][featureDim]; 
		for (int i = 0; i < sizeSig; i++){

			// hand
			tempArray[i][0] = handFeature.get(i).isLeft;
			tempArray[i][1] = handFeature.get(i).grabStrength;
			tempArray[i][2] = handFeature.get(i).pinchStregth;
			tempArray[i][3] = handFeature.get(i).pitch;
			tempArray[i][4] = handFeature.get(i).roll;
			tempArray[i][5] = handFeature.get(i).yaw;
			tempArray[i][6] = handFeature.get(i).widthP;
			tempArray[i][7] = handFeature.get(i).xAxisP;
			tempArray[i][8] = handFeature.get(i).yAxisP;
			tempArray[i][9] = handFeature.get(i).zAxisP;
			tempArray[i][10] = handFeature.get(i).armX;
			tempArray[i][11] = handFeature.get(i).armY;
			tempArray[i][12] = handFeature.get(i).armZ;
			tempArray[i][13] = handFeature.get(i).wristX;
			tempArray[i][14] = handFeature.get(i).wristY;
			tempArray[i][15] = handFeature.get(i).wristZ;
			//gesture
			tempArray[i][16] = handFeature.get(i).gestureType[0];
			tempArray[i][17] = handFeature.get(i).gestureType[1];
			tempArray[i][18] = handFeature.get(i).gestureType[2];
			tempArray[i][19] = handFeature.get(i).gestureType[3];

			// finger
			for (int j = 0; j < 5; j++){
				tempArray[i][19*j + 20 + 0] = handFeature.get(i).xAxis[j];
				tempArray[i][19*j + 20 + 1] = handFeature.get(i).yAxis[j];
				tempArray[i][19*j + 20 + 2] = handFeature.get(i).zAxis[j];
				tempArray[i][19*j + 20 + 3] = handFeature.get(i).dis3D[j];

				tempArray[i][19*j + 20 + 4] = handFeature.get(i).xVel[j];
				tempArray[i][19*j + 20 + 5] = handFeature.get(i).yVel[j];
				tempArray[i][19*j + 20 + 6] = handFeature.get(i).zVel[j];
				tempArray[i][19*j + 20 + 7] = handFeature.get(i).xAcc[j];
				tempArray[i][19*j + 20 + 8] = handFeature.get(i).yAcc[j];
				tempArray[i][19*j + 20 + 9] = handFeature.get(i).zAcc[j];
				tempArray[i][19*j + 20 + 10] = handFeature.get(i).xDir[j];
				tempArray[i][19*j + 20 + 11] = handFeature.get(i).yDir[j];
				tempArray[i][19*j + 20 + 12] = handFeature.get(i).zDir[j];
				tempArray[i][19*j + 20 + 13] = handFeature.get(i).FingerLenth[j];
				tempArray[i][19*j + 20 + 14] = handFeature.get(i).FingerWidth[j];
				tempArray[i][19*j + 20 + 15] = handFeature.get(i).yxSlopeAngle[j];
				tempArray[i][19*j + 20 + 16] = handFeature.get(i).zxSlopeAngle[j];
				tempArray[i][19*j + 20 + 17] = handFeature.get(i).threeDimAngle[j];
				tempArray[i][19*j + 20 + 18] = handFeature.get(i).logCurv[j];



			}

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
				if ((j != 0) && (j != 1) && (j != 2) && (j != 16) && (j != 17) && (j != 18) && (j != 19)){
					tempArray[i][j] = (tempArray[i][j] - total[j]) / totalSTD[j];
				}
			} //end i
		
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
		float dtwDis =  0;

		DTW dtw = new DTW(handFeatureNormed, anotherSig);
		dtwDis = dtw.dpDistance();		
		return dtwDis;

	}

	/**
	 * 
	 * @return raw data in String format
	 */
	public String toStringFrame(){
		String s = "";
		for (int i = 0; i< this.handFrameList.size(); i++){
		//for (OneHandFrame frames: this.handFrameList) {
			s += this.handFrameList.get(i).toString();
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
		for (OneHandFeature features: this.handFeatureList) {
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

		handFeatureNormed = getNormedFeature();

		String s = "";
		// to-do:
		//sigFeatureNormed
		for (int i = 0; i < handFeatureNormed.length; i++){
			String sTemp = "";
			for (int j = 0; j< handFeatureNormed[i].length; j++){

				sTemp +=  handFeatureNormed[i][j];
				sTemp += ", ";
			}
			s += sTemp;
			s += "\n";
		}		

		return s;
	}

}// class


