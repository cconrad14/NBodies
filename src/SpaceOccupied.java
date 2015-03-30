
public class SpaceOccupied {

	//class to represent the space a body can occupy during a time step
	public double[] _xSpace = new double[2];
	public double[] _ySpace = new double[2];
	public double[] _zSpace = new double[2];
	
	public SpaceOccupied(Body b){
		_xSpace[0] = b.getXPosition() - b.getRadius();
		_xSpace[1] = b.getXPosition() + b.getRadius();
		
		_ySpace[0] = b.getYPosition() - b.getRadius();
		_ySpace[1] = b.getYPosition() + b.getRadius();
		
		_zSpace[0] = b.getZPosition() - b.getRadius();
		_zSpace[1] = b.getZPosition() + b.getRadius();
	}
	
	// assumes all the bodies have been moved
	// TODO implement!
	public void updateSpace(Body b){
		double[] temp = b.getAccel();
		if(temp[0] < 0)
		//_xSpace[0] = b.nextPosition[0] ;
		_xSpace[1] = b.getXPosition() + b.getRadius();
		
		_ySpace[0] = b.getYPosition() - b.getRadius();
		_ySpace[1] = b.getYPosition() + b.getRadius();
		
		_zSpace[0] = b.getZPosition() - b.getRadius();
		_zSpace[1] = b.getZPosition() + b.getRadius();
	}
	
}
