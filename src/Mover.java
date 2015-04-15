import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Mover extends SimulationThread {

	private static int _barrierCounter = 0;
	private static float _collisionSmallestTime = Float.MAX_VALUE;
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

		while (!_isDone) {
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
				double[] mine = new double[3];
				double[] his = new double[3];
				if(_collisionDetected){
					Body other = b.popBodyOffCollisionStack();
					for(int j = 0; j < 3; j++){
						mine[i] = b.velocity[i];
						his[i] = other.velocity[i];
					}
					boolean doCollision = SimulationThread.checkCollision(
							_bodies.indexOf(b),	_bodies.indexOf(other)); 
					if (doCollision) {
						//b.CcomputeCollision(other);
						b.Collide(other);
					}
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

	synchronized private void barrier() {
		_barrierCounter++;
		if (_barrierCounter < _numThreads)
			try {
				wait();
			} catch (InterruptedException ie) {
				System.out.print(ie.getStackTrace());
				System.exit(_threadId);
			}
		else {
			_barrierCounter = 0;
			notifyAll();
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
