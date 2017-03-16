package model;

public class Instructor extends Person{
	//instructors add seats to classes
	private int seatsForStudents;
	
	public Instructor(int Uuid, String Name,  String Address, String PhoneNumber){
		super(Uuid, Name, Address, PhoneNumber );
		
		seatsForStudents=0;
	}
	public int getSeats(){
		return this.seatsForStudents;
	}
	public void setSeats(int Seats){
		this.seatsForStudents=Seats;
		System.out.println("new seat "+ Seats);
	}
	
}
