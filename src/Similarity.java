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
	
	public Similarity(OneGesture currGes, String savePath, String userName, int GestureNum) throws IOException {
		RawFiles2Map rf2map = new RawFiles2Map(savePath, userName);
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
