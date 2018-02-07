package com.Sudoku;

import java.util.ArrayList;


public class intSet {
	private ArrayList<Integer> myList = new ArrayList<Integer>();
	private int name;
	
	public intSet(int x){
		name = x;
	}
	
	public int getName(){
		return name;
	}
	
	
	public void addInt(int x){
			myList.add(x);
	}
	
	public int contains(int x){
		int temp = 0;
		
		for (int i = 0; i < myList.size(); i++) {
			if (myList.get(i)== x) 
				return 1;
		}
		return temp;
	}
	
	
	/*check all 3 sets (Grid, column, row) to see if value is in each set. The temp variable starts of as false, because
	 * we are checking that we don't have any duplicates in any of the sets. If in any of the sets a duplicate is found
	 * we return false. 
	 */
	
	
	public boolean contains(intSet setA, intSet setB, int x ){
		boolean temp = false;

		for (int i = 0; i < myList.size(); i++) {
			if (myList.get(i)== x) {
				return true;
			}
		}
		
		for (int i = 0; i < setA.getmyList().size(); i++) {
			if (setA.getmyList().get(i)== x) {
				return true;
			}
		}
		
		for (int i = 0; i < setB.getmyList().size(); i++) {
			if (setB.getmyList().get(i)== x){ 
				return true;
			}
		}
		return temp;
	}
	
	
	public ArrayList<Integer> getmyList(){
		return myList;
	}
	
	public String toString(){
		String temp = "[";
		
		for (int i = 0; i< myList.size(); i ++){
			temp = temp + myList.get(i) + ",";
		}
		temp = temp + "]";
		return temp;
	}
}
