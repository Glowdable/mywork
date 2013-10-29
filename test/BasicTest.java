import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import models.DeliveryVouch;
import models.DeliveryVouchSub;
import models.IWorkFlow;
import models.MockWorkFlow;
import models.OutboundVouch;
import models.OutboundVouchSub;
import models.Person;
import models.StandingCrop;

import org.junit.Before;
import org.junit.Test;

import play.db.jpa.JPA;
import play.test.Fixtures;
import play.test.UnitTest;

//import org.easymock.*;

public class BasicTest extends UnitTest {

	private IWorkFlow workFlow;
	private MockWorkFlow mockWorkFlow;

	// 每个方法都是一个过程，都会首先运行这个before方法
	@Before
	public void setup() {
		Fixtures.deleteAll();
		// Mock 工作流准备
		workFlow = createNiceMock(IWorkFlow.class);
		// workFlow = createMock(IWorkFlow.class);
		mockWorkFlow = new MockWorkFlow();
	}

	@Test
	public void createAndRetrieve() {
		// 发货单
		// Create a new DeliveryVouch and save it
		new DeliveryVouch("00000001", "0", "01", "001").save();
		// Retrieve the DeliveryVouch with cReceiptMainID 123
		DeliveryVouch vouch = DeliveryVouch.find("cReceiptMainID", 1).first();
		// Test
		assertNotNull(vouch);
		assertEquals(1, vouch.cReceiptMainID);

		// 出库单
		new OutboundVouch("00000001", vouch.cReceiptMainID, vouch.cDLCode)
				.save();

		OutboundVouch outVouch = OutboundVouch.find("cReceiptMainID",
				(long) vouch.cReceiptMainID).first();
		// Test
		assertNotNull(outVouch);
		assertEquals(1, outVouch.cReceiptMainID);

	}

	@Test
	// @Ignore
	public void CascadeVouchOutboundVouch() {
		// Create a new OutboundVouch and save it
		new OutboundVouch("00000002", 123, "test").save();
		// Retrieve the OutboundVouch with cReceiptMainID 123
		OutboundVouch outVouch = OutboundVouch.find("cStockCode", "00000002")
				.first();
		assertNotNull(outVouch);

		OutboundVouchSub vouchsub = new OutboundVouchSub(outVouch,
				outVouch.cReceiptMainID, BigInteger.valueOf(1), 1, 123, "TEST",
				BigDecimal.valueOf(100));
		OutboundVouchSub vouchsub1 = new OutboundVouchSub(outVouch,
				outVouch.cReceiptMainID, BigInteger.valueOf(2), 1, 123, "TEST",
				BigDecimal.valueOf(100));
		OutboundVouchSub vouchsub2 = new OutboundVouchSub(outVouch,
				outVouch.cReceiptMainID, BigInteger.valueOf(3), 1, 123, "TEST",
				BigDecimal.valueOf(100));
		outVouch.addRow(vouchsub);
		outVouch.addRow(vouchsub1);
		outVouch.addRow(vouchsub2);
		// Test
		assertEquals(3, OutboundVouchSub.count());
		assertEquals("00000002", outVouch.cStockCode);
		// 此处的byOutBoundVouch，对应OutboundVouchSub中的OutboundVouch类型变量，然后第一个字母大写
		List<OutboundVouchSub> outboundVouchSubs = OutboundVouchSub.find(
				"byOutBoundVouch", outVouch).fetch();
		assertEquals(3, outboundVouchSubs.size());
		OutboundVouchSub firstOutboundVouchSub = outboundVouchSubs.get(0);
		assertEquals(BigInteger.valueOf(1), firstOutboundVouchSub.iRowNo);

	}

	@Test
	// @Ignore
	public void CascadeVouchDeliveryVouch() {
		// Create a new DeliveryVouch and save it
		new DeliveryVouch("00000002", "0", "01", "001").save();
		// Retrieve the DeliveryVouch with cReceiptMainID 123
		DeliveryVouch vouch = DeliveryVouch.find("cDLCode", "00000002").first();

		assertNotNull(vouch);
		DeliveryVouchSub vouchsub = new DeliveryVouchSub(vouch,
				vouch.cReceiptMainID, BigInteger.valueOf(1), "00001", "01",
				BigDecimal.valueOf(100));
		DeliveryVouchSub vouchsub1 = new DeliveryVouchSub(vouch,
				vouch.cReceiptMainID, BigInteger.valueOf(2), "00001", "01",
				BigDecimal.valueOf(100));
		DeliveryVouchSub vouchsub2 = new DeliveryVouchSub(vouch,
				vouch.cReceiptMainID, BigInteger.valueOf(3), "00001", "01",
				BigDecimal.valueOf(100));
		vouch.addRow(vouchsub);
		vouch.addRow(vouchsub1);
		vouch.addRow(vouchsub2);
		// Test

		assertEquals(3, DeliveryVouchSub.count());
		assertEquals("00000002", vouch.cDLCode);

		// Retrieve all DeliveryVouchSub saved by DeliveryVouch
		List<DeliveryVouchSub> deliveryVouchSubs = DeliveryVouchSub.find(
				"byDeliveryVouch", vouch).fetch();
		assertEquals(3, deliveryVouchSubs.size());
		DeliveryVouchSub firstDeliveryVouchSub = deliveryVouchSubs.get(0);
		assertEquals(BigInteger.valueOf(1), firstDeliveryVouchSub.iRowNo);

	}

	@Test
	// @Ignore
	public void useRelationOutboundVouch() {

		OutboundVouch outVouch = new OutboundVouch("00000003", 123, "test")
				.save();

		OutboundVouchSub vouchsub = new OutboundVouchSub(outVouch,
				outVouch.cReceiptMainID, BigInteger.valueOf(1), 1, 123, "TEST",
				BigDecimal.valueOf(100));
		OutboundVouchSub vouchsub1 = new OutboundVouchSub(outVouch,
				outVouch.cReceiptMainID, BigInteger.valueOf(1), 1, 123, "TEST",
				BigDecimal.valueOf(100));
		OutboundVouchSub vouchsub2 = new OutboundVouchSub(outVouch,
				outVouch.cReceiptMainID, BigInteger.valueOf(1), 1, 123, "TEST",
				BigDecimal.valueOf(100));

		assertEquals(0, OutboundVouchSub.count());
		outVouch.addRow(vouchsub);
		outVouch.addRow(vouchsub1);
		outVouch.addRow(vouchsub2);

		assertEquals(1, OutboundVouch.count());
		assertEquals(3, OutboundVouchSub.count());

		OutboundVouchSub testsub = OutboundVouchSub.find("byOutBoundVouch",
				outVouch).first();
		assertNotNull(testsub);

		// Delete the DeliveryVouch
		outVouch.delete();// 主实体删除的时候，子实体也必须级联删除

		// Check that all count
		assertEquals(0, OutboundVouch.count());
		assertEquals(0, OutboundVouchSub.count());
	}

	@Test
	// @Ignore
	public void useRelationDeliveryVouch() {
		// Create a new DeliveryVouch and save it
		DeliveryVouch vouch = new DeliveryVouch("00000003", "0", "01", "001")
				.save();
		// Create 3 new DeliveryVouchSub
		DeliveryVouchSub vouchsub = new DeliveryVouchSub(vouch,
				vouch.cReceiptMainID, BigInteger.valueOf(1), "00001", "01",
				BigDecimal.valueOf(100));
		DeliveryVouchSub vouchsub1 = new DeliveryVouchSub(vouch,
				vouch.cReceiptMainID, BigInteger.valueOf(2), "00001", "01",
				BigDecimal.valueOf(100));
		DeliveryVouchSub vouchsub2 = new DeliveryVouchSub(vouch,
				vouch.cReceiptMainID, BigInteger.valueOf(3), "00001", "01",
				BigDecimal.valueOf(100));

		assertEquals(0, DeliveryVouchSub.count());
		vouch.addRow(vouchsub);
		vouch.addRow(vouchsub1);
		vouch.addRow(vouchsub2);

		assertEquals(1, DeliveryVouch.count());
		assertEquals(3, DeliveryVouchSub.count());

		DeliveryVouchSub testsub = DeliveryVouchSub.find("byDeliveryVouch",
				vouch).first();
		assertNotNull(testsub);

		// Delete the DeliveryVouch
		vouch.delete();// 主实体删除的时候，子实体也必须级联删除

		// Check that all count
		assertEquals(0, DeliveryVouch.count());
		assertEquals(0, DeliveryVouchSub.count());
	}

	@Test
	// @Ignore
	public void verifyStandingCrops() {
		new StandingCrop("001", new BigDecimal(100)).save();
		new StandingCrop("002", new BigDecimal(100)).save();
		assertEquals(2, StandingCrop.count());
		OutboundVouch outVouch = new OutboundVouch("00000003", 123, "test")
				.save();

		OutboundVouchSub vouchsub = new OutboundVouchSub(outVouch,
				outVouch.cReceiptMainID, BigInteger.valueOf(1), 1, 123, "001",
				BigDecimal.valueOf(10));
		outVouch.addRow(vouchsub);

		assertEquals(1, OutboundVouch.count());
		assertEquals(1, OutboundVouchSub.count());
		// 以上为数据准备,只取一行进行测试
		// 下面为Mock模拟工作流测试审核
		Queue<Person> personQueue = new LinkedList<Person>();
		personQueue.offer(new Person("01", "zhangxg"));
		personQueue.offer(new Person("02", "zengchq"));
		assertEquals(2, personQueue.size());
		Person p1 = personQueue.poll();
		Person p2 = personQueue.poll();
		// Setting up the expected value of the method call calc
		expect(workFlow.Audit(p1)).andReturn(Boolean.TRUE).times(1);// 都只能调用一次
		expect(workFlow.Audit(p2)).andReturn(Boolean.TRUE).times(1);
		// Setup is finished need to activate the mock
		replay(workFlow);

		 assertEquals(0, personQueue.size());
		//Person ptem=new Person("01", "zhangxg");//其实两个Person不一样
	/*	Person ptem=p1;
		assertEquals(p1,ptem);*/
	//	assertEquals(Boolean.TRUE, workFlow.Audit(p1));
	//	assertEquals(Boolean.TRUE, workFlow.Audit(p2));

		personQueue.offer(p1);
		personQueue.offer(p2);
		assertEquals(2, personQueue.size());
		
		//Mock模拟工作流审核通过

		mockWorkFlow.setAbstractModel(outVouch);
		try {
			mockWorkFlow.Audit(personQueue);
			fail("Exception did not occur");
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
		mockWorkFlow.setWorkFlow(workFlow);
		assertEquals(Boolean.TRUE, mockWorkFlow.Audit(personQueue));// 如果mock对象两次都是true，那么结果就会是true
		verify(workFlow);

		// test
		StandingCrop standingCrop = StandingCrop.find("cInventoryID",
				vouchsub.cInvCode).first();
		if (standingCrop.iQuantiy.compareTo(vouchsub.iQuantity) == -1) {// 存货>现存量,修改存货数量，扣减现存量
			OutboundVouch outTemVouch = OutboundVouch
					.findById(outVouch.cReceiptMainID);
			outTemVouch.outboundVouchSubs.get(0).iQuantity = standingCrop.iQuantiy;
			outTemVouch.save();
			standingCrop.iQuantiy = new BigDecimal(0);
			standingCrop.save();

			// 验证
			OutboundVouch verifyOutVouch = OutboundVouch
					.findById(outVouch.cReceiptMainID);
			assertEquals(standingCrop.iQuantiy,
					verifyOutVouch.outboundVouchSubs.get(0).iQuantity);

			StandingCrop verifyStandingCrop = StandingCrop.find("cInventoryID",
					vouchsub.cInvCode).first();
			assertEquals(new BigDecimal(0), verifyStandingCrop.iQuantiy);

		} else {// 存货<=现存量,直接扣减现存量
			BigDecimal temiQuantiy = standingCrop.iQuantiy;
			standingCrop.iQuantiy = standingCrop.iQuantiy
					.subtract(vouchsub.iQuantity);
			standingCrop.save();

			// 验证
			StandingCrop verifyStandingCrop = StandingCrop.find("cInventoryID",
					vouchsub.cInvCode).first();
			assertEquals(verifyStandingCrop.iQuantiy,
					temiQuantiy.subtract(vouchsub.iQuantity));
		}

	}

}
