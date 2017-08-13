package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
/**
 * 
 * the DTW distance of current sig to exisiting sig, it claims.
 * return 
 * exiting 
 * @author jingtian
 *
 */

public class Similarity {
	Map<Integer, Float> dismap = new HashMap<>();
	public float indis = 0;
	public float dis = 0;
	public static List<Float> disAll = new ArrayList<>();
	public static File disfile = new File("disMap.txt");
	
	public Similarity(String savePath, String userName) throws IOException {
		RawFiles2Map rf2map = new RawFiles2Map(savePath, userName, "20170803");
		dismap = insideSimilarity(rf2map.map);
		write(dismap, userName);
	}
	
	public void write(Map<Integer, Float> m, String username) throws IOException {
		
	    disfile.createNewFile();
	    FileWriter writer = new FileWriter(disfile, true); 
	      
	    // Writes the content to the file
	    for (int key : m.keySet()) {
	    	writer.write(String.format("%s : %d : %.2f\n", username, key, m.get(key))); 
	    }
	    writer.flush();
	    writer.close();
	    
	}
	
	public Map<Integer, Float> readDis(String username) throws IOException {
		// Creates a FileReader Object
		Map<Integer, Float> map = new HashMap<>();
	    FileReader fr = new FileReader(disfile); 
	    BufferedReader bufferedReader = new BufferedReader(fr);
	    String line = null;
	    while ((line = bufferedReader.readLine()) != null) {
	    	String[] strs = line.split(" : ");
	    	if(strs[0].equals(username)) {
	    		map.put(Integer.parseInt(strs[1]), Float.parseFloat(strs[2]));
	    	}
	    	
	    }

	    fr.close();
		return map;
	}
	
	public Similarity(OneGesture currGes, String savePath, String userName, String date, int GestureNum) throws IOException {
		RawFiles2Map rf2map = new RawFiles2Map(savePath, userName, date);
		System.out.println("instance map.size() " + GestureNum);
		if (dismap.size() == 0) {
			dismap = insideSimilarity(rf2map.map);
		}
		if (dismap.containsKey(GestureNum)) {
			indis = dismap.get(GestureNum);
		}
		float[][] f = currGes.getNormedFeature();
		List<Float> disA = new ArrayList<>();
		System.out.println(GestureNum);
		for (List<OneHandFrame> list : rf2map.map.get(GestureNum)) {
			OneGesture one = new OneGesture(list);
			float[][] f2 = one.getNormedFeature(); 
			DTW dtw = new DTW(f, f2);
			disA.add(dtw.costValue);
		}
		float sum = 0;
		for (float d : disA) sum += d;
		dis = sum / disA.size();
		disAll.add(dis);
	}
	
	public Map<Integer, Float> insideSimilarity(Map<Integer, List<List<OneHandFrame>>> map) {
		for (int gNum : map.keySet()) {
			List<List<OneHandFrame>> samples = map.get(gNum);
			List<Float> disA = new ArrayList<>();
			
			for (List<OneHandFrame> aS : samples) {
				OneGesture ges = new OneGesture();
				ges.handFrameList = aS;
				float[][] f1 = ges.getNormedFeature();
				for (List<OneHandFrame> aS2 : samples) {
					OneGesture ges2 = new OneGesture();
					ges2.handFrameList = aS2;
					float[][] f2 = ges2.getNormedFeature();
					DTW dtw = new DTW(f1,f2);
					disA.add(dtw.costValue);
				}
			}
			//to-do, for a reasonable distance
			//int cnt = 0;
			float sum = 0;
			List<Float> list = new ArrayList<>();
			for (float f : disA) {
				if (f - 0 < 0.02) continue;
				System.out.print(f + "->");
				list.add(f);
				sum += f;
			}
			float fAve = sum / list.size(); 
			
			
			dismap.put(gNum, fAve);
		}
			
		return dismap;
	}
	
	//public String toString(String userName) {
		
	//}
}
