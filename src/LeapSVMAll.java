import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import jnisvmlight.LabeledFeatureVector;
import jnisvmlight.SVMLightModel;
import jnisvmlight.SVMLightInterface;
import jnisvmlight.TrainingParameters;

public class LeapSVMAll {

	//TableDTW.totalSeqNum -1; // number of training docs
	public static int N = TableDTW.totalSeqNum - 1;
	public static int M = N; // max. number of features per doc
	static int x,y; // counters for testing data(x) and training data(y)
	public  String currUserName;
	public  boolean verified = false;

	public LeapSVMAll(String currUserName){
		this.currUserName = currUserName;  
	}

	public boolean getResults() throws Exception{
		x = trainTest();
		return this.verified;
	}

	public  int trainTest() throws Exception {
		// The trainer interface with the native communication to the SVM-light 
		// shared libraries
		SVMLightInterface trainer = new SVMLightInterface();

		// The training data
		LabeledFeatureVector[] traindata = new LabeledFeatureVector[N];
		LabeledFeatureVector[] testdata = new LabeledFeatureVector[M];


		// Sort all feature vectors in ascedending order of feature dimensions
		// before training the model
		SVMLightInterface.SORT_INPUT_VECTORS = true;

		x = 1; y = 1;
		for (int i = 0; i < TableDTW.totalSeqNum; i++) {
			//each sample (a row)
			int[] dims = new int[TableDTW.totalSeqNum - 1];
			double[] values = new double[TableDTW.totalSeqNum - 1];


			// Fill the vectors
			String s = "";
			for (int j = 0; j < TableDTW.totalSeqNum -1; j++) {
				dims[j] = j+1;
				values[j] = TableDTW.tableDTW[i][j];
				s = s + "," + TableDTW.tableDTW[i][j];
			}
			//System.out.println("The values are" + s  + "\n");

			// Store dimension/value pairs in new LabeledFeatureVector object

			UserDataSeq ud =  UserDataAll.existedUserData.get(i);
			String userName = ud.userData.userName;

			//if(i != TableDTW.totalSeqNum - 1){
			if (userName.equals(currUserName)){

				if (i%2 ==0){
					traindata[y] = new LabeledFeatureVector(+1, dims, values);
					traindata[y].normalizeL2();
					y = y+1;
					System.out.print("TableDTW.totalSeqNum: " + TableDTW.totalSeqNum);
					System.out.print("   The i is : " + i);
					System.out.print("   The y is : " + y);
					//System.out.println("    traindata.length:  " + traindata.length);
				} else {
					testdata[x] = new LabeledFeatureVector(+1, dims, values);
					testdata[x].normalizeL2();
					x = x+1;
					System.out.print("TableDTW.totalSeqNum: " + TableDTW.totalSeqNum);
					System.out.print("   The i is : " + i);
					System.out.print("   The x is : " + x);
				}
				//System.out.println(traindata[i].toString());

			} else{
				if (i%2 ==0){
 					traindata[y] = new LabeledFeatureVector(-1, dims, values);
					traindata[y].normalizeL2(); 
					y = y+1;
				}
				else {
					testdata[x] = new LabeledFeatureVector(-1, dims, values);
					testdata[x].normalizeL2();
					x = x+1;
				}
			} // end if (userName.equals(currUserName))

		} // end for (i)

		saveSVMfeature(dataToString(traindata, testdata));



		// Initialize a new TrainingParamteres object with the default SVM-light
		// values
		TrainingParameters tp = new TrainingParameters();

		// Switch on some debugging output
		tp.getLearningParameters().verbosity = 1;

		System.out.println("\nTRAINING SVM-light MODEL ..");


		LabeledFeatureVector[] traindata_actual = new LabeledFeatureVector[y-1];
		for (int i =1; i<y; i++){
			traindata_actual[i-1] = traindata[i];
		}


		SVMLightModel model = trainer.trainModel(traindata_actual, tp);
		System.out.println(" DONE.");

		// Use this to store a model to a file or read a model from a URL.
		//model.writeModelToFile("jni_model.dat");

		//model = SVMLightModel.readSVMLightModelFromURL(new java.io.File("jni_model.dat").toURL());
		//model = SVMLightModel.readSVMLightModelFromURL(new java.io.File("jni_model.dat").toURL());

		// Use the classifier on the randomly created feature vectors
		System.out.println("\nVALIDATING SVM-light MODEL in Java..");


		//setUp a Threshold
		double threshold = - 0.97; 
		int tpn = 0, tnn = 0, fpn = 0, fnn = 0;
		for (int i =1; i<x; i++){
			double d = model.classify(testdata[i]);
			if (d > threshold){
				d = 0.5;
			}
			if (testdata[i].getLabel() * d > 0 ){ // correctly predicted

				System.out.format("Correct!  The " + i + "-th 's label is %f", d);
				if (testdata[i].getLabel() > 0) {// TP
					tpn++;
					System.out.println("   Valid User");
				}
				else { // TN
					tnn++;
					System.out.println("   ATTACKERS!!");
				}

			} else{ // wrongly predicted

				System.out.format("WRONG!  The i-th " + i + "-th 's label is %f", d);
				if (testdata[i].getLabel() > 0) {//FN
					fnn++;
					System.out.println("   Valid User");
				}
				else  {//FP
					fpn++;
					System.out.println("   ATTACKERS!!");
				}
			} //end if
		} // end for (i)
		System.out.println( "tpn = " + tpn + ", tnn = " + tnn + ", fpn = " + fpn + ", fnn = " + fnn);
		double precision = (double) tpn / (tpn + fpn);
		double recall  = (double) tpn / (tpn + fnn);
		System.out.println("Precision of " + currUserName + ": " + precision);
		System.out.println("Recall of " + currUserName + ": " + recall);

		return x; //the number of test samples.

	}// end traintest()




	public static String dataToString(LabeledFeatureVector[] traindata, LabeledFeatureVector[] testdata){
		String  s= "";
		for (int i = 1; i < y; i++){
			s = s + traindata[i].toString();
			s = s + "\n";
		}

		s = s + "*********************" +
				"*******************"  + "\n";

		for (int i = 1; i < x; i++){
			s = s + testdata[i].toString();
			s = s + "\n";
		}

		return s;

	}

	public static void saveSVMfeature(String content ){
		Writer writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(LeapWriteDemo.svmFeatureFile));
			//String content = Integer.toString(num); 
			writer.write(content);
		} catch (IOException ex) {
			// report
		} finally {
			try {writer.close();} catch (Exception ex) {}
		}
	}
}
