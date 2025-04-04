package system;

import java.util.concurrent.atomic.AtomicInteger;

import system.OVcard.Status;

public class CheckPoint {
	
	private int id;
	private String name;
	private double kmMark;
	static AtomicInteger nextId = new AtomicInteger();
	
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
	
	public void checkIn(OVcard card) {
		if (card.checkCardBalance() == true && card.checkDateValid() == true) {
			if (card.getStatus() == Status.CHECKIN) {
				System.out.println("You are already Checked In");
			}
			else {
				card.changeStatus();
				card.getStartPointkmMark(kmMark);
				System.out.println("Checked In on station: "+ name);
			}
		}
	}
	public void checkOut(OVcard card, BankAccount bank) {
		card.changeStatus();
		System.out.println("Checked Out on station: "+ name);
		
		card.transaction(bank);
		
		
	}
	


	
}
