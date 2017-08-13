package main;

import com.leapmotion.leap.*;


/**
 * raw data from the leap
 * this class serves as a data structure, not an OO class.
 * @author 
 *
 */
public class OneFrame {
	long frameID;
	long fingerID;
	long timeStamp;
	float recordSpeed;
	float xAxis, yAxis ,zAxis ;
	float xVel, yVel, zVel;
	float xDir, yDir, zDir;
	float FingerLenth;
	float FingerWidth;
	
	
	public OneFrame(Finger f,long frameID, long timeStamp, float recordSpeed ){
		
		this.frameID = frameID;
		this.fingerID = f.id();
		this.timeStamp = timeStamp;
		this.recordSpeed = recordSpeed;
		
		this.xAxis = f.tipPosition().getX();
		this.yAxis = f.tipPosition().getY();
		this.zAxis = f.tipPosition().getZ();

		this.xVel = f.tipVelocity().getX();
		this.yVel = f.tipVelocity().getY();
		this.zVel = f.tipVelocity().getZ();

		this.xDir = f.direction().getX();
		this.yDir = f.direction().getY();
		this.zDir = f.direction().getZ();
		
		this.FingerLenth = f.length();
		this.FingerWidth = f.width();
		

	}
	
	
	@Override
	public String toString(){
		String s = "";
		
		s = timeStamp + ", " +frameID + ", " +fingerID + ", " +recordSpeed + ", " + 
		xAxis + ", " +yAxis + ", " +zAxis + ", " +
		xVel + ", " +yVel + ", " +zVel + ", " +
		xDir + ", " +yDir + ", " +zDir + ", " +
		FingerLenth  + ", " + FingerWidth;
		
		return s;
	}

}
