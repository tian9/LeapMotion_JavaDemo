package main;

/**
 * This is borrowed from the Internet.
 * Dynamic programming to compute similarity measure
 * @author sathish
 *
 */

public class DTW {

    float[][] signal;
    float[][] reference;
    // the slope constraint value
    float slope = 0;
    String distanceFunction;
    
    float costValue;

    /**
     * Dynamic time warping (DTW) cost signal and reference  
     * Default 'Euclidean' distance function
     * @param signal
     * @param reference
     */
    public DTW(float[][] signal,float[][] reference){
        this.signal = signal;
        this.reference = reference;
        this.distanceFunction = "Euclidean";
        setCost(dpDistance());
    }
    
    /**
     * Dynamic time warping (DTW) cost signal and reference
     * distanceFunction = {"Euclidean" or "Absolute"} 
     * @param signal
     * @param reference
     */
    public DTW(float[][] signal,float[][] reference, String distanceFunction){
        this.signal = signal;
        this.reference = reference;
        this.distanceFunction = distanceFunction;
        setCost(dpDistance());
    }
    
    /**
     * Set cost of best path
     * @param cost
     */
    public void setCost(float cost){
        this.costValue = cost;
    }
    
    
    /**
     * Get cost of best path 
     * @return cost
     */
    public float getCost(){
        return this.costValue;
    }
    
    /**
     * the major method to compute the matching score between selected test 
     * signal and reference. 
     *
     * @return DP cost  
     */
  public float dpDistance() {
    if ((signal == null ) || (reference == null)) {
        return -1;
    }

    float cost = 0;
    int s;
    int t;
    int ns;
    int nt;
    float maxV = (float) 1.0e32;
    ns = reference.length; // ns = rr;
    nt = signal.length; // nt = dr;
    //System.out.println("ns--" + ns + ", nt --" + nt);
    float[][] trellis = new float[ns][nt];
    //Initializing first column.......
    t = 0;
    for (s = 0; s < ns; s++) {
       if (s == 0) {
           trellis[s][t] = frameDistance(reference[s], signal[t], distanceFunction);
           //pbuf[s][t] = 0;
       } else {
           trellis[s][t] = maxV;
       }
    }

    for (t = 1; t < nt; t++) {
        for (s = 0; s < ns; s++) {
       //connections s t-1, s-1, t-1, s-2, t-1           
       trellis[s][t] = minConnect(trellis, s, t)  + frameDistance(reference[s], signal[t], distanceFunction);
       // pbuf[s][t] = idx;
        }
     }
     cost = trellis[ns-1][nt-1];
     if(Double.isNaN(cost)){
    	 System.out.println("COST IS NAN-------------------");
     }
     
     return cost;
     
  }
    
  /**
   * Euclidean distance 
   */  
  public float EuclideanDistance(float[] x, float[] y) {

      float sum = 0;
      if(x.length != y.length){
          throw new RuntimeException("Given array lengths were not equal."); 
      }
      int d = x.length;
      for (int i = 0; i < d; i++) {
    	  if(Double.isNaN(x[i]) || Double.isNaN(y[i])){
    		  continue;
    	  }else{
    		  sum = sum + (x[i] - y[i]) * (x[i] - y[i]);
    	  }
       //System.out.println(x[i] + "X[I],, " + y[i] +"y[i] ");
      }
      
    		  
      //System.out.println("sum = " + sum + ", dx= " + x.length + ", dy= " + y.length);
      
      if(sum<0) System.out.println("SQRT by NEGTIVE NUMBER!!");
      sum = (float) Math.sqrt(Math.abs(sum));
      
      if(Double.isNaN(sum)){
     	 System.out.println("EuclideanDistance IS NAN--");
      }
      
      return sum;
     }
       
  /**
   * Absolute distance
   * @param x
   * @param y
   * @return
   */
  public float AbsDistance(float[] x, float[] y) {

      float sum = 0;
      if(x.length != y.length){
          throw new RuntimeException("Given array lengths were not equal."); 
      }
      int d = x.length;
      for (int i = 0; i < d; i++) {
       sum = sum + Math.abs(x[i] - y[i]);
      }
      return sum;
     }
  
  /**
   * Index of minimum connection  
   * @param trel
   * @param s
   * @param t
   * @return
   */
  private float minConnect(float[][] trel, int s, int t) {

      float minV;
      int t1 = t - 1;

      if (s - 2 >= 0) {
        minV = trel[s-2][t1];           
        //idx = s-2;
        if (minV > trel[s-1][t1]) {
           minV = trel[s-1][t1]; 
           //idx = s-1;
        }
        if (minV > trel[s][t1]) {
           minV = trel[s][t1];
           //idx = s;
        }
      } else if (s - 1 >= 0) {
        minV = trel[s-1][t1];           
        //idx = s-1;
        if (minV > trel[s][t1]) {
          minV = trel[s][t1];
          //idx = s;
        }
      } else {
        minV = trel[s][t1];
        //idx = s;
    }
      
      if(Double.isNaN(minV)){
      	 System.out.println("minConnect IS NAN--");
       }
      return(minV);
  }
  
  // methods to compute distance between two frames    
  private float frameDistance(float f1[], float f2[], String distanceType) {
  float dis = 0;
  if (distanceType == "Euclidean") 
      dis = EuclideanDistance(f1, f2);
  else dis = AbsDistance(f1, f2);
  return dis;
  }
    
  
}