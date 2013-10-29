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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.validation.Required;

import com.google.gson.annotations.Expose;
 
 
@Entity
public class OutboundVouch extends AbstractModel{
	
	@Expose
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long cReceiptMainID; 
	//@Id不能使用BigInteger
	
	@Expose
	@Required
	@Column(length=30)
	public String cStockCode; 
	@Expose
	@Required
	public int cDLReceiptMainID; 
	@Expose
	@Required
	@Column(length=30)
	public String cDLCode ;
	@Expose
	public Date dDate ;

	@Expose
	@Column(length=12)
	public String cDepCode ;
	@Expose
	@Column(length=20)
	public String cPersonCode ;
	@Expose
	@Column(length=20)
	public String cCusCode;
	@Expose
	@Column(length=20)
	public String cCusAddress ;
	@Expose
	@Column(length=10)
	public String cWarehouseCode ;
	@Expose
	@Column(length=60)
	public String cMemo ;
	@Expose
	public Date dverifysystime ;
	@Expose
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
	public int iPrintCount ;
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
	
/*	@Temporal(TemporalType.TIMESTAMP)	//@GeneratedValue(strategy=GenerationType.AUTO)
    public Timestamp getufts() {
    	return this.ufts;
    }*/
    
   /* @Lob
    public String content;*/
    
   /* @ManyToOne
    public User author;*/
	@Expose
    @OneToMany(mappedBy="outBoundVouch", cascade=CascadeType.ALL)
    public List<OutboundVouchSub> outboundVouchSubs;
    
    public OutboundVouch(String cstockcode,int cdlreceiptMainID,String cdlCode) {
    	this.outboundVouchSubs = new ArrayList<OutboundVouchSub>();
        this.cStockCode = cstockcode;
        this.cDLReceiptMainID = cdlreceiptMainID;
        this.cDLCode=cdlCode;
        this.dDate = new Date();    
    }
    
    public OutboundVouch addRow(OutboundVouchSub outboundvouchsub) {
    	//OutboundVouchSub outboundvouchsub = new Comment(this, author, content).save();
    	outboundvouchsub.save();
        this.outboundVouchSubs.add(outboundvouchsub);
        this.save();
        return this;
    }
    
    //However, persist operation is intended for brand new transient objects and 
//    it fails if id is already assigned. In your case you probably want to call saveOrUpdate instead of persist.
    public OutboundVouch addRowWithoutSave(OutboundVouchSub outboundvouchsub) {
        this.outboundVouchSubs.add(outboundvouchsub);
        return this;
    }
    
//    @Override
//    public OutboundVouch update() {
//      updatedAt();
//      return super.update();
//    }
  
    @PrePersist
    void createdAt() {
      this.dDate = new Date();
    }
   
    @PreUpdate
    void updatedAt() {
      this.dmodifysystime = new Date();
    }
  
//此处应重载自己的Save，Delete等方法以实现自己的业务逻辑
    @Override
    public OutboundVouch save() {
    	createdAt();
        return super.save();
      }

//@Override
//public Object _key() {
//	return cReceiptMainID;
//}
//    Then when you want to access a property you can just write:
//
//    	product.name = "My product";
//    	product.price = 58;
//    	Which is translated at load time to:
//
//    	product.setName("My product");
//    	product.setPrice(58);
    
 
}