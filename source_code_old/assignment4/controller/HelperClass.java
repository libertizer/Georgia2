package controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelperClass {
	
	//declare Lists of objects
	private ArrayList<Student> students=null;
	private ArrayList<Instructor> instructors=null;
	private ArrayList<Course> courses=null;
	private ArrayList<Course> courseWithTerm=null;
	private ArrayList<Term> terms=null;
	private ArrayList<Prerequisite> prereq=null;
	
	//declare csv file types
	private String studentType="s";
	private String instructorType="i";
	private String courseType="c";
	private String termType="t";
	private String prereqType="pr";
	private String actionsType="ac";
	
	  //declare file Names
  	private String studentFile="students.csv";
  	private String instructorFile="instructors.csv";
  	private String courseFile="courses.csv";
  	private String termFile="terms.csv";
 	private String prereqFile="prereqs.csv";
  	private String actionsFile="actions.csv";
	
	//declare classes
	private CourseCatalog courseCatalog;
	private Term te;
	private Student st;
	private Instructor in;
	private Course co;
	private Prerequisite pr;
	
	//declare actions
	private String startSim="start_sim";
	private String stopSim="stop_sim";
	private String nextTerm="next_term";
	private String seatAllocation="allocate_seats";
	private String courseRequest="request_course";
	private String gradeAllocation="assign_grade";
	private String instructorReport="instructor_report";
	private String studentReport="student_report";
	
	//to read CSV file
    private BufferedReader buffer=null;
    private String line="";
    private String cvsSplitBy= ",";
    
    //current year and semester
    private static int currentTermIndex=0;
    private static int currentYear;
    
    //other
    String errorMessage="unknown";
    Boolean end=false;
    
    
	public HelperClass(){
        synchronized(this)
        {
			instructors=new ArrayList<Instructor>();
			students=new ArrayList<Student>();
			courses=new ArrayList<Course>();
			courseWithTerm=new ArrayList<Course>();
			terms=new ArrayList<Term>();
			courseCatalog = CourseCatalog.getCourseCatalog();
			prereq=new ArrayList<Prerequisite>();
			//read csv Files
			getCsvData(studentFile, studentType);
			getCsvData(instructorFile, instructorType);
			getCsvData(courseFile, courseType);
			getCsvData(termFile, termType);
			
			addTermsToCourses();
			getCsvData(prereqFile, prereqType);
			
	        addPrerequisites();
			getCsvData(actionsFile, actionsType);
			//from previous assignment
			//printResult();
        }
	}
	public String[] getCsvData(String csvFile, String typeFile) {
		synchronized(this){
			String []result=null;
			String errorMessage="unknown";
			try{
				buffer = new BufferedReader(new FileReader(csvFile));
				while ((line = buffer.readLine()) != null) {
					
				result = line.split(cvsSplitBy);
				
				//this is a dispatcher for the different CSV files to read
				try{
					if(typeFile.equals(studentType)){
						errorMessage="student";
						try{
						st=new Student(Integer.parseInt(result[0]), result[1], result[2], result[3]);}
						//in case of anonymized student data
						catch(Exception e){st=new Student(Integer.parseInt(result[0]), "null", "null", "null");}
						Integer id=st.getUuid();
						//student exists if he has an ID!
						if(id!=null){
							students.add(st);
						}
					}
					else if(typeFile.equals(instructorType)){
						errorMessage="instructor";
						try{
						in=new Instructor(Integer.parseInt(result[0]), result[1], result[2], result[3]);}
						//in case of anonymized instructor data
						catch(Exception e){in=new Instructor(Integer.parseInt(result[0]), "null", "null", "null");}
						Integer id=in.getUuid();
						//check student if exists
						if(id!=null){
							instructors.add(in);
						}
					}
					else if(typeFile.equals(courseType)){
						errorMessage="course";
						co=new Course(Integer.parseInt(result[0]), result[1]);
						Integer id=co.getId();
						//check student if exists
						if(id!=null){
							courses.add(co);
						}
					}
					else if(typeFile.equals(termType)){
						errorMessage="term";
						te=new Term(Integer.parseInt(result[0]), TermEnum.valueOf(result[1]));
						Integer id=te.getCourseId();
						//check student if exists
						if(id!=null){
							terms.add(te);
						}
					}
					else if(typeFile.equals(prereqType)){
						errorMessage="prerequisites";
						pr=new Prerequisite(Integer.parseInt(result[0]),Integer.parseInt(result[1]));
						Integer prId=pr.getPrereqId();
						Integer coId=pr.getCourseId();
						//check prerequisite if exists
						if(coId!=null&&prId!=null){
							prereq.add(pr);
						}
					}
					else if(typeFile.equals(actionsType)){
						errorMessage="actions";
						//check stop_sim
						if(result[0].equals(stopSim)){
							System.out.println("> end,"+TermEnum.values()[currentTermIndex]+","+currentYear);
							end=true;
						}
						//check actions if exists and not stop_sim
						else if(result[0]!=null&&end==false){
							doAction(result);
						}
					}
				}
				catch (Exception e)
					{
					
					}
				}
			
			}
			catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			}
			return result;
			}
	}
	//Dispatcher for commands from the actions.csv file
	public void doAction(String [] action){
		synchronized(this){
			try{
				if(action[0].equals(startSim)){
					errorMessage="start_sim";
					if(action[1]!=null){
		                currentYear=Integer.parseInt(action[1]);
		                System.out.println("> begin,"+TermEnum.values()[currentTermIndex]+","+currentYear);
					}
				}
				else if(action[0].equals(nextTerm)){
					errorMessage="next_term";
					//new year  and semester cycle
					if(currentTermIndex==2)
					{
		                currentYear+=1;
		                currentTermIndex=0;
		                System.out.println("> begin,"+TermEnum.values()[currentTermIndex]+","+currentYear);
					}
					//just go to next semester, year stays the same
					else
					{
	                    currentTermIndex+=1;
	                    System.out.println("> begin,"+TermEnum.values()[currentTermIndex]+","+currentYear);
					}
				}
				else if(action[0].equals(seatAllocation)){
					boolean courseExists=false;
					int InstructorID=Integer.parseInt(action[1]);
					int capacity=Integer.parseInt(action[3]);
					List<Course> getAllCoursesWithTerm=courseCatalog.getAllCoursesWithTerms();
					for (Course c: getAllCoursesWithTerm){
						String t=TermEnum.values()[currentTermIndex].name();
	                    if (c.getId()==Integer.parseInt(action[2])&& c.getTerm().equals(t)){
	                        courseExists=true;
	                        Instructor inst=instructors.stream().filter(i ->i.getUuid()==InstructorID) .findFirst().orElse(null);
	                        c.allocateSeats(inst.getUuid(), c.getId(), capacity);
	                    }
					}
					//course does not exist
					if(!courseExists)
					{
						System.err.println("Error: "+ "- course ID does not exist");
					}
					
				}
				else if(action[0].equals(courseRequest)){
					
					requestCourse(Integer.parseInt(action[1]),Integer.parseInt(action[2]));
				}
				else if(action[0].equals(gradeAllocation)){
					assignGrade(Integer.parseInt(action[1]),Integer.parseInt(action[2]), GradeEnum.valueOf(action[3]));
				}
				else if(action[0].equals(instructorReport)){
					instructorReport(Integer.parseInt(action[1]));
				}
				else if(action[0].equals(studentReport)){
					studentReport(Integer.parseInt(action[1]));
					
				}
			
			}
			catch (Exception e)
			{
				System.err.println(e);
			}
		}
	}
	public void addPrerequisites()
	{
		courseCatalog.setAllPrerequisites(prereq);
	}
	public void addTermsToCourses ()
	{
		//sort terms: Fall, Spring, Summer
		Collections.sort(terms, (o1, o2) -> o1.getTerm() .compareTo( o2.getTerm()));
		synchronized(this)
		{        
			for(Course c: courses )
			{
				List<String>get=new ArrayList<String>();
				
				for(Term t: terms){
					//addterms during which course was taught to arraylist
					if(c.getId()==t.getCourseId()){
						get.add(t.getTerm().toString());
						Course nc=new Course(c.getId(),c.getShortName());
						nc.setTerm(t.getTerm().toString());
						//nc.setYear(currentYear);
						//add courses with terms to coursecatalog
						courseCatalog.addCourseSingleTerm(nc);
						
	                    }
					}                 
				
				/*course list only for printing out multiple semesters*/
				Course newC=c;
				newC.setAllTermsTaught(get);
				courseCatalog.addCourse(newC);
			}
		} 
	}
	//output to console
	public void printResult(){
		printNumStudents();
		printNumInstructors();
		printNumInstrStudents();
		printNumCourses();
		courseCatalog.printAllEntries();
	}
	
	public void printNumInstructors(){
		System.out.println(instructors.size());
	}
	public void printNumStudents(){
		System.out.println(students.size());
	}
	public void printNumCourses(){
		System.out.println(courses.size());
	}
	//get Students also being Instructors
	public void printNumInstrStudents(){
		HashSet<Integer> allPersons=new HashSet<Integer>();
		for(Student s: students){
			allPersons.add(s.getUuid());
		}
		for(Instructor i: instructors){
			allPersons.add(i.getUuid());
		}
		System.out.println((instructors.size()+students.size())-allPersons.size());
	}

	public void requestCourse(int studentId, int courseId)
	{
		//public void requestCourse(int courseId, int year, TermEnum t)
		Student s;
		Optional<Student> sget= students.stream()
				.filter(Student -> Student.getUuid()==studentId)
				.findFirst();
		 if (sget.isPresent()){
		 	s= sget.get();
		 	s.requestCourse(courseId, TermEnum.values()[currentTermIndex]);
		 }
		 else{System.out.println("Error - Student id not found");}
	}
	
	public void assignGrade(int studentId , int courseId , GradeEnum grade)
	{
		for(Student s: students){
			if(s.getUuid()==studentId)
			{
				TermEnum term=TermEnum.values()[currentTermIndex];
				s.assignGrade(courseId, grade, term);
			}
		}
		
	}
	public void instructorReport(Integer instructorId){
		
		Instructor i=instructors.stream()
				.filter(x -> x.getUuid()==instructorId)
				.findFirst()
				.get();
		System.out.println("> instructor,"+i.getName());
		
		List<Course>allCourse=courseCatalog.getAllCoursesWithTerms();
		List<Course> hastaught=new ArrayList<Course>();
		for(Course c: allCourse){
			HashMap<Integer, Integer> get=c.getPlacesInstructor();
			if(get.size()>0){
				if(get.containsKey(instructorId)){
					int places=c.getPlacesInstructor().get(instructorId);	
					System.out.println("> "+c.getId()+","+c.getShortName()+","+places);
				}
			}
		}
	}
	
	
	public void studentReport(int studentId){
		
		Student s=students.stream()
				.filter(x -> x.getUuid()==studentId)
				.findFirst()
				.get();
		System.out.println("> student,"+s.getName());
		
		List<Course>allCourse=courseCatalog.getAllCoursesWithTerms();
		HashMap<Integer, GradeEnum> get=s.getGradesAssigned();
		
		Iterator it = get.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry)it.next();
	    	int courseId=(Integer)pair.getKey();
	    	GradeEnum grade= (GradeEnum)pair.getValue();
	    	String name="";
	    	Optional<Course> cc=allCourse.stream()
	    			.filter(x->x.getId() == courseId)
	    			.findFirst();
	    	if(cc.isPresent())
	    	{
	    		Course c=cc.get();
	    		name=c.getShortName();
	    	}
	    	System.out.println("> "+courseId+","+name+","+grade.name());
	    }
	}
		
	public static void main(String[] args) {
		HelperClass m=new HelperClass();
	}
}
