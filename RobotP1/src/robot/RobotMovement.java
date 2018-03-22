package robot;

//RobotMovement interface by Caspar and Ross (used internally by Robot)
//do not modify this code!
public interface RobotMovement
{
	/**
	 * pick up block from top of column (arm 3 must be directly above block)
	 */
	public abstract void pick();

	/**
	 * drop block in column (arm 3 must be directly above drop location) 
	 */
	public abstract void drop();

	/**
	 * move arm 1 up (height)
	 */
	public abstract void up();

	/**
	 * move arm 1 down (height)
	 */
	public abstract void down();

	/**
	 * move arm2 left (width)
	 */
	public abstract void contract();

	/**
	 * move arm2 right (width)
	 */
	public abstract void extend();

	/**
	 * move arm3 down (depth)
	 */
	public abstract void lower();

	/**
	 * move arm3 up (depth)
	 */
	public abstract void raise();
}
