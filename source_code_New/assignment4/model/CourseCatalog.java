package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import controller.HelperClass;

/*This class is not in the UML diagramm since it is a kind of helper class*/
public class CourseCatalog {
	
	private static CourseCatalog instance = null;
	private static List<Course>catalog;
	private static List<Course>catalogOneCoursePerTerm;
	private CourseCatalog(){
		
	}
	//add Courses with terms to courseCatalog
	public void addCourse(Course c ){
		this.catalog.add(c);
	}
	public void addCourseSingleTerm(Course c ){
            this.catalogOneCoursePerTerm.add(c);
            //System.out.println("SIZE COURSECATALOG "+catalogOneCoursePerTerm.size());
	}
	public Course getCourseSingleTerm(int courseId, int year, TermEnum t ){

            for(Course c: catalogOneCoursePerTerm)
            {
                if(c.getId()==courseId && c.getYear()==year && c.getTerm().equals(t))
                {
                    System.out.println("iD: " +c.getShortName());
                    return c;
                }
            }
            return null;
	}
	
	//print out courses with terms
	public void printAllEntries(){
		sortCatalog();
		/*Adapt output to match requirements*/
		for (Course c: catalog)
		{	List<String>toClean=c.getAllTermsTaught();
			
		StringBuilder listString = new StringBuilder();

		for (String s : toClean)
		     listString.append(s+",");
			 String toPrint=listString.toString();
			String print=(c.getId()+","+ c.getShortName()+","+toPrint);
			if(!print.isEmpty())
				print = print.substring(0, print.length()-1);
			System.out.println(print);
		}
	}
	public void sortCatalog(){
		//sort catalog by courseId
		Collections.sort(catalog, (o1, o2) -> o1.getId() - o2.getId());
		Collections.sort(catalogOneCoursePerTerm, (o1, o2) -> o1.getId() - o2.getId());
	}
	
	public static CourseCatalog getCourseCatalog()
	{
		if(instance==null)
		{
			instance=new CourseCatalog();
			catalog=new ArrayList <Course>();
			catalogOneCoursePerTerm=new ArrayList <Course>();
			return instance;
		}
		return instance;
	}
	
	
	public void printNumCourses()
        {
		System.out.println(catalog.size());
	}
}
