import java.util.ArrayList;


public abstract class SimulationThread implements Runnable {

	// static vars required during run time
	protected static ArrayList<Body> _bodies;
	protected static float _timestep;
	
	static {
		_bodies = new ArrayList<Body>();
		_timestep = 0.001f;
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
