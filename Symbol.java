
/**
 * @author Lauren
 *
 * The symbol object represents a symbol that is defined within one of the modules of the input for the Linker class.
 * The two main components of a symbol are the name of the symbol, a string entitled symbol, and the location or address of the symbol.
 * 
 */
public class Symbol {
	private String symbol;
	private int location;
	private boolean isUsed;
	private boolean isDoublyDefined;
	private boolean isBiggerThanAdd;
	
	/**
	 * @param symbol is a String containing the name of the Symbol
	 * @param location is an int containing the location of the symbol relative to the start
	 * of the first module.
	 * 
	 * Overloaded constructor to make a new symbol, with all of the boolean values within
	 * the class being set to a default of "false"
	 */
	public Symbol(String symbol, int location)
	{
		this.symbol = symbol;
		this.location = location;
	}
	
	/**
	 * Getter method that returns the name of the Symbol
	 * 
	 * @return the name of the symbol
	 */
	public String getSymbol(){
		return symbol;
	}
	/**
	 * Getter method that returns the location of the symbol
	 * relative to the first module
	 * @return the location of the module
	 */
	public int getLocation(){
		return location;
	}
	/**
	 * Getter method that returns the variable "isUsed"
	 * @return "isUsed", a boolean value to show whether
	 * or not the symbol is contained in any usage lists throughout all modules
	 */
	public boolean getUsage()
	{
		return isUsed;
	}
	/**
	 * Getter method that returns whether or not the
	 * symbol has been defined more than once
	 * @return the boolean value "isDoublyDefined", which
	 * is true if the symbol has been defined more than once,
	 * and false if it has only been defined once.
	 */
	public boolean getDoubleDef()
	{
		return isDoublyDefined;
	}
	/**
	 * Getter method that returns whether or not
	 * the symbol's address surpasses the size of the 
	 * module that it is located in.  
	 * @return a boolean value which is true if the symbol's address
	 * is larger than the address of the module, and false otherwise.
	 */
	public boolean getIsBiggerThanAdd()
	{
		return isBiggerThanAdd;
	}
	/**
	 * @param i is an integer containing the location of the symbol
	 * 
	 * This method changes the location of the symbol to @param i.  It is used
	 * when the original address of the symbol is too large for the module 
	 * that it was defined in, and needs to be changed in order to fit
	 * within the module.  In this case, it is set to true
	 */
	public void setLocation(int i)
	{
		this.location = i;
	}
	/**
	 * @param used is a boolean value that holds whether the defined symbol is used or not
	 * 
	 * This method changes the boolean value of used to true if the symbol is used within
	 * any module to true.  Otherwise it is left at it's default state of false.
	 */
	public void setUsed(boolean used)
	{
		this.isUsed = used;
	}
	/**
	 * @param dd is a boolean value that determines whether the symbol is defined more than once
	 * 
	 * This method changes the boolean value for isDoublyDefined to the value of the parameter, dd.
	 * If isDoublyDefined is true, then the symbol has been defined more than once, and will later
	 * generate an error and have its address set to the first time the symbol was defined.
	 */
	public void setDoublyDefined(boolean dd)
	{
		this.isDoublyDefined = dd;
	}
	/**
	 * @param b is a boolean value that determines whether the address of the symbol exceeds that of the module it is defined in
	 * 
	 * This method sets the value of the boolean "isBiggerThanAdd" which is true if the address of the symbol is larger than
	 * that of the module it is defined in.  If it fits within the module it is defined in, isBiggerThanAdd stays at it's default
	 * value of false
	 */
	public void setisBigger(boolean b)
	{
		this.isBiggerThanAdd = b;
	}
}
