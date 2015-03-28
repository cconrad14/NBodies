
public class TimeKeeper {

	private int seconds;
	private float precision;
	
	
	public TimeKeeper(){
		seconds = 0;
		precision = (float) 0.0;
	}
	
	public void addTime(float t){
		precision += t;
		if(precision >= 1){
			seconds++;
			precision -= 1;
		}	
	}
	
	public int getSeconds(){
		return seconds;
	}
	
	public float getPrecision(){
		return precision;
	}
	
	
	
}
