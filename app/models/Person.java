package models;

public class Person{
	private String code;
	private String name;
	public String getCode(){
		return this.code;
	}
	public void setCode(String value){
		this.code=value;
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(String value){
		this.name=value;
	}
	
	public Person(String code,String name){
		this.code=code;
		this.name=name;
	}
	
	public Person(){
		
	}
}