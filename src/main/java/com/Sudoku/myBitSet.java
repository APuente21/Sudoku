package com.Sudoku;

import java.util.*;

class myBitSet {
    public byte [] byteArray;     // the array of bytes (8-bit integers)
    public ArrayList<Integer> intArray = new ArrayList<Integer>();
    public int maxSize;           // max # of set elements allowed
    
    /**
     *  This constructor takes a size and creates a byte array that will accommodate
     *  that number of elements.  Since there could possible be up to 7 bits needed
     *  before we would have another complete byte, 7 is added to the requested size, 
     *  then when the size is divided by 8 for the number of bytes, the extra bits
     *  have been allowed for.
     */
    public myBitSet (int size) {
        maxSize   = size;
        int nbyte = (size + 7) / 8;
        byteArray = new byte [nbyte]; // new array, all zeroes
    }

    /***
     *  This constructor is a "copy constructor", which takes an existing Bitset
     *  and makes a duplicate of it.  Since no elements are being modified the
     *  param Bitset can be copied as bytes rather than doing individual bits.
     */
    
    public myBitSet (myBitSet setA) {
        maxSize   = setA.maxSize;
        int nbyte = setA.byteArray.length;
        byteArray = new byte [nbyte];          // new array, all zeroes
        System.arraycopy (setA.byteArray, 0, 
                          byteArray, 0, setA.byteArray.length);  
    } 
    
    public void setBit (int n) {
        int whichByte = n / 8;
        int whichBit = n % 8;
        byteArray[whichByte] |= (1 << whichBit);
    }

    /**
     *  Returns true if the bit at given offset from address 
     * .. byteArray is set; else false. 
     *  i.e., determines whether element 'offset' is in the set
     */
    public boolean getBit (int n) {
        int whichByte = n / 8;
        int whichBit = n % 8;
        return ( (byteArray[whichByte] & (1 << whichBit)) != 0 );
    }

    public int cardinality(){
    	int count = 0;
		for (int i = 0; i < 16; i++) {
			int whichByte = i / 8;
			int whichBit =  i% 8;	
			if ( (byteArray[whichByte] & (1 << whichBit)) != 0 ) {
				intArray.add(i);
				count++;
			}
		}
		return count;	
	}

    /**
     *  Clears the bit at given offset from address byteArray to 0 
     *  i.e., removes element 'offset' from the set, if present.
     */
     public void clearBit (int n) {
        int whichByte = n / 8;
        int whichBit = n % 8;
        byteArray[whichByte] &= ( (1 << whichBit)^255);
    }

    /**
     *  This method checks first that a Bitset has a byte array, and if so
     *  it clears, or zeroes out, the existing array.
     */
    public void clear() {
        if (byteArray == null)
            error ("clear: Can't clear a set that hasn't been constructed!");
        for (int i = 0; i < byteArray.length; i++)
            byteArray[i] = 0;
    }

    /**
     *  Change existing set to capacity 'size'  (see comments for the constructor
     *  for an explanation of the sizing math).
     */
    public void setSize (int size) {
        maxSize   = size;
        int nbyte = (size + 7) / 8;
        byteArray = new byte [nbyte];    // new array, all zeroes
    }

    /**
     *  This method will ensure that the parameter is within the allowable values
     *  for the set, then will add it to the set.  If it was already present, it
     *  remains so.
     */
    public void include (int i) {
        if (i >= maxSize)
            error ("include: " + i + "  is too large to fit inside the set");
        setBit (i);
    }

    
    /**
     *  Copies setA to receiver, without changing latter's capacity.  In other 
     *  words, when passed another Bitset, the values that it contains are copied
     *  into _this_ Bitset's byte array.  If the parameter set is larger than
     *  this set, an error message is printed and the program halts.
     */
    myBitSet getSet (myBitSet setA) {
        if (byteArray.length < setA.byteArray.length)
            error ("getSet: source set larger than dest. set");
        clear();

        int nbyte = setA.byteArray.length;
        for (int i = 0; i < nbyte; i++) // copy byteArray from arg.
            byteArray[i] = setA.byteArray[i];
        return this; // return receiver, updated
    }
    
    /**
     *  This method performs union on itself combined with setB, and produces
     *  a new Bitset containing the values from both sets.
     *
     *  @param   setB    The Bitset to add (union) with _this_ Bitset
     *  @return          A new Bitset containing all the elements of the combined sets
     */
    
  myBitSet union (myBitSet setB) {
        myBitSet temp = new myBitSet (this.maxSize > setB.maxSize ? this : setB);

        int nbyte = Math.min (byteArray.length, setB.byteArray.length);
        for (int i = 0; i < nbyte; i++)
            temp.byteArray[i] = (byte) (byteArray[i] | setB.byteArray[i]);
        return temp;
    }

    /**
     *  This method performs the difference between _this_ Bitset and the 
     *  parameter Bitset - it determines the elements in this set that are
     *  NOT present in setB, and returns them as a new Bitset.
     */

    public myBitSet missing(){
    	myBitSet temp = new myBitSet (16);
    	
    	for (int i = 1; i < 10; i++){
    		if (!this.getBit(i))
    			temp.setBit(i);
    	}
    	return temp;
    }
    
     public myBitSet difference (myBitSet setB) {
        myBitSet temp = new myBitSet (this);
        int nbyte = Math.min (byteArray.length, setB.byteArray.length);
        for (int i = 0; i < nbyte; i++)
            temp.byteArray[i] = (byte)(byteArray[i] & (setB.byteArray[i] ^ 255));
        return temp;
    }
     
    
    /**
     *  This method performs intersection on itself and the parameter Bitset.  A new
     *  resulting Bitset is created which has the elements that are present in both
     *  Bitsets and is returned.
     */
     public myBitSet intersect (myBitSet setB){
        myBitSet temp = new myBitSet (Math.min (this.maxSize, setB.maxSize));
        int nbyte = Math.min (byteArray.length, setB.byteArray.length);
        for (int i = 0; i < nbyte; i++)
            temp.byteArray[i] = (byte)(byteArray[i] & setB.byteArray[i]);
        return temp;
    }

    /**
     *  This method does content comparison to determine equality of sets.  Sets
     *  of different lengths are allowed, and will result in true as long as the
     *  larger set has no elements above the maxSize of the smaller set.
     *
     *  @param   setB   The Bitset to test (compare) to _this_ Bitset
     *  @return         True if the same elements are set in both Bitsets, false if not
     */
    public boolean equals (myBitSet setB) {
	/*  Begin by getting the length of the _shorter_ Bitset array...  */
        int nbyte = Math.min (byteArray.length, setB.byteArray.length);

	/*  Now we test both sets for that length of elements  */
        for (int i = 0; i < nbyte; i++)
        {
            if (byteArray[i] != setB.byteArray[i]) return false;
        }

	/*  If the sets are not of equal length, is _this_ set the larger one?  If so,
	    go through and look for other elements above the maxSize of the other set...  */
        if (byteArray.length > nbyte) {
            for (int i = nbyte; i < byteArray.length; i++)
            {
              if (byteArray[i] != 0) return false;
            }
        }

	/*  Otherwise, is the _other_ set the larger one?  If so, go through and 
	    look for other elements greater than the maxSize of _this_ Bitset  */
        if (setB.byteArray.length > nbyte)
        {
            for (int i = nbyte; i < setB.byteArray.length; i++)
            {
              if (setB.byteArray[i] != 0) return false;
            }
        }
        return true;
    }

    /**
     *  This method tests for an empty, or null, set, by walking through the
     *  byteArray and checking each byte for inclusions (which produce a value
     *  greater than 0).  If any are found, the Bitset is not a null set.
     *
     *  @return       True if this is a null (empty) set, false if it is not.
     */
    public boolean isNull ()
    {
        for (int i = 0; i < byteArray.length; i++)
        {
            if (byteArray[i] != 0) return false;
        }
        return true;
    }

    /**
     *  This creates a String representation of a Bitset object in standard
     *  set notation:  { x  x  x  x }  Elements present are listed.
     *
     *  @return       A String representing the Bitset in printable format
     */
    
    public int [] toArray(){
    	cardinality();
    	int [] temp = new int[intArray.size()];
    	
    	for (int i = 0; i < intArray.size(); i++){
    		temp[i] = intArray.get(i);
    	}
    	return temp;
    }
   
    public String toString()  {  
       String str = "{  ";
       for (int i = 0 ; i < maxSize; i++) {
             if ( getBit(i)) str += i + "  ";
       }
       return str + "}";
    }

    /**
     *  A method to print a custom error message to the user, then exit
     */
    private void error (String msg) {
        System.out.print (" " + msg);
        System.exit (1);
    }
}
