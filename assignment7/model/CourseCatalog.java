package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import controller.HelperClass;

/*This class is not in the UML diagramm since it is a kind of helper class*/
public final class CourseCatalog {
	
	private static CourseCatalog instance = null;
	private static List<Course>catalog;
	private static List<Course>catalogOneCoursePerTerm;
	private CourseCatalog(){
		catalogOneCoursePerTerm=new ArrayList<Course>();
		catalog=new ArrayList<Course>();
	}
	//add Courses with terms to courseCatalog
	public void addCourse(Course c ){
		this.catalog.add(c);
	}
	public void addCourseSingleTerm(Course c ){
            CourseCatalog.catalogOneCoursePerTerm.add(c);
            //System.out.println("SIZE COURSECATALOG "+catalogOneCoursePerTerm.size());
	}
	public Course getCourseSingleTerm(int courseId, TermEnum t ){

            for(Course c: catalogOneCoursePerTerm)
            {
                if(c.getId()==courseId && c.getTerm().equals(t.name()))
                {
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
	public Course getCourse(int courseId){
		//sort catalog by courseId
		for(Course c: catalogOneCoursePerTerm){
			if(c.getId()==courseId){return c;}
		}
		return null;
	}
	
	public static CourseCatalog getCourseCatalog()
	{
		if(instance==null)
		{
			instance=new CourseCatalog();
			return instance;
		}
		else{return instance;}
	}
	public List<Course> getAllCoursesWithTerms(){
		return CourseCatalog.catalogOneCoursePerTerm;
	}
	
	public void printNumCourses()
        {
		System.out.println(catalog.size());
	}
	public void setAllPrerequisites(ArrayList<Prerequisite> prereq){
		for(Course c:catalogOneCoursePerTerm)
		{
			for(Prerequisite p:prereq)
			{
				if(p.getCourseId()==c.getId())
				{
					c.setPrerequisites(p.getPrereqId());
				}
					
			}		
		}
	}
}
