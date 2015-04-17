import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.Semaphore;


public abstract class SimulationThread implements Runnable {

	// static vars required during run time
	protected static ArrayList<Body> _bodies;
	protected static boolean _isDone = false;
	protected static ArrayList<Semaphore> _sems;
	protected static float _timestep = 0.001f;
	protected static float _frameRate = 1.0f / 10.0f;
	protected static long _currentFrame = -1;
	protected static TimeKeeper _tk;
	protected static HashMap<String, Boolean> _collisionMap;
	protected static int _barrierCounter;
	protected static Stack<Body> _hasSmallestTime = new Stack<Body>();
	
	static {
		_barrierCounter = 0;
		_bodies = new ArrayList<Body>();
		_sems = new ArrayList<Semaphore>();
		_tk = new TimeKeeper();
		_collisionMap = new HashMap<String, Boolean>();
	}
	
	// instance vars
	protected int _threadId;
	protected int _numThreads;
	
	public SimulationThread(
			int threadId,
			int numThreads) {
		_threadId = threadId;
		_numThreads = numThreads;
	}
	
	protected int mapSequenceToId(
			int i) {
		return (_numThreads * i) + _threadId;
	}
	
	public static boolean CheckIfDone(){
		return _isDone;
	}
	public static void setTimestep(
			float step) {
		_timestep = step;
	}
	
	public static void setBodies(Body b){
		_bodies.add(b);
	}
	
	public static ArrayList<Body> getBodies(){
		return _bodies;
	}
	
	public static void addToMap(int index1, int index2){
		String key = mapIndecies(index1, index2);
		_collisionMap.put(key, false);
	}
	
	public static void clearMap(){
		_collisionMap.clear();
	}
	
	public static boolean checkCollision(int index1, int index2){
		
		String check = mapIndecies(index1, index2);
		Body b1 = _bodies.get(index1);
		Body b2 = _bodies.get(index2);
		boolean intersecting = b1.distance(b2) < (b1.getRadius() + b2.getRadius());
		
		if(_collisionMap.containsKey(check)) {
			_collisionMap.remove(check);
			return true;
		} else
			return false;
		
	}
		
	private static String mapIndecies(int index1, int index2){
		String toRet = "";
		if(index1 < index2){
			toRet += Integer.toString(index1);
			toRet += ",";
			toRet += Integer.toString(index2);
		} else {
			toRet += Integer.toString(index2);
			toRet += ",";
			toRet += Integer.toString(index1);
		}
		return toRet;
	}
	
}
