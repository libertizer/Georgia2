package model;

import java.util.HashSet;

public abstract class Person {
	
	private int uuid;
	private String name;
	private String address;
	//could be +49/...
	private String phoneNumber;
	
	public Person(int Uuid, String Name,  String Address, String PhoneNumber){
		this.uuid=Uuid;
		this.name=Name;
		this.address=Address;
		this.phoneNumber=PhoneNumber;

	}
	public int getUuid(){
		return this.uuid;
	}
	public void setUuid(int Uuid){
		this.uuid=Uuid;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String Name){
		this.name=Name;
	}
	public String getAddress(){
		return this.address;
	}
	public void setAddress(String Address){
		this.address=Address;
	}
	public String getPhoneNumber(){
		return this.phoneNumber;
	}
	public void setPhoneNumber(String PhoneNumber){
		this.phoneNumber=PhoneNumber;
	}
}
