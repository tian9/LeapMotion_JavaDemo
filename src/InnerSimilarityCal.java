import java.io.IOException;


public class InnerSimilarityCal {
	
	public InnerSimilarityCal(){
		
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/**
		 *the new similarity calculation
		 */
		String[] userName = {"linzhenpeng", "jingtian", "laixiaohan", "juchuanzhang"};
		for (String s : userName) {
			//write inner dis to a file
			Similarity similarity = new Similarity("C:\\Users\\jingtian\\workspace\\leap_DEMO\\MultiFinger\\", s);
			similarity.hashCode();
		}
		
	}

}
