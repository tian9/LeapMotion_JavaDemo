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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.timeseries.TimeSeries;
import com.util.DistanceFunction;
import com.util.DistanceFunctionFactory;






@SuppressWarnings("serial")
class DrawMultiFigure extends JFrame implements ActionListener{

	//Global variables
	static OneGesture oneGesture = new OneGesture();
	static List<OneHandFrame> handFrameList;
	static ArrayList<OneHandFrame> handFrameListStr;
	
	static boolean isVerified = false;
	static String savePath = LeapWriteWin.savePath;

	static DrawMultiFigureD listener ;
	static Controller controller ;
	static String userName  = "";
	static String passCode = "";
	static int sigNum;

	static DrawOnPanel pl;
	static DrawMultiFigure comp;
	private final static ButtonGroup buttonGroup = new ButtonGroup();
	LeapWriteWin loginWin;

	private static JTextField textField;
	private static JRadioButton rdbtnV;
	private static JRadioButton rdbtnE;
	private static JButton btnStart;
	private static JButton btnNextGesture;
	private static JButton btnEnd;
	private static JLabel lblUsername;
	private static JLabel lblWelcomeToLeapwrite;
	private static JButton btnLogIn;	
	private static JLabel lblAlreadyExistChoose;
	private static JPanel panelShow;
	private static JTextPane txtpnInstruction;
	private static JLabel lblResultsYouHave;
	private static JLabel lblGesture, lblFeedback;
	private static JLabel lblSample;
	private static JButton btnVerify;
	public static Color foreGround = new Color(0, 0, 153);
	public static Color bkGround = UIManager.getColor("InternalFrame.activeTitleGradient");
	
	static ArrayList<String> OneGestureDataFrame = new ArrayList<String>();
	static double predis = LeapWriteWin.DTW_TH;


	public DrawMultiFigure(){	
		textField = new JTextField();
		textField.setVisible(false);
		btnVerify = new JButton("Logout(Admin)");
		btnNextGesture = new JButton("Next Gesture");
		rdbtnV = new JRadioButton("Verification");
		rdbtnE = new JRadioButton("Enrollment");
		btnStart = new JButton("Start(F7)");
		btnEnd = new JButton("Finish(F8)");
		lblUsername = new JLabel("UserName");
		lblUsername.setVisible(false);
		lblWelcomeToLeapwrite = new JLabel("Welcome to LeapGesture");
		btnLogIn = new JButton("Log in");	
		btnLogIn.setVisible(false);
		lblAlreadyExistChoose = new JLabel();
		panelShow = new JPanel();
		txtpnInstruction = new JTextPane();
		lblResultsYouHave = new JLabel("Welcome");
		lblGesture = new JLabel();
		lblSample = new JLabel();
		lblFeedback = new JLabel("Feedback");

		lblSample.setText("Enrolled 0 sample"); 
		lblGesture.setText("Design your SECURE gesture.");
		/*
		if (LeapWriteWin.newUser) {
			lblResultsYouHave.setText("Welcome! " + userName + "!");
		} else {
			lblResultsYouHave.setText("Hello! " + userName + "!");
		}
		*/
		lblResultsYouHave.setVisible(true);
		lblGesture.setVisible(true);
		lblSample.setVisible(true);
		lblFeedback.setVisible(true);
	}

	/**
	 * pass data from LeapWriteWin 
	 * initialize the GUI
	 */
	public static void frameDesign(){
		userName = LeapWriteWin.userName;
		passCode = LeapWriteWin.passCode;
		comp = new DrawMultiFigure();
		comp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		

		comp.getContentPane().setBackground(bkGround);			
		comp.getContentPane().setLayout(null);

		textField.addActionListener(comp);
		btnStart.addActionListener(comp);
		btnLogIn.addActionListener(comp);
		btnEnd.addActionListener(comp);
		btnVerify.addActionListener(comp);
		btnNextGesture.addActionListener(comp);
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
		rdbtnE.setSelected(true);
		comp.getContentPane().add(rdbtnE);
		comp.getContentPane().add(rdbtnV);

		startEndButt(btnStart, btnEnd, btnVerify, btnNextGesture);

		comp.getContentPane().add(btnStart);
		comp.getContentPane().add(btnEnd);
		comp.getContentPane().add(btnVerify);
		comp.getContentPane().add(btnNextGesture);

		lebals(lblWelcomeToLeapwrite,textField,
				lblUsername,btnLogIn,lblAlreadyExistChoose, lblFeedback);
		comp.getContentPane().add(textField);
		comp.getContentPane().add(lblUsername);
		comp.getContentPane().add(lblWelcomeToLeapwrite);
		comp.getContentPane().add(btnLogIn);
		comp.getContentPane().add(lblFeedback);

		meassageShowPanel(panelShow, txtpnInstruction, lblResultsYouHave, lblGesture, lblSample, lblFeedback);

		comp.getContentPane().add(panelShow);
		panelShow.add(txtpnInstruction);
		comp.add(lblResultsYouHave); 
		comp.add(lblGesture);
		comp.add(lblSample);
		comp.add(lblFeedback);

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



	private static  void lebals(JLabel lblWelcomeToLeapwrite,JTextField textField,
			JLabel lblUsername,JButton btnLogIn,JLabel lblAlreadyExistChoose, JLabel lblFeedback){

		textField.setBounds(118, 58, 138, 20);
		textField.setColumns(10);

		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblUsername.setForeground(foreGround);
		lblUsername.setBounds(26, 57, 102, 21);

		lblWelcomeToLeapwrite.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblWelcomeToLeapwrite.setForeground(foreGround);
		lblWelcomeToLeapwrite.setBounds(26, 12, 306, 25);

		btnLogIn.setBounds(26, 38, 87, 23);

		lblAlreadyExistChoose.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblAlreadyExistChoose.setForeground(foreGround);
		lblAlreadyExistChoose.setBounds(277, 87, 393, 25);
		
		lblFeedback.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblFeedback.setForeground(Color.RED);
		lblFeedback.setBounds(180, 87, 393, 25);

	}
	
	private static  void startEndButt(JButton btnStart, JButton btnEnd, JButton btnVerify, JButton btnNextGesture){

		btnStart.setBounds(26, 80, 130, 30);
		btnEnd.setBounds(325, 80, 130, 30);
		btnVerify.setBounds(549, 30, 130, 30);
		
		btnNextGesture.setBounds(480, 450, 200, 40);
		btnNextGesture.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNextGesture.setForeground(foreGround);
	}

	private static void meassageShowPanel(JPanel panelShow, JTextPane txtpnInstruction, 
			JLabel lblResultsYouHave, JLabel lblGesture, JLabel lblSample, JLabel lblFeedback){

		panelShow.setLayout(null);
		panelShow.setBorder(new LineBorder(Color.BLUE));
		panelShow.setBackground(bkGround);
		panelShow.setBounds(480, 116, 220, 310);
		
		lblResultsYouHave.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblGesture.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblGesture.setForeground(foreGround);
		lblGesture.setBounds(35, 460, 350, 30);

		
		panelShow.setBorder(new LineBorder(Color.DARK_GRAY));
		lblSample.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		txtpnInstruction.setText("Instruction: \n" +
				"1)   Press \"Start(F7)\" button and start a gesture \n   --  Put your hand above the device until the gray area shows some dots. \n\n" +
				"2)   Finished,please click \"Finish(F8)\".\n\n" + 
				"3)   Comfirm or refuse the sample after a pop massage.\n\n" + 
				"4)   Press \"next Gesture\".\n\n" +
				"5)   repeat above steps till you finish all gestures.");
		txtpnInstruction.setBounds(10, 11, 200, 290);
		lblResultsYouHave.setBounds(290, 500, 200, 30);
		lblSample.setBounds(35, 500, 350, 30);
	}

	private static void writeToFile(String contentFrame, File fileName){

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
	private static void writeMoreToFile(int gestureIndex){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int mininute = calendar.get(Calendar.MINUTE);
		int sec = calendar.get(Calendar.SECOND);
		//System.out.println("hours " + hour + ", " + mininute +  " " + sec);
		
		Date date = new Date();
		String ds = dateFormat.format(date);
		String ds1 = ds.substring(0, 4) + ds.substring(5, 7) + ds.substring(8);
		System.out.println("before: " + ds1);
		ds1 = ds1 + "_" + String.format("%2d%2d%2d" , hour, mininute, sec);
		System.out.println("after:" + ds1);
		System.out.println(); //2014/08/06 15:59:48
		
		File fileName2 = new File(savePath +  userName.toLowerCase() +"_" +  ds1  + "_Frame_" + gestureIndex + ".txt");

		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName2);
				for(int j = 0; j < OneGestureDataFrame.size(); j++){
					String sampleIndex = "" + j;
					
					writer.write(gestureIndex); 
					writer.write(System.lineSeparator());
					writer.write(sampleIndex);
					writer.write(System.lineSeparator());
					writer.write(OneGestureDataFrame.get(j));
					writer.write(System.lineSeparator());
				}
			
		} catch (IOException ex) {
			// report
			System.out.println(ex.getMessage());
		} finally {
			try {writer.close();} catch (Exception ex) {}
		}

	}
	private static void writeMoreToFile(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int mininute = calendar.get(Calendar.MINUTE);
		int sec = calendar.get(Calendar.SECOND);
		
		Date date = new Date();
		String ds = dateFormat.format(date);
		String ds1 = ds.substring(0, 4) + ds.substring(5, 7) + ds.substring(8);
		System.out.println("before: " + ds1);
		ds1 = ds1 + "_" + String.format("%2d%2d%2d" , hour, mininute, sec);
		System.out.println("after:" + ds1);
		
		
		//ds1 += "_" + hour + mininute +sec;
		System.out.println(); //2014/08/06 15:59:48
		
		File fileName2 = new File(savePath +  userName.toLowerCase() +"_" +  ds1  + "_Frame.txt");

		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName2);
			for(int i = 0; i < LeapWriteWin.FRAME_DATA.size(); i++){
				String gestureIndex = "" + i;
				for(int j = 0; j < LeapWriteWin.FRAME_DATA.get(i).size(); j++){
					String sampleIndex = "" + j;
					
					writer.write(gestureIndex); 
					writer.write(System.lineSeparator());
					writer.write(sampleIndex);
					writer.write(System.lineSeparator());
					writer.write(LeapWriteWin.FRAME_DATA.get(i).get(j));
					writer.write(System.lineSeparator());
				}
			}
		} catch (IOException ex) {
			// report
			System.out.println(ex.getMessage());
		} finally {
			try {writer.close();} catch (Exception ex) {}
		}

	}

	static void startKey(){
		//clear the paint
		lblResultsYouHave.setText("Started!");
		pl.clearLines(); 
		if (rdbtnV.isSelected() && sigNum <1){
			JOptionPane.showMessageDialog(comp, "Please enroll at least one signture");
		}else{
			if(!rdbtnE.isSelected() && !rdbtnV.isSelected()){
				JOptionPane.showMessageDialog(comp, "Select Enrollment or Verification as an option!");
			}else {
				start();//create a new instance of a Sig

				// Create a sample listener and controller
				listener = new DrawMultiFigureD();
				controller = new Controller();
				// Have the sample listener receive events from the controller
				controller.addListener(listener);
			}
		}
	} // end startKey

	 static void finishKey() throws IOException{
		if (controller!= null && userName.length() >= 1){
			controller.removeListener(listener);				

			if(oneGesture.handFrameList.size()>20 && userName.length() >= 1){

				// to confirm if the recorded signature should be written to the files.
				int reply = JOptionPane.showConfirmDialog(comp, "Confirm the recording?","Choose One", JOptionPane.YES_NO_OPTION);
				// TODO  appear multiple times when 
				if (reply ==  JOptionPane.YES_OPTION){
					//count #signatures for each user when they finished.
					sigNum ++;
					int gsindex = LeapWriteWin.FRAME_DATA.size();
					
					/**
					 *the new similarity calculation
					 */
					// load the gsindex + username,
					//List<List<Double[]>> trainFeatures = LeapWriteWin.map.get(gsindex);
					DistanceFunction distFn = DistanceFunctionFactory.getDistFnByName("EuclideanDistance"); 
					TimeSeries tsI = new TimeSeries(oneGesture.getNormedFeature());
					
					// calculate dtw distance.
					
					double dis = 0;
					int cntt = 0;
					File files = new File(LeapWriteWin.datapath  + gsindex);
					
					for (File file : files.listFiles()) {
						if (!file.getName().contains(userName)) continue;
						cntt++;
						TimeSeries tsJ = new TimeSeries(file.toString(), false, false, ',');
						double temp = com.dtw.FastDTW.getWarpDistBetween(tsI, tsJ, 10, distFn);
						dis += temp;
					}
					dis /= cntt;
					
					System.out.println("Distance: " + predis + " , "+ dis);
			        //calculate
					if (dis - LeapWriteWin.DTW_TH < 0.1) {
						lblFeedback.setText("Good Job");
					} else if (Math.abs(dis - predis) <= 500){
						lblFeedback.setText("Good Job, Keep trying");
					} else if (dis < predis){
						lblFeedback.setText("Great!!");
					} else {
						lblFeedback.setText("Try Again!");
					}
					predis = dis;
					//
					
					/*
					Similarity similarity = new Similarity(oneGesture,savePath, userName.toLowerCase(), gsindex);
					String disT = String.format("Current Similarity: %.2f, Ideal Similarity: %.2f", similarity.dis, similarity.indis);
					if (similarity.disAll.size() == 0 || similarity.dis < similarity.disAll.get(Similarity.disAll.size() - 1)) {
						System.out.println("***********Closer************");
						
					} else {
						System.out.println("***********Faster************");
					}
					System.out.println("dis: " + disT);
					 */
					
					/*
					 * raw data
					 */
					Date d = new Date(); long before = d.getTime();
					

					String contentFrame = oneGesture.toStringFrame();
					
					
					File fileName = new File(savePath +  userName.toLowerCase() + "_" + passCode + "_" + sigNum +"_FRAME.txt");
					//*  ***************************************************
					//writeToFile(contentFrame,fileName);
					// *************************************************** 
					
					OneGestureDataFrame.add(contentFrame);
					
					
					int size = OneGestureDataFrame.size();
					if(size == 1){
						lblSample.setText("enrolled " + size + " sample");
					}else{
						lblSample.setText("enrolled " + size + " samples");
					}
					

					Date d1 = new Date(); long after = d1.getTime();
					System.out.println("--Time to save RawData (s): " + (after-before)/1000);
					lblResultsYouHave.setText("Finished!");
					
					//JOptionPane.showMessageDialog(comp, 
					//		userName + ", You have saved  the raw data of the  gesture!");
					//******************
					if (LeapWriteDemo.dataMode == false){
						/*
						 * normed feature data
						 */	
						d = new Date(); before = d.getTime();

						String contentNormedFeature = oneGesture.toStringNormedFeature();

						d1 = new Date();after = d1.getTime(); System.out.println("--Time to calculate Normed Feature Data (s): " + (after-before)/1000);

						File fileName2 = new File(savePath +  userName.toLowerCase() + "_" + passCode + "_" + sigNum +"_NormedFeature.txt");
						writeToFile(contentNormedFeature,fileName2);

						Date d2 = new Date(); long after1 = d2.getTime(); 
						System.out.println("--Time to finish saving Normed Feature Data (s): " + (after1-after)/1000);


						//update account file
						UserDataAll.addAccount(TableDTW.totalSeqNum, new UserData(userName, passCode, sigNum));


						//update Table and the Table file
						FileToArray currentSig = new FileToArray(savePath, userName, passCode, sigNum);
						try {
							TableDTW.addTable(LeapWriteDemo.tableFile, TableDTW.totalSeqNum, currentSig.readLines());
						} catch (IOException e1) {
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

							lblResultsYouHave.setText(userName + ", You have enrolled  " + sigNum + "  signatures!");



						}else if (rdbtnV.isSelected()){

							//multiple template!
							if (sigNum >=3){

								// CREATE FEATURE VECTOR!
								// Label all the training data: self +1; others -1;
								// label the current data: +1;						

								// VERIFICATION: if results label != +1, Fail;
								try {
									LeapSVMSingleVeri svm = new LeapSVMSingleVeri(userName);
									isVerified = svm.getResults();
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}


								if(isVerified = true){
									lblResultsYouHave.setText("You are verified as " + userName);		
									System.out.println("You are verified as " + userName);
									int enroll = JOptionPane.showConfirmDialog(null, "Enroll this signature? ","Choose One", JOptionPane.YES_NO_OPTION);

									if ( enroll ==  JOptionPane.YES_OPTION){
										;
									}
									else{
										fileName.renameTo(new File(savePath + "FAILED_" + userName.toLowerCase() + "_" + passCode + "_" + sigNum +"_FRAMEFAILED.txt"));
										fileName2.renameTo(new File(savePath + "FAILED_" + userName.toLowerCase() + "_" + passCode + "_" + sigNum +"_NormedFeatureFAILED.txt"));
										sigNum = sigNum - 1;
										//updated & save Table
										UserDataAll.deleteAccount(TableDTW.totalSeqNum);
										UserDataAll.saveAccount(LeapWriteDemo.accountFile);

										TableDTW.deleteTable(LeapWriteDemo.tableFile, TableDTW.totalSeqNum);
										TableDTW.saveTable(LeapWriteDemo.tableFile, TableDTW.tableDTW, TableDTW.totalSeqNum);

										SeqNum.saveSeqNum(TableDTW.totalSeqNum);
									}
								} else{ // isVerified
									JOptionPane.showMessageDialog(comp, 
											"You are NOT verified as " + userName);
									fileName.renameTo(new File(savePath + "FAILED_" + userName.toLowerCase() + "_" + passCode + "_" + sigNum +"_FRAMEFAILED.txt"));
									fileName2.renameTo(new File(savePath + "FAILED_" + userName.toLowerCase() + "_" + passCode + "_" + sigNum +"_NormedFeatureFAILED.txt"));
									sigNum = sigNum - 1;

									//updated & save Table
									UserDataAll.deleteAccount(TableDTW.totalSeqNum);
									UserDataAll.saveAccount(LeapWriteDemo.accountFile);

									TableDTW.deleteTable(LeapWriteDemo.tableFile, TableDTW.totalSeqNum);
									TableDTW.saveTable(LeapWriteDemo.tableFile, TableDTW.tableDTW, TableDTW.totalSeqNum);

									SeqNum.saveSeqNum(TableDTW.totalSeqNum);


									//TableDTW.totalSeqNum --;

									System.out.println("You are FAILED to verified as " + userName);


								}//NOT VERIFIED

							}else {
								lblResultsYouHave.setText("Please record at least 4 valid signatures\n before Verificatioin!");
							}
						} //if (rdbtnV.isSelected())
					}

				} else {//yesOPTION -- accept the signature
					//do nothing
					;
				}
			}else {
				lblResultsYouHave.setText("Please write at least 3 seconds !");
			}

		}else{
			lblResultsYouHave.setText("Click Start first!");
		}
	} //end finishKey


	@Override
	public void actionPerformed(ActionEvent e) {

		// calculate the whole DTWdistance Table
		if(e.getActionCommand().equals("Start(F7)")){
			
			startKey();
		} 
		else if (e.getActionCommand().equals("Finish(F8)")){
			//lblResultsYouHave.setText("Finished!");
			try {
				finishKey();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} //end the finish 
		else if(e.getActionCommand().equals("Next Gesture")){
			int gsindex = LeapWriteWin.FRAME_DATA.size();
			LeapWriteWin.FRAME_DATA.add(new ArrayList<String>(OneGestureDataFrame));
			writeMoreToFile(gsindex);
			OneGestureDataFrame.clear();
			LeapWriteWin.DTW_TH = 20;
			lblFeedback.setText("Try");
			lblSample.setText("enrolled 0 sample");
			
			switch(gsindex + 1){
			case 1:
				lblGesture.setText("Swipe hand (e.g., swipe a page)"); break;
			case 2:
				lblGesture.setText("Wave hand (e.g., byebye)");break;
			case 3:
				lblGesture.setText("Make Circle(s)");break;
			case 4:
				lblGesture.setText("Zooming(open / shutdown)");break;
			case 5:
				lblGesture.setText("Grab something");break;
			case 6:
				lblGesture.setText("Write 'abc'");break;
			case 7:
				lblGesture.setText("Sign your name / initial");break;
				
			//case 8:
			//lblGesture.setText("Again, design another gesture");break;
			default:
				break;
			}
			

		}
		else if (e.getActionCommand().equals("Logout(Admin)")){
			if(LeapWriteWin.FRAME_DATA.size() < 9){
				
			}
			comp.dispose();
			loginWin =  new LeapWriteWin( );
			loginWin.frameDesign();
			loginWin.setVisible(true);
			//loginWin.pack();
			loginWin.setLocationRelativeTo(null);
			
			LeapWriteWin.FRAME_DATA.add(new ArrayList<String>(OneGestureDataFrame));
			OneGestureDataFrame.clear();
			writeMoreToFile();
			LeapWriteWin.FRAME_DATA.clear();
		}// end of LOGOUT
		else if (e.getActionCommand().equals("Verification")){
			if(sigNum<1){
				lblResultsYouHave.setText("You haven't enrolled any signatures!");
			} else if(sigNum <= 3){
				lblResultsYouHave.setText("Please record at least 4 valid signatures\n before Verificatioin!");
			} 
			rdbtnV.setSelected(true);
			rdbtnE.setSelected(false);
			System.out.println("Verification");
		} else if (e.getActionCommand().equals("Enrollment")){

			rdbtnE.setSelected(true);
			rdbtnV.setSelected(false);
			System.out.println("Enrollment");
		} // END OF VERIFY



	} // end actionPerformed



	/**
	 * start() 
	 */

	private static void start(){
		oneGesture = new OneGesture();
		handFrameList = oneGesture.handFrameList;
		//handFrameListStr = oneGesture.handFrameStrList;

	}	// end start()







} // end class DrawMultiFigure




class DrawMultiFigureD extends Listener {

	float TimeStamp,recordSpeed; 

	@Override
	public void onInit(Controller controller) {
		System.out.println("Controller Initialized");
	}

	@Override
	public void onConnect(Controller controller) {
		System.out.println("Controller Connected");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
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
		GestureList gL = null;

		/*
		String frameInfo = "Frame id: " + frame.id()
				+ ", timestamp: " + frame.timestamp()
				+ ", hands: " + frame.hands().count()
				+ ", fingers: " + frame.fingers().count()
				+ ", tools: " + frame.tools().count()
				+ ", gestures: " + frame.gestures().count();
		*/

		if (!frame.hands().isEmpty()) {
			OneHandFrame OneHandFrame;
			//OneHandFrame OneHandFrameStr;

			if(frame.gestures().count()>0)
				gL= frame.gestures();

			//a new OneFrame;
			long frameID = frame.id();
			long timeStamp = frame.timestamp();
			//float recordSpeed = frame.currentFramesPerSecond();

			// Get the first hand
			Hand hand = frame.hands().get(0);

			OneHandFrame = new OneHandFrame(hand,frameID, timeStamp, gL);
			//OneHandFrameStr = new OneHandFrame(hand, gL);
			DrawMultiFigure.handFrameList.add(OneHandFrame);
			//DrawMultiFigure.handFrameListStr.add(OneHandFrameStr);

			// Draw 5 fingers!
			FingerList fingers = hand.fingers();
			if (!fingers.isEmpty()) {

				Color[] color = new Color[5];
				color[0] = Color.blue;
				color[1] = Color.red;
				color[2] = Color.green;
				color[3] = Color.darkGray;
				color[4] = Color.ORANGE;
				int colorCount = 0;
				int fingerCount = fingers.count();
				if (fingerCount >= 1){
					for (Finger finger : fingers) {

						// This is different, we need to parse the saved file 
						//to determine which five(multiple) fingers belongs to a same frame

						int x1 = (int) ((finger.tipPosition().getX() +250) / 1.5);
						int y1 = (int) ((450 - finger.tipPosition().getY())/1.5);
						int r = 2;
						DrawMultiFigure.pl.addCircle(x1, y1, r, color[colorCount]);
						colorCount++;
					}
				}// finger drawing

			}
		}
	} //onFrame

} // end class DrawMultiFigureD extends Listener




