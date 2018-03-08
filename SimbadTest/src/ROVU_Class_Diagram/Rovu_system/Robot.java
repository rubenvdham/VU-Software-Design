// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package ROVU_Class_Diagram.Rovu_system;

import ROVU_Class_Diagram.Rovu_system.RobotDirection;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;
import simbad.sim.SensorMatrix;
import ROVU_Class_Diagram.Rovu_system.Subject;


public class Robot extends Observer {
	/**
	 * 
	 */
	public RobotDirection robotstatus;
	public CentralStation station;
	
	private RangeSensorBelt sonars;
	private CameraSensor camera;
	private boolean lastTurnLeft;
	
	private final Enum[] direction = {RobotDirection.NORTH, RobotDirection.WEST, RobotDirection.SOUTH, RobotDirection.EAST}; 
	private int currentDirection;
	private boolean sendCoordinatesWithInterval;

	public Robot(Vector3d position, String name) {
		super(position, name);
		sonars = RobotFactory.addSonarBeltSensor(this,8); 
	    camera = RobotFactory.addCameraSensor(this);
	    lastTurnLeft = false;
	    sendCoordinatesWithInterval = false;
	}
	
	public void setStation(CentralStation station) {
		this.station = station;
		station.attach(this);
	}
	
	public void update() {
		executeTask(this.subject.newTask);
	}
	
	private void executeTask(Task executable) {
		switch(executable.request) {
		case turnLeft: turnLeft();
		case turnRight: turnRight();
		case sendCoordinates: sendCoordinates();
		case sendCoordinatesWithInterval: //setCoordinateInterval();
			this.sendCoordinatesWithInterval  = true;
		default: System.out.printf("Rover %s got an unkown task\n",this.name);
		}
		
	}
	
	private void sendCoordinates() {
		this.station.reportCoordinate(this);
	}
	
	public void initBehavior() {
        System.out.println("I exist and my name is " + this.name);
	}

	/**
	 * 
	 * @return 
	 */
	public double[] getCurrentCoordinate() {
		double[] returnVec = new double[2];
		Point3d temp = new Point3d();
    	this.getCoords(temp);
    	returnVec[0] = temp.x;
    	returnVec[1] = temp.z;
    	return returnVec; 
    }
	
	public void setDirection(int direc) {
		currentDirection = direc;
	}
	
	public void turnLeft() {
		if(currentDirection == 3) {
			currentDirection = 0;
		}else {
			currentDirection++;
		}
	}
	
	public void turnRight() {
		if(currentDirection == 0) {
			currentDirection = 3;
		}else {
			currentDirection--;
		}
	}

	/**
	 * 
	 */
	public void performBehavior() {
		// perform the following actions every 7 virtual seconds
    	if(this.getCounter() % 6 == 0) {
    		if(lastTurnLeft) {
    			if(sonars.getMeasurement(5) > 0.5 && sonars.getMeasurement(6) > 1 && sonars.getMeasurement(7) > 1.5) {
    				turnRight();
    				lastTurnLeft = false;
        			this.setTranslationalVelocity(0.0);
        			this.rotateY(Math.PI / 2 * 3);
        			System.out.println(this.name + " driving " + direction[currentDirection]);
        		}
    		}else{
    			if(sonars.getMeasurement(5) > 0.9 && sonars.getMeasurement(6) > 1 && sonars.getMeasurement(7) > 1.5) {
    				turnRight();
    				lastTurnLeft = false;
        			this.setTranslationalVelocity(0.0);
        			this.rotateY(Math.PI / 2 * 3);
        			System.out.println(this.name + " driving " + direction[currentDirection]);
        		}
    		}
    		
    		if(sonars.getMeasurement(0) < 0.28) {
    			turnLeft();
    			lastTurnLeft = true;
    			this.setTranslationalVelocity(0.0);
    			this.rotateY(Math.PI/2);
    			System.out.println(this.name + " driving " + direction[currentDirection]);
    		}else {
    			this.setTranslationalVelocity(0.5);
    		}
    	}
    	if(sendCoordinatesWithInterval == true) {
    		if(this.getCounter() % 20 == 0) {
        		//station.report(this);
        		sendCoordinates();
        	}
    	}
    	System.out.println("Percentage: " + station.map.getCoveredPercentage() + "%");
	}
};
