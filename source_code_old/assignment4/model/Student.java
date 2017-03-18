package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Student extends Person {
	private CourseCatalog courseCatalog;
	private HashMap<Integer, GradeEnum> coursesTaken;
	
	public Student(int Uuid, String Name,  String Address, String PhoneNumber){
		super(Uuid, Name, Address, PhoneNumber );
		courseCatalog=CourseCatalog.getCourseCatalog();
		coursesTaken=new HashMap <Integer, GradeEnum>();
	}
	public void requestCourse(int courseId, TermEnum t)
	{
		Course c=courseCatalog.getCourseSingleTerm(courseId, t);
		List <Integer> prerequ=c.getPrerequisites();
		List <Integer> buffer=prerequ;
        String GradeEnumString="";
		/*check prerequisites*/   
		int check=0;
		for(int prerequisiteId : prerequ){
			check=-1;  
			List<Entry<Integer,GradeEnum>> optional = coursesTaken.entrySet().stream()
                    .filter(e->e.getKey()==prerequisiteId)
                    .collect(Collectors.toList());	
			      
			for (Entry<Integer,GradeEnum> f :optional){
				String getEnum=(String)f.getValue().name();
				if((getEnum.equals("A"))||(getEnum.equals("B"))||
						(getEnum.equals("C"))||(getEnum.equals("D")));
				{
					check=1;
				}
			}
				
		}
		if(check>=0)
		{
			if(c.getAvailableSeats()>0)
			{
				//enroll student
				c.enrollStudent(this);
				coursesTaken.put(courseId, GradeEnum.I);
			}
			else{System.out.println("> not enrolled - no avialable seats");}
		}
		else{System.out.println("> not enrolled - missing prerequisites");}
		
	}
	
	public void assignGrade(int courseId, GradeEnum grade, TermEnum term){
		//We dont remember the grade for now, we only check if it is A,B,C,D
		if((grade.equals(GradeEnum.A))||(grade.equals(GradeEnum.B))||(grade.equals(GradeEnum.C))||(grade.equals(GradeEnum.D))){
			
			coursesTaken.put(courseId, grade);
			System.out.println("> recorded,"+grade.name());
		}
	}
	public HashMap<Integer, GradeEnum>  getGradesAssigned(){
		return this.coursesTaken;
	}
	
}
