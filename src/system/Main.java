package system;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		
		OVcard card = new OVcard();
		BankAccount bankAccount = new BankAccount(2000);
		
		CheckPoint[] i = {
			new CheckPoint("Nijemgen", 0),
			new CheckPoint("Nijemgen Lent", 1.75),
			new CheckPoint("Nijemgen Heyendaal", 3),
			new CheckPoint("Nijemgen Dukenburg", 5.5),
			new CheckPoint("Nijemgen Goffert", 6.8),
			new CheckPoint("Wijchen", 10)
		};
		
		
		card.addBalance(bankAccount);
		card.printCardInfo();
		
		
		System.out.println("\n\nList of stations: ");
		for (CheckPoint s : i) {
            s.printNameAndId();
        }
		
		while(true) {
			System.out.println("\n\nWhere do you want to use card(Use Station Number): ");
			int test = 0;
			test+=1;
			try {
				int stationID = sc.nextInt();
				card.useCard(i[stationID], card, bankAccount);
				
		
				}
			catch (ArrayIndexOutOfBoundsException | java.util.InputMismatchException e) {
				System.err.println("Choose only existing station");
				sc.nextLine();
				
			}
			if (test == 30) {
				break;
			}
			
		}
		
		
		
		sc.close();
		
	}

}
