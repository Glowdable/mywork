package models;
 
import java.util.*;
import javax.persistence.*;

import com.google.gson.annotations.Expose;

import java.math.*;
 
import play.data.validation.Required;
import play.db.jpa.*;
 
@Entity
public class DeliveryVouchSub extends AbstractModel {
	
	@Expose
	@Id
	@GeneratedValue
	public int AutoID;
	
	@Expose
    public BigInteger  iRowNo;   
	@Expose
	@Required
	@Column(length = 20)
    public String cInvCode ;
	@Expose
	@Column(length = 100)
    public String  cInvName ;
	@Expose
	@Column(length = 35)
    public String  cUnitNO ;
	@Expose
    public BigDecimal  iQuantity  ;
	@Expose
	public BigDecimal  iNum ; 
	@Expose
	public BigDecimal  iUnitPrice  ;
	@Expose
	public BigDecimal iAssUnitPrice;
	@Expose
	public BigDecimal  iNatMoney  ;
	@Expose
	public BigDecimal  iTaxRate  ;
    
	@Expose
    public BigDecimal iNatTax ;
	@Expose
    public BigDecimal iNatSum ;
	@Expose
	@Column(length = 60)
	public String cMemo ;
	@Expose
	public BigDecimal iDoneQuantity;
	@Expose
	public BigDecimal iDoneNum;
	@Expose
	@Column(length = 20)
	public String cFree1;
	@Expose
	@Column(length = 20)
	public String cFree2;  
    
    @ManyToOne
	@JoinColumn(name="DLID",insertable=true,updatable=true)
    public DeliveryVouch deliveryVouch;
    
    public DeliveryVouchSub(int DLID,BigInteger irowno,String cinvCode,
    		String cInvName,BigDecimal iQuantity) { 
    	         this.iRowNo = irowno;
    	         this.cInvCode = cinvCode;
    	         this.cInvName=cInvName;
    	         this.iQuantity=iQuantity;
    	    }
    
    public DeliveryVouchSub(DeliveryVouch deliveryvouch,int DLID,BigInteger irowno,String cinvCode,
    		String cInvName,BigDecimal iQuantity) { 
    	    	this.deliveryVouch=deliveryvouch;
    	         this.iRowNo = irowno;
    	         this.cInvCode = cinvCode;
    	         this.cInvName=cInvName;
    	         this.iQuantity=iQuantity;
    	    }
   
}