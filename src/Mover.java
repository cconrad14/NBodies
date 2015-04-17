import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Mover extends SimulationThread {

	private static int _barrierCounter = 0;
	private static final boolean REAL_TIME = true;
	private static Float _collisionSmallestTime = Float.MAX_VALUE;
	private static boolean _collisionDetected = false;
	private float _precisestepTaken = 0f;
	private static int stepsTaken = 0;
	private static boolean _isPrecise;
	private int _numCollisions;
	private int maxSteps;
	
	

	// where do we assign
	// constructor was set to be private but should be public?
	public Mover(int threadId, int numThreads, int max, boolean precise) {
		super(threadId, numThreads);
		maxSteps = max;
		_isPrecise = precise;
	}

	@Override
	public void run() {
		if(_isPrecise)
			callPreciseAlgorithm();
		else{

		long nano = 0;
		while (!_isDone) {
			if(_threadId == 0)
				nano = System.nanoTime();
			
			calcChanges(CalculationStrategy.ACCEL);
			barrier();
			calcChanges(CalculationStrategy.CHECK);
			barrier();
			calcChanges(CalculationStrategy.COLLISION);
			barrier();
			_collisionDetected = false;
			calcChanges(CalculationStrategy.MOVE);
			barrier();
			
			if (_threadId == 0) {
				// attempt gui update
				try { UpdateGui(); }
				catch(Exception e) {
					e.printStackTrace();
				}
				
				// compute whether we should sleep so we update in real time
				nano = System.nanoTime() - nano;
				double executionFraction = nano / 1000000000.0;
				long sleepMillis = (long)((_timestep - executionFraction) * 1000);
				if(sleepMillis > 0 && REAL_TIME) {
					try {
						Thread.sleep(sleepMillis);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				_tk.addTime(_timestep);
				stepsTaken++;
				if (maxSteps != 0 && stepsTaken >= maxSteps)
					_isDone = true;
			}
		}
		}
	}

	private void callPreciseAlgorithm() {
		long nano = 0;
		while (!_isDone) {
			
			if(_threadId == 0)
				nano = System.nanoTime();
			calcChanges(CalculationStrategy.ACCEL);
			barrier();
			calcChanges(CalculationStrategy.CHECK);
			barrier();
			while(_collisionDetected){
				_collisionDetected = false;
				_collisionSmallestTime = Float.MAX_VALUE;
				//first time is redundant :/
				calcChanges(CalculationStrategy.CHECK);
				barrier();
				calcChanges(CalculationStrategy.PRECISE);
				barrier();
				calcChanges(CalculationStrategy.MOVE);
				barrier();
				calcChanges(CalculationStrategy.COLLISION);
				barrier();
				if(_threadId == 0 ){
					_precisestepTaken += _collisionSmallestTime;
					if(_precisestepTaken > _timestep)
						_collisionDetected = false;
				}
				barrier();
			}
			_collisionDetected = false;
			calcChanges(CalculationStrategy.MOVE);
			barrier();
			
			if (_threadId == 0) {
				// attempt gui update
				try { UpdateGui(); }
				catch(Exception e) {
					e.printStackTrace();
				}
				// compute whether we should sleep so we update in real time
				nano = System.nanoTime() - nano;
				double executionFraction = nano / 1000000000.0;
				long sleepMillis = (long)((_timestep - executionFraction) * 1000);
				if(sleepMillis > 0 && REAL_TIME) {
					try {
						Thread.sleep(sleepMillis);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				_tk.addTime(_timestep);
				stepsTaken++;
				if (maxSteps != 0 && stepsTaken >= maxSteps)
					_isDone = true;
			}
		}
		
	}

	private void calcChanges(CalculationStrategy flag) {
		int i = 0;
		while (true) {

			// do this loop until the index is out of range
			int idx = mapSequenceToId(i++);
			if (idx >= _bodies.size())
				break;

			Body b = _bodies.get(idx);
			switch (flag) {
			case ACCEL:
				ComputeAcceleration(b);
				break;
			case MOVE:
				if(_isPrecise && _collisionDetected)
					b.move(_collisionSmallestTime);
				else 
					b.move(_timestep);
				break;
			case CHECK:
				for (Body other : _bodies) {
					if (b.distance(other) == 0)
						continue;
					boolean intersecting = checkCollisions(b, other); // this is calling n^2 -
													// n(n+1)/2 too many
													// collision checks.
					// TODO reduce the number of repeated checks
					
				}
				break;
			case COLLISION:
				if(_collisionDetected && !_hasSmallestTime.empty()){
					Body other, first, second;
					if(_isPrecise){
						first = _hasSmallestTime.pop();
						second = _hasSmallestTime.pop();
						boolean doCollision = SimulationThread.checkCollision(
								_bodies.indexOf(first),	_bodies.indexOf(second)); 
						if (doCollision) {
							first.Collide(second);
						}
						break;
					}
					other = b.popBodyOffCollisionStack();
					while(other != null) {
						boolean doCollision = SimulationThread.checkCollision(
								_bodies.indexOf(b),	_bodies.indexOf(other)); 
						if (doCollision) {
							//b.CcomputeCollision(other);
							b.Collide(other);
						}
						
					}
					
				}
				break;
			case PRECISE:
				
				Body winning1, winning2, other;
				while(b.checkStack()){
					 other = b.popBodyOffCollisionStack();
					
					double time = b.ComputeCollisionTime(b, other);
					if(time < _collisionSmallestTime)
						synchronized(_collisionSmallestTime){
							if(time < _collisionSmallestTime){
								_collisionSmallestTime = (float)(time);
								if(!_hasSmallestTime.empty()){
									_hasSmallestTime.pop();
									_hasSmallestTime.pop();
								}
								_hasSmallestTime.push(b);
								_hasSmallestTime.push(other);
							}
						}
					b.mapHashString(other, time);
				}
						
				break;
			default:
				break;
			}

		}

	}

	private void ComputeAcceleration(Body b) {
		b.ClearAccel();
		for (Body other : _bodies) {
			double dist = b.distance(other);

			if (dist == 0.0)
				continue;

			b.updateTimestepAccel(other);
		}
	}

	private boolean checkCollisions(Body b, Body other) {

		double dist = b.distance(other);
		if (dist < b.getRadius() + other.getRadius()) {
			b.numCollisions++;
			b._hasCollided = true;
			b.pushBodyOnCollisionStack(other);
			SimulationThread.addToMap(
					_bodies.indexOf(b),
					_bodies.indexOf(other));
			_collisionDetected = true;
		}
		else {
			b.RemoveFromStillIntersecting(other);
			other.RemoveFromStillIntersecting(b);
		}
		return _collisionDetected;
	}

	private void barrier() {
		synchronized(_tk) {
			_barrierCounter++;
			if (_barrierCounter < _numThreads)
				try {
					_tk.wait();
				} catch (InterruptedException ie) {
					System.out.print(ie.getStackTrace());
					System.exit(_threadId);
				}
			else {
				_barrierCounter = 0;
				_tk.notifyAll();
			}
		}
	}

	private void UpdateGui() throws JSONException {
		
		if(_currentFrame == _tk.getCurrentFrame(_frameRate))
			return;
		
		// pack each body into JSON array
		JSONArray arr = new JSONArray();
		for (Body b : _bodies)
			arr.put(b.toJson());

		// send to GUI
		GuiCommunication.Send("update-positions", arr);
		
		_currentFrame++;
	}

}
