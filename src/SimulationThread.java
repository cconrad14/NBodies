import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public abstract class SimulationThread implements Runnable {

	// static vars required during run time
	protected static ArrayList<Body> _bodies;
	protected static ArrayList<Semaphore> _sems;
	protected static float _timestep = 0.001f;
	protected static float _frameRate = 1.0f / 15.0f;
	protected static TimeKeeper _tk;
	
	static {
		_bodies = new ArrayList<Body>();
		_sems = new ArrayList<Semaphore>();
		_tk = new TimeKeeper();
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
	
	public static void setTimestep(
			float step) {
		_timestep = step;
	}
}
