
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Write2Feature {
	
	public void write(String savePath, String userName, String date) throws IOException {
		System.out.println(savePath);
		RawFiles2Map maps = new RawFiles2Map(savePath + "raw\\", userName, date);
		for (int gesNum : maps.map.keySet()) {
			int cnt = 0;
			for (List<OneHandFrame> list : maps.map.get(gesNum)) {
	
				OneGesture one = new OneGesture(list);
				float[][] f = one.getNormedFeature();
				String fileName = "feature_" + userName + "_" + gesNum 
								+ '_' + cnt + ".csv";
				write2file(new File(savePath + "feature\\" + fileName), f);
					
				cnt++;
			}
		}
	}
	public void writeTrainPCA(File file, String savePath, String userName, String date) throws IOException {
		System.out.println(savePath);
		RawFiles2Map maps = new RawFiles2Map(savePath, userName, date);
		for (int gesNum : maps.map.keySet()) {
			//for (List<OneHandFrame> list : maps.map.get(gesNum)){
				List<OneHandFrame> list = maps.map.get(gesNum).get(0);
				OneGesture one = new OneGesture(list);
				float[][] f = one.getNormedFeature();
				write2file(file, f);
			//}
			
		}
	}
	
	private void write2file(File file, float[][] f) throws IOException {
		// 
		FileWriter writer = new FileWriter(file, false); 
	      
	    // Writes the content to the file
		StringBuilder sb = new StringBuilder();
	    for (float[] ff : f) {
	    	for (float fff : ff) {
	    		if (Float.isNaN(fff)) {
	    			sb.append(0.0).append(",");
	    			//System.out.println(sb.toString());
	    		} else {
	    			sb.append(fff).append(",");
	    		}
	    	}
	    	sb.append("\n");
	    }
	    writer.write(sb.toString()); 
	    
	    writer.flush();
	    writer.close();
	    
	}
	
	public static void mergefile(String path, String subpath, int gesNum, int step) throws IOException {
		File folder = new File(path + subpath + "\\" + gesNum + "\\");
		File[] files = folder.listFiles();
		Writer wr = new FileWriter(path + subpath + "_" + gesNum + ".csv", true);
		for (int i = 0; i < files.length; i = i + step) {
			System.out.println(files[i].getName());
			Scanner sc = new Scanner(files[i]);
			while (sc.hasNext()) {
				wr.write(sc.nextLine() + "\n");
			}
		}
		wr.close();
	}


	public static void main(String[] args) throws IOException {
		// 
		Write2Feature ins = new Write2Feature();
		
		
		String[] allnames = {"chaohao", "jingtian", "jichuanzhang", "laixiaohan", "linzhenpeng", "luxin", "mazhuoran", "xinyan", "yanchen", "zhangguoming", "zhangtianchen", "zhangyanmiao", "zhuang"};
		//String[] allnames = {};
		//File file = new File("C:\\Users\\jingtian\\workspace\\leap_DEMO\\MultiFinger\\train\\jingtian_7sample.csv");
		String path = "C:\\Users\\jingtian\\workspace\\leap_DEMO\\MultiFinger\\train\\";
		
		for (int i = 2; i < 8; i++) {
		//merge some files
			ins.mergefile(path, "feature", i, 5);
		}
		/*
		for (String userName : allnames) {
			 //write to each files
			ins.write(path, userName, "20170803");
		}
		*/
	}

}
