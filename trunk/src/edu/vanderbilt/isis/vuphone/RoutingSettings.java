package edu.vanderbilt.isis.vuphone;

/**
 * A mutable storage class that contains routing information.
 * @author Krzysztof Zienkiewcz
 *
 */

public class RoutingSettings{

	// TODO: Redefine these objects
	
	private String strA_ = "";
	private int intA_ = 0;
	
	/**
	 * Default constructor.
	 */
	public RoutingSettings(){
		
	}

	/**
	 * Sets the A string setting.
	 * @param str
	 */
	public void setIntA(int num){
		intA_ = num;
	}
	
	/**
	 * Sets the A string setting.
	 * @param str
	 */
	public void setStringA(String str){
		strA_ = str;
	}
	
	/**
	 * Returns a string representation of this object.
	 */
	public String toString(){
		return strA_ + ", " + intA_;
	}

}
