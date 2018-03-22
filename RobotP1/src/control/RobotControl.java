package control;

import robot.Robot;

//Robot Assignment for Programming 1 s1 2018
//Adapted by Caspar and Ross from original Robot code in written by Dr Charles Thevathayan
public class RobotControl implements Control
{
	// we need to internally track where the arm is
	private int height = Control.INITIAL_HEIGHT;
	private int width = Control.INITIAL_WIDTH;
	private int depth = Control.INITIAL_DEPTH;

	private int[] barHeights;
	private int[] blockHeights;

	private Robot robot;

	// called by RobotImpl
	@Override
	public void control(Robot robot, int barHeightsDefault[], int blockHeightsDefault[])
	{
		this.robot = robot;

		// some hard coded init values you can change these for testing
		this.barHeights = new int[] { 7, 3, 1, 7, 5, 3, 2 };
		this.blockHeights = new int[] { 3, 1, 2, 3, 1, 1, 1 };

		// initialise the robot
		robot.init(this.barHeights, this.blockHeights, height, width, depth);

		// a simple private method to demonstrate how to control robot
		// note use of constant from Control interface
		// You should remove this method call once you start writing your code
		core();

		// ADD ASSIGNMENT PART A METHOD CALL(S) HERE

	}
	// Global variables
	private int pileHeight = 0; // Height of the pile
	private int blockMoved = 0; // How many block we've moved so far
	private int[] blockMovedL = {0, 0, 0};

	private void core()
	{
	  // We compute the Pile Height
	  initPileHeight();

	  for (int i = 0; i < this.blockHeights.length; i++)
	  {
		  pickupElem();
		  placeElem();
	  }
	}

	private void pickupElem()
	{
	  // Make depth go high enouth not to touch any obstacle
	  if (!(getExt() > maxBarHeight(false) && getExt() > pileHeight))
	  {
	    while(Math.max(maxBarHeight(false), pileHeight) > getExt())
	    {
	      moveDepth(true);
	    }
	  }

	  // Make arm go back above the pile
	  while (this.width < Control.MAX_WIDTH)
	  {
	    moveWidth(true);
	  }

	  // Make arm go down to grab element
	  while(getExt() > pileHeight)
	  {
	    moveDepth(false);
	  }

	  // Then we finaly grab the element
	  robot.pick();
	}

	private void placeElem()
	{
	    // We first determine wich size is the block
	    int size = this.blockHeights[this.blockHeights.length - blockMoved - 1];

	    // Make arm go hight enought to make the travel
	    while (getExt() < maxBarHeight(size == 3) + size)
	    {
	        moveDepth(true);
	        System.out.println("Max bar height: " + maxBarHeight(size == 3));
	    }

	    // Make arm go place the block (width)
	    if (size < 3)
	    {
	        while (this.width > size)
	        {
	          moveWidth(false);
	        }
	    }
	    else
	    {
	        while (this.width > 3 + this.blockMovedL[size - 1])
	        {
	          moveWidth(false);
	        }
	    }

	    // Drop it at the right height
	    if (size < 3)
	    {
	        while (getExt() > blockMovedL[size - 1] * size + size)
	        {
	            moveDepth(false);
	        }
	    }
	    else
	    {
	        while (getExt() > this.barHeights[this.blockMovedL[size - 1]] + size)
	        {
	            moveDepth(false);
	        }
	    }

	    // We drop the item
	    robot.drop();

	    // We update the max bar height
	    if(size == 3)
	    {
	    	this.barHeights[this.blockMovedL[size - 1]] += size;
	    }
			System.out.println("Current updated bar height: " + this.barHeights[this.blockMovedL[size - 1]]);

			// We update the data
	    blockMoved++;
			blockMovedL[size - 1]++;
			updPileHeight(size);
	}

	// Get the maximum obstacle height
	private int maxBarHeight(Boolean isThree)
	{
		if (isThree)
		{
			return maxInArray(this.barHeights, 2 + blockMovedL[2]);
		}
		else
		{
			return maxInArray(this.barHeights, 0);
		}
	}

	// Get the maximum value in the array
	private int maxInArray(int[] array, int start)
	{
	  int max = 0;
	  for (int i = start; i < array.length; i++)
	  {
	  	max = Math.max(array[i], max);
	  }
	  return max;
	}

	// Track how high is the extremity of the arm
	private int getExt()
	{
	  return this.height - this.depth - 1;
	}

	/*
	 * Informations relative to the PILE
	 */
	private void initPileHeight()
	{
	  for (int i = 0; i < this.blockHeights.length; i++)
	  {
	    pileHeight += this.blockHeights[i];
	  }
	}

	private void updPileHeight(int howMuch)
	{
	  pileHeight -= howMuch;
	}

	/*
	 * Functions to move and track moves
	 */
	private void moveHeight(Boolean dir) // dir : true > up and false > down
	{
	  if(dir)
	  {
	    this.height++;
	    robot.up();
	  }
	  else
	  {
	    this.height--;
	    robot.down();
	  }
	}

	private void moveWidth(Boolean dir) // dir : true > right and false > left
	{
	  if (dir)
	  {
	    this.width++;
	    robot.extend();
	  }
	  else
	  {
	    this.width--;
	    robot.contract();
	  }
	}

	private void moveDepth(Boolean dir) // dir : true > up and false > down
	{
	  if(dir)
	  {
	    this.depth--;
	    robot.raise();
	  }
	  else
	  {
	    this.depth++;
	    robot.lower();
	  }
	}
}
