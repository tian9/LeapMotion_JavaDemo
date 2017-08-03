import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class SeqNum {
	static int num = 0;
	
		// read the sequencial number
		public static int readSeqNum(){

			try {		
				FileReader fileReader = new FileReader(LeapWriteDemo.seqFile);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				//num 
				String s = bufferedReader.readLine();
				num = Integer.parseInt(s);
				//System.out.println("the read in number is: " + );
				bufferedReader.close();
				fileReader.close();

			} catch (IOException e) {
				// 
				e.printStackTrace();
			}

			return num;
		}

		/**
		 * 
		 * @return
		 */
		public static int createSeqNum(){
			
			File dir = new File(LeapWriteWin.savePath);
			File[] dirListing = dir.listFiles();
			if(dirListing != null){
				num = 0;
				for (File allFile: dirListing){
					String currentFileName = allFile.getName();				
					if (currentFileName.endsWith("_NormedFeature.txt") && !currentFileName.startsWith("FAILED")){
						num ++;
					}
				}
				TableDTW.totalSeqNum = num;

			}
			return num;

		} // end method

		/**
		 * 
		 * @param file
		 * @param num
		 */
		public static void saveSeqNum(int num1){
			FileWriter writer = null;
			try {
				writer = new FileWriter(LeapWriteDemo.seqFile);
				String content = Integer.toString(num1); 
				writer.write(content);
			} catch (IOException ex) {
				// report
			} finally {
				try {writer.close();} catch (Exception ex) {}
			}
		}

}
