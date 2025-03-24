package system;

import java.util.ArrayList;

public class CheckPoint extends OVcard {
	
	public ArrayList<String> stationName = new ArrayList<String>();
	public ArrayList<Integer> stationId = new ArrayList<Integer>();
	public int id;
	public String name;
	
	public CheckPoint( String name) {
		this.name = name;
		
	}
	public void getID() {
		
	}
	
	public boolean checkCardBalance() {
		boolean cardBalanceB = false;
		// if (cardBalance > 4) {
			// cardBalanceB = true;
		// }
		// else {
			// System.out.println("Not enough balance!");
		// }
		cardBalanceB = true;
		return cardBalanceB;
	}
	
	public void checkIn() {
		boolean checkBalance = checkCardBalance();
		if (checkBalance == true) {
			changeStatus();
			
		}
	}
	
}
