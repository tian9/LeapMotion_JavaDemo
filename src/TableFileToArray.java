

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author 
 * The class is just converted saved file (Normed Features) into arrays
 *
 */
public class TableFileToArray {

	File tableFile ;
	int totalSeqNum;
	float[][] floatArray;


	public TableFileToArray(File tableFile, int totalSeqNum){
		this.tableFile = tableFile;
		this.totalSeqNum = totalSeqNum;
	
	}


	public float[][] readLines() throws IOException {


		FileReader fileReader = new FileReader(tableFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		fileReader.close();
	

		String[] fileString = lines.toArray(new String[lines.size()]);

		floatArray = new float[totalSeqNum][totalSeqNum];

		for (int i = 0; i < totalSeqNum; i++){
			String[] lineString = fileString[i].split(",");
			//System.out.println(lineString.length);
			for (int j = 0; j < totalSeqNum; j++){
				floatArray[i][j] = Float.parseFloat(lineString[j]); 
			}
		}

		return floatArray;
	}


	@Override
	public String toString(){

		//sigFeatureNormed = getNormedFeature();

		String s = "";
		// to-do:
		//sigFeatureNormed
		for (int i = 0; i < floatArray.length; i++){
			String sTemp = "";
			for (int j = 0; j< floatArray[i].length; j++){

				sTemp +=  floatArray[i][j];
				sTemp += ", ";
			}
			s += sTemp;
			s += "\n";
		}		

		return s;
	}


}
