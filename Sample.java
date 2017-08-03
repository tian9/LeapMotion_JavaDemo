/******************************************************************************\
 * Copyright (C) 2012-2013 Leap Motion, Inc. All rights reserved.               *
 * Leap Motion proprietary and confidential. Not for distribution.              *
 * Use subject to the terms of the Leap Motion SDK Agreement available at       *
 * https://developer.leapmotion.com/sdk_agreement, or another agreement         *
 * between Leap Motion and you, your company or other organization.             *
\******************************************************************************/

import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.LinkedList;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

class SampleListener extends Listener {

	public String s;

	@Override
	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}

	@Override
	public void onConnect(Controller controller) {
		System.out.println("Connected");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
	}

	@Override
	public void onDisconnect(Controller controller) {
		//Note: not dispatched when running in a debugger.
		System.out.println("Disconnected");
	}

	@Override
	public void onExit(Controller controller) {
		System.out.println("Exited");
	}

	@Override
	public void onFrame(Controller controller) {
		// Get the most recent frame and report some basic information
		Frame frame = controller.frame();
		if(frame.hands().count() == 1){
			s = "";
			s =  s + "Frame id: " + frame.id()
					+ ", timestamp: " + frame.timestamp()
					+ ", hands: " + frame.hands().count()
					+ ", fingers: " + frame.fingers().count()
					+ ", tools: " + frame.tools().count()
					+ ", gestures " + frame.gestures().count()
					+ "\n";
			//Get hands
			for(Hand hand : frame.hands()) {
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
				}
			} // end hands

			// Get tools
			

			GestureList gestures = frame.gestures();
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
					double sweptAngle = 0;
					if (circle.state() != State.STATE_START) {
						CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
						sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
					}


					s = s + "  Circle id: " + circle.id()
							+ ", " + circle.state()
							+ ", progress: " + circle.progress()
							+ ", radius: " + circle.radius()
							+ ", angle: " + Math.toDegrees(sweptAngle)
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
			s = s + "\n";
			Sample.list.add(s);
		} // have hands

		}
}

class Sample {
	public static LinkedList<String> list = new LinkedList<String>();

	public static void main(String[] args) {
		// Create a sample listener and controller
		SampleListener listener = new SampleListener();

		Controller controller = new Controller();

		// Have the sample listener receive events from the controller
		controller.addListener(listener);
		File fileName = new File("c:\\jing2013\\Dropbox\\Leap Motion\\Leap\\MultiFinger\\ALL.txt");


		// Keep this process running until Enter is pressed
		System.out.println("Press Enter to quit...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();

		}

		// Remove the sample listener when done
		controller.removeListener(listener);


		DrawTraceSample.writeToFile(list.toString(), fileName);


	}
}
