/******************************************************************************\
 * Copyright (C) 2012-2013 Leap Motion, Inc. All rights reserved.               *
 * Leap Motion proprietary and confidential. Not for distribution.              *
 * Use subject to the terms of the Leap Motion SDK Agreement available at       *
 * https://developer.leapmotion.com/sdk_agreement, or another agreement         *
 * between Leap Motion and you, your company or other organization.             *
\******************************************************************************/
// to-do:
// start and stop the signature,
// preprocessing. delete invalid frames.
// calculate feature.
// if Enroll: write the data to file,
// else VERIFY: start DTW.
// write to files for each signature,
// 


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import com.leapmotion.leap.*;






@SuppressWarnings("serial")
class DrawTraceSample extends JFrame implements ActionListener{

	//Global variables
	static OneSig oneSig = new OneSig();
	static ArrayList<OneFrame> sigFrame;

	//float disDTW, disDTW1, disDTW2, disDTW3;
	boolean isVerified = false;
	String savePath = LeapWriteWin.savePath;

	DrawTraceSampleD listener ;
	static Controller controller ;
	static String userName  = "";
	static String passCode = "";
	static int sigNum;

	static DrawOnPanel pl;
	static DrawTraceSample comp;
	private final static ButtonGroup buttonGroup = new ButtonGroup();
	LeapWriteWin loginWin;

	private static JTextField textField;
	private static JRadioButton rdbtnV;
	private static JRadioButton rdbtnE;
	private static JButton btnStart;
	private static JButton btnEnd;
	private static JLabel lblUsername;
	private static JLabel lblWelcomeToLeapwrite;
	private static JButton btnLogIn;	
	private static JLabel lblAlreadyExistChoose;
	private static JPanel panelShow;
	private static JTextPane txtpnInstruction;
	private static JLabel lblResultsYouHave;
	private static JButton btnVerify;
	public static Color foreGround = new Color(0, 0, 153);
	public static Color bkGround = UIManager.getColor("InternalFrame.activeTitleGradient");



	public DrawTraceSample(){	

		textField = new JTextField();
		textField.setVisible(false);
		btnVerify = new JButton("Logout");
		rdbtnV = new JRadioButton("Verification");
		rdbtnE = new JRadioButton("Enrollment");
		btnStart = new JButton("Start(F4)");
		btnEnd = new JButton("Finish(F8)");
		lblUsername = new JLabel("UserName");
		lblUsername.setVisible(false);
		lblWelcomeToLeapwrite = new JLabel("Welcome to LeapGesture");
		btnLogIn = new JButton("Log in");	
		btnLogIn.setVisible(false);
		lblAlreadyExistChoose = new JLabel();
		panelShow = new JPanel();
		txtpnInstruction = new JTextPane();
		lblResultsYouHave = new JLabel();
		if (LeapWriteWin.newUser) {
			lblResultsYouHave.setText("Hello! " + userName + "! You are NEW!");
		} else {
			lblResultsYouHave.setText("Hello! " + userName + "!");

		}
		lblResultsYouHave.setVisible(true);


		// for shortcut of finish and start button
	/*	KeyboardFocusManager.getCurrentKeyboardFocusManager()  
		.addKeyEventDispatcher(new KeyEventDispatcher(){  
			public boolean dispatchKeyEvent(KeyEvent e){  
				if(e.getID() == KeyEvent.KEY_PRESSED)  
				{  
					if(e.getKeyCode() == KeyEvent.VK_F4 ) startKey();  
					if(e.getKeyCode() == KeyEvent.VK_F8 ) finishKey();
				}  
				return false;}});*/
		
		this.setLocationRelativeTo(null);

	}

	/**
	 * pass data from LeapWriteWin 
	 * initialize the GUI
	 */
	public static void frameDesign(){
		userName = LeapWriteWin.userName;
		passCode = LeapWriteWin.passCode;
		comp = new DrawTraceSample();
		comp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		

		comp.getContentPane().setBackground(bkGround);			
		comp.getContentPane().setLayout(null);

		textField.addActionListener(comp);
		btnStart.addActionListener(comp);
		btnLogIn.addActionListener(comp);
		btnEnd.addActionListener(comp);
		btnVerify.addActionListener(comp);
		rdbtnV.addActionListener(comp);
		rdbtnE.addActionListener(comp);

		comp.setPreferredSize(new Dimension(800, 600));

		pl = new DrawOnPanel();

		pl.setBackground(bkGround);
		pl.setBounds(26, 116, 430, 310);
		pl.setBorder(new LineBorder(Color.BLUE));
		comp.getContentPane().add(pl);
		pl.setLayout(null);


		enrollVerifiButt(rdbtnE,rdbtnV);
		comp.getContentPane().add(rdbtnE);
		comp.getContentPane().add(rdbtnV);

		startEndButt(btnStart, btnEnd,btnVerify);
		
		comp.getContentPane().add(btnStart);
		comp.getContentPane().add(btnEnd);
		comp.getContentPane().add(btnVerify);

		lebals(lblWelcomeToLeapwrite,textField,
				lblUsername,btnLogIn,lblAlreadyExistChoose);
		comp.getContentPane().add(textField);
		comp.getContentPane().add(lblUsername);
		comp.getContentPane().add(lblWelcomeToLeapwrite);
		comp.getContentPane().add(btnLogIn);

		meassageShowPanel(panelShow, txtpnInstruction, lblResultsYouHave);

		comp.getContentPane().add(panelShow);
		panelShow.add(txtpnInstruction);
		comp.add(lblResultsYouHave);

		comp.pack();
		comp.setVisible(true);
		comp.setLocationRelativeTo(null);


	} //END FrameDesign()


	private static  void enrollVerifiButt(JRadioButton rdbtnE, JRadioButton rdbtnV){

		buttonGroup.add(rdbtnV);
		rdbtnV.setForeground(foreGround);
		rdbtnV.setBackground(bkGround);
		rdbtnV.setFont(new Font("Tahoma", Font.BOLD, 17));
		rdbtnV.setBounds(338, 48, 131, 23);

		buttonGroup.add(rdbtnE);
		rdbtnE.setForeground(foreGround);
		rdbtnE.setFont(new Font("Tahoma", Font.BOLD, 17));
		rdbtnE.setBackground(bkGround);
		rdbtnE.setBounds(338, 10, 131, 23);
	}

	private static  void startEndButt(JButton btnStart, JButton btnEnd, JButton btnVerify){

		btnStart.setBounds(26, 89, 92, 23);

		btnEnd.setBounds(355, 89, 101, 23);

		btnVerify.setBounds(589, 15, 123, 23);
	}

	private static  void lebals(JLabel lblWelcomeToLeapwrite,JTextField textField,
			JLabel lblUsername,JButton btnLogIn,JLabel lblAlreadyExistChoose){

		textField.setBounds(118, 58, 138, 20);
		textField.setColumns(10);

		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblUsername.setForeground(foreGround);
		lblUsername.setBounds(26, 57, 102, 21);

		lblWelcomeToLeapwrite.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblWelcomeToLeapwrite.setForeground(foreGround);
		lblWelcomeToLeapwrite.setBounds(26, 12, 306, 25);

		btnLogIn.setBounds(26, 38, 87, 23);

		lblAlreadyExistChoose.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblAlreadyExistChoose.setForeground(foreGround);
		lblAlreadyExistChoose.setBounds(277, 87, 393, 25);

	}

	private static void meassageShowPanel(JPanel panelShow, 
			JTextPane txtpnInstruction, JLabel lblResultsYouHave){

		panelShow.setLayout(null);
		panelShow.setBorder(new LineBorder(Color.BLUE));
		panelShow.setBackground(bkGround);
		panelShow.setBounds(480, 116, 220, 310);

		txtpnInstruction.setText("Instruction: \n" +
				"1)   New user, please check \"Enrollment\"; return user, please check \"Verification\". \n" +
				"2)   Press \"Start\" button and start a signature \n   --  put your finger on top of the leap motion and writie. \n" +
				"3)   Finished,please click \"Finsh\".");
		txtpnInstruction.setBounds(10, 11, 200, 234);

		lblResultsYouHave.setBounds(10, 480, 330, 30);

	}

	@SuppressWarnings("unused")
	private float std(float[] arr){
		float sum = 0, ave, std = 0;
		for(float a: arr){
			sum = sum + a;
		}
		ave =  sum/ arr.length;

		for(float a: arr)
			std = (float) Math.sqrt(((a-ave)* (a-ave))/arr.length);
		return std;
	}


	/**
	 * check the whole the directory and Create a list of account information
	 * @param savePath 
	 * @return a LinkedList that record the userName, passCode and sigNum in the savePath Folder.
	 */
	private static LinkedList<UserData> AllFiles(String savePath){
		File dir = new File(savePath);
		File[] dirListing = dir.listFiles();

		if(dirListing != null){
			LinkedList<UserData> existedUserData = new LinkedList<UserData>();
			for (File allFile: dirListing){
				boolean existName = false;
				String currentFileName = allFile.getName();

				if (currentFileName.endsWith("NormedFeature.txt") && !currentFileName.startsWith("FAILED")){
					String[] fileNameSplit = currentFileName.split("_");
					//System.out.println(currentFileName);

					String passCode = fileNameSplit[1];
					int currentSigNum = Integer.parseInt(fileNameSplit[2]);

					UserData currentUserData = new UserData(fileNameSplit[0],passCode, currentSigNum);

					for (UserData user: existedUserData){
						if (fileNameSplit[0].trim().equalsIgnoreCase(user.userName.trim()) 
								&& passCode.trim().equalsIgnoreCase(user.passCode.trim())
								&& currentSigNum >= user.sigNum){
							user.sigNum = currentSigNum;
							existName = true;
							//System.out.println(user.userName);

						} else if(fileNameSplit[0].trim().equalsIgnoreCase(user.userName.trim()) 
								&& !passCode.trim().equalsIgnoreCase(user.passCode.trim())){
							;
						}

					} //end for: replace the sigNum with the bigger one.
					if (!existName){
						existedUserData.add(currentUserData);
					} //end if: new exist Name

				} // end if 
			} //end for

			// just for test

			return existedUserData;

		} else {
			return null;
		}		

	} // end of allFiles


	/**
	 * If the returnValue ==0, then userName does not exist.
	 * If the returnValue ==-1, then userName is someone else, invalid.
	 * If the return value >0, then user Exist.
	 * @param savePath
	 * @param userName
	 * @return Exist sigNum, the newly one should use sigNum +1
	 */
	public static int searchFiles(String savePath, String userName, String passCode){
		int sigNum = 0;
		//boolean existName = false;
		//boolean invalidUser = false;
		LinkedList<UserData> userData = AllFiles(savePath);

		if (!userData.isEmpty()){
			System.out.print("The existing users are: ");
			for (int i = 0 ; i < userData.size() - 1; i++)
				System.out.print(userData.get(i).userName + ", "); 
			System.out.println(userData.get(userData.size()-1).userName + "."); 
		}
		if (userData !=null){
			for (UserData user: userData){
				if (user.userName.trim().equalsIgnoreCase(userName.trim()) 
						&& (!user.passCode.trim().equalsIgnoreCase(passCode.trim()))){
					//invalidUser = true;
					sigNum = -1;
					userName = "";
					System.out.println(user.toString() + "INVALID");
					break;
				}
			}
			for (UserData user: userData){
				if (user.userName.trim().equalsIgnoreCase(userName.trim()) 
						&& (user.passCode.trim().equalsIgnoreCase(passCode.trim()))){
					//existName = true;
					sigNum = user.sigNum;
					System.out.println(user.userName + " EXIST " + user.sigNum + " enrolled signatures.");
					break;
				} 
			}
		}
		return sigNum;

	} //end of searchFiles

	/**
	 * write the toString-ed content(raw data, features to given file)
	 * @param contentFrame
	 * @param fileName
	 */
	public static void writeToFile(String contentFrame, File fileName){

		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName);
			writer.write(contentFrame);
		} catch (IOException ex) {
			// report
		} finally {
			try {writer.close();} catch (Exception ex) {}
		}

	}



	private void startKey(){
		//clear the paint
		pl.clearLines(); 
		if (rdbtnV.isSelected() && sigNum <1){
			JOptionPane.showMessageDialog(comp, "Please enroll at least one signture");
		}else{
			if(!rdbtnE.isSelected() && !rdbtnV.isSelected()){
				JOptionPane.showMessageDialog(comp, "Select Enrollment or Verification as an option!");
			}else {
				start();//create a new instance of a Sig

				// Create a sample listener and controller
				listener = new DrawTraceSampleD();
				controller = new Controller();
				// Have the sample listener receive events from the controller
				controller.addListener(listener);
			}
		}
	}// end startKey


	private void finishKey(){
		if (controller!= null && userName.length() >= 1){
			controller.removeListener(listener);				

			if(oneSig.sigFrame.size()>20 && userName.length() >= 1){

				// to confirm if the recorded signature should be written to the files.
				int reply = JOptionPane.showConfirmDialog(comp, "Confirm the recording?","Choose One", JOptionPane.YES_NO_OPTION);

				if (reply ==  JOptionPane.YES_OPTION){
					//count #signatures for each user when they finished.
					sigNum ++;

					/*
					 * raw data
					 */
					String contentFrame = oneSig.toStringFrame();
					File fileName = new File(this.savePath +  userName.toLowerCase() + "_" + passCode + "_" + sigNum +"_FRAME.txt");
					writeToFile(contentFrame,fileName);
					System.out.println("-- Saved raw data.");
					JOptionPane.showMessageDialog(comp, 
							userName + ", You have saved  the raw data of the  gesture!");

				if (LeapWriteDemo.dataMode == false){
					/*
					 * normed feature data
					 */	
					oneSig.calFeature();
					String contentNormedFeature = oneSig.toStringNormedFeature();

					File fileName2 = new File(this.savePath +  userName.toLowerCase() + "_" + passCode + "_" + sigNum +"_NormedFeature.txt");
					writeToFile(contentNormedFeature,fileName2);
					System.out.println("-- Saved feature data.");


					
						//update account file
						UserDataAll.addAccount(TableDTW.totalSeqNum, new UserData(userName, passCode, sigNum));
						//update Table and the Table file
						FileToArray currentSig = new FileToArray(this.savePath, userName, passCode, sigNum);
						try {
							TableDTW.addTable(LeapWriteDemo.tableFile, TableDTW.totalSeqNum, currentSig.readLines());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						SeqNum.saveSeqNum(TableDTW.totalSeqNum);
						System.out.println("**Updated sequencial file");
						UserDataAll.saveAccount(LeapWriteDemo.accountFile);
						System.out.println("**Updated account file");
						TableDTW.saveTable(LeapWriteDemo.tableFile, TableDTW.tableDTW, TableDTW.totalSeqNum);
						System.out.println("**Updated the TABLE file.");

						/*
						 * normal enrollment & verification 
						 */
						if(rdbtnE.isSelected()){
							JOptionPane.showMessageDialog(comp, 
									userName + ", You have enrolled  " + sigNum + "  signatures!");
						}else if (rdbtnV.isSelected()){
							//multiple template!
							if (sigNum >=3){
								try {
									LeapSVMSingleVeri svm  = new LeapSVMSingleVeri(userName);
									isVerified = svm.getResults();
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								if(isVerified){
									JOptionPane.showMessageDialog(comp, 
											"You are verified as " + userName);		
									System.out.println("You are verified as " + userName);
									int enroll = JOptionPane.showConfirmDialog(null, "Enroll this signature? ","Choose One", JOptionPane.YES_NO_OPTION);

									if ( enroll ==  JOptionPane.YES_OPTION){
										;
									}
									else{
										fileName.renameTo(new File(this.savePath + "FAILED_" + userName.toLowerCase() + "_" + passCode + "_" + sigNum +"_FRAMEFAILED.txt"));
										fileName2.renameTo(new File(this.savePath + "FAILED_" + userName.toLowerCase() + "_" + passCode + "_" + sigNum +"_NormedFeatureFAILED.txt"));
										sigNum = sigNum - 1;
										//updated & save Table
										UserDataAll.deleteAccount(TableDTW.totalSeqNum);
										UserDataAll.saveAccount(LeapWriteDemo.accountFile);
										System.out.println(TableDTW.totalSeqNum);

										TableDTW.deleteTable(LeapWriteDemo.tableFile, TableDTW.totalSeqNum);
										TableDTW.saveTable(LeapWriteDemo.tableFile, TableDTW.tableDTW, TableDTW.totalSeqNum);
										System.out.println(TableDTW.totalSeqNum);

										SeqNum.saveSeqNum(TableDTW.totalSeqNum);
										System.out.println(TableDTW.totalSeqNum);

									}

								} else{ // isVerified 
									JOptionPane.showMessageDialog(comp, 
											"You are NOT verified as " + userName);
									fileName.renameTo(new File(this.savePath + "FAILED_" + userName.toLowerCase() + "_" + passCode + "_" + sigNum +"_FRAMEFAILED.txt"));
									fileName2.renameTo(new File(this.savePath + "FAILED_" + userName.toLowerCase() + "_" + passCode + "_" + sigNum +"_NormedFeatureFAILED.txt"));
									sigNum = sigNum - 1;

									//updated & save Table
									UserDataAll.deleteAccount(TableDTW.totalSeqNum);
									UserDataAll.saveAccount(LeapWriteDemo.accountFile);
									System.out.println(TableDTW.totalSeqNum);

									TableDTW.deleteTable(LeapWriteDemo.tableFile, TableDTW.totalSeqNum);
									TableDTW.saveTable(LeapWriteDemo.tableFile, TableDTW.tableDTW, TableDTW.totalSeqNum);
									System.out.println(TableDTW.totalSeqNum);

									SeqNum.saveSeqNum(TableDTW.totalSeqNum);
									System.out.println(TableDTW.totalSeqNum);
									//TableDTW.totalSeqNum --;

									System.out.println("You are FAILED to verified as " + userName);
								}//NOT VERIFIED

							}else {
								JOptionPane.showMessageDialog(comp, 
										"Please record at least 4 valid signatures\n before Verificatioin!");
							}
						} //if (rdbtnV.isSelected())
					} // if it is Verification Mode 

				} else { //yesOPTION -- accept the signature
					//do nothing
					;
				}
			}else {
				JOptionPane.showMessageDialog(comp, "Please write at least 3 seconds !");
			}// SigFrame is too short or empty string
		}else{
			//JOptionPane.showMessageDialog(comp, "Click Start first!");
		} //not started yet
	} //end finishKey()

	@Override
	public void actionPerformed(ActionEvent e) {

		// calculate the whole DTWdistance Table
		if(e.getActionCommand().equals("Start(F4)")){
			startKey();
		}

		else if (e.getActionCommand().equals("Finish(F8)")){
			finishKey();
		} //end the finish 

		else if (e.getActionCommand().equals("Logout")){
			userName = "";
			passCode = "";	
			comp.dispose();
			loginWin =  new LeapWriteWin( );
			loginWin.frameDesign();
			loginWin.setVisible(true);
		}// end of Logout
		else if (e.getActionCommand().equals("Verification")){
			if(sigNum<1){
				JOptionPane.showMessageDialog(comp, "You haven't enrolled any signatures!");
			} else if(sigNum <= 3){
				JOptionPane.showMessageDialog(comp, "Please record at least 4 valid signatures\n before Verificatioin!");
			}
			rdbtnV.setSelected(true);
			rdbtnE.setSelected(false);
			System.out.println("Verification");

		} else if (e.getActionCommand().equals("Enrollment")){

			rdbtnE.setSelected(true);
			rdbtnV.setSelected(false);
			System.out.println("Enrollment");
		}// end of Verify



	} // end actionPerformed



	/**
	 * start() 
	 */

	private void start(){
		oneSig = new OneSig();
		sigFrame = oneSig.sigFrame;

	}	// end start()






} // end class DrawTraceSample


/**
 * 
 *  class draw circles on the panel
 * -- view the trajectories.
 *
 */
@SuppressWarnings("serial")
class DrawOnPanel extends JPanel{

	public DrawOnPanel(){
		super();
	}

	private class Circle{
		final int x1; 
		final int y1;
		final int r; 
		final Color color;

		public Circle(int x1, int y1, int r, Color color) {
			this.x1 = x1;
			this.y1 = y1;
			this.r = r;
			this.color = color;
		}               
	} // end class Circle

	private final LinkedList<Circle> circles = new LinkedList<Circle>();

	/*public void addCircle(int x1, int x2, int r) {
		addCircle(x1, x2, r, Color.blue);
	}*/

	public void addCircle(int x1, int x2, int r, Color color) {
		circles.add(new Circle(x1,x2,r, color));
		repaint();
	}


	public void clearLines() {
		circles.clear();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		
		//pl.setBounds(26, 116, 430, 310);
		g.setColor(Color.gray);
		g.drawRect(100, 50, 200, 200);
		
		if (LeapWriteDemo.SingleFinger){
			for (int i = 0; i< circles.size(); i++){
				g.drawOval(circles.get(i).x1, circles.get(i).y1, circles.get(i).r, circles.get(i).r);
				g.setColor(circles.get(i).color);
				g.fillOval(circles.get(i).x1, circles.get(i).y1, circles.get(i).r, circles.get(i).r);
			}
		} else{ // multifinger
			if 	(circles.size() <= 50){	
				for (int i = 0; i< circles.size(); i++){
					g.setColor(circles.get(i).color);
					g.drawOval(circles.get(i).x1, circles.get(i).y1, circles.get(i).r, circles.get(i).r);

					g.fillOval(circles.get(i).x1, circles.get(i).y1, circles.get(i).r, circles.get(i).r);
				}
			} else {
				for (int i = circles.size() - 46; i< circles.size(); i++){
				//for (int i = 0; i< circles.size(); i++){

					g.setColor(circles.get(i).color);
					g.drawOval(circles.get(i).x1, circles.get(i).y1, circles.get(i).r, circles.get(i).r);
					g.fillOval(circles.get(i).x1, circles.get(i).y1, circles.get(i).r, circles.get(i).r);
				}
			}
		}



	} // end paintComponent.

} // end class DrawOnPanel



class DrawTraceSampleD extends Listener {

	float TimeStamp,recordSpeed; 

	@Override
	public void onInit(Controller controller) {
		System.out.println("Controller Initialized");
	}

	@Override
	public void onConnect(Controller controller) {
		System.out.println("Controller Connected");
		//controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		//controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		//controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		//controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
	}

	@Override
	public void onDisconnect(Controller controller) {
		//Note: not dispatched when running in a debugger.
		System.out.println("Controller Disconnected");
	}

	@Override
	public void onExit(Controller controller) {
		System.out.println("Controller Exited");
	}

	/**
	 * connections to the LEAP MOTION
	 */
	@Override
	public void onFrame(Controller controller) {
		// Get the most recent frame and report some basic information
		Frame frame = controller.frame();
		/*
			System.out.println("Frame id: " + frame.id()
					+ ", timestamp: " + frame.timestamp()
					+ ", hands: " + frame.hands().count()
					+ ", fingers: " + frame.fingers().count()
					+ ", tools: " + frame.tools().count()
					+ ", gestures " + frame.gestures().count());
		 */

		if (!frame.hands().isEmpty()) {
			// Get the first hand
			Hand hand = frame.hands().get(0);

			// Check if the hand has any fingers
			FingerList fingers = hand.fingers();
			if (!fingers.isEmpty()) {

				Finger f = fingers.frontmost();
				//data of the frame

				//a new OneFrame;
				long frameID = frame.id();
				long timeStamp = frame.timestamp();
				float recordSpeed = frame.currentFramesPerSecond();

				OneFrame oneFrame = new OneFrame(f,frameID, timeStamp, recordSpeed);


				//add each frame to the sigFrame (to form a new signature)
				DrawTraceSample.sigFrame.add(oneFrame);


				int x1 = (int) ((oneFrame.xAxis +250) / 1.2);
				int y1 = (int) ((450 - oneFrame.yAxis)/1.2);
				int r = 3;
				DrawTraceSample.pl.addCircle(x1, y1, r, Color.blue);

			}
		}
	}
} // end class DrawTraceSampleD extends Listener




