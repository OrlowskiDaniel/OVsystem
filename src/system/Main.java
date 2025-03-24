package system;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		OVcard card = new OVcard();
		Scanner sc = new Scanner(System.in);
		
		card.makeNewCard();
		card.printCardInfo();
		
		boolean cardUsed = false;
		
		System.out.println("\n\nWhere do you want to check in: ");
		String stationName = sc.next();
		
		cardUsed = true;
		if (cardUsed == true) {
			CheckPoint i = new CheckPoint(stationName);
			card.checkIn(i.name);
		}
		
	}

}
