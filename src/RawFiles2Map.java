import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RawFiles2Map {

		String filename = null;
		//<gesture type, number of samples <sample has number of Frames>>
		Map<Integer, List<List<OneHandFrame>>> map = new HashMap<>();
		
		public RawFiles2Map(String savePath, String userName) throws IOException{
			int len = savePath.length();
			String pattern ="[A-Za-z]+_20170803_[0-9_\\s]*Frame_[0-9]+.txt";//to-do
			Pattern r = Pattern.compile(pattern);
			File folder = new File(savePath); 
			for (File fileEntry : folder.listFiles()) {
				String currFileName = fileEntry.toString();
				String fname = currFileName.substring(len); 
				Matcher matcher = r.matcher(fname);

				if (matcher.matches() && currFileName.indexOf(userName + "_") >= 0) {
					System.out.println("currFileName: " + currFileName);
					System.out.println("matches");
					//get gesture number
					int preInd = currFileName.lastIndexOf("_");
					int endInd = currFileName.lastIndexOf(".txt");
					int gestureNum = Integer.parseInt(currFileName.substring(preInd + 1, endInd));
					//get the content;
					List<List<OneHandFrame>> list = readLines(currFileName);
					map.put(gestureNum, list);
				}
			}
			System.out.println("map.size : " + map.size());			
		}
		

//List<OneHandFrame>
		public List<List<OneHandFrame>> readLines(String filename) throws IOException {
			//public List<OneHandFrame> readLines(File filename) throws IOException {
			
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			List<List<OneHandFrame>> samples = new ArrayList<>();
			List<OneHandFrame> aSample = null;
			String line = null;
			int d = 0;
			boolean started = false;
			long frameId = 0;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.length() == 0) {
					samples.add(aSample);
				} else if (line.length() < 20) {
					started = false;
				} else {
					if (!started && line.length() > 20) {
						started = true;
						frameId = 0;
						aSample = new ArrayList<>();
					}
					aSample.add(convert2HandFrame(line, frameId++));
				}
			}
			
			bufferedReader.close();
			fileReader.close();
			return samples;
		
		}
			//String[] fileString = lines.toArray(new String[lines.size()]);
			
		private OneHandFrame convert2HandFrame(String line, long frameId) {
			OneHandFrame f = new OneHandFrame();
			String[] s = line.split(", ");
			
			//System.out.println(s.length);
			f.handID = 1;
			f.isLeft = 0;
			int d = 0;
			f.frameID = frameId;
			
			f.timeStamp = Long.parseLong(s[d++]);
			f.grabStrength =Float.parseFloat(s[d++]);
			f.pinchStregth = Float.parseFloat(s[d++]);
			f.pitch = Float.parseFloat(s[d++]);
			f.roll = Float.parseFloat(s[d++]);
			f.yaw = Float.parseFloat(s[d++]);
			f.widthP = Float.parseFloat(s[d++]);
			f.xAxisP = Float.parseFloat(s[d++]);
			f.yAxisP = Float.parseFloat(s[d++]);
			f.zAxisP = Float.parseFloat(s[d++]);
			f.armX = Float.parseFloat(s[d++]);
			f.armY = Float.parseFloat(s[d++]);
			f.armZ = Float.parseFloat(s[d++]);
			f.wristX = Float.parseFloat(s[d++]);
			f.wristY = Float.parseFloat(s[d++]);
			f.wristZ = Float.parseFloat(s[d++]);
			f.gestureType[0] = Float.parseFloat(s[d++]);
			f.gestureType[1] = Float.parseFloat(s[d++]);
			f.gestureType[2] = Float.parseFloat(s[d++]);
			f.gestureType[3] = Float.parseFloat(s[d++]);
			
			for (int j = 0; j < 5; j++){
				f.xAxis[j]=Float.parseFloat(s[d++]);
				f.yAxis[j]=Float.parseFloat(s[d++]);
				f.zAxis[j]=Float.parseFloat(s[d++]);
				f.xVel[j]=Float.parseFloat(s[d++]);
				f.yVel[j]=Float.parseFloat(s[d++]);
				f.zVel[j]=Float.parseFloat(s[d++]);
				f.xDir[j]=Float.parseFloat(s[d++]);
				f.yDir[j]=Float.parseFloat(s[d++]);
				f.zDir[j]=Float.parseFloat(s[d++]);
				f.FingerLenth[j]=Float.parseFloat(s[d++]);
				f.FingerWidth[j]=Float.parseFloat(s[d++]);
			}
			
			return f;
		}


		
	
}
