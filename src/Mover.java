

public class Mover extends SimulationThread {

	private static int _barrierCounter = 0;
	private static float  _collisionSmallestTime = Float.MAX_VALUE;
	private static float _floatMoveTime = 0;
	private static int _intMoveTime = 0;
	private static boolean _collisionDetected = false;
	private static int stepsTaken = 0;
	private int maxSteps;
	// where do we assign 
	// constructor was set to be private but should be public?
	public Mover(int threadId, int numThreads, int max) {
		super(threadId, numThreads);
		maxSteps = max;
	}

	@Override
	public void run() {
		
		while(!_isDone) {
			calcChanges(CalculationStrategy.ACCEL);
			barrier();
			calcChanges(CalculationStrategy.COLLISION);
			barrier();
			_collisionDetected = false;
			calcChanges(CalculationStrategy.MOVE);
			barrier();
			
			if(_threadId == 0) {
				UpdateGui();
				_tk.addTime(_floatMoveTime, _intMoveTime);
				stepsTaken++;
				if(maxSteps != 0 && stepsTaken > maxSteps)
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
					b.move();
					break;
				case COLLISION:
					for(Body other : _bodies){
						if(b.distance(other) == 0)
							continue;
						else
							checkCollisions(other); // this is calling n^2 - n(n+1)/2 too many collision checks.
						//TODO reduce the number of repeated checks
					}
					break;
				default:
					break;
			}

		}

	}
	
	private void ComputeAcceleration(Body b) {
		for (Body other : _bodies) {
			double dist = b.distance(other);

			// TODO skip self, not sure what this break will do
			if (dist == 0.0)
				continue;

			b.updateTimestepAccel(other);
		}
	}

	private boolean checkCollisions(Body b) {
		for(Body other : _bodies) {
			double dist = b.distance(other);
			if(dist < b.getRadius() + other.getRadius()) {
				b._hasCollided = true;
				b.pushBodyOnCollisionStack(other);
				_collisionDetected = true;
			}			
		}
		return _collisionDetected;
	}

	synchronized private void barrier() {
		_barrierCounter++;
		if(_barrierCounter < _numThreads )
			try { wait(); }
			catch(InterruptedException ie) {
				System.out.print(ie.getStackTrace());
				System.exit(_threadId);
			}
		else{
			_barrierCounter = 0;
			notifyAll();
		}
	}
	
	private void UpdateGui() {
		// combine JSON into single array
		String json = "[";
		for(Body b : _bodies)
			json += b.toJson() + ",";
		json += "]";
		
		// send to GUI
		
	}

}
