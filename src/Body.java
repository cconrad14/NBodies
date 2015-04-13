import java.util.Stack;




public class Body {

	private double radius = 1;
	private double mass = 1;
	
	private final int DIMENSION = 3;
	private double[] deltaAccel = new double[DIMENSION];
	private double[] velocity = new double[DIMENSION];
	
	//take the assumption that the position is the center of the ball
	private double[] position;
	public double[] nextPosition = new double[DIMENSION];
	
	private Stack<Body> _stackCollision = new Stack<Body>();
	private float _mySmallestFloatTime = 0;
	private int _mySmallestIntTime = 0;
	private double[] normalVector;
	private double[] tangentVector;
	
	public Body(double radius,
			double velocity_x,
			double velocity_y,
			double zVelocity,
			double[] position )
	{
		this.radius = radius;
		velocity[0] = velocity_x;
		velocity[1] = velocity_y;
		velocity[2] = zVelocity;
		this.position = position;
		
	}
	
	
	public double[] getAccel(){
		return deltaAccel;
	}

	public void setZVelocity(double z){
		velocity[2] = z;
	}
	
	public double getZvelocity(){
		return velocity[2];
	}
	
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getXVelocity() {
		return velocity[0];
	}

	public void setXVelocity(double velocity_x) {
		this.velocity[0] = velocity_x;
	}

	public double getYVelocity() {
		return velocity[1];
	}

	public void setYVelocity(double velocity_y) {
		this.velocity[1] = velocity_y;
	}

	public double getXPosition() {
		return position[0];
	}

	public void setXPosition(double p) {
		position[0] = p;
	}
	
	public double getYPosition() {
		return position[1];
	}

	public void setYPosition(double p) {
		position[1] = p;
	}
	
	public double getZPosition() {
		return position[2];
	}

	public void setZPosition(double p) {
		position[2] = p;
	}
	
	public double distance(Body other) {
		double xDiff = position[0] - other.position[0];
		double yDiff = position[1] - other.position[1];
		double zDiff = position[2] - other.position[2];		
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff + zDiff*zDiff);
	}

	public static double Magnitude(
		double[] vector,
		int dimension)
	{
		double sum = 0;
		for(int i = 0; i < dimension; i++)
			sum += vector[i] * vector[];

		return Math.sqrt(sum);
	}
	
	public void updateTimestepAccel(Body other) {
		double d = distance(other);
		
		for(int i = 0; i < 3; i++) {
			double a = Constants.G * other.mass * (position[i] - other.position[i]);
			deltaAccel[i] += a / (d * d * d);
		}
	}
	
	public void move(){
		for(int i = 0; i < 3; i++){
			nextPosition[i] = position[i] + velocity[i] + deltaAccel[i] / 2;
			velocity[i] += deltaAccel[i];
		}
	}
	
	public void pushBodyOnCollisionStack(Body other) {
		_stackCollision.push(other);
	}
	
	public Body popBodyOffCollisionStack() {
		return _stackCollision.pop();
	}
	
	public float ComputeCollisionTime(
			Body b1,
			Body b2) {
		float collide = 0; //?????
				
		
		return collide;
	}
	
	public void computeCollision(Body b){
		// find normal and tangent vectors
		// tangent vectors will not change
		
		
	}
	
	 public void computeNormal( Body b){
		 double magnitude = distance(b);
		 normalVector[0] = (this.getXPosition() - b.getXPosition()) / magnitude;
		 normalVector[1] = (this.getYPosition() - b.getYPosition()) / magnitude;
		 normalVector[2] = (this.getZPosition() - b.getZPosition()) / magnitude;
		 
	 }
	 
	 
	 //switches axis to make the x-axis along the normal vector between the two objects.
	private void CalcAngle(Body b){
		double magnitude = distance(b);
		double distX = this.getXPosition() - b.getXPosition();
		double distY = this.getYPosition() - b.getYPosition();
		double distZ = this.getZPosition() - b.getZPosition();
		double myMag = velocity[0] + velocity[1] + velocity[2];
		double hisMag = b.velocity[0] + b.velocity[1] + b.velocity[2];
		double numerator = distX*velocity[0] + distY*velocity[1] + distZ*velocity[2];
		double denominator =  Math.sqrt((distX + distY + distZ) * myMag);
		double myAngle = Math.acos(numerator / denominator);
		numerator = distX*b.velocity[0] + distY*b.velocity[1] + distZ*b.velocity[2];
		denominator =  Math.sqrt((distX + distY + distZ) * hisMag);
		double hisAngle = Math.acos(numerator / denominator);
		double myVCenter = myMag*Math.cos(myAngle);
		double hisVCenter = hisMag * Math.cos(hisAngle);
		double myXZ = Math.asin(velocity[2] / velocity[0]); // angle of vector on the xz plane
		double myXY = Math.asin(velocity[1] / velocity[0]);
		double hisXZ = Math.asin(b.velocity[2] / b.velocity[0]);
		double hisXY = Math.asin(b.velocity[1] / b.velocity[0]);
		double[] myCenter = new double[3];
		myCenter[0] = myVCenter * Math.sin(myXZ) * Math.cos(myXY);
		myCenter[1] = myVCenter * Math.sin(myXZ) * Math.sin(myXY);
		myCenter[2] = myVCenter * Math.cos(myXZ);
		double[] hisCenter = new double[3];
		hisCenter[0] = hisVCenter * Math.sin(hisXZ) * Math.cos(hisXY);
		hisCenter[1] = hisVCenter * Math.sin(hisXZ) * Math.sin(hisXY);
		hisCenter[2] = hisVCenter * Math.cos(hisXZ);
		double temp = 0;
		double[] myNormal = new double[3];
		double[] hisNormal = new double[3];
		for(int i = 0; i < 3; i++){
			myNormal[i] = velocity[i] - myCenter[i];
			hisNormal[i] = b.velocity[i] - hisCenter[i];
		}
		for(int i = 0; i < 3; i++){
			temp = myCenter[i];
			myCenter[i] = hisCenter[i];
			hisCenter[i] = temp;
		}
		for(int i = 0; i < 3; i++){
			velocity[i] = myCenter[i] + myNormal[i];
			b.velocity[i] = hisCenter[i] + hisNormal[i];
		}
		
	}

	// returns the 3D angle with other.
	public double CalculateAngleXYZ(
		Body other)
	{
		double distX = this.getXPosition() - b.getXPosition();
		double distY = this.getYPosition() - b.getYPosition();
		double distZ = this.getZPosition() - b.getZPosition();

		double numerator = 0.0;
		numerator += distX * velocity[0];
		numerator += distY * velocity[1];
		numerator += distZ * velocity[2];

		double denominator = 0.0;
		denominator += (distX + distY + distZ);
		denominator *= (velocity[0] + velocity[1] + velocity[2]);
		denominator = Math.sqrt(denominator);

		return Math.acos(numerator / denominator);
	}

	public double CalculateVelocityCenterMagnitude(
		double xyzAngle)
	{
		return Magnitude(velocity, 3) * Math.cos(xyzAngle);
	}

	public static void DecomposeVelocityCenter(
		double velocityCenterMagnitude,
		double[] velocityCenterComponents,
		int dimension)
	{
		if(dimension != DIMENSION)
			throw new Exception("dimension must match!");

		// x
		// y
		// z
	}

	public static void ComputeVelocityNormal(
		double[] velocityCenterComponents,
		double[] velocityNormalComponents,
		int dimension)
	{
		if(dimension != DIMENSION)
			throw new Exception("dimension must match!");

		for(int i = 0; i < dimension; i++)
		velocityNormalComponents[i] = velocity[i] - velocityCenterComponents[i];
	}
	
	
	
}
