package model;

public class Prerequisite {
	private int courseid;
	private int prerequisiteId;
	
	public Prerequisite(int PrerequisiteId, int Courseid){
		this.prerequisiteId=PrerequisiteId;
		this.courseid=Courseid;
	}
	public int getPrereqId(){
		return this.prerequisiteId;
	}
	public void setPrereqId(int PrerequisiteId){
		this.prerequisiteId=PrerequisiteId;
	}
	public int getCourseId(){
		return this.courseid;
	}
	public void setCourseId(int Courseid){
		this.courseid=Courseid;
	}
}
