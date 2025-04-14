package system;

import java.util.concurrent.atomic.AtomicInteger;

import system.OVcard.Status;

public class CheckPoint {
	
	private int id;
	private String name;
	private double kmMark;
	private double pricePerKm = 0.20;
	private double standartTicketPrice = 2.50;
	static AtomicInteger nextId = new AtomicInteger();
	
	// check point
	// kmMarker is used to calculate distance of the ride
	// you can check in and check out here
	
	
	
	public CheckPoint(String name, double kmMark) {
		this.name = name;
		this.kmMark = kmMark;
		id = nextId.incrementAndGet() - 1;
		
	}
	
	public String getStationName() {
		return name;
	}

	public int getID() {
		return id;
	}
	
	public double getKmMarker() {
		return kmMark;
	}
	
	public void printNameAndId() {
		System.out.println("   "+id+" - "+name);
	}
	
	public void checkIn(OVcard card, BankAccount bank) {
		if (card.checkCardMininumBalance() == true && card.checkDateValid() == true) {
			if (card.getStatus() == Status.CHECKIN) {
				System.out.println("You are already Checked In");
			}
			else {
				card.changeStatus(bank);
				card.setStartPoint(kmMark, name);
				System.out.println("Checked In on station: "+ name);
			}
		}
		else {
			System.err.println("error");
		}
	}
	public void checkOut(OVcard card, BankAccount bank) {
		card.changeStatus(bank);
		card.setEndPoint(kmMark, name);
		System.out.println("Checked Out on station: "+ name);
		
		card.transaction(bank, standartTicketPrice, pricePerKm);
		
		
	}
	


	
}
