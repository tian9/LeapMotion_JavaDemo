package main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;


public class UserDataAll {
	
	public static LinkedList<UserDataSeq> existedUserData = new LinkedList<UserDataSeq>();;
	public static ArrayList<String> existedUserNames = new ArrayList<String>();
	
	
	public static LinkedList<UserDataSeq> createAccount(){
		
		File dir = new File(LeapWriteWin.savePath);
		File[] dirListing = dir.listFiles();
		int seqNum = -1;
		if(dirListing != null){
			existedUserData = new LinkedList<UserDataSeq>();
			for (File allFile: dirListing){
				String currentFileName = allFile.getName();

				if (currentFileName.endsWith("NormedFeature.txt") && !currentFileName.startsWith("FAILED")){
					String[] fileNameSplit = currentFileName.split("_");
					//System.out.println(currentFileName);
					String userName =  fileNameSplit[0];
					String passCode = fileNameSplit[1];
					int currentSigNum = Integer.parseInt(fileNameSplit[2]);
					if (!existedUserNames.contains(userName))
						existedUserNames.add(userName);
					UserDataSeq currentUserData = new UserDataSeq(userName,passCode, currentSigNum);
					
					// SET a sequence number to each one.
					seqNum = seqNum +1;
					currentUserData.setSequancialNum(seqNum);
					
				existedUserData.add(currentUserData);

				} // end if 
			} //end for

			// existing total signatures!
			TableDTW.totalSeqNum = seqNum + 1;
			return existedUserData;

		} else {
			return null;
		}		

	} // end of allFiles
	
	public static UserData getUserData(int seqNum){
		return existedUserData.get(seqNum).userData;
		
	}	
	public static void addAccount(int totalSeqNum, UserData currSig){
		existedUserData.add(new UserDataSeq(totalSeqNum,currSig));
		
	}
	
	
	
	public static void deleteAccount(int totalSeqNum){

		existedUserData.remove(totalSeqNum-1);

	}
	
	
	public static void saveAccount(File file){

		
		String contentFrame = UserDataAll.toString1();
		
		DrawTraceSample.writeToFile(contentFrame, file);
	
	}
	
	
	//accountFile
	
	public static void readAccount(){		
		if (LeapWriteDemo.accountFile.exists()){
			AccountFileToList accountList = new AccountFileToList();
			try {
				LinkedList<UserDataSeq> temp = accountList.readLines();

				// COPY THE READED TABLE TO THE tableDTW
				for(int i=0; i<temp.size(); i++){
					existedUserData.add(temp.get(i));
				}
			} catch (IOException e) {
				// 
				e.printStackTrace();
			}

		} else {
			System.out.println("accountFile does Not exist!");
		}

	}
	
	
	public static String toString1(){
		String s = "";
		for (UserDataSeq ud: existedUserData){
			s = s + ud.toString();
		}
		return s;
	}
	
	
}
