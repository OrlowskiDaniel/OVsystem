package system;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;



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
	private String stationNameStart;
	private String stationNameEnd;
	
	
	//Card balance
	private double cardBalance = 0;
	
	//Card Status
	private Status cardStatus = Status.CHECKOUT;
	
	// Using timer and timer task to update failed to check out variable
    private Timer timer = new Timer();
    private TimerTask autoCheckout;
    private int failedToCheckOut = 7;
    private double standartPrice = 2.5;
    private double penalyPrice = 10;
    private boolean penaltyToPay = false;
    
	
	private final AtomicInteger nextId = new AtomicInteger();
	private Scanner sc;
	
	//Constructor
	public OVcard(Scanner sc) {
		this.sc = sc;
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
			cancelAutoCheckOut();
			break;
		case CHECKOUT:
			cardStatus = Status.CHECKIN;
			System.out.println("You are Checked In");
			scheduleAutoCheckOut(bank);
			break;
		}
		
		
	}
	public Status getStatus() {
		return cardStatus;
	}
	private void clearRideInfo() {
		stationNameStart = null;
		stationNameEnd= null;
		startPoint = 0;
		endPoint = 0;
	}
	
	// if the user is in CHECKIN state for to long system will change it back to CHECKOUT and add +1 to faildToCheckOut variable
    private void scheduleAutoCheckOut(BankAccount bank) {
        cancelAutoCheckOut(); // clear previous timer

        autoCheckout = new TimerTask() {
        	@Override
            public void run() {
                cardStatus = Status.CHECKOUT;
                failedToCheckOut++;
                System.out.println("Automatically Checked Out. Your Faild attempt to check out: "+ failedToCheckOut);
                updateCardBalance(standartPrice);
                if (failedToCheckOut >= 8) {
                	penaltyToPay = true;
                }
            }
        };

        timer.schedule(autoCheckout, 30 * 1000); // 30 seconds
    }   
    
    // penalty you have to pay if you forget to check out 8 times
    public boolean isPenaltyToPay() {
        return penaltyToPay;
    }
    public void payPenalty(BankAccount bank) {
    	System.out.println("You forget to check out to many times: "+failedToCheckOut);
    	System.out.println("Your penalty: "+ penalyPrice);
    	failedToCheckOut = 0;
    	while (true) {
            if (cardBalance > penalyPrice) {
                updateCardBalance(penalyPrice);
                penaltyToPay = false;
                break;
            } else {
                System.err.println("Not enough balance on the card!!");
                addBalance(bank);
            }
        }
    	}
    
    // cancel timer if you check out
    private void cancelAutoCheckOut() {
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
	public void setStartPoint(double km, String sName) {
		this.startPoint = km;
		this.stationNameStart = sName;
		
	}
	public void setEndPoint(double km, String sName) {
		this.endPoint = km;
		this.stationNameEnd = sName;
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
			// System.out.println(stationNameStart+"\n"+stationNameEnd);
			clearRideInfo();
		}
		else {
			System.err.println("Error: Can't pay");
		}
		
	}
		
	public void useCard(CheckPoint s, BankAccount bankAcc) {
		
			if (cardStatus == Status.CHECKOUT) {
				// valid check in
				s.checkIn(this, bankAcc);
			}
			else if (cardStatus == Status.CHECKIN) {
				// already checked in
				if (stationNameStart.equals(s.getStationName())) {
					// check in again at the same station — not allowed
					s.checkIn(this, bankAcc);
					return;
				}
				else {
					// Valid check-out attempt
					s.checkOut(this, bankAcc);
				}
			}
			
		}
	
	
	

	
}


