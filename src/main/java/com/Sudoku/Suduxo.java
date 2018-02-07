package com.Sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Suduxo {
	public static void main (String [] args) {
		myPuzzle game = new myPuzzle ();
	}
}

/*All of the code in the constructor is for the start screen, which will be shown to the user so they may
 * select the difficulty level of the game they choose to play. The actual game starts once the user selects
 * the difficulty level, at which point a method the StartGame method is called, which calls on the mainPanel class
 * which is where most of the code for the Sudoku game is stored
 * 	
*/
class myPuzzle extends JFrame{
	
	myPuzzle(){

		//create JPanel for start page, set layout, and specify gridbag constraints
		JPanel intPanel = new JPanel(new GridBagLayout());
		intPanel.setBackground(Color.LIGHT_GRAY);
		
		GridBagConstraints c = new GridBagConstraints();
			c.gridwidth=	5;
			c.gridheight=	5;
			c.weightx= 		1;
			c.weighty= 		1;
			c.gridx = 		0;
			c.gridy= 		1;
			c.insets= 		new Insets(3,2,3,5);
			c.ipadx=		0;
			c.fill= 		GridBagConstraints.VERTICAL;
		
		//user instructions
		JLabel back = new JLabel("SELECT LEVEL TO START THE GAME!");
		intPanel.add(back, c);	

			c.gridx = 		2;
			c.gridy= 		5;
			c.insets= 		new Insets(4,5,4,5);
			c.fill= 		GridBagConstraints.BOTH;
		
		//create buttons representing the difficulty level of the game. Also add action listner and add to the panel
		JButton easy = new JButton("EASY");
		easy.addActionListener(new StartListen());
		c.gridy+= 		5;
		intPanel.add(easy,c);
		
		//create buttons representing the difficulty level of the game. Also add action listner and add to the panel
		JButton med = new JButton("MED");
		med.addActionListener(new StartListen());
		c.gridy+= 		5;
		intPanel.add(med,c);
		
		//create buttons representing the difficulty level of the game. Also add action listner and add to the panel
		JButton hard = new JButton("HARD");
		hard.addActionListener(new StartListen());
		c.gridy+= 		5;
		intPanel.add(hard,c);
		
		setSize(300,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//set default close
		add(intPanel, BorderLayout.CENTER);
		setResizable(false);
		
		setVisible(true);
	}
	
	//method calls on the mainPanel class, which starts the game
	public void StartGame(String level) {
		
		JFrame game = new JFrame();// frame will be used to display the sudoku board
		game.setSize(600,600);//set frame size
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//set default close
		mainPanel myPanel = new mainPanel(level);
		game.add(myPanel, BorderLayout.CENTER);
		game.setResizable(false);
		
		game.setVisible(true);// make the sudoku board visible
		
	}
	
	//Action listner method is invoked once the user clicks on one of the buttons
	class StartListen implements ActionListener {
		public void actionPerformed(ActionEvent e){
			
			JButton temp= (JButton)e.getSource();
			setVisible(false);// turn off start screen
			
			//switch case calls on the StartGame method, which starts the game. Furthermore, the difficulty
			//level is passed to method, so it can be passed to the mainPanel class.
			String lvl = temp.getText();
			if (lvl.equals("EASY"))
				StartGame("EASY");
			else if (lvl.equals("MED"))
				StartGame("MED");
			else
				StartGame("HARD");
				
		}
	}
}

class mainPanel extends JPanel{
	JButton [][] myPanelButtons = new JButton[9][9];//array where I store all 81 buttons in sudoku board
	myBitSet [] rowSet = new myBitSet [9];//set used to check the numbers passed to each row
	myBitSet [] colSet = new myBitSet [9];//set used to check the numbers passed to each col
	myBitSet [] gridSet = new myBitSet [9];//set used to check the numbers passed to each grid
	JButton numberPanel[] = new JButton [9];//panel that the player uses to complete the board
	JButton clear = new JButton("del");//button will be used to clear items from the board
	String level = "";// used to store the difficulty level of the game
	JButton clickedButton = null;//to identify which button in the board has been selected
	Color clickedButtonColor = null;//to store background color of the button clicked
	Font f = new Font("Calibri", Font.BOLD,20);//font
	myBitSet completed = new myBitSet(16); 
	
	/*The constructor is where the game is created. The actual sudoku board is created in the constructor. In addion,
	 * several methods are called from the constructor- i will explain before each method
	 */
	mainPanel(String level){
		this.level = level;//store difficulty level selected by user
		
		createBitSets();//call on method that creates all of the bitSets
		
		setBackground(Color.BLACK);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
			c.gridwidth=	1;
			c.gridheight=	1;
			c.weightx= 		1;
			c.weighty= 		1;
			c.gridx = 		1;
			c.gridy= 		1;
			c.insets= 		new Insets(1,1,1,1);
			c.ipadx=		0;
			c.fill= 		GridBagConstraints.BOTH;
		Dimension d = new Dimension (40,40);
			int count = 0;
			int grid;
		//create all 81 buttons, which represents the sudoku board.	
		for (int i = 0; i < 9;i++){
			for (int j = 0; j <9; j++){
				myPanelButtons[i][j]= new JButton();//create JButton
				add(myPanelButtons[i][j], c);//add button to panel and apply gridconstraints
				myPanelButtons[i][j].addActionListener(new buttonListener());//add action listner to button
				myPanelButtons[i][j].setName(Integer.toString(count));//store the name of the button
				myPanelButtons[i][j].setPreferredSize(d);
				c.gridx++;//increase the position of the x axis in the board- so the buttons appear next to eachother
				count++;
				
				grid = getGrid(i, j);
				
				//set the color of the grids
				if (grid%2 ==0)
					myPanelButtons[i][j].setBackground(Color.LIGHT_GRAY);
				else
					myPanelButtons[i][j].setBackground(Color.WHITE);
						
			}
			c.gridy++;//bring buttons to new line
			c.gridx = 1;//reset the x coordinate to the begining of the board
		}
		//add a JLabel, which will appear as a blank space between the main grid and number panel
		c.gridy= 		11;
		add(new JLabel(),c);
		
		//add the pencil and delete button to the board and modify constraints
		c.gridx=		9;
		c.gridy++;
		c.fill=			GridBagConstraints.BOTH;
		c.anchor =		GridBagConstraints.EAST;
		add(clear,c);
		clear.addActionListener(new panelListener());
		
		c.fill= 		GridBagConstraints.HORIZONTAL;
		c.anchor =		GridBagConstraints.WEST;
		c.gridwidth=	1;
		c.gridheight=	1;
		c.weightx= 		0;
		c.weighty= 		0;
		c.gridx = 		1;
		c.gridy= 		20;
		c.insets		= new Insets(1,1,1,1);

		//create the number panel at the bottom of the board
		for (int k = 0; k< 9; k++){
			numberPanel[k] = new JButton(Integer.toString(k+1));
			add(numberPanel[k], c);
			numberPanel[k].addActionListener(new panelListener());;
			numberPanel[k].setPreferredSize(d);
			c.gridx += 1;
		}
		
		fillBoard();//this method completes the sudoku board with 
		removeNumbers(level);//this method removes items from the board. 
		
		
		
	}
	
	
	/*This method completes the sudoku board by applying the rules of sudoku. In a sudoku board each row, column, and 
	 * grid, must contain the numbers 1-9 without any duplicates. The method starts by calling on a method that 
	 * generates a random number between 1 and 9, inclusive. Once the random number is generated, it checks the row
	 * set, column set and grid	set- the location of the button on the board determines which set gets called.
	 * As an example, button 0,0, checks the 0 row and column bitSet in each array. Since 0,0 is in the first grid
	 * it also checks the number in the first grid
	*/
	private void fillBoard(){
		boolean test = false;
		int grid;
		for (int i = 0; i < myPanelButtons.length;i++){//loop through rows of the 2D array
			for (int j = 0; j< myPanelButtons[i].length; j++){//loop through columns of 2D array
				
				grid = getGrid(i, j);
 				test = fillHelper(i, j, grid);
				
 				if (test)//test if we backtracked. If we did then we reset the col to zero
					j = -1;// it's -1 because one this loop ends it will start at zero, which is the first col
			}
		}	 
	}
	
	private boolean fillHelper(int row, int col, int grid){
		boolean check;
		int randomTemp = getRanNumber(1,9);
		int [] choices = posNumbers(row, col, grid);
		boolean reset = false; 

		check = checkHelp(randomTemp, row, col, grid); 
			if (!check) {//if num is not in sets, add it to sets and set txt to Jbuttons
				myPanelButtons[row][col].setText(Integer.toString(randomTemp));
				addToSets(row, col, grid, randomTemp);
			} else if (choices.length > 0){//if i have any choices, choose from choice array and add to sets
				randomTemp = getRanNumber(0,choices.length-1);
				myPanelButtons[row][col].setText(Integer.toString(choices[randomTemp]));
				addToSets(row, col, grid, choices[randomTemp]);
			} else {// if i don't have any choices, backtrack to find a solution
				backTrack(row, col, grid);
			}
		
		//if after backtracking a number has not been set to the button, then wipe the row and restart
		if (myPanelButtons[row][col].getText()==""){
			reset = true;
			if (col == 0)
				reStartRow(row, 0);
			else 
				reStartRow(row, col);
		}
		
		return reset;//if i resets, then return true, so that i know to loop from the first col
	}
	
	//check if a number is present in all three sets. If true 
	private boolean checkHelp(int ranNum, int row, int col, int grid){
		boolean rowSetCheck= rowSet[row].getBit(ranNum);
		boolean colSetCheck = colSet[col].getBit(ranNum);
		boolean gridSetCheck = gridSet[grid].getBit(ranNum);
		boolean check;
		
		if ((!rowSetCheck && !colSetCheck && !gridSetCheck))
			check = false;
		else 
			check = true;
		
		return check;
	}
	
	/*This returns an array of the possible numbers that can go in a button. This works by returning a bit set with
	 * all of the numbers that are missing in the row, col, and grid. It then returns an array with all the number
	 * that all three sets have in common. One special mention, the first version of this code used union and intersect
	 * operations from the sets, but this was giving me some error in the code, which I couldn't understand why. As,
	 * a workaround i created a missing method in the bitSet class
	 */
	private int [] posNumbers(int row, int col, int grid){
		int [] temp;

		myBitSet possibleRow = rowSet[row].missing();
		myBitSet possibleCol = colSet[col].missing();
		myBitSet possibleGrid = gridSet[grid].missing();
		
		myBitSet possibleAns = possibleRow.intersect(possibleCol);
		possibleAns = possibleAns.intersect(possibleGrid);		
		
		temp = possibleAns.toArray();
		
		return temp;
	}
	
	/*Sometimes when you are going through a row, specially near the end, you might find that you have some empty
	 * buttons, but you can't fill them because it would violate the rules of the game. In this case, this method
	 * takes the missing numbers and checks if they can be inserted in an earlier col, within the same row. If it can,
	 * it checks if the button occupying the old col location can be placed in current col location. If it can, the 
	 * missing number goes to the old col location, and the number that was in the old col location goes to the
	 * location
	 */
	private void backTrack(int row, int col, int grid){
		int misRow = rowSet[row].cardinality();//get size of sets
		int misCol = colSet[col].cardinality();//get size of sets
		int misGrid = gridSet[grid].cardinality();//get size of sets
		int originalCol = col;
		boolean backTrack = false;
		int newGrid;
		int currentInt;
		boolean check;
		int [] misNum;
		
		//to find the set that is missing the least amount of elements. The missing elements are stored in an array
		if (misRow >= misCol){
			if (misRow >= misGrid){
				misNum = rowSet[row].missing().toArray();
			} else { 
				misNum = gridSet[grid].missing().toArray();
				}
		} else if (misCol >= misGrid) { 
				misNum = colSet[col].missing().toArray();
				
		} else { 	
				misNum = gridSet[grid].missing().toArray();	
			}
		
		for (int i = 0; i < misNum.length; i++) {
			if (col == 0) {
				newGrid = getGrid(row, col);
				check = checkHelp(misNum[i], row, col, newGrid);
				
				if (!check) {
					myPanelButtons[row][col].setText(Integer.toString(misNum[i]));
					addToSets(row, col, grid, misNum[i]);
				}
			} else {
				col--;
				while (col>0){
					currentInt = Integer.parseInt(myPanelButtons[row][col].getText());
					newGrid = getGrid(row, col);
					check = checkHelp(misNum[i], row, col, newGrid);
					//check = checkHelp(i, row, col, newGrid);
			
					//check if i can put old value into new value
					if (!check){
						removeFromSets(row, col, newGrid, currentInt);
				
						if (!checkHelp(currentInt,row, originalCol, grid)){
							//add miss number to old button
							myPanelButtons[row][col].setText(Integer.toString(misNum[i]));
							addToSets(row, col, newGrid, misNum[i]);
							
							myPanelButtons[row][originalCol].setText(Integer.toString(currentInt));
							addToSets(row, originalCol, grid, currentInt);
							backTrack = true;
						}else {
							addToSets (row, col, newGrid, currentInt);
						}
					}	 
			
					if (backTrack)
						break;			
					col--;
					}
			}
			if (backTrack)
				break;
			col = originalCol;
			
		}
	}
	
	/*This method removes all of the numbers in the row from the col, row, and grid sets. In addition, it all wipes
	 * the text of all the buttons. This method is needed because there are sometimes that backtracking won't produce
	 * a solution to the row that satisfies all of the rules of Sudoku. In this case, I wipe the row until i get a 
	 * solution that would fill the row correctly.
	 */
	private void reStartRow(int row, int col){
		int grid;
		int getNumber;
		
		for (int i = col; i > -1; i--){
			if (myPanelButtons[row][i].getText()!= ""){
				getNumber = Integer.parseInt(myPanelButtons[row][i].getText());
				myPanelButtons[row][i].setText("");
				grid = getGrid(row, i);
				removeFromSets(row, i, grid, getNumber);
			}
		}
	}
	
	//removes a number from a row, col, grid set
	private void removeFromSets(int row, int col, int grid, int number) {
		rowSet[row].clearBit(number);
		colSet[col].clearBit(number);
		gridSet[grid].clearBit(number);
	}
	
	//adds a number to a row, col, and grid set
	private void addToSets (int row, int col, int grid, int num) {
		rowSet[row].setBit(num);
		colSet[col].setBit(num);
		gridSet[grid].setBit(num);
	}
	
	//this method gets the grid of the current button.
	private int getGrid(int row, int col) {
		int grid;
	
		if (row < 3) {
			if (col < 3)
				grid = 0;
			else if (col < 6)
				grid = 1;
			else
				grid = 2;
		} else if (row < 6) {
			if (col < 3)
				grid = 3;
			else if (col < 6)
				grid = 4;
			else
				grid = 5;
		} else {
			if (col < 3)
				grid = 6;
			else if (col < 6)
				grid = 7;
			else
				grid = 8;	
		}	
	return grid;
	}
	
	//This method creates 27 bitSets. 9 row each row, 9 for each columb, and 9 for each grid.
	// The sets are stored are in Arrays, which are then called by the fillBoard method to complete the sudoku board
	private void createBitSets(){
		for (int i =0; i < 9; i++){
			rowSet[i] = new myBitSet(16);
			colSet[i] = new myBitSet(16);
			gridSet[i] = new myBitSet(16);
			completed.setBit(i+1);
		}
	}

	//returns a random number between 1 and 9, inclusive
	private int getRanNumber(int min, int max){
		return (int)(Math.random()*((max-min)+1))+ min;
	}
	
	//Af the Sudoku board is completed, this method removes items from the board, which will be the items the user
	//has to fill. The number of items that get removed depend on the difficulty level chosen by the user
	private void removeNumbers(String level) {
		
		if (level.equals("EASY"))
			removeHelper(40);//remove the txt for 40 bttns
		else if (level.equals("MED"))
			removeHelper(45);//remove the txt for 50 bttns
		else
			removeHelper(50);//remove the txt for 55 bttns

		updateColors();
	}
	
	/*This method is called from the removeNumbers method. The purpose of this code is to clear items from the board.
	 * The method works by generating 2 random digits between 0 and 8, which corresponds a row and column in the board.
	 */
	public void removeHelper(int numToBeDel) {
		int randRow;
		int randCol;
		int oldVal;
		int grid;

		String test;
		for (int i = 0; i < numToBeDel; i ++){
			randRow = getRanNumber(0,8);//get random row number in array where buttons are stored
			randCol = getRanNumber(0,8);//get random column number in array where buttons are stored
			test = myPanelButtons[randRow][randCol].getText();
			//check the random select was already removed. If it was, get a new random button
			
			if (test == ""){
				i--;//

			} else {
			
				//saved the number random number to be removed. 
				oldVal = Integer.parseInt(myPanelButtons[randRow][randCol].getText());
				//clear text of button selected
				myPanelButtons[randRow][randCol].setText("");

				grid = getGrid(randRow, randCol);
				removeFromSets(randRow, randCol, grid, oldVal);
			}
		}
	}
	
	/*This method invoked from the removeNumbers method. Once the numbers have been removed from the board, the
	 * font of the remaining buttons need to be updated. These button will be hardcoded in the board, which means
	 * the user won't be able to modify them in the board 
	 */
	public void updateColors(){
		for (int i = 0; i < 9; i++){
			for (int j = 0; j <9; j++){
				if (myPanelButtons[i][j].getText()!="")
					myPanelButtons[i][j].setForeground(Color.RED);
					myPanelButtons[i][j].setFont(f);
			}
		}
	}
	
	/*action listener is called whenever a user clicks on one of the 81 buttons on the board. The color of the
	 * button select changes yellow, assuming it's not one of the hardcoded numbers. If it's not one of the hardcoded
	 * buttons, then clickedButton variable is updated to reference the JButton clicked. The variable
	 * ckicledButtonColor is also updated to store the background color of the button clicked
	 */
	class buttonListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			JButton temp = (JButton)e.getSource();
			
			//if button clicked is one of the hardcoded buttons
			//	set reference of clicked button to null
			//  set reference of clicked button to null
			if (temp.getForeground()== Color.RED) {
				if (clickedButton != null) {
					clickedButton.setBackground(clickedButtonColor);
					clickedButton = null;
				}
			}
			else {
				if (clickedButton != null){
					clickedButton.setBackground(clickedButtonColor);
				}
					clickedButton= temp; 
					clickedButtonColor = clickedButton.getBackground(); 
					clickedButton.setBackground(Color.YELLOW );
			}
		}
	}
	
	class panelListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			JButton temp = (JButton)e.getSource();
			int row;
			int col;
			int grid; 
			
			
			if (clickedButton != null) {
				row = Integer.parseInt(clickedButton.getName())/9;
				col = Integer.parseInt(clickedButton.getName())%9;
				grid = getGrid(row, col);
				
				if (temp.getText()== "del"){
					removeFromSets(row, col, grid, Integer.parseInt(clickedButton.getText()));
					clickedButton.setText("");
				} else if (clickedButton.getText() != ""){
					//left empty on purpose to
				}
				else if (!checkHelp(Integer.parseInt(temp.getText()), row, col, grid)) {
					clickedButton.setText(temp.getText());
					addToSets(row, col, grid, Integer.parseInt(temp.getText()));
					
					finished();
				}
			}
		}
		
		private void finished(){
			boolean testFinished = true;
			
			for (int i = 0; i < myPanelButtons.length; i++){
				for (int j = 0; j < myPanelButtons[i].length; j++){
					if (myPanelButtons[i][j].getText() == "")
						testFinished = false;
				}
			}
			
			if (testFinished){
				JOptionPane.showMessageDialog(null, "Congratualtions, you completed the game");
			}
			
		}
	}
	
}