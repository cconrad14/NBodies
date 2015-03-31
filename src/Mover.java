public class Mover extends SimulationThread {

	private static int _barrierCounter = 0;
	private static float  _collisionSmallestTime = Float.MAX_VALUE;
	
	private Mover(int threadId, int numThreads) {
		super(threadId, numThreads);
	}

	@Override
	public void run() {
		
		while(true) {
			calcChanges(CalculationStrategy.ACCEL);
			barrier();
			calcChanges(CalculationStrategy.COLLISION);
			barrier();
			calcChanges(CalculationStrategy.MOVE);
			barrier();
			
			if(_threadId == 0) {
				UpdateGui();
				_tk.addTime(t);
				
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
				b.pushBodyOnCollisionStack(other);
				
			}			
		}
	}

	synchronized private void barrier() {
		_barrierCounter++;
		if(_barrierCounter < _numThreads)
			try { wait(); }
			catch(InterruptedException ie) {
				System.out.print(ie.getStackTrace());
				System.exit(_threadId);
			}
		else
			notifyAll();
	}
	
	private void UpdateGui() {
		
	}

}
