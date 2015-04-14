
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Semaphore;

// do graphical interface in browser with javascript?
// add charge as an additional force
// 3-D
// performance analysis
// 
public class NBodies {

	public static void main(String[] args) {
		// args: # of workers, ignored by sequential solution
		// # of bodies
		// size of each body (radius)
		// number of time steps

		int numWorkers = Integer.parseInt(args[0]);
		int numBodies = Integer.parseInt(args[1]);
		double radius = Double.parseDouble(args[2]);
		int numSteps = Integer.parseInt(args[3]);
		Date startTime, midTime, endTime;
		ArrayList<Mover> workers = new ArrayList<Mover>();
		
		
		// initialize the bodies!!
		// TODO where do we place these guys??
		/*for (int i = 0; i < numBodies; i++) {
			// reassign positions?
			SimulationThread.setBodies(new Body(radius, 0.0, 0.0, 0.0, position));
			position[0] = 5;
		}*/
		SimulationThread.setBodies(new Body(radius, 2.0, 0.0, 0.0, -100, 0.0, 0.0));
		SimulationThread.setBodies(new Body(radius, -2.0, 0.0, 0.0, 100, 0.0, 0.0));
		ArrayList<Body> bodies = SimulationThread.getBodies();
		startTime = new Date();
		double start = startTime.getTime();

		// create the threads
		for (int i = 0; i < numWorkers; i++) {
			workers.add( new Mover(i, numWorkers, numSteps));
		}
		
		midTime = new Date();
		double mid = midTime.getTime();
		// run the threads!!
		for (int i = 0; i < numWorkers; i++) {
			//TODO implementing runnable doesnt give you the start method????
			workers.get(i).run();
		}
		// collect the workers!
		// need to have a barrier here!
		while(!SimulationThread.CheckIfDone()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		endTime = new Date();
		double end = endTime.getTime();
		int seconds = (int) Math.floor(end*1000);
		end -= (seconds * 1000);
		
		int collisions = 0;
		for(int i = 0; i < numBodies; i++)
			collisions += bodies.get(1).numCollisions / 2;
		
		//TODO do some output bro!
		System.out.println("computation time: " + seconds + " milliseconds "+ end);
		System.out.println(collisions + " total collisions");
		String fileName = System.getProperty("user.dir");
		fileName += "NBodies ouput";
		File file = new File(fileName);
		PrintWriter out;
		Body list;
		double xP, yP, zP, xV, yV, zV;
		try {
			out = new PrintWriter (file);
			for(int i = 0; i < numBodies; i++){
				list = bodies.get(i);
				xP = list.getXPosition();
				xV = list.getXVelocity();
				yP = list.getYPosition();
				yV = list.getYVelocity();
				zP = list.getZPosition();
				zV = list.getZvelocity();
				
				collisions = list.numCollisions / 2;
				out.print("Body #" + i + " Position = <" + xP + ',' + yP + ',' + zP + ">\t");
				out.print(" Velocity = <" + xV + ',' + yV + ',' + zV + ">\t");
				out.println(collisions + " Collisions.");
			}
			out.close();
			if(file.exists() && file.isFile()){
				System.out.println(file.getAbsolutePath());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
