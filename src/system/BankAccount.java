package system;

public class BankAccount {

	private double saldo;
	
	public BankAccount(double saldo) {
		this.saldo = saldo;
	}
	
	public double getSaldo() {
		return saldo;
	}
	
	public void processPayment(double amout) {
		saldo = saldo - amout;
		System.out.println("Your new Bank Balance: "+ saldo);
	}
}
