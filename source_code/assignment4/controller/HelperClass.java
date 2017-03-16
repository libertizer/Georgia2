package controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
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
	private Person pe;
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
    private  int currentTermIndex=0;
    private int year;
    
    //other
    String errorMessage="unknown";
    Boolean end=false;
    
	public HelperClass(){
		instructors=new ArrayList<Instructor>();
		students=new ArrayList<Student>();
		courses=new ArrayList<Course>();
		terms=new ArrayList<Term>();
		//read csv Files
		getCsvData(studentFile, studentType);
		getCsvData(instructorFile, instructorType);
		getCsvData(courseFile, courseType);
		getCsvData(termFile, termType);
		addTermsToCourses();
		//output to Console
		printResult();
	}
	public String[] getCsvData(String csvFile, String typeFile) {
		String []result=null;
		String errorMessage="unknown";
		try{
			buffer = new BufferedReader(new FileReader(csvFile));
			while ((line = buffer.readLine()) != null) {
				
			result = line.split(cvsSplitBy);
			
			//try error clause to not stop the program if a single row cannot be read
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
						end=true;
					}
					//check actions if exists and not stop_sim
					else if(result[0]!=null&&end==false){
						doAction(result);
					}
				}
			}
			catch (Exception e){
				/*PLEASE UNCOMMENT TO GET ERROR MESSAGES IN STANDART OUTPUT*/
				//System.err.println("Error: Corrupt data for "+ errorMessage+" row. Please check csv file");
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
	
	public void doAction(String [] action){
		try{
			if(action[0].equals(startSim)){
				errorMessage="start_sim";
				if(action[1]!=null){
					year=Integer.parseInt(action[1]);
					System.out.println("> begin,"+TermEnum.values()[currentTermIndex]+","+year);
				}
			}
			
			else if(action[0].equals(nextTerm)){
				errorMessage="next_term";
				//new year  and semester cycle
				if(currentTermIndex==2){
					year+=1;
					currentTermIndex=0;
					System.out.println("> begin,"+TermEnum.values()[currentTermIndex]+","+year);
				}
				//just go to next semester, year stays the same
				else{
					currentTermIndex+=1;
					System.out.println("> begin,"+TermEnum.values()[currentTermIndex]+","+year);
				}
				
			}
			else if(action[0].equals(seatAllocation)){
				boolean courseExists=false;
				int InstructorID=Integer.parseInt(action[1]);
				int capacity=Integer.parseInt(action[3]);
				for (Course c: courses){
					if (c.getId()==Integer.parseInt(action[2])){
						courseExists=true;
						Instructor inst=instructors.stream().filter(i ->i.getUuid()==InstructorID) .findFirst().orElse(null);
						c.allocateSeats(inst, capacity);
					}
				}
				//course does not exist
				if(!courseExists)
				{
					System.err.println("Error: "+ "- course ID does not exist");
				}
				
			}
			else if(action[0].equals(courseRequest)){
				
			}
			else if(action[0].equals(gradeAllocation)){
				
			}
			else if(action[0].equals(instructorReport)){
				
			}
			else if(action[0].equals(studentReport)){
				
			}
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
		
	}
	
	public void addTermsToCourses ()
	{
		//sort terms: FAll, Spring, Summer
		Collections.sort(terms, (o1, o2) -> o1.getTerm() .compareTo( o2.getTerm()));
		for(Course c: courses ){
			ArrayList<String> termArray=new ArrayList<String>();
			for(Term t: terms){
				//addterms during which course was taught to arraylist
				if(c.getId()==t.getCourseId()){
					c.setTerm(t.getTerm().toString());
				}
			}
		}
	
	}
	//output to console
	public void printResult(){
		printNumStudents();
		printNumInstructors();
		printNumInstrStudents();
		printNumCourses();
		printCoursesWithTerms();
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

	//print Courses with restective terms
	public void printCoursesWithTerms(){
		courseCatalog = new CourseCatalog();
		for(Course c: courses ){
			//add course id and courses with their terms to course catalog
			courseCatalog.addCourse(c);
		}
		courseCatalog.printAllEntries();
	}
		
	public static void main(String[] args) {
		HelperClass m=new HelperClass();
	}
}
