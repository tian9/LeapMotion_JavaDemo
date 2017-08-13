package main;

import jnisvmlight.LabeledFeatureVector;
import jnisvmlight.SVMLightModel;
import jnisvmlight.SVMLightInterface;
import jnisvmlight.TrainingParameters;

public class LeapSVMSingleVeri {

	public static int N = TableDTW.totalSeqNum -1; // number of training docs
	
	public static int M = 1; // max. number of features per doc

	public  String currUserName;
	public  static boolean verified = false;

	public LeapSVMSingleVeri(String currUserName) throws Exception{
		this.currUserName = currUserName; 
		trainTest();
	}

	public boolean getResults(){
		return verified;
	}

	public  void trainTest() throws Exception {
		System.out.println("totalSeqNum before train: "+ TableDTW.totalSeqNum);


		// The trainer interface with the native communication to the SVM-light 
		// shared libraries
		SVMLightInterface trainer = new SVMLightInterface();

		// The training data
		LabeledFeatureVector[] traindata = new LabeledFeatureVector[N];
		LabeledFeatureVector[] testdata = new LabeledFeatureVector[M];

		System.out.println("traindata.length:  " + traindata.length);

		// Sort all feature vectors in ascedending order of feature dimensions
		// before training the model
		SVMLightInterface.SORT_INPUT_VECTORS = true;


		for (int i = 0; i < TableDTW.totalSeqNum; i++) {
			//each sample (a row)
			int[] dims = new int[TableDTW.totalSeqNum];
			double[] values = new double[TableDTW.totalSeqNum];


			// Fill the vectors
			for (int j = 0; j < TableDTW.totalSeqNum; j++) {
				dims[j] = j+1;
				values[j] = TableDTW.tableDTW[i][j];
			}

			// Store dimension/value pairs in new LabeledFeatureVector object
			UserDataSeq ud = UserDataAll.existedUserData.get(i);
			String userName = ud.userData.userName;
			if(i != TableDTW.totalSeqNum - 1){
				try {
				if (userName.equals(currUserName)){
					traindata[i] = new LabeledFeatureVector(+1, dims, values);
					traindata[i].normalizeL2();
				} else{
					traindata[i] = new LabeledFeatureVector(-1, dims, values);
					traindata[i].normalizeL2();
					//System.out.println(traindata[i].toString());
				}
				}catch (Exception e){
					System.out.println(e.getMessage());
					System.out.println("i: " + i);
					System.out.println("TableDTW.totalSeqNum: " + TableDTW.totalSeqNum);
					System.out.println("traindata.length: " + traindata.length);
				}
			}else{
				testdata[0] = new LabeledFeatureVector(+1, dims, values);
				testdata[0].normalizeL2();
				System.out.println();
				System.out.println("i = " + i + ".");
			}

		}
					System.out.println(" DONE.");

		//saveSVMfeature(dataToString(traindata, testdata));


		// Initialize a new TrainingParamteres object with the default SVM-light
		// values
		TrainingParameters tp = new TrainingParameters();

		// Switch on some debugging output
		tp.getLearningParameters().verbosity = 1;

		System.out.println("\nTRAINING SVM-light MODEL ..");
//		LabeledFeatureVector[] traindata_actual = new LabeledFeatureVector[N];
//		for (int i =1; i<N+1; i++){
//			traindata_actual[i-1] = traindata[i];
//		}
		SVMLightModel model = trainer.trainModel(traindata, tp);
		System.out.println(" DONE.");

		// Use the classifier on the randomly created feature vectors
		System.out.println("\nVALIDATING SVM-light MODEL in Java..");


		double d = model.classify(testdata[0]);
		System.out.println(d);
		if (d > -0.97){
			d = 0.5;
		}
		if (d < 0){
			verified = false;
		} else
			verified = true;

	}
}
