package fr.ensimag.deca.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Manage unique symbols.
 * 
 * A Symbol contains the same information as a String, but the SymbolTable
 * ensures the uniqueness of a Symbol for a given String value. Therefore,
 * Symbol comparison can be done by comparing references, and the hashCode()
 * method of Symbols can be used to define efficient HashMap (no string
 * comparison or hashing required).
 * 
 * @author gl49
 * @date 01/01/2021
 */
public class SymbolTable {
    private Map<String, Symbol> map = new HashMap<String, Symbol>();

    /**
     * Create or reuse a symbol.
     * 
     * If a symbol already exists with the same name in this table, then return
     * this Symbol. Otherwise, create a new Symbol and add it to the table.
     */
    public Symbol create(String name) {
//        throw new UnsupportedOperationException("Symbol creation");
    	if (this.map.containsKey(name)) {
            return this.map.get(name);
        }
        Symbol nvSymbol = new Symbol(name);
        this.map.put(name, nvSymbol);
        return nvSymbol;
    }

    public static class Symbol {
        // Constructor is private, so that Symbol instances can only be created
        // through SymbolTable.create factory (which thus ensures uniqueness
        // of symbols).
        private Symbol(String name) {
            super();
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        private String name;
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Symbol)) {
            	return false;
            }else {
            	return this.name.equals(((Symbol) o).name);    
            }
    	}
        
        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
