
public class TimeKeeper {

	private int seconds;
	private float precision;
	
	
	public TimeKeeper() {
		seconds = 0;
		precision = 0.0f;
	}
	
	/**
	 * Adds time to the timekeeper. Assumes t is between 0 and 1!
	 * @param t
	 */
	public void addTime(float t, int m){
		precision += t;
		if(precision >= 1){
			seconds++;
			precision -= 1.0f;
		}	
	}
	
	public int getSeconds(){
		return seconds;
	}
	
	public float getPrecision(){
		return precision;
	}
	
	
	
}
