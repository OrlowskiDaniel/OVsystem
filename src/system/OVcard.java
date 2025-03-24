package system;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class OVcard {
	
	//Card info 
	private int cardID;
	private String name;
	Date dateStart;
	Date dateEnd;
	boolean bankCn = false;
	
	//Ride info
	String startPoint;
	String endPoint;
	
	//Card balance
	double cardBalance = 0;
	
	//Card Status
	boolean cardBalanceB = false;
	int statusNow = 1;
	int faildToCheckOut = 0;
	
	public Scanner sc = new Scanner(System.in);
	public Date date = new Date();
	static AtomicInteger nextId = new AtomicInteger();
	
	//Constructor
	public OVcard() {
		
	}
	
	
	// changeStatus method changes between CHECKIN and CHECKOUT by Integer of the state
	// if the user is in CHECKOUT state for to long system will change it back to CHECKIN and add +1 to faildToCheckOut variable
	enum Status {
		CHECKIN, // 0
		CHECKOUT // 1
	}
	public void changeStatus() {
		Status cardStatus = Status.values()[statusNow];
		
		switch (cardStatus) {
		case CHECKIN:
			statusNow = 1;
			System.out.println("You are Checked Out");
			break;
		case CHECKOUT:
			statusNow = 0;
			System.out.println("You are Checked In");
			break;
		}
	}
	public boolean checkCardBalance() {
		cardBalanceB = false;
		if (cardBalance > 5) {
			cardBalanceB = true;
		}
		else {
			System.out.println("Not enough balance!");
		}
		
		return cardBalanceB;
	}
	
	
	
	public void makeNewCard() {
		System.out.println("Make your OV-card.");
		
		cardID = nextId.incrementAndGet();
		// name card
		getName();
		
		dateStart = date;
		getEndDate();
		
		bankConnection();
		
		addBalance();
	}
	
	
	public String getName() {
		System.out.println("Enter your name: ");
		String userInput = sc.next();
		
		return name = userInput;
	}
	
	
	public Date getEndDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 5);

        Date expirationDate = cal.getTime();
        
        dateEnd = expirationDate;
		return dateEnd;
	}
	
	
	public boolean bankConnection() {
		System.out.println("Do you wish to connect your bank to the card(Yes or No): ");
		String userInput = sc.next();
		if (userInput.equalsIgnoreCase("yes")) {
			bankCn = true;
		}
		
		return bankCn;
	}
	
	
	// Ride Information 
	public void getStartPoint(String userInput) {
		startPoint = userInput;
	}
	public void getEndPoint(String userInput) {
		endPoint = userInput;
	}
	
	// Card Balance
	public double addBalance() {
		System.out.println("Your current balance (least 4 euro to Check In): "+ cardBalance);
		System.out.println("How much balance you wish to add: ");
		double depositAmout = sc.nextDouble();
		cardBalance = depositAmout + cardBalance;
		return cardBalance;
	}
	
	public void printCardInfo() {
		System.out.println("\nYour card Information: ");
		System.out.print("ID:  "+cardID+"\nYour Name:  "+ name+"\nCreation date:  "+ dateStart+"\nExpiration date:  "+ dateEnd+"\nBank Connection:  "+ bankCn+"\nCard Balance:  "+ cardBalance);
	}
	
	public void checkIn(String stationName) {
		checkCardBalance();
		if (cardBalanceB == true) {
			changeStatus();
			getStartPoint(stationName);
			System.out.println("Checked in on station: "+ startPoint);
		}
		
		
	}
}


