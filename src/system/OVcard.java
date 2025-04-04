package system;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import system.OVcard.Status;

public class OVcard {
	
	//Card info 
	private int cardID;
	private String name;
	private Date dateStart;
	private Date dateEnd;
	private boolean bankConn = false;
	private boolean cardValid = true;
	
	//Ride info
	private double startPoint;
	private double endPoint;
	
	//Card balance
	private double cardBalance = 0;
	
	//Card Status
	private Status cardStatus = Status.CHECKOUT;
	private int faildToCheckOut = 0;
	private boolean cardMinimBalance = false;
	
	private Scanner sc = new Scanner(System.in);
	private Date date = new Date();
	static AtomicInteger nextId = new AtomicInteger();
	
	//Constructor
	public OVcard() {
		
	}
	
	
	// changeStatus method changes between CHECKIN and CHECKOUT states
	// if the user is in CHECKOUT state for to long system will change it back to CHECKIN and add +1 to faildToCheckOut variable
	enum Status {
		CHECKIN, 
		CHECKOUT 
		
		
	}
	public void changeStatus() {
		
		switch (cardStatus) {
		case CHECKIN:
			cardStatus = Status.CHECKOUT;
			System.out.println("You are Checked Out");
			break;
		case CHECKOUT:
			cardStatus = Status.CHECKIN;
			System.out.println("You are Checked In");
			break;
		}
	}
	public Status getStatus() {
		return cardStatus;
	}
	
	public boolean checkCardBalance() {
		cardMinimBalance = false;
		if (cardBalance > 5) {
			cardMinimBalance = true;
		}
		else {
			System.out.println("Not enough balance!");
		}
		
		return cardMinimBalance;
	}
	
	
	
	public void makeNewCard(BankAccount bank) {
		System.out.println("Make your OV-card.");
		
		cardID = nextId.incrementAndGet();
		// name card
		setName();
		
		dateStart = date;
		getEndDate();
		
		bankConnection();
		
		addBalance(bank);
	}
	
	public String getName() {
		return name;
	}
	public String setName() {
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
	public boolean checkDateValid() {
		Calendar cal = Calendar.getInstance();
		Date currentDate = cal.getTime();
		
		if (currentDate.after(dateEnd)) {
			cardValid = false;
			System.out.println("Card is Invalid");
		}
		
		return cardValid;
	}
	
	
	public boolean bankConnection() {
		System.out.println("Do you wish to connect your bank to the card(Yes or No): ");
		String userInput = sc.next();
		if (userInput.equalsIgnoreCase("yes")) {
			bankConn = true;
		}

		return bankConn;
	}
	
	
	// Ride Information 
	public void getStartPointkmMark(double s) {
		startPoint = s;
	}
	public void getEndPoint(double s) {
		endPoint = s;
	}
	
	// Card Balance
	public void addBalance(BankAccount bank) {
		System.out.println("Your current balance (least 4 euro to Check In): "+ cardBalance);
		
		System.out.println("How much balance you wish to add: ");
		double depositAmout = sc.nextDouble();
		if (bank.getSaldo() > depositAmout) {
			// add balance from bank account
		}
		else {
			System.out.println("Not enough money in the bank account");
		}
		cardBalance = depositAmout + cardBalance;
	}
	
	public void printCardInfo() {
		System.out.println("\nYour card Information: ");
		System.out.print("ID:  "+cardID+"\nYour Name:  "+ name+"\nCreation date:  "+ dateStart+"\nExpiration date:  "+ dateEnd+"\nBank Connection:  "+ bankConn+"\nCard Balance:  "+ cardBalance);
	}
	
	
	
	
	public boolean checkOutUseCorrect(CheckPoint s, OVcard card) {
		if (cardStatus == Status.CHECKIN) {
			getEndPoint(s.getKmMarker());
			if (endPoint == startPoint) {
				s.checkIn(card);
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return true;
		}
	}

	public void cardUsed(CheckPoint s, OVcard card, BankAccount bankAcc) {
		if (checkOutUseCorrect(s, card) == true) {
			if (cardStatus == Status.CHECKOUT) {
				s.checkIn(card);
			}
			else if (cardStatus == Status.CHECKIN) {
				s.checkOut(card, bankAcc);
			}
			
		}
	}
	
	
	
	public void transaction(BankAccount bank) {
		calcDistance();
		double price = priceCalc();
		if (price > cardBalance || bankConn == true) {
			takeFromBank(bank, price);
		}
		else {
			updateBalance(price);
		}
		
	}
	
	public double calcDistance() {
		double distance = Math.abs(startPoint - endPoint);
		return distance;
	}
	public double priceCalc() {
		double distance = calcDistance();
		double price = 2.5 + (distance * 0.20);
		System.out.println("Distance of the ride: "+ distance+" km");
		return price;
	}
	public void updateBalance(double price) {
		System.out.println("Price you have to pay: €"+ price);
		cardBalance = cardBalance - price;
		System.out.println("Your new balance: "+ cardBalance);
		
	}
	public void takeFromBank(BankAccount bank, double price) {
		bank.processPayment(price);
	}
	
	
}


