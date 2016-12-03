import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * @author: Lauren DiGiovanni
 */
/*
 * The Linker class is the main class of Lab 1.  In this class, a two-pass linker with 
 * a machine size of 600 is generated.  A text file is needed as input, with the user
 * inputting the filepath directly to the program.
 * 
 * The output of this program is a file entitled output.txt that contains a symbol table
 * with a memory map of the input values.  The addresses in the output have been resolved
 * to their proper values, and have been checked for several errors and warnings, producing 
 * messages if there are errors.
 */

public class Linker {
	public static void main(String[] args)
	{
		
		System.out.print("Please input the filepath you'd like to parse:\n");
		try{
			int machineSize = 600;
			Scanner s = new Scanner(System.in);
			String fileName = s.nextLine();
			s.close();
			File f = new File(fileName);
			Scanner fs = new Scanner(f);
			int numModules = fs.nextInt();
			
			ArrayList<Symbol> symbols = new ArrayList<Symbol>();
			ArrayList<Integer> addresses = new ArrayList<Integer>();
			ArrayList<Module> modules = new ArrayList<Module>();
			ArrayList<String> usages = new ArrayList<String>();
			
			int[] modsize = new int[numModules];
			modsize[0] = 0;
			int howManySymbols = 0;
			
			/*
			 * This is the first pass through the input file, which splits the modules apart into
			 * individual objects, and adds the information from the file into seperate parts
			 * of the module objects, which are contained in one ArrayList entitled modules.
			 * 
			 * The first pass fills an ArrayList with symbols titled "symbols" in order to generate a symbol table.
			 * Each pass of the outer loop increments the module being looked at.
			 */
			for(int i = 0; i < numModules; i++)
			{
				Module m = new Module();
				modules.add(m);
				m.setAddress(addresses.size());
				for(int j = 0; j < 3; j++)
				{
					int currNum = fs.nextInt();
						if (j == 0)
						{
							//Definitions are looked at here, with symbols being added to the symbol table
							howManySymbols = currNum;
							while(howManySymbols != 0)
							{
								//If the module being looked at is not the first in the list, the address must be relocated
								if(i > 0){
									Symbol tempSym = new Symbol(fs.next(), fs.nextInt() + modsize[i-1]);
									symbols.add(tempSym);
									m.setDef(tempSym);
								}
								else
								{
									Symbol tempSym = new Symbol(fs.next(), fs.nextInt());
									symbols.add(tempSym);
									m.setDef(tempSym);
								}
								howManySymbols--;
							}
						}
						else if (j == 1)
						{
							//The symbols used are added to the appropriate module here
							for(int numOfUses = 0; numOfUses < currNum; numOfUses++)
							{
								String temp = fs.next();
								m.setUsage(temp);
								usages.add(temp);
							}	
							
						}
						else if (j == 2)
						{
							//The text of the module, or the memory addresses are added to the appropriate module here
							if(i > 0)
							{
								modsize[i] = currNum + modsize[i-1];
							}
							else
								modsize[i] = currNum;
							for(int addr = 0; addr < currNum; addr++)
							{
								int currAddr = fs.nextInt();
								addresses.add(currAddr);
								m.addText(currAddr);
							}
						}
				}
			}
			fs.close();
			
			symbols = symbolCheck(symbols);
			checkSymbolUse(symbols, usages);
			checkSymbolSize(symbols, modules);
			
			File output = new File("output.txt");
			FileWriter fw = new FileWriter(output);
			PrintWriter pw = new PrintWriter(fw);
			pw.println("Symbol Table");
			//Print symbols and check to see if any error messages need to be produced.
			for(int u = 0; u < symbols.size(); u++)
			{
				pw.print(symbols.get(u).getSymbol() + " = " + symbols.get(u).getLocation());
				if(symbols.get(u).getDoubleDef() == true)
				{
					pw.print("\tError: Symbol has been defined more than once.  Using first value."); 
				}
				if(symbols.get(u).getIsBiggerThanAdd() == true)
				{
					pw.print("\tError: Symbol address excedes the size of the module it was defined in.  Address zeroed relatively.");
				}
				pw.println();
			}
			
			pw.println("Memory Map");
			
			for(int i = 0; i < numModules; i++)
			{
				/* 
				 * Pass through the second time and iterate through all of the memory addresses. 
				 * Make sure that the addresses that end in a 3 are all resolved, and that those that end in
				 * a 4 are linked to the correct external symbol.  Addresses that end in 1 are kept the same, and
				 * those that end in 2 are kept the same so long as the middle 3 integers
				 * do not add up to a number greater than 600
				 * 
				 * If the 3 middle integers add up to a number that is greater than 600 for absolute
				 * addresses, return an error and 0 the middle 3 digits
				 */
				Module currMod = modules.get(i);
				for(int k = 0; k < currMod.getText().size(); k++)
				{
					int currNum = k + currMod.getAddress();
					pw.print(currNum + ":");
					int currAdd = currMod.getText().get(k);
					/*
					 * If the address is relative and needs
					 * to be resolved (ends in a 3)
					 */
					if((currAdd - 3) % 10 == 0)
					{
						currAdd = currAdd / 10;
						int tempAdd = currAdd;
						int sizeOfAdd = 0;
						while(tempAdd >= 1000)
						{
							tempAdd = tempAdd - 1000;
							sizeOfAdd++;
						}
						if((tempAdd + currMod.getAddress()) > machineSize)
						{
							currAdd = sizeOfAdd*1000;
							pw.print("\t" + currAdd);
							pw.println("\tError: Address excedes machine size. Address has been zeroed.");
						}
						else if ((tempAdd + currMod.getAddress()) > (currMod.getText().size() + currMod.getAddress()))
						{
							currAdd = sizeOfAdd*1000;
							pw.print("\t" + currAdd);
							pw.println("\tError: Relative address exceeds the size of the Module.  Address has been zeroed.");
						}
						else{
							currAdd += currMod.getAddress();
							pw.println("\t" + currAdd);
						}
					}
					/* If the address is external
					 * and needs to be linked to the correct symbol (ends in a 4)
					 */
					else if((currAdd - 4) % 10 == 0)
					{
						//change to address of the symbol
						currAdd = currAdd / 10;
						int tempAdd = currAdd;
						while(tempAdd >= 1000)
						{
							tempAdd = tempAdd - 1000;
						}
						if(tempAdd > currMod.getUsage().size() + 1)
						{
							pw.print("\t" + currAdd);
							pw.println("\tError: the external address excedes the usage list size.  Address is being treated as immediate.");
						}
						
						/*
						 * Here the program checks to make sure that all symbols within the usage of a module
						 * have been defined at some point in the program by comparing all of the usage lists
						 * of modules with the existing defined ArrayList of symbols
						 */
						else{
							String currSymbol = currMod.getUsage().get(tempAdd);
							
							boolean found = false;
							for(int inc = 0; inc < symbols.size(); inc++)
							{
								if(currSymbol.equals(symbols.get(inc).getSymbol()))
								{
									currAdd = (currAdd / 1000) * 1000 + symbols.get(inc).getLocation();
									found = true;
									currMod.addUsed(currSymbol);
									pw.println("\t" + currAdd);
								}
							}
							if(found == false)
							{
								currAdd = currAdd / 1000;
								currAdd = currAdd * 1000;
								pw.print("\t" + currAdd);
								pw.println("\tError: the symbol is used but not defined.  Address has been zeroed");
								currMod.addUsed(currSymbol);
							}
						}
					}
					/*
					 * If the address is absolute and needs to be checked for machine size overflow
					 */
					else if((currAdd - 2) % 10 == 0){
						currAdd = currAdd / 10;
						int tempAdd = currAdd;
						int sizeOfAdd = 0;
						while(tempAdd >= 1000)
						{
							tempAdd = tempAdd - 1000;
							sizeOfAdd++;
						}
						if(tempAdd > 600)
						{
							currAdd = sizeOfAdd*1000;
							pw.print("\t" + currAdd);
							pw.println("\tError: Absolute address excedes machine size. Address has been zeroed.");
						}
						else
						{
							pw.println("\t" + currAdd);
						}
					}
					/*
					 * If the address is an immediate, unchanged operand
					 */
					else{
						currAdd = currAdd / 10;
						pw.println("\t" + currAdd);
					}
				}	
			}
			pw.println();
			/*
			 * Warning check that looks to see if a symbol is defined in the Symbol table, but 
			 * it is never used
			 */
			for(int i = 0; i < symbols.size(); i++)
			{
				if(symbols.get(i).getUsage() == false)
				{
					int modnum = 0;
					for(int k = 0; k < modules.size(); k++)
					{
						for(int def = 0; def < modules.get(k).getDef().size(); def++)
						{
							if (symbols.get(i).getSymbol().equals(modules.get(k).getDef().get(def).getSymbol()))
							{
								modnum = k;
							}
						}
					}
					pw.println("Warning: Symbol " + symbols.get(i).getSymbol() + " is defined in module " + modnum + " but not used");
				}
			}
			/*
			 * Looking at a list of used variables created earlier, these
			 * loops determine whether or not all symbols that are on usage lists in modules
			 * are actually used within the module.  Generates a warning otherwise
			 */
			for(int i = 0; i < numModules; i++)
			{
				boolean found = false;
				for(int k = 0; k < modules.get(i).getUsage().size(); k++)
				{
					String currSymbol = modules.get(i).getUsage().get(k);
					for(int j = 0; j < modules.get(i).getUsed().size(); j++)
					{
						if(currSymbol.equals(modules.get(i).getUsed().get(j)))
						{
							found = true;
						}
					}
					if(found == false)
					{
						pw.println("Warning: In module " + i + " symbol " + currSymbol + " is on the usage list but is not used");
					}
				}
					
			}
			pw.close();
			System.out.println("Output has been generated as output.txt in the current directory");
		}
		catch(Exception ex){
			System.out.println("Invalid file; either the filepath is wrong or the input is invalid");
		}
	}

	/**
	 * @param s is an ArrayList of type symbol containing all symbols defined in modules
	 * @return a modified version of the original ArrayList with any double definitions
	 * of Symbols removed, the first definition being kept.  A flag for "isDoublyDefined" is also
	 * set to true in those symbols which are defined more than once, generating an error message
	 * when the symbol table is generated.
	 */
	public static ArrayList<Symbol> symbolCheck(ArrayList<Symbol> s)
	{
		for(int i = 0; i < s.size(); i ++)
		{
			for(int j = 0; j < s.size(); j++)
			{
				if((s.get(i).getSymbol()).equals(s.get(j).getSymbol()))
				{
					if(i > j)
					{
						s.remove(i);
						s.get(j).setDoublyDefined(true);
					}
					else if (i == j)
					{
						;
					}
					else {
						s.remove(j);
						s.get(i).setDoublyDefined(true);
					}
				}
			}
		}
		return s;
	}
	/**
	 * @param s is an ArrayList of type Symbol containing all of the symbols defined in
	 * modules, with no symbol being equal to another.
	 * @param u is an ArrayList of type String containing all symbols in the
	 * usage list of every module.
	 * 
	 * This method modifies the Symbols in s to ensure that all symbols defined are actually used.
	 * Later on, a loop will check to see whether the "isUsed" flag is set to true on all defined symbols.
	 * If the flag is not set to true on any symbol, a warning will be generated
	 */
	public static void checkSymbolUse(ArrayList<Symbol> s, ArrayList<String> u)
	{
		/*Compares all elements of s to those of u to check 
		 * and see first if all symbols in ArrayList s are used, then
		 * removes them from u as they are found.
		 */
		for(int i = 0; i < s.size(); i++)
		{
			for(int j = 0; j < u.size(); j++)
			{
				if(s.get(i).getSymbol().equals(u.get(j)))
				{
					s.get(i).setUsed(true);
					u.remove(j);
					j--;
				}
			}
		}
	}
	/**
	 * @param s is an ArrayList of type Symbol containing all defined symbols with no repeats.
	 * @param m is an ArrayList of type Module containing all modules defined within the input file.
	 * 
	 * This method checks the address of the symbols within the module that they were defined in to ensure
	 * that the addresses of the symbols fits within the bounds of the module.  If they do not, then a flag
	 * "isBiggerThanAdd" will be set to true, the address will be relatively located to the 0th position in
	 * the module, and an error message will be generated.
	 */
	public static void checkSymbolSize(ArrayList<Symbol> s, ArrayList<Module> m)
	{
		for(int i = 0; i < m.size(); i++)
		{
			for(int j = 0; j < m.get(i).getDef().size(); j++)
			{
				Symbol tempSym = m.get(i).getDef().get(j);
				if(tempSym.getLocation() > (m.get(i).getAddress() + m.get(i).getText().size()))
				{
					int symnum = s.indexOf(tempSym);
					s.get(symnum).setisBigger(true);
					s.get(symnum).setLocation(m.get(i).getAddress());
				}
			}
		}
	}
}
