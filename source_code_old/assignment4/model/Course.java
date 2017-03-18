package model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap; 
import java.util.Map;

public class Course {
	private int id;
	private String shortName;
	private List<String> allTerms;
	private List<Integer> prerequisites;
	private String term;
	private int availableSeats;
	private int totalSeats;

    private HashMap<Integer, Integer> placesInstructor;
	//private int year;
	private ArrayList<Student> studentsEnrolled=new ArrayList <Student>();
	
	public Course(int Id, String ShortName){
            this.id=Id;
            this.shortName=ShortName;
            this.allTerms=new ArrayList<String>();
            this.availableSeats=0;
            this.totalSeats=0;
            this.placesInstructor=new HashMap<Integer, Integer>();
            this.prerequisites=new ArrayList<Integer>();
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
	public List <Student> getStudentsEnrolled(){
		return this.studentsEnrolled;
	}
	public void enrollStudent(Student s)
        {
            this.studentsEnrolled.add(s);
            //adjust seats avialable
            this.availableSeats=availableSeats-1;
            System.out.println("> enrolled");
	}
	
	public String getTerm(){
		return this.term;
	}
	public void setTerm(String t){
		 this.term=t;
	}
	/*public int getYear(){
		return this.year;
	}
	public void setYear(int y){
		 this.year=y;
	}*/
	
	public List<String> getAllTermsTaught(){
		return this.allTerms;
	}
	public void setAllTermsTaught(List<String>result){
		this.allTerms=result;
	}
	public int getTotalSeats(){
            int places=0;
            for(Map.Entry<Integer, Integer> entry : placesInstructor.entrySet()) 
            {
            		places+= entry.getKey();
            }
            return places;
	}
        
	public HashMap<Integer, Integer> getPlacesInstructor(){
		return this.placesInstructor;
	}
	public int getAvailableSeats(){
		return this.availableSeats;
	}
	public void setAvailableSeats(int AvailableSeats){
		this.availableSeats=AvailableSeats;
	}
	
	public void setPrerequisites(int prereqId){
		this.prerequisites.add(prereqId);
	
	}
	public List<Integer> getPrerequisites(){
		
		return this.prerequisites;
	}
	
	public void allocateSeats(int instuctorId, int courseId, int capacity){
		
	    Integer placesAlreadyAssigned=placesInstructor.get(instuctorId);
	    if(placesAlreadyAssigned==null)
	    {
	        placesInstructor.put(instuctorId, capacity);
	        this.availableSeats+=capacity;
	        this.totalSeats+=capacity;
	        System.out.println("> total,"+totalSeats+",avialable,"+availableSeats);
	    }
	    else 
	    { 
	        int balance=capacity-placesAlreadyAssigned;
	        //adding places, no problem
	        if(balance>=0)
	        {
	             placesInstructor.put(instuctorId, capacity);
	             this.availableSeats+=balance;
	             this.totalSeats+=balance;
	             System.out.println("> total,"+totalSeats+",avialable,"+availableSeats);
	        }
	        else
	        {	//no substraction possible
	        	if(availableSeats<=0)
	        	{
	        		System.out.println("> warning - total,"+totalSeats+",avialable,"+availableSeats);
	        	}
	        	else{
	                int balanceAviabable=availableSeats+balance;
	                //more avialable places than places subtracted
	                    if(this.availableSeats+balance>=0){
	                    	this.availableSeats+=balance;
	                    	this.totalSeats+=balance;
	                    	placesInstructor.put(instuctorId, capacity);
	                    	System.out.println("> total,"+totalSeats+",avialable,"+availableSeats);
	                    	}
	                  //less avialable places than places subtracted, we subtract only until avialable seats=0
	                    else
	                    {	 
							capacity=placesAlreadyAssigned-this.availableSeats;
							placesInstructor.put(instuctorId, capacity);
							this.availableSeats=0;
	                    	this.totalSeats+=capacity- placesAlreadyAssigned;
							System.out.println("> warning - total,"+totalSeats+",avialable,"+availableSeats);
	                    }
		        }
	    	}
	    }
	}
}
     