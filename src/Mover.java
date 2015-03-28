
public class Mover extends SimulationThread {

	
	private Mover(
			int threadId,
			int numThreads) {
		super(threadId, numThreads);
	}

	@Override
	public void run() {
		
		int i = 0;
		while(true) {
			// do this loop until the index is out of range
			int idx = mapSequenceToId(i++);
			if(idx >= _bodies.size())
				break;
			
			Body b = _bodies.get(idx);
			for(Body other : _bodies) {
				double dist = b.distance(other);
				
				// skip self
				if(dist == 0.0)
					break;
				
				b.updateTimestepAccel(other);
			}
			
		}
	}

	
}
