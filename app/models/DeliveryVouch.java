package models;
 
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
 
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.*;
 
@Entity
public class DeliveryVouch extends AbstractModel {
 
	@Expose
	@Id
	@GeneratedValue
    public int cReceiptMainID;
	@Expose
	@Required
	@Column(length=30)
    public String  cDLCode;
	
	@Expose
	@Required
	@Column(length=2)
    public String  cSTCode;   
    
	@Expose
    public Date  dDate ;    
	@Expose
	@Required
	@Column(length=12)
    public String  cDepCode ;
    
	@Expose
    @Required
	@Column(length=20)
    public String  cPersonCode ;
	@Expose
	@Column(length=20)
    public String  cCusCode  ;
	@Column(length=10)
	@Expose
    public String  cWarehouseCode  ;
	@Expose
	@Column(length=60)
    public String  cMemo  ;
    
	@Expose
    public String dverifysystime ;
    
	@Expose
    @Min(0)
    public int iverifystate ;
	@Expose
	@Column(length=20)
    public String cVerifier ;
	@Expose
	@Column(length=20)
    public String cMaker ;
	@Expose
	@Column(length=120)
    public String cCusName ;
	@Expose
	@Column(length=20)
    public String cmodifier ;
	@Expose
    public Date dmodifysystime ;
    
	@Expose
    @Min(0)
    public int iPrintCount ;
    
	@Expose
	@Column(length=255)
    public String cSaleOut ;
	@Expose
	@Column(length=20)
    public String cDefine1 ;
	@Expose
	@Column(length=20)
    public String cDefine2 ;
	@Expose
	@Column(length=20)
    public String cDefine3 ;
	@Expose
    public Date dcreatesystime ;
    @Expose
	@Temporal(TemporalType.TIMESTAMP)
    public Date ufts ;
    //Note how we have used the mappedBy attribute to tell JPA 
    //that the Comment class’ post field maintains the relationship
    //mappedBy=后面的值必须是DeliveryVouchSub类的成员
    @OneToMany(mappedBy="deliveryVouch", cascade=CascadeType.ALL)
    public List<DeliveryVouchSub> deliveryvouchsubs;
    
    public DeliveryVouch(String cdlCode,String cstCode,   
    String cdepCode,String cpersonCode) {
    	this.deliveryvouchsubs = new ArrayList<DeliveryVouchSub>();
        this.cDLCode = cdlCode;
        this.cSTCode = cstCode;
        this.dDate = new Date();
        this.cDepCode=cdepCode;
        this.cPersonCode=cpersonCode;
    }
    
    public DeliveryVouch addRow(DeliveryVouchSub deliveryvouchsub) {
    	deliveryvouchsub.save();
        this.deliveryvouchsubs.add(deliveryvouchsub);
        this.save();
        return this;
    }
    
    public DeliveryVouch addRowWithoutSave(DeliveryVouchSub deliveryvouchsub) {
        this.deliveryvouchsubs.add(deliveryvouchsub);
        return this;
    }
    
  //此处应重载自己的Save，Delete等方法以实现自己的业务逻辑

//	@Override
//	public Object _key() {
//		return cReceiptMainID;
//	}
 
}