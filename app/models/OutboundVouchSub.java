package models;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.data.validation.Required;

import com.google.gson.annotations.Expose;


@Entity
public class OutboundVouchSub extends AbstractModel {

	@Expose
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long AutoID;

	@Expose
	@Required
	public int cDLReceiptDetailID;

	@Expose
	public int cDLReceiptMainID;
	@Expose
	public BigInteger iRowNo;
	@Expose
	@Column(length = 20)
	public String cInvCode;
	@Expose
	@Column(length = 35)
	public String cUnitNO;
	@Expose
	public BigDecimal iQuantity;
	@Expose
	public BigDecimal iNum;
	@Expose
	private BigDecimal iUnitPrice;
	@Expose
	public BigDecimal iAssUnitPrice;
	@Expose
	public BigDecimal iNatMoney;
	@Expose
	public BigDecimal iTaxRate;
	@Expose
	public BigDecimal iNatTax;
	@Expose
	public BigDecimal iNatSum;
	@Expose
	@Column(length = 60)
	public String cMemo;
	@Expose
	@Column(length = 20)
	public String cFree1;
	@Expose
	@Column(length = 20)
	public String cFree2;

	public BigDecimal getiUnitPrice() {
		String n="zengchq";
		System.out.println(n);
		System.out.println();
		return this.iUnitPrice;
	}

	public BigDecimal getiNatMoney() {
		return this.iNatMoney;
	}

	public BigDecimal getiNatTax() {
		return this.iNatTax;
	}

	public BigDecimal getiNatSum() {
		return this.iNatSum;
	}

	// 此处不能加@Expose，否则造成无穷递归
	// When you use Gson, the default JSON representation of a class is a map
	// with key-value pairs that represent the field names and their values.
	// Serialization is done recursively, which poses the first problem, namely
	// that you cannot serialize objects that have circular references
	// @MapsId("cReceiptMainID")
	@ManyToOne
	@JoinColumn(name="cReceiptMainID",insertable=true,updatable=true)
	public OutboundVouch outBoundVouch;

	public OutboundVouchSub(long creceiptMainID, int cdlreceiptDetailID,
			BigInteger irowno, int cdlreceiptMainID, String cinvCode,
			BigDecimal iquantity) {
		this.cDLReceiptDetailID = cdlreceiptDetailID;
		this.cDLReceiptMainID = cdlreceiptMainID;
		this.iRowNo = irowno;
		this.cInvCode = cinvCode;
		this.iQuantity = iquantity;
	}

	public OutboundVouchSub(OutboundVouch outboundvouch,
			long creceiptMainID, BigInteger irowno,
			int cdlreceiptDetailID, int cdlreceiptMainID, String cinvCode,
			BigDecimal iquantity) {
		this.outBoundVouch = outboundvouch;
		this.cDLReceiptDetailID = cdlreceiptDetailID;
		this.cDLReceiptMainID = cdlreceiptMainID;
		this.iRowNo = irowno;
		this.cInvCode = cinvCode;
		this.iQuantity = iquantity;
	}

//	@Override
//	public Object _key() {
//		return AutoID;
//	}
}