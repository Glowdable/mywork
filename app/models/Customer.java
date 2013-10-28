package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.validation.Required;

import com.google.gson.annotations.Expose;
 
 
@Entity
public class Customer extends AbstractModel{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	@Column(length=30)
	public String name;
	public Customer(String name){
		this.name=name;
	}
}