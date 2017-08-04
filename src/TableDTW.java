import java.io.File;
import java.io.IOException;


public class TableDTW {

	public static final int TABLESIZE =  1000;
	public static float[][] tableDTW = new float[TABLESIZE][TABLESIZE];;
	public static int totalSeqNum;

	// calculate tableDTW
	public static void createTable(int totalSeqNum){	
		int upperBoundDTW;
		if (LeapWriteDemo.SingleFinger)
			upperBoundDTW = 5000;
		else 
			upperBoundDTW = 200000;
		
		// initialize tableDTW
		for (int i = 0; i < tableDTW.length; i++){
			for(int j = 0; j < tableDTW[i].length; j++){
				tableDTW[i][j] = -999;
			}
		}

		FileToArray[] currSig = new FileToArray[totalSeqNum];
		float[][] currSigArr, refSigArr;
		DTW dtw = null;
		int ii = 0;	
		//System.out.println(totalSeqNum);
		//System.out.println(UserDataAll.existedUserData.size());
		for (UserDataSeq ud_i: UserDataAll.existedUserData){
			currSig[ii] = new FileToArray(LeapWriteWin.savePath,ud_i.userData.userName,ud_i.userData.passCode,ud_i.userData.sigNum);
			ii++;
		}// end for
		

		try {
			for (int i = 0; i < totalSeqNum; i++){
				currSigArr = currSig[i].readLines();
				for (int j = 0; j <= i; j++){
					if (j==i)
						tableDTW[i][j] = 0;
					
					refSigArr = currSig[j].readLines();
					dtw = new DTW(currSigArr, refSigArr);
					tableDTW[i][j] = dtw.getCost();
					if (tableDTW[i][j] >= upperBoundDTW)
						tableDTW[i][j]  = upperBoundDTW;
				}
				System.out.println("DTW Table row" + i + ", ");
			}// end for
			System.out.println("HALF DTW Table Created");

			for (int i = 0; i < totalSeqNum; i++){
				for (int j = i+1; j < totalSeqNum; j++){
					tableDTW[i][j] = tableDTW[j][i];
				}
			}// end for
			
			System.out.println("DTW Table Created");

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception");
		} // end try

	}

	// if we have a new sig, add the column and then increase the seqNum 

	public static void addTable(File file, int totalSeqNum, float[][] currSigArr){

		int upperBoundDTW;
		if (LeapWriteDemo.SingleFinger)
			upperBoundDTW = 5000;
		else 
			upperBoundDTW = 20000;
		
		FileToArray[] currSig = new FileToArray[totalSeqNum];
		float[][] refSigArr;
		DTW dtw = null;

		for (int i = 0; i < totalSeqNum; i++){
			UserData ud_i = UserDataAll.getUserData(i);			
			//System.out.println(ud_i.userName);
			currSig[i] = new FileToArray(LeapWriteWin.savePath,ud_i.userName,ud_i.passCode,ud_i.sigNum);

		}// end for

		try {
			for (int i = 0; i < totalSeqNum; i++){
				refSigArr = currSig[i].readLines();
				dtw = new DTW(currSigArr, refSigArr);
				tableDTW[totalSeqNum][i] = dtw.getCost();
				if (tableDTW[totalSeqNum][i] >= upperBoundDTW)
					tableDTW[totalSeqNum][i]  = upperBoundDTW;
				tableDTW[i][totalSeqNum] = tableDTW[totalSeqNum][i];
				
			}// end for
			tableDTW[totalSeqNum][totalSeqNum] = 0;
			TableDTW.totalSeqNum ++;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public static void deleteTable(File file, int totalSeqNum){

		for (int i = 0; i < totalSeqNum; i++){

			tableDTW[totalSeqNum-1][i] = -999;
			tableDTW[i][totalSeqNum-1] = -999;

		}// end for

		TableDTW.totalSeqNum--;
		

	}


	public static void saveTable(File file, float[][] tableDTW, int totalSeqNum){
		String contentFrame = "";

		for (int i = 0; i< totalSeqNum; i++){
			for (int j = 0; j < totalSeqNum; j++){
				if (j != totalSeqNum-1){
					contentFrame = contentFrame + tableDTW[i][j] +",  ";
				}else {
					contentFrame = contentFrame + tableDTW[i][j];
				}

			}// end j
			contentFrame = contentFrame + "\n";
		}

		DrawTraceSample.writeToFile(contentFrame, file);
		System.out.println("DTW Table Saved to File");


	}


	// read the file into float[][];
	public static void readTable(){
		if (LeapWriteDemo.tableFile.exists()){
			TableFileToArray tableArray = new TableFileToArray(LeapWriteDemo.tableFile, TableDTW.totalSeqNum);
			try {
				float[][] temp = tableArray.readLines();

				// COPY THE READED TABLE TO THE tableDTW
				for(int i=0; i<temp.length; i++)
					for(int j=0; j<temp[i].length; j++)
						tableDTW[i][j]=temp[i][j];


			} catch (IOException e) {
				// 
				e.printStackTrace();
			}

		} else {
			System.out.println("tableFile does Not exist!");
		}

	}



}
