// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package ROVU_Class_Diagram.Rovu_system;

import javax.vecmath.Vector3d;

import ROVU_Class_Diagram.Rovu_system.Subject;
import simbad.sim.Agent;

/************************************************************/
/**
 * 
 */
public abstract class Observer extends Agent{
	public Subject subject;
	
	public Observer(Vector3d arg0, String arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	

	/**
	 * 
	 */
	public abstract void update();
};
