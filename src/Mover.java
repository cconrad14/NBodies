public class Mover extends SimulationThread {

	private Mover(int threadId, int numThreads) {
		super(threadId, numThreads);
	}

	@Override
	public void run() {
		calcChanges("accel");
		barrier();
		calcChanges("move");
		barrier();
		checkCollisions();
		barrier();
		

	}

	private void calcChanges(String flag) {
		int i = 0;
		while (true) {
			// do this loop until the index is out of range
			int idx = mapSequenceToId(i++);
			if (idx >= _bodies.size())
				break;
			Body b = _bodies.get(idx);
			switch (flag) {
			case "accel":

				loop: for (Body other : _bodies) {
					double dist = b.distance(other);

					// TODO skip self, not sure what this break will do
					if (dist == 0.0)
						continue loop;

					b.updateTimestepAccel(other);
				}
			case "move":
				b.move();
				break;
			}

		}

	}

	private boolean checkCollisions() {
		return false;
	}

	private void barrier() {

	}

}
