import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


public class LeapWriteDemo {
	/**
	 * @param args
	 */
	//static int num = 0;
	public static File seqFile,  tableFile, svmFeatureFile;
	public static File accountFile = null; 

	public static boolean SingleFinger = true; 
	public static boolean dataMode = true;

	public static boolean testSVMHalfData = false;

	public static File jarFile;

	public static void main(String[] args) {

		jarFile = null;
		try {
			jarFile = (new File(LeapWriteDemo.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		// initialize the log in window
		
		LeapWriteWin loginWin =  new LeapWriteWin( );
		loginWin.frameDesign();
		loginWin.setVisible(true);
		loginWin.setLocationRelativeTo(null);
		
		// for shortcut of finish and start button
				KeyboardFocusManager.getCurrentKeyboardFocusManager()  
				.addKeyEventDispatcher(new KeyEventDispatcher(){  
					public boolean dispatchKeyEvent(KeyEvent e){  
						if(e.getID() == KeyEvent.KEY_PRESSED)  
						{  
							if(e.getKeyCode() == KeyEvent.VK_F7 ) DrawMultiFigure.startKey();  
							if(e.getKeyCode() == KeyEvent.VK_F8 )
								try {
									DrawMultiFigure.finishKey();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
						}  
						return false;}}); 


		System.out.println("Hi There, Let's Start Testing");
		// 
		if(testSVMHalfData){
			String[] userNames= {"jing-circles","jingjing", "songwang","nihao","wang","tian","wu","xian","qian","huan","bao","dian", 
					"tai", "ni","jingjing","one", "two", "three","four", "five"};
			
			for (int i = 0; i < userNames.length;  i++){

				System.out.println(userNames[i] + "*******" + userNames.length); 

				LeapSVMAll lpSVM = new LeapSVMAll(userNames[i]);
				try {
					lpSVM.trainTest();
					System.out.println("End of Test for: " + userNames[i]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} //end for
		} //end if(testSVMHalfData)

	}// end main







	public static void initializeTables(){
		System.out.println(jarFile);

		if(LeapWriteDemo.SingleFinger){

			LeapWriteWin.savePath = jarFile.getParentFile().getPath() + "\\Signatures\\";
			System.out.println(LeapWriteWin.savePath);
		} else {
			LeapWriteWin.savePath = jarFile.getParentFile().getPath() + "\\MultiFinger\\";
			System.out.println(LeapWriteWin.savePath);
		}

		LeapWriteDemo.seqFile =new File(LeapWriteWin.savePath + "0_sequencialNum");
		LeapWriteDemo.accountFile = new File(LeapWriteWin.savePath + "0_Account");
		LeapWriteDemo.tableFile = new File(LeapWriteWin.savePath + "0_TableDTW");
		LeapWriteDemo.svmFeatureFile = new File(LeapWriteWin.savePath + "0_svmFeatureFile");

		if (LeapWriteDemo.seqFile.exists())
			TableDTW.totalSeqNum = SeqNum.readSeqNum();
		else {
			SeqNum.createSeqNum();
			SeqNum.saveSeqNum(TableDTW.totalSeqNum);
		}
		System.out.println(TableDTW.totalSeqNum);


		if (LeapWriteDemo.accountFile.exists()){
			UserDataAll.readAccount();
			System.out.println("Read ACCOUNT INFO from the existing file");

		} else{
			UserDataAll.createAccount();
			UserDataAll.saveAccount(LeapWriteDemo.accountFile);	
			System.out.println("Saved the account info");
		}


		if (LeapWriteDemo.tableFile.exists()){
			TableDTW.readTable();
			System.out.println("Read DTW Table from the existing file");
		} else{
			System.out.println("Try to create a  DTW Table");
			TableDTW.createTable(TableDTW.totalSeqNum);
			System.out.println("Created DTW Table");
			TableDTW.saveTable(LeapWriteDemo.tableFile, TableDTW.tableDTW, TableDTW.totalSeqNum);
			System.out.println("Saved DTW Table");
		}

	} // initialize tables.

	public static void reInitializeTables(){
		System.out.println(jarFile);
		if(LeapWriteDemo.SingleFinger){
			LeapWriteWin.savePath = jarFile.getParentFile().getPath() + "\\Signatures\\";
			System.out.println(LeapWriteWin.savePath);
		} else {
			LeapWriteWin.savePath = jarFile.getParentFile().getPath() + "\\MultiFinger\\";
			System.out.println(LeapWriteWin.savePath);
		}

		LeapWriteDemo.seqFile =new File(LeapWriteWin.savePath + "0_sequencialNum");
		LeapWriteDemo.accountFile = new File(LeapWriteWin.savePath + "0_Account");
		LeapWriteDemo.tableFile = new File(LeapWriteWin.savePath + "0_TableDTW");
		LeapWriteDemo.svmFeatureFile = new File(LeapWriteWin.savePath + "0_svmFeatureFile");

		SeqNum.createSeqNum();
		SeqNum.saveSeqNum(TableDTW.totalSeqNum);
		System.out.println("Saved Sig/guesture Num" + TableDTW.totalSeqNum);

		UserDataAll.createAccount();
		UserDataAll.saveAccount(LeapWriteDemo.accountFile);	
		System.out.println("Saved the account info");

		System.out.println("Try to create a DTW Table");
		TableDTW.createTable(TableDTW.totalSeqNum);
		System.out.println("Created DTW Table");
		TableDTW.saveTable(LeapWriteDemo.tableFile, TableDTW.tableDTW, TableDTW.totalSeqNum);
		System.out.println("Saved the DTW Table");
		
		
		if(testSVMHalfData){
			for (String userName: UserDataAll.existedUserNames) {
	

				System.out.println(userName + "*******" + UserDataAll.existedUserNames.size()); 

				LeapSVMAll lpSVM = new LeapSVMAll(userName);
				try {
					lpSVM.trainTest();
					System.out.println("End of Test for: " + userName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} //end for
		} //end if(testSVMHalfData)

	} //end re-initialize tables
	


}// end class
