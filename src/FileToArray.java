
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author 
 * The class is just converted saved file (Normed Features) 
 * into arrays
 *
 */
public class FileToArray {

	String filename ;
	int featureDim;
	float[][] floatArray;
	
	public FileToArray(String savePath, String userName, String passCode, int sigNum){

		this.filename = savePath +  userName.toLowerCase() + "_" 
				+ passCode + "_" + sigNum +"_NormedFeature.txt";
	}
	
	public FileToArray(String filename){

		this.filename = filename;
	}



	public float[][] readLines() throws IOException {
		FileReader fileReader = new FileReader(filename);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		fileReader.close();
	

		String[] fileString = lines.toArray(new String[lines.size()]);
		
		featureDim = fileString[0].split(",").length -1;
		
		floatArray = new float[fileString.length - 1][featureDim];

		for (int i = 0; i < fileString.length - 1; i++){
			String[] lineString = fileString[i].split(",");
			for (int j = 0; j < featureDim; j++){
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
