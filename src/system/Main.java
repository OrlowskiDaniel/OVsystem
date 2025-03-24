package system;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		OVcard card = new OVcard();
		
		
		
		CheckPoint i = new CheckPoint();
		
		card.makeNewCard();
		card.printCardInfo();
		
		boolean cardUsed = false;
		
		i.printStationList();
		System.out.println("\n\nWhere do you want to check in: ");
		String stationName = sc.next();
		
		cardUsed = true;
		if (cardUsed == true) {
			i.getStationName(stationName);
			card.checkIn(i.name);
		}
		
		
		
		
		sc.close();
		
	}

}
