
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.Semaphore;
import java.util.Date;
import java.util.concurrent.Semaphore;

// do graphical interface in browser with javascript?
// add charge as an additional force
// 3-D
// performance analysis
// 
public class NBodies {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// args: # of workers, ignored by sequential solution
		// # of bodies
		// size of each body (radius)
		// number of time steps

		int numWorkers = Integer.parseInt(args[1]);
		int numBodies = Integer.parseInt(args[2]);
		double radius = Double.parseDouble(args[3]);
		int numSteps = Integer.parseInt(args[4]);
		Date startTime, midTime, endTime;
		Computator[] workers = new Computator[numWorkers];
		Body[] bodies = new Body[numBodies];
		// initialize the bodies!!
		// TODO where do we place these guys??
		double[] position = {0.0,0.0,0.0};
		for (int i = 0; i < numBodies; i++) {
			// reassign positions?
			bodies[i] = new Body(radius, 0.0, 0.0, 0.0, position);
		}
		startTime = new Date();
		double start = startTime.getTime();

		// create the threads
		for (int i = 0; i < numWorkers; i++) {
			workers[i] = new Computator(bodies, numSteps, i, numWorkers);
		}
		midTime = new Date();
		double mid = midTime.getTime();
		// run the threads!!
		for (int i = 0; i < numWorkers; i++) {
			workers[i].start();
		}
		// collect the workers!
		for (int i = 0; i < numWorkers; i++) {
			try {
				workers[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		endTime = new Date();
		double end = endTime.getTime();
		
		//TODO do some output bro!
		System.out.println("computation time: ");

	}

}
