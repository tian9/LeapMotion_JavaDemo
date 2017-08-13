package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class ReadLine {
	
	public List<List<OneHandFrame>> readLines(String filename) throws IOException {
		//public List<OneHandFrame> readLines(File filename) throws IOException {
		
		FileReader fileReader = new FileReader(filename);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<List<OneHandFrame>> samples = new ArrayList<>();
		List<OneHandFrame> aSample = null;
		String line = null;
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

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String filename = "C:/Users/jingtian/workspace/leap_DEMO/MultiFinger/laixiaohan_20170803_155849_Frame_7.txt";
		ReadLine r = new ReadLine();
		r.readLines(filename);
		
		
	}

}
