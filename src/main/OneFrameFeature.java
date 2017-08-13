package main;

/**
 * Feature extracted from  OneFrame
 * this class serves as a data structure, not an OO class.
 * @author 
 *
 */
public class OneFrameFeature {
	
	int featureDim;
	float xAxisF;
	float yAxisF;
	float zAxisF;
	float dis3D;
	float xVelF;
	float yVelF;
	float zVelF;
	float xDirF;
	float yDirF;
	float zDirF;
	float xAccF;
	float yAccF;
	float zAccF;
	float FingerLenth;
	float FingerWidth;	
	float yxSlopeAngleF;
	float zxSlopeAngleF;
	float threeDimAngleF;
	float logCurv;
	
	public OneFrameFeature(){
		this.featureDim = 19;
	}
	
	
	@Override
	public String toString(){
		String s = "";
		// to-do:
		s = 	xAxisF +", " + yAxisF +", " + zAxisF +", " + dis3D
				+", " + xVelF +", " + yVelF +", " + zVelF
				+", " + xAccF +", " + yAccF +", " + zAccF
				+", " + xDirF +", " + yDirF +", " + zDirF
				+", " + FingerLenth +", " + FingerWidth
				+", " + yxSlopeAngleF +", " + zxSlopeAngleF +", " + threeDimAngleF +", " 
				+ logCurv;
		return s;
		
	}

}
