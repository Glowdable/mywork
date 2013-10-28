package models;
 
import java.math.BigDecimal;
import java.util.*;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
 
import play.data.validation.Required;
import play.db.jpa.*;
 
@Entity
public class StandingCrop extends AbstractModel {
 
	@Expose
	@Id
	@GeneratedValue
	public long AutoID;//自动生成
	@Required
	@Expose
	public String cInventoryID;
	@Expose
	public BigDecimal iQuantiy; 
	 
    public StandingCrop(String cinventoryid, BigDecimal iquantiy) {
    	this.cInventoryID=cinventoryid;
    	this.iQuantiy=iquantiy;
    } 
}