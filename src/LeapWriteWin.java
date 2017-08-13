import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;


/**
 Simple demonstration of putting buttons in a JFrame.
 */
@SuppressWarnings("serial")
public class LeapWriteWin extends JFrame implements ActionListener
{

	static ArrayList<ArrayList<String>> FRAME_DATA = new ArrayList<ArrayList<String>>(); 
	static int gestureNum = 0;
	public static String savePath;
	public static boolean newUser;
	private JTextField textFielduserName;
	private JTextField textFieldpassCode;
	public static String userName = null;
	public static String passCode = null;
	//LeapWriteWin loginWin;
	public static double DTW_TH = 20.0;
	public static Map<Integer, List<List<Double[]>>> map = new HashMap<>();
	//LeapWriteWin loginWin;
	int featureDimension = 115;
	boolean returnUser = true;
	
	public LeapWriteWin( )
	{
		; 
		setSize(480, 340);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		/*
		int reply = JOptionPane.showConfirmDialog(null, "1-Finger: YES;      5-Fingers: NO","Choose One", JOptionPane.YES_NO_OPTION);
		if ( reply ==  JOptionPane.YES_OPTION)
			LeapWriteDemo.SingleFinger = true;
		else
		*/
			LeapWriteDemo.SingleFinger = false;

		/*
		 * int datamode = JOptionPane.showConfirmDialog(null, "Data Collection?:  YES", "Verification Mode?: No",  JOptionPane.YES_NO_OPTION);
		if ( datamode ==  JOptionPane.YES_OPTION)
			LeapWriteDemo.dataMode = true;
		else
			
			LeapWriteDemo.dataMode = false;
			// initialize tables with a new login
		*/
			
		/*int initTable = JOptionPane.showConfirmDialog(null, "Update all Account Info?", null, JOptionPane.YES_NO_OPTION);
		if ( initTable ==  JOptionPane.YES_OPTION)
			LeapWriteDemo.reInitializeTables();
		else
		*/
			LeapWriteDemo.initializeTables();
	}

	public void frameDesign(){

		setTitle("LeapGesture");
		Container contentPane = getContentPane( );
		contentPane.setBackground(DrawTraceSample.bkGround);


		JButton stopButton = new JButton("Proceed[Enter]");
		stopButton.setBounds(130, 178, 158, 30);
		stopButton.addActionListener(this);
		getContentPane().setLayout(null);
		contentPane.add(stopButton);
		this.getRootPane().setDefaultButton(stopButton); // enter Key?

		JButton goButton = new JButton("Cancel");
		goButton.setBounds(130, 217, 158, 30);
		goButton.addActionListener(this);
		contentPane.add(goButton);

		JLabel lblWelcomeToLeapwrite = new JLabel("Welcome to LeapGesture");
		lblWelcomeToLeapwrite.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblWelcomeToLeapwrite.setForeground(DrawTraceSample.foreGround);
		lblWelcomeToLeapwrite.setBounds(100, 11, 364, 45);
		getContentPane().add(lblWelcomeToLeapwrite);

		JLabel lbluserName = new JLabel("userName: ");
		lbluserName.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbluserName.setForeground(DrawTraceSample.foreGround);
		lbluserName.setBounds(10, 75, 103, 45);
		getContentPane().add(lbluserName);

		JLabel lblpassCode = new JLabel("passCode:");
		lblpassCode.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblpassCode.setForeground(DrawTraceSample.foreGround);
		lblpassCode.setBounds(10, 120, 103, 45);
		getContentPane().add(lblpassCode);	
		lblpassCode.setVisible(false);

		textFielduserName = new JTextField();
		textFielduserName.setBounds(112, 86, 198, 23);
		getContentPane().add(textFielduserName);
		textFielduserName.setColumns(10);

		textFieldpassCode = new JTextField();
		textFieldpassCode.setColumns(10);
		textFieldpassCode.setBounds(112, 131, 198, 23);
		getContentPane().add(textFieldpassCode);
		textFieldpassCode.setVisible(false);

		JLabel lblInvalidpassCode = new JLabel("*userName and passCode can be any string without underscore " + "\"_\""  +  ".");
		lblInvalidpassCode.setVisible(true);
		lblInvalidpassCode.setBounds(10, 260, 450, 25);
		getContentPane().add(lblInvalidpassCode);
		
		setVisible(true);
	}

	public String getuserName(){
		return userName;
	}
	public String getpassCode(){
		return passCode;

	}




	@Override
	public void actionPerformed(ActionEvent e)
	{

		if (e.getActionCommand( ).equals("Proceed[Enter]")){

			
			File newDir = new File(savePath);
			if (!newDir.exists()){
				newDir.mkdirs();
			} 
			
			System.out.println(savePath);

			userName = textFielduserName.getText();
			if(userName.length() < 1) passCode = "";
			else passCode = userName.substring(0, 1);
			//passCode = textFieldpassCode.getText();			
			System.out.println("Welcome!" + userName);
			

			//load the feature.
			if (returnUser) {
				String datapath = "C:\\jing2017\\data\\train\\feature\\";
				try {
					loadFeautre(datapath, userName, featureDimension);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			if (LeapWriteDemo.SingleFinger){

				DrawTraceSample.sigNum = -100;

				if (userName.contains("_") || passCode.contains("_")){
					JOptionPane.showMessageDialog(textFieldpassCode, "Cannot use underscore " + "\"_\" !");
				} else if(userName.length()>=1 && passCode.length() >=0){ //if (userName.contains("_") || passCode.contains("_")){
					DrawTraceSample drawsample = null;
					DrawTraceSample.sigNum = DrawTraceSample.searchFiles(savePath, userName, passCode);
					System.out.println("The savePath is: " + savePath);
					//System.out.println("the sigNum of current user is: " + DrawTraceSample.sigNum);
					if (DrawTraceSample.sigNum  == -1){
						JOptionPane.showMessageDialog(textFieldpassCode, "Invalid user name! Please change to another one!");
					} else if (DrawTraceSample.sigNum  == 0){
						newUser = true;
						System.out.println("Welcome, new user!");

						DrawTraceSample.frameDesign();
						this.setVisible(false);
						drawsample =  new DrawTraceSample( );
						drawsample.setLocationRelativeTo(null);

					} else{
						newUser = false;
						DrawTraceSample.frameDesign();
						this.setVisible(false);
						drawsample =  new DrawTraceSample( );
						drawsample.setLocationRelativeTo(null);
					}
				}

				else { //if (userName.contains("_") || passCode.contains("_")){
					JOptionPane.showMessageDialog(textFieldpassCode, "Please input a user name and a passCode!");
				}

			}else{//if (LeapWriteDemo.SingleFinger)
				DrawMultiFigure.sigNum = -100;

				if (userName.contains("_") || passCode.contains("_")){
					JOptionPane.showMessageDialog(textFieldpassCode, "Cannot use underscore " + "\"_\" !");
				} else if(userName.length()>=1 && passCode.length() >=0){
					//if (userName.contains("_") || passCode.contains("_")){
					DrawMultiFigure.sigNum = DrawTraceSample.searchFiles(savePath, userName, passCode);
					System.out.println("The savePath is: " + savePath);
					//System.out.println("the sigNum of current user is: " + DrawTraceSample.sigNum);
					if (DrawMultiFigure.sigNum  == -1){
						JOptionPane.showMessageDialog(textFieldpassCode, "Invalid user name! Please change to another one!");
					} else if (DrawMultiFigure.sigNum  == 0){
						newUser = true;
						System.out.println("Welcome, new user!");

						DrawMultiFigure.frameDesign();
						this.setVisible(false);

					} else{
						newUser = false;
						DrawMultiFigure.frameDesign();
						this.setVisible(false);
					}
				}

				else { //if (userName.contains("_") || passCode.contains("_")){
					JOptionPane.showMessageDialog(textFieldpassCode, "Please input a user name and a passCode!");
				}


			}



		} else if (e.getActionCommand( ).equals("Cancel")){

			userName = null;
			passCode = null;
			textFielduserName.setText("");
			textFieldpassCode.setText("");



		}
		//contentPane.setBackground(Color.GREEN);
		else
			System.out.println("Error in interface.");
	}
	
	private void loadFeautre(String path, String username, int featureDim) throws IOException {
		// TODO Auto-generated method stub
		
		map = new HashMap<>();
		for (int i = 0; i < 8; i++) {
			map.put(i, new ArrayList<List<Double[]>>());
			File folder = new File(path + i + "\\");
		
			File[] files = folder.listFiles();
			for (File f : files) {
				if (f.getName().contains(username)) {
						map.get(i).add(loadAFile(f,featureDim));
				}
			}
			
		}
		
		
	}

	private List<Double[]> loadAFile(File f, int dim) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = "";
		List<Double[]> list = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			 StringTokenizer token = new StringTokenizer(line, ",", false);
			 Double[] d = new Double[dim];
			 int cnt = 0;
			 while (token.hasMoreElements()) {
				 d[cnt++] = (double) Float.parseFloat(token.nextToken());
			 }
			 list.add(d);
		}
		
		return list;
	}



}

