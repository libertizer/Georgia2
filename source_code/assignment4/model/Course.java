package model;

import java.util.ArrayList;
import java.util.List;

public class Course {
	private int id;
	private String shortName;
	private List<String> terms;
	private int availableSeats;
	private int totalSeats;
	private Instructor instructor;
	private ArrayList<Instructor> instructorSeats=new ArrayList <Instructor>();
	
	public Course(int Id, String ShortName){
		this.id=Id;
		this.shortName=ShortName;
		this.terms=new ArrayList<String>();
		this.availableSeats=0;
		this.totalSeats=0;
	}
	public int getId(){
		return this.id;
	} 
	public void setId(int ID){
		this.id=ID;
	}
	public String getShortName(){
		return this.shortName;
	}
	public void setShortName(String ShortName){
		this.shortName=ShortName;
	}
	
	public List<String> getTerms(){
		return this.terms;
	}
	public void setTerm(String Term){
		this.terms.add(Term);
	}
	public int getTotalSeats(){
		return this.totalSeats;
	}
	public void setTotalSeats(int TotalSeats){
		this.totalSeats=TotalSeats;
	}
	public int getAvailableSeats(){
		return this.availableSeats;
	}
	public void setAvailableSeats(int AvailableSeats){
		this.availableSeats=AvailableSeats;
	}
public void allocateSeats(Instructor Instructor, int Capacity){
		
		String output="";
		if(instructorSeats.size()>0){
			for(Instructor inst:instructorSeats){
				System.out.println("getSeats " +inst.getSeats());
				if(inst.getUuid()==Instructor.getUuid())
				{
					System.out.println(" get new Seats Instructor "+Instructor.getSeats()+"  inst "+ inst.getSeats());
					int balance= Capacity- inst.getSeats();
					//seats added so new seatnumber can be taken without checks!
					System.out.println("balance "+balance);
					if(balance>=0){
						inst.setSeats(Capacity);
						totalSeats+=balance;
						availableSeats+=balance;
						output="> total,"+getTotalSeats()+",available,"+getAvailableSeats();
					}
					//remove seats operation
					else{
						int toSubstract=availableSeats+balance;
						System.out.println("toSubstract "+toSubstract);
						//more places availabale than places to remove
						if(toSubstract>=0){
							inst.setSeats(Capacity);
							setTotalSeats(getTotalSeats()+balance);
							setAvailableSeats(getAvailableSeats()+balance);
							output="> total,"+getTotalSeats()+",available,"+getAvailableSeats();
						}
						//only remaining places can be deduced
						else{
							inst.setSeats(inst.getSeats()-availableSeats);
							totalSeats-=availableSeats;
							availableSeats=0;
							output="> warning, - total "+getTotalSeats()+",available,"+getAvailableSeats();
						}
					}
				}
			}
		}
		//first Input
		else{
			System.out.println("first input ");
			Instructor.setSeats(Capacity);
			instructorSeats.add(Instructor);
			setTotalSeats(Capacity);
			setAvailableSeats(Capacity);
			
			output="> total,"+getTotalSeats()+",available,"+getAvailableSeats();
		}
		System.out.println(output);
		
		
	}
	
}
