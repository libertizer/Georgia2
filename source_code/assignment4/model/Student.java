package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
	private CourseCatalog courseCatalog;
	private List<Course> coursesTakenWithSuccess;
	public Student(int Uuid, String Name,  String Address, String PhoneNumber){
		super(Uuid, Name, Address, PhoneNumber );
		courseCatalog=CourseCatalog.getCourseCatalog();
		coursesTakenWithSuccess=new ArrayList<Course>();
		
	}
	public void requestCourse(int courseId, int year, TermEnum t)
	{
		Course c=courseCatalog.getCourseSingleTerm(courseId, year, t);
		List <Course> prerequ=c.getPrerequisites();
		List <Course> buffer=prerequ;
        
		/*check prerequisites*/        
		for(Course cc : prerequ){
			for(Course ct : coursesTakenWithSuccess){
				if (ct.getId()==cc.getId())
				{
					buffer.remove(cc);
				}
			}
		}
		if(buffer.isEmpty())
		{
			if(c.getAvailableSeats()>0)
			{
				//enroll student
				c.setStudentEnrollment(this);
				
			}
			else{System.out.println("not enrolled - no avialable seats");}
		}
		else{System.out.println("not enrolled - missing prerequisites");}
		
		
	}
	
}
