package ass4;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicArrowButton;


public class Restaurant extends JFrame {
	
	private Food[] foods; // store all food
	private String[] category; // store all categories
/*	private Food[][] foundFoods; // store food in different categories
*/	private int capacity; 
	private int size; // store the size of the quantity of food
	private int categorySize; // store the size of category
	private int foundCount;
	
	public Restaurant() { // initialize variables
		size = 0;
		capacity = 20;
		categorySize = 0;
		foods = new Food[capacity];
		category = new String[20];
/*		foundFoods = new Food[20][capacity];*/
		for(int i=0; i<capacity; i++)
			foods[i] = new Food();	
/*		for(int i=0; i<20; i++) {
			for(int j=0; j<capacity; j++) {
				foundFoods[i][j] = new Food();
			}
		}*/
	}
	
	public void readData(String infileName) { // read food data from a txt
		Scanner infile = null;
		try {
			infile = new Scanner(new File(infileName));
		}
		catch (FileNotFoundException ex) {
			System.out.println("file not found");
			System.exit (0);
		}

		while(infile.hasNext()) {
			String nName = infile.nextLine();
			String nId = infile.nextLine();
			String nCategory = infile.nextLine();
			String nDescription = infile.nextLine();
			String nType = infile.nextLine();
			String nSize = infile.nextLine();
			double nPrice = Double.parseDouble(infile.nextLine());
			Food nFood = new Food(nName, nId, nCategory, nDescription, nType, 
					nSize, nPrice);
			append(nFood);
		}

		infile.close();
	}

	public void writeBack(String outfileName) { // write back food to a txt

		PrintWriter outfile = null;

		try {
			outfile = new PrintWriter(outfileName);
		}
		catch(FileNotFoundException ex) {
			System.out.println("Cannot open file. Please check.");
			System.exit(0);
		}

		for(int i=0; i<size; i++) {
			outfile.print(foods[i].toString());
			if(i!=size-1)
				outfile.println();
		}

		outfile.close();
	}
	
	public void start() { // start the app
		sortFood();
		JButton adminButton = new JButton("Admin");
		JButton serverButton = new JButton("Server");
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300,300);
		setVisible(true);
		adminButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logIn(1);			
			}
		});
		this.add(adminButton);
		serverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logIn(2);
			}
		});
		this.add(serverButton);
	}
	
	public void append(Food nFood) {
		foods[size++] = nFood;
        writeBack("foods.txt");
	}
	
	public void search(String nName) { // search and modify food
		String temp = "";
		foundCount = 0;
		for (int i=0; i<size; i++) {
			temp = foods[i].getName();
			if(nName.equals(temp)) {
				foundCount++;
				operate(foods[i]);
			}
		}
		if(foundCount==0) 
			JOptionPane.showMessageDialog(null, "Food does not exist!");	
	}
	
	public void sortFood() { // sort all food by categories
		category[0] = foods[0].getCategory();
		categorySize = 1;
		boolean dup;
		
		for(int i=0; i<size; i++) { // sort all food
			dup = false;
			for(int j=0; j<categorySize; j++) {
/*				int temp = 0;
				for(int k=0; k<foundFoods[j].length; k++) {
					if(!foundFoods[j][k].getName().equals(""))
						temp++;
				}*/

				if(foods[i].getCategory().equals(category[j])) { // if a food belongs to a known category then put this food in the category
/*					foundFoods[j][temp] = foods[i];*/
					dup = true;
				}
			}
			
			if(!dup) { // if a food has a new category then create a new category
				category[categorySize] = foods[i].getCategory(); 
/*				foundFoods[categorySize][0] = foods[i];*/
				categorySize++;
			}
			
			
		}
	}
	
	public boolean duplicate(String nName) { // check if the input food is duplicated
		boolean dup = false;
		foundCount = 0;
		String temp = "";
		for(int i=0; i<size; i++) {
			temp = foods[i].getName();
			
			if(nName.equals(temp))
				foundCount++;
		}
		if(foundCount!=0) 
			dup = true;					
		else if(foundCount==0)
			dup = false;
		return dup;
	}
	
	public void newFood(String nName, String nCate, String nDes, String nType, String nSize, double nPrice) {
		boolean isOK = false;
		while(!isOK) {
			if(duplicate(nName)) {
				JOptionPane.showMessageDialog(null, "Food already exists.");
				break;
			}
			else {
				isOK = true;
			}
		}
		if(isOK) {
			String nId = Integer.toString(size+1);
			Food nFood = new Food(nName, nId, nCate, nDes, nType, nSize, nPrice);
			append(nFood);
			JOptionPane.showMessageDialog(null, "New food information:\nName:" + nName +
					"\nID: " + nId +
					"\nCategory: " + nCate +
					"\nDescription: " + nDes +
					"\nType: " + nType +
					"\nSize " + nSize +
					"\nPrice " + nPrice);
		}
	}

	public void logIn(int key) { // log in to manage food or browse food
		String password = "123456";
		boolean isOK = false;
		while(!isOK) {
			String input = JOptionPane.showInputDialog("Please enter the password:");
			if(input.equals(password)) {
				isOK = true;
				switch(key) {
				case 1: manage(); break;
				case 2: sortFood(); new order(); break;				
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "Invalid Password!");
			}
		}
	}
	
	public void manage() { // manage food including add/modify food
		JButton addFoodBtn, modifyFoodBtn;
		JFrame managePane = new JFrame();
		managePane.setSize(300,300);
		managePane.setLayout(new FlowLayout());
		managePane.setVisible(true);	
		managePane.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		addFoodBtn = new JButton("Add a Food");
		addFoodBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new newFoodPane();
			}
		});
		managePane.add(addFoodBtn);
			
		modifyFoodBtn = new JButton("Modify a Food");
		modifyFoodBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String foodName = JOptionPane.showInputDialog("Enter the food name:");
				search(foodName);
			}
		});
		managePane.add(modifyFoodBtn);
	}
	
	public void operate(final Food mFood) { // delete or modify food
		JButton deleteBtn = new JButton("Delete");
		JButton updateBtn = new JButton("Update");
		JLabel nameLbl = new JLabel(mFood.getName());
		JLabel cateLbl = new JLabel(mFood.getCategory());
		JLabel desLbl = new JLabel(mFood.getDescription());
		JLabel typeLbl = new JLabel(mFood.getType());
		JLabel sizeLbl = new JLabel(mFood.getSize());
		JLabel priceLbl = new JLabel(mFood.getPrice());
		final JTextField nameTxt = new JTextField(20);
		final JTextField cateTxt = new JTextField(20);
		final JTextField desTxt = new JTextField(20);
		final JTextField typeTxt = new JTextField(20);
		final JTextField sizeTxt = new JTextField(20);
		final JTextField priceTxt = new JTextField(20);
		JFrame operateFrame = new JFrame();
		operateFrame.setSize(250,400);
		operateFrame.setLayout(new FlowLayout());
		operateFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		operateFrame.setVisible(true);		
		operateFrame.add(nameLbl);
		operateFrame.add(nameTxt);
		operateFrame.add(cateLbl);
		operateFrame.add(cateTxt);
		operateFrame.add(desLbl);
		operateFrame.add(desTxt);
		operateFrame.add(typeLbl);
		operateFrame.add(typeTxt);
		operateFrame.add(sizeLbl);
		operateFrame.add(sizeTxt);
		operateFrame.add(priceLbl);
		operateFrame.add(priceTxt);
		
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mFood.setFood("", "", "This food has been deleted", "", "", 0);
				writeBack("foods.txt");
			}
		});
		operateFrame.add(deleteBtn);
		
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					double nPrice = Double.parseDouble(mFood.getPrice());
					if(!priceTxt.getText().equals(""))
						nPrice = Double.parseDouble(priceTxt.getText());
					String nName = nameTxt.getText();
					if(nName.equals(""))
						nName = mFood.getName();
					String nCate = cateTxt.getText();
					if(nCate.equals(""))
						nCate = mFood.getCategory();
					String nDes = desTxt.getText();
					if(nDes.equals(""))
						nDes = mFood.getDescription();
					String nType = typeTxt.getText();
					if(nType.equals(""))
						nType = mFood.getType();
					String nSize = sizeTxt.getText();
					if(nSize.equals(""))
						nSize = mFood.getSize();

					mFood.setFood(nName, nCate, nDes, nType, nSize, nPrice);
					JOptionPane.showMessageDialog(null, mFood.getFood());
					writeBack("foods.txt");
				}
				catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error!");
				}
			}
		});
		operateFrame.add(updateBtn);
	}
	
	class order extends JFrame {
		private JFrame catePane = new JFrame(); 
		private JFrame foodPane;
		private JFrame desPane = new JFrame();
		private JButton[] cateBtn;
		private JTextArea desArea = new JTextArea();
		private String[] description = new String[size];
		
		public order() {					
			catePane.setSize(150,400);
			catePane.setLayout(new GridLayout(10,1));
			catePane.setVisible(true);
			catePane.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

					
			for(int i=0; i<categorySize; i++) {
				cateBtn = new JButton[categorySize];
				cateBtn[i] = new JButton(category[i]);
				catePane.add(cateBtn[i]);
				cateBtn[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent getRelatedFood) {
						String actionCommand = getRelatedFood.getActionCommand();
						for(int i=0; i<categorySize; i++) {
							if(actionCommand.equals(category[i])) {
								getRelatedFood(category[i]);
							}
						}
					}
				});
			}		
		}
		
		public void getRelatedFood(String key) {
			JButton[] foodBtn;
			foodPane = new JFrame();		
			foodPane.setSize(250,400);
			foodPane.setLayout(new GridLayout(10,1));
			foodBtn = new JButton[size];
			for(int i=0; i<size; i++) {
				if(foods[i].getCategory().equals(key)) {
					foodBtn[i] = new JButton(foods[i].getName());
					foodPane.add(foodBtn[i]);
					foodBtn[i].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String actionCommand = e.getActionCommand();
							for(int i=0; i<size; i++) {
								if(actionCommand.equals(foods[i].getName())) {
									getRelatedInfo(i);
								}
							}
						}
					});
				}			
			}
			foodPane.setLocation(150,0);
			foodPane.setVisible(true);
		}
		
		public void getRelatedInfo(int key) {
			desArea.setText(foods[key].getFood());
			desArea.setEditable(false);
			desPane.setLocation(400,0);
			desPane.setSize(350,400);
			desPane.add(desArea);
			desPane.setVisible(true);	
			desPane.setLayout(new FlowLayout());
		}
	}
	
	class newFoodPane extends JFrame { //for creating a newAccount
		private JLabel nameLbl = new JLabel("Food Name:");
		private JLabel cateLbl = new JLabel("Food Category:");
		private JLabel desLbl = new JLabel("Food Description:");
		private JLabel typeLbl = new JLabel("Food Type:");
		private JLabel sizeLbl = new JLabel("Food Size:");
		private JLabel priceLbl = new JLabel("Food Price:");
		private JTextField nameTxt;
		private JTextField cateTxt;
		private JTextField desTxt;
		private JTextField typeTxt;
		private JTextField sizeTxt;
		private JTextField priceTxt;
		
		public newFoodPane() {
			this.setSize(300,400);
			this.setLayout(new FlowLayout());
			this.setVisible(true);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.add(nameLbl);
			nameTxt  = new JTextField(20);
			this.add(nameTxt);
			this.add(cateLbl);
			cateTxt = new JTextField(20);
			this.add(cateTxt);
			this.add(desLbl);
			desTxt = new JTextField(20);
			this.add(desTxt);
			this.add(typeLbl);
			typeTxt = new JTextField(20);
			this.add(typeTxt);
			this.add(sizeLbl);
			sizeTxt = new JTextField(20);
			this.add(sizeTxt);
			this.add(priceLbl);
			priceTxt = new JTextField(20);
			this.add(priceTxt);

			JButton quitNew = new JButton("Cancel");
			quitNew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			this.add(quitNew);
			
			JButton resetInput = new JButton("Reset");
			resetInput.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					nameTxt.setText("");
					cateTxt.setText("");
					desTxt.setText("");
					typeTxt.setText("");
					sizeTxt.setText("");
					priceTxt.setText("");
				}
			});
			this.add(resetInput);
			
			JButton createFood = new JButton("OK");
			createFood.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						String newName = nameTxt.getText();
						String newCate = cateTxt.getText();
						String newDes = desTxt.getText();
						String newType = typeTxt.getText();;
						String newSize = sizeTxt.getText();
						double newPrice = Double.parseDouble(priceTxt.getText());
						newFood(newName, newCate, newDes, newType, newSize, newPrice);						
					}
					catch(Exception ex) {
						JOptionPane.showMessageDialog(null, "Error!");						
					}
				}
			});
			this.add(createFood);
			quitNew.setHorizontalAlignment(JButton.RIGHT);
			resetInput.setHorizontalAlignment(JButton.RIGHT);
			createFood.setHorizontalAlignment(JButton.RIGHT);			
		}
	}

}
