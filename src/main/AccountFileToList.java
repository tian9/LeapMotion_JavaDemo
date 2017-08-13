package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * 
 * @author 
 * The class is just converted saved file (Normed Features) into arrays
 *
 */
public class AccountFileToList {

	//File accountFile ;
	//int totalSeqNum;
	LinkedList<UserDataSeq> existedUserData;

	public LinkedList<UserDataSeq> readLines() throws IOException {

		FileReader fileReader = new FileReader(LeapWriteDemo.accountFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		fileReader.close();
	

		String[] fileString = lines.toArray(new String[lines.size()]);
		//System.out.println("The line is:" + fileString[0]);
		existedUserData = new LinkedList<UserDataSeq>();
		
		for (int i = 0; i < fileString.length; i++){
			String[] lineString = fileString[i].split("--");
			
			existedUserData.add(new UserDataSeq(Integer.parseInt(lineString[0]),lineString[1],lineString[2],Integer.parseInt(lineString[3])));

		}

		return existedUserData;
	}


}
