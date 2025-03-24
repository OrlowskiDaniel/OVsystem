package system;

import java.util.ArrayList;

public class CheckPoint {
	
	public ArrayList<String> stationName = new ArrayList<String>();
	public ArrayList<Integer> stationId = new ArrayList<Integer>();
	public int id;
	public String name;
	
	public CheckPoint() {
		stationName.add("Nijemgen");
		stationName.add("Nijemgen Lent");
		stationName.add("Nijemgen Heyendaal");
		stationName.add("Nijemgen Dukenburg");
		stationName.add("Nijemgen Goffert");
		stationName.add("Wijchen");
		
	}
	public void getStationName(String name) {
		this.name = name;
	}
	
	public void printStationList() {
		System.out.println("\n\nStation List: ");
		for (int i = 0; i < stationName.size(); i++) {
			System.out.println(stationName.get(i));
		}
	}
	public void getID() {
		
	}
	


	
}
