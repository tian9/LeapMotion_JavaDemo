import com.leapmotion.leap.Arm;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.ScreenTapGesture;
import com.leapmotion.leap.SwipeGesture;
import com.leapmotion.leap.Vector;


public class OneHandFrame {
	long frameID; 
	long timeStamp;
	long handID;

	float isLeft;
	float grabStrength;
	float pinchStregth;
	float pitch;
	float roll;
	float yaw;

	float widthP;
	float xAxisP;
	float yAxisP;
	float zAxisP;

	float armX;
	float armY;
	float armZ;

	float wristX;
	float wristY;
	float wristZ;

	//finger
	float[] xAxis, yAxis ,zAxis ;
	float[] xVel, yVel, zVel;
	float[] xDir, yDir, zDir;
	float[] FingerLenth;
	float[] FingerWidth;

	//gesture 
	float[] gestureType;


	String s = "";

	public OneHandFrame(){
		xAxis = new float[5]; yAxis = new float[5]; zAxis = new float[5]; 
		xVel = new float[5]; yVel = new float[5];  zVel = new float[5]; 
		xDir = new float[5];  yDir = new float[5];  zDir = new float[5]; 
		FingerLenth = new float[5]; 
		FingerWidth = new float[5]; 
		gestureType = new float[5]; 
	}
	
	public OneHandFrame(Hand hand,long frameID, long timeStamp, GestureList gestures){


		/**
		 * each field
		 */

		this.frameID = frameID; 
		this.timeStamp = timeStamp;
		handID = hand.id();

		boolean isLeftB = hand.isLeft();
		if (isLeftB) isLeft = 1; else isLeft = 0;

		grabStrength = hand.grabStrength();
		pinchStregth = hand.pinchStrength();
		pitch = hand.direction().pitch();
		roll = hand.palmNormal().roll();
		yaw = hand.direction().yaw();

		widthP = hand.palmWidth();
		xAxisP = hand.palmPosition().getX();
		yAxisP = hand.palmPosition().getY();
		zAxisP = hand.palmPosition().getZ();

		Vector armDir = hand.arm().direction();
		armX = armDir.getX();
		armY = armDir.getY();
		armZ = armDir.getZ();

		Vector wrist = hand.arm().wristPosition();
		wristX = wrist.getX();
		wristY = wrist.getY();
		wristZ = wrist.getZ();

		//finger

		xAxis = new float[5];
		yAxis = new float[5];
		zAxis = new float[5];
		xVel = new float[5]; 
		yVel = new float[5]; 
		zVel = new float[5];
		xDir = new float[5]; 
		yDir = new float[5]; 
		zDir = new float[5];
		FingerLenth = new float[5];
		FingerWidth = new float[5];

		int fC = 0;
		for (Finger f : hand.fingers()) {

			this.xAxis[fC] = f.tipPosition().getX();
			this.yAxis[fC] = f.tipPosition().getY();
			this.zAxis[fC] = f.tipPosition().getZ();

			this.xVel[fC] = f.tipVelocity().getX();
			this.yVel[fC] = f.tipVelocity().getY();
			this.zVel[fC] = f.tipVelocity().getZ();

			this.xDir[fC] = f.direction().getX();
			this.yDir[fC] = f.direction().getY();
			this.zDir[fC] = f.direction().getZ();

			this.FingerLenth[fC] = f.length();
			this.FingerWidth[fC] = f.width();
			fC++;
		}

		//Gestures
		gestureType = new float[4]; 
		//float[] gestureDuration = new float[4];
		for (int i = 0; i < gestureType.length; i++){
			gestureType[i] = 0;
		}

		if (gestures != null){
			for (Gesture g: gestures){
				// 1 for circle; 2 for keyTap; 3 for screenTap; 4 for swipe;
				switch (g.type()) {
				case TYPE_CIRCLE:
					//Handle circle gestures
					gestureType[0] = 1;
					//gestureDuration[0] = g.duration();
					break;
				case TYPE_KEY_TAP:
					//Handle key tap gestures
					gestureType[1] = 1;
					//gestureDuration[1] = g.duration();
					break;
				case TYPE_SCREEN_TAP:
					//Handle screen tap gestures
					gestureType[2] = 1;
					//gestureDuration[2] = g.duration();
					break;
				case TYPE_SWIPE:
					//Handle swipe gestures
					gestureType[3] = 1;
					//gestureDuration[3] = g.duration();
					break;
				default:
					//Handle unrecognized gestures
					break;
				}
			}
		} //if

	} // constructor

	public  OneHandFrame(Hand hand, GestureList gestures){

		String handType = hand.isLeft() ? "Left hand" : "Right hand";
		s = s + "  " + handType + ", id: " + hand.id()
				+ ", palm position: " + hand.palmPosition()
				+ ", palm width: " + hand.palmWidth()
				+ ", grab strength: " + hand.grabStrength()
				+ ", pinch strength: " + hand.pinchStrength() 
				+ "\n";

		// Get the hand's normal vector and direction
		Vector normal = hand.palmNormal();
		Vector direction = hand.direction();

		// Calculate the hand's pitch, roll, and yaw angles
		s = s + "  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
				+ "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
				+ "yaw: " + Math.toDegrees(direction.yaw()) + " degrees"
				+ "\n";

		// Get arm bone
		Arm arm = hand.arm();
		s = s + "  Arm direction: " + arm.direction()
				+ ", wrist position: " + arm.wristPosition()
				+ ", elbow position: " + arm.elbowPosition()
				+ "\n";

		// Get fingers
		for (Finger finger : hand.fingers()) {
			// General info
			s = s + "    " + finger.type() + ", id: " + finger.id()
					+ ", length: " + finger.length()
					+ "mm, width: " + finger.width() + "mm"
					+ "\n";
			//Get Positions
			s = s + "     finger position: " + finger.tipPosition()
					+", finger dirction: " + finger.direction()
					+", finger velocity: " + finger.tipVelocity()
					+"\n";
			//Get Bones
			for(Bone.Type boneType : Bone.Type.values()) {
				Bone bone = finger.bone(boneType);
				s = s + "      " + bone.type()
						+ " bone, start: " + bone.prevJoint()
						+ ", end: " + bone.nextJoint()
						+ ", direction: " + bone.direction()
						+ ", width: " + bone.width()
						+ ", length: " + bone.length()
						+ "\n";
			}
		} //fingers

		//GestureList gestures = frame.gestures();
		for (int i = 0; i < gestures.count(); i++) {
			Gesture gesture = gestures.get(i);

			switch (gesture.type()) {
			case TYPE_CIRCLE:
				CircleGesture circle = new CircleGesture(gesture);

				// Calculate clock direction using the angle between circle normal and pointable
				String clockwiseness;
				if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2) {
					// Clockwise if angle is less than 90 degrees
					clockwiseness = "clockwise";
				} else {
					clockwiseness = "counterclockwise";
				}

				// Calculate angle swept since last frame
				//			double sweptAngle = 0;
				//			if (circle.state() != State.STATE_START) {
				//				CircleGesture previousUpdate = new CircleGesture(circle.id());
				//				sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
				//			}


				s = s + "  Circle id: " + circle.id()
						+ ", " + circle.state()
						+ ", progress: " + circle.progress()
						+ ", radius: " + circle.radius()
						//					+ ", angle: " + Math.toDegrees(sweptAngle)
						+ ", " + clockwiseness
						+ "\n";
				break;
			case TYPE_SWIPE:
				SwipeGesture swipe = new SwipeGesture(gesture);
				s = s + "  Swipe id: " + swipe.id()
						+ ", " + swipe.state()
						+ ", position: " + swipe.position()
						+ ", direction: " + swipe.direction()
						+ ", speed: " + swipe.speed()
						+ "\n";
				break;
			case TYPE_SCREEN_TAP:
				ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
				s = s + "  Screen Tap id: " + screenTap.id()
						+ ", " + screenTap.state()
						+ ", position: " + screenTap.position()
						+ ", direction: " + screenTap.direction()
						+ "\n";
				break;
			case TYPE_KEY_TAP:
				KeyTapGesture keyTap = new KeyTapGesture(gesture);
				s = s + "  Key Tap id: " + keyTap.id()
						+ ", " + keyTap.state()
						+ ", position: " + keyTap.position()
						+ ", direction: " + keyTap.direction()
						+ "\n";
				break;
			default:
				System.out.println("Unknown gesture type.");
				break;
			}
		}


	}// toSaveString

	

	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append(timeStamp); 
		sb.append(", "); sb.append(grabStrength);
		sb.append(", "); sb.append(pinchStregth);
		sb.append(", "); sb.append(pitch);
		sb.append(", "); sb.append(roll);
		sb.append(", "); sb.append(yaw);
		sb.append(", "); sb.append(widthP);
		sb.append(", "); sb.append(xAxisP);
		sb.append(", "); sb.append(yAxisP);
		sb.append(", "); sb.append(zAxisP);
		sb.append(", "); sb.append(armX);
		sb.append(", "); sb.append(armY);
		sb.append(", "); sb.append(armZ);
		sb.append(", "); sb.append(wristX);
		sb.append(", "); sb.append(wristY);
		sb.append(", "); sb.append(wristZ);
		sb.append(", "); sb.append(gestureType[0]);
		sb.append(", "); sb.append(gestureType[1]);
		sb.append(", "); sb.append(gestureType[2]);
		sb.append(", "); sb.append(gestureType[3]);
		
		/*s1 = isLeft + ", " + grabStrength + ", " + pinchStregth + ", " + pitch + ", " + roll + ", " + yaw
				+ ", " + widthP + ", " + xAxisP + ", " + yAxisP + ", " + zAxisP + ", " + armX + ", " + armY
				+ ", " + armZ + ", " + wristX + ", " + wristY+ ", " + wristZ
				+ ", " + gestureType[0] + ", " + gestureType[1] + ", " + gestureType[2] + ", " + gestureType[3];
		*/
		for (int j = 0; j < 5; j++){
			sb.append(", "); sb.append(xAxis[j]);
			sb.append(", "); sb.append(yAxis[j]);
			sb.append(", "); sb.append(zAxis[j]);
			sb.append(", "); sb.append(xVel[j]);
			sb.append(", "); sb.append(yVel[j]);
			sb.append(", "); sb.append(zVel[j]);
			sb.append(", "); sb.append(xDir[j]);
			sb.append(", "); sb.append(yDir[j]);
			sb.append(", "); sb.append(zDir[j]);
			sb.append(", "); sb.append(FingerLenth[j]);
			sb.append(", "); sb.append(FingerWidth[j]);
			
			/* s1 = s1 + ", " + xAxis[j] + ", " + yAxis[j] + ", " + zAxis[j] + ", " + xVel[j] + ", " + yVel[j]
					+ ", " + zVel[j] + ", " + xDir[j] + ", " +  yDir[j] + ", " + zDir[j]
							+ ", " + FingerLenth[j] + ", " + FingerWidth[j]; 	*/
		}
		return sb.toString();

	}// toStirng

}//end Class
