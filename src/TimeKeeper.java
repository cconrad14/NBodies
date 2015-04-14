
public class TimeKeeper {

	private long seconds;
	private double precision;
	
	
	public TimeKeeper() {
		seconds = 0;
		precision = 0.0f;
	}
	
	/**
	 * Adds time to the timekeeper. Assumes t is between 0 and 1!
	 * @param t
	 */
	public void addTime(float t){
		precision += t;
		if(precision >= 1){
			seconds++;
			precision -= 1.0;
		}	
	}
	
	public long getSeconds(){
		return seconds;
	}
	
	public double getPrecision(){
		return precision;
	}
	
	public long getCurrentFrame(double frameRate)
	{
		double frame = seconds / frameRate;
		frame += precision / frameRate;
		
		return (long)frame;
	}
	
	
	
}
