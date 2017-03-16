package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*This class is not in the UML diagramm since it is a kind of helper class*/
public class CourseCatalog {
	
	private List<Course>catalog;
	//necessary for lambda expression sorting
	private Map<Course, Object>buffer;
	public CourseCatalog(){
		catalog=new ArrayList <Course>();
	}
	//add Courses with terms to courseCatalog
	public void addCourse(Course c ){
		this.catalog.add(c);
	}
	//print out courses with terms
	public void printAllEntries(){
		sortCatalog();
		/*Adapt output to match requirements*/
		for (Course c: catalog)
		{	List<String>toClean=c.getTerms();
			
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
		//sort terms
		Collections.sort(catalog, (o1, o2) -> o1.getId() - o2.getId());
	}
	
	
	
	
	public void printNumCourses(){
		
		System.out.println(catalog.size());
	}
}
