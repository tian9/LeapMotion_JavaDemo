package main;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;


public class OneHandFeature extends OneHandFrame{
	
	int handFeatureDim = 115; 

	float[] dis3D;
	float[] xAcc, yAcc, zAcc;
	float[] yxSlopeAngle;
	float[] zxSlopeAngle;
	float[] threeDimAngle;
	float[] logCurv;

	public OneHandFeature(){
		super();
		dis3D = new float[5]; 
		xAcc = new float[5]; yAcc = new float[5];  zAcc = new float[5]; 
		yxSlopeAngle = new float[5]; 
		zxSlopeAngle = new float[5]; 
		threeDimAngle = new float[5]; 
		logCurv = new float[5]; 
	}
	
	public OneHandFeature(Hand hand, GestureList gestures){
		super(hand,gestures);
	}

	public OneHandFeature(Hand hand, long frameID, long timeStamp,
			GestureList gestures) {
		super(hand, frameID, timeStamp, gestures);
	}


	public String toString(){
		String s1 = "";
		s1 = isLeft + ", " + grabStrength + ", " + pinchStregth + ", " + pitch + ", " + roll + ", " + yaw
				+ ", " + widthP + ", " + xAxisP + ", " + yAxisP + ", " + zAxisP + ", " + armX + ", " + armY
				+ ", " + armZ + ", " + wristX + ", " + wristY+ ", " + wristZ
				+ ", " + gestureType[0] + ", " + gestureType[1] + ", " + gestureType[2] + ", " + gestureType[3];
		for (int j = 0; j < 5; j++){
			s1 = s1 + ", " + xAxis[j] + ", " + yAxis[j] + ", " + zAxis[j] + ", " + xVel[j] + ", " + yVel[j]
					+ ", " + zVel[j] + ", " + xDir[j] + ", " +  yDir[j] + ", " + zDir[j]
							+ ", " + FingerLenth[j] + ", " + FingerWidth[j]
									+ ", " +  dis3D[4] + ", " + xAcc[4] + ", " +  yAcc[4] + ", " + zAcc[4]
											+ ", " + yxSlopeAngle[4] + ", " +  zxSlopeAngle[4] + ", " + threeDimAngle[4]
													+ ", " + logCurv[4]; 	
		}
		return s1;

	}// toStirng


}
