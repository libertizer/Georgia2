package model;

public class Instructor extends Person{
	//instructors add seats to classes
    
        private CourseCatalog courseCatalog;
	
	public Instructor(int Uuid, String Name,  String Address, String PhoneNumber){
		super(Uuid, Name, Address, PhoneNumber );
		
	}
	/*public int getSeats(){
		return this.seatsForStudents;
	}
	public void setSeats(int Seats){
		this.seatsForStudents=Seats;
		System.out.println("new seat number:"+ Seats);
	}*/
	
}
