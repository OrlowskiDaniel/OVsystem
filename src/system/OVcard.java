package system;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import system.OVcard.Status;

public class OVcard {
	
	// card states 
	enum Status {
		CHECKIN, 
		CHECKOUT 
		
	}
	//Card info 
	private int cardID;
	private String name;
	private Date dateStart;
	private Date dateEnd;
	private boolean cardValid = true;
	
	//Ride info
	private double startPoint;
	private double endPoint;
	
	//Card balance
	private double cardBalance = 0;
	
	//Card Status
	private Status cardStatus = Status.CHECKOUT;
	
	// Using timer and timer task to update failed to check out variable
    private Timer timer = new Timer();
    private TimerTask autoCheckout;
    private int failedToCheckOut = 8;
    private double standartPrice = 2.5;
    private double penalyPrice = 10;
    
	
	private final Scanner sc = new Scanner(System.in);
	private final AtomicInteger nextId = new AtomicInteger();
	
	
	//Constructor
	public OVcard() {
		makeNewCard();
	}
	

	// Making new card methods
	public void makeNewCard() {
		System.out.println("Make your OV-card.");
		
		// card id
		this.cardID = nextId.incrementAndGet();
		// name card
		this.name = setName();
		// card date 
		this.dateStart = new Date();
		this.dateEnd = getEndDate();
		
	}
	
	// card name methods
	public String getName() {
		return name;
	}
	public String setName() {
		System.out.println("Enter your name: ");
		return sc.next();
	}
	
	
	// add 5 years to calendar then return it as time 
	public Date getEndDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 5);
		
		return cal.getTime();
	}
	
	// checking if card date is valid 
	public boolean checkDateValid() {
		// if current date is after end date
		if (new Date().after(dateEnd)) {
			cardValid = false;
			System.out.println("Card is Expired");
		}
		
		return cardValid;
	}
	public void printCardInfo() {
		System.out.println("\nYour card Information: ");
		System.out.print("ID:  "+cardID+"\nYour Name:  "+ name+"\nCreation date:  "+ dateStart+"\nExpiration date:  "+ dateEnd+"\nCard Balance:  "+ cardBalance);
	}
	
	
	// changeStatus method changes between CHECKIN and CHECKOUT states
	public void changeStatus(BankAccount bank) {
		
		switch (cardStatus) {
		case CHECKIN:
			cardStatus = Status.CHECKOUT;
			System.out.println("You are Checked Out");
			cancelAutoCheckout();
			break;
		case CHECKOUT:
			cardStatus = Status.CHECKIN;
			System.out.println("You are Checked In");
			scheduleAutoCheckout(bank);
			break;
		}
		
		
	}
	public Status getStatus() {
		return cardStatus;
	}
	
	// if the user is in CHECKIN state for to long system will change it back to CHECKOUT and add +1 to faildToCheckOut variable
	//
    private void scheduleAutoCheckout(BankAccount bank) {
        cancelAutoCheckout(); // clear previous timer

        autoCheckout = new TimerTask() {
        	@Override
            public void run() {
                cardStatus = Status.CHECKOUT;
                failedToCheckOut++;
                System.out.println("Automatically Checked Out. Your Faild attempt to check out: "+ failedToCheckOut);
                updateCardBalance(standartPrice);
                if (failedToCheckOut >= 8) {
                	payPenalty(bank);
                }
            }
        };

        timer.schedule(autoCheckout, 30 * 1000); // 30 seconds
    }   
    public void payPenalty(BankAccount bank) {
    	System.out.println("You forget to check out to many times: "+failedToCheckOut);
    	System.out.println("Your penalty: "+ penalyPrice);
    	failedToCheckOut = 0;
    	while(true) {
    		if (cardBalance > penalyPrice) {
    			updateCardBalance(penalyPrice);
    			break;
    		}
    		else {
    			System.err.println("Not enough balance on the card!!");
    			addBalance(bank);
    		}
    	}	
    	}
    
    
    private void cancelAutoCheckout() {
        if (autoCheckout != null) {
            autoCheckout.cancel();
        }
    }

    public int getFailedToCheckOut() {
        return failedToCheckOut;
    }

    
	// Card Balance management
	public boolean checkCardMininumBalance() {
		if (cardBalance > 5) return true;
		System.out.println("Not enough balance on card!");
		return false;
	}
	
	public void addBalance(BankAccount bank) {
		System.out.println("Your current balance (least 4 euro to Check In): "+ cardBalance);
		
		System.out.println("How much balance you wish to add: ");
		double depositAmout = sc.nextDouble();
		if (bank.getSaldo() > depositAmout) {
			bank.processPayment(depositAmout);
			cardBalance = depositAmout + cardBalance;
		}
		else {
			System.err.println("Not enough money in the bank account");
		}
	}
	
	public void updateCardBalance(double price) {
		System.out.println("Price you have to pay: €"+ price);
		cardBalance = cardBalance - price;
		System.out.println("Your new balance: "+ cardBalance);
		
	}
    
	
	
	// Ride Information and fare calculation
	public void setStartPointkmMark(double km) {
		this.startPoint = km;
	}
	public void setEndPoint(double km) {
		this.endPoint = km;
	}
	public double calcDistance() {
		double distance = Math.abs(startPoint - endPoint);
		return distance;
	}
	public double priceCalc(double standartTicketPrice, double pricePerKm) {
		double distance = calcDistance();
		double price = standartTicketPrice + (distance * pricePerKm);
		System.out.println("Distance of the ride: "+ distance+" km");
		return price;
	}
	public void transaction(BankAccount bank, double standartTicketPrice, double pricePerKm) {
		
		double price = priceCalc(standartTicketPrice, pricePerKm);
		if(price < cardBalance) {
			updateCardBalance(price);
		}
		else {
			System.err.println("Error: Can't pay");
		}
		
	}
	
	
	// checking if card is used the correct way
	public boolean checkOutUseCorrect(CheckPoint s, OVcard card, BankAccount bankAcc) {
		if (cardStatus == Status.CHECKIN) {
			if (endPoint == startPoint) {
				s.checkIn(card, bankAcc);
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
	
	public void useCard(CheckPoint s, OVcard card, BankAccount bankAcc) {
		if (checkOutUseCorrect(s, card, bankAcc) == true) {
			if (cardStatus == Status.CHECKOUT) {
				s.checkIn(card, bankAcc);
			}
			else if (cardStatus == Status.CHECKIN) {
				s.checkOut(card, bankAcc);
			}
			
		}
	}
	
	

	
}


