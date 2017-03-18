package model;

public class Term {
	
	private int courseId;
	private TermEnum termTaught;
	
	public Term(int CourseId, TermEnum Term) {
		this.courseId=CourseId;
		this.termTaught=Term;
	}
	public int getCourseId(){
		return this.courseId;		
	}
	public void setCourseId(int CourseId){
		this.courseId=CourseId;		
	}
	public TermEnum getTerm(){
		return this.termTaught;		
	}
	public void setTerm(TermEnum TermToSet){
		this.termTaught=TermToSet;		
	}
}

