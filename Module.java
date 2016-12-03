import java.util.ArrayList;

/**
 * @author Lauren
 *
 * The module object represents a module in a linker.
 * 
 * This module contains ArrayLists which hold information for each of the three corresponding parts of the Module,
 * as well as one titled "used" which stores the names of symbols that are used within the text of the module
 * 
 * It also has a value called "address" which stores the location of the module relative to the other modules.
 */
public class Module {
	private ArrayList<Symbol> definition;
	private ArrayList<String> usage;
	private ArrayList<Integer> text;
	private ArrayList<String> used;
	private int address;
	
	/**
	 * The overloaded constructor for a Module
	 */
	public Module()
	{
		this.address = 0;
		definition = new ArrayList<Symbol>();
		usage = new ArrayList<String>();
		text = new ArrayList<Integer>();
		used = new ArrayList<String>();
	}
	/**
	 * @param s is a Symbol input that is added to the definition of the Module
	 * 
	 * This method simply adds any symbol defined in the module to the ArrayList definition
	 */
	public void setDef(Symbol s)
	{
		this.definition.add(s);
	}
	/**
	 * Getter method to return the ArrayList of type Symbol called definition
	 * @return the definition ArrayList
	 */
	public ArrayList<Symbol> getDef()
	{
		return definition;
	}
	/**
	 * @param s is a String to be added to the module's usage
	 * Setter method to add to the usage ArrayList of the module
	 */
	public void setUsage(String s)
	{
		this.usage.add(s);
	}
	/**
	 * @param s is an integer representing an address to the text of the module
	 * Setter method to add text to the ArrayList representing the text of the module
	 */
	public void addText(int s)
	{
		this.text.add(s);
	}
	/**
	 * @param a is an integer representing the relative address of the module
	 * Setter method to set the address of the module relative to the other modules in the input
	 */
	public void setAddress(int a)
	{
		this.address = a;
	}
	/** Getter method that returns an ArrayList type Integer of the text of the module
	 * @return type ArrayList<Integer> representing the module's text
	 */
	public ArrayList<Integer> getText()
	{
		return text;
	}
	/** Getter method to retrieve the address of the module relative to the others
	 * @return an int representing the address of the module
	 */
	public int getAddress()
	{
		return address;
	}
	/**
	 * @param s is a String representing a symbol that has been used
	 * Adds a used symbol to the ArrayList of type String called used in the module
	 */
	public void addUsed(String s)
	{
		used.add(s);
	}
	/** Getter method that returns the ArrayList type String of the used
	 * symbols within the module
	 * @return the ArrayList type String called used
	 */
	public ArrayList<String> getUsed()
	{
		return used;
	}
	/**Getter method that returns the ArrayList containing the usage of the current module
	 * @return ArrayList type String called usage
	 */
	public ArrayList<String> getUsage()
	{
		return usage;
	}
}
