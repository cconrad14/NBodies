public class Mover extends SimulationThread {

	static int counter = 0;
	
	private Mover(int threadId, int numThreads) {
		super(threadId, numThreads);
	}

	@Override
	public void run() {
		calcChanges(CalculationStrategy.ACCEL);
		barrier();
		calcChanges(CalculationStrategy.MOVE);
		barrier();
		calcChanges(CalculationStrategy.COLLISION);
		barrier();
		

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
	
					loop: for (Body other : _bodies) {
						double dist = b.distance(other);
	
						// TODO skip self, not sure what this break will do
						if (dist == 0.0)
							continue loop;
	
						b.updateTimestepAccel(other);
					}
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

	private boolean checkCollisions() {
		return false;
	}

	synchronized private void barrier() {
		counter++;
		if(counter < _numThreads)
			try { wait(); }
			catch(InterruptedException ie) {
				System.out.print(ie.getStackTrace());
				System.exit(_threadId);
			}
		else
			notifyAll();
	}

}
