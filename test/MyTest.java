import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import models.DeliveryVouch;
import models.DeliveryVouchSub;
import models.IWorkFlow;
import models.MockWorkFlow;
import models.OutboundVouch;
import models.OutboundVouchSub;
import models.Person;
import models.StandingCrop;
import models.Customer;

import org.hibernate.HibernateException;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentMap;
import org.hibernate.proxy.HibernateProxy;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import play.Logger;
import play.PlayPlugin;
import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import play.db.jpa.NoTransaction;
import play.db.jpa.Transactional;
import play.exceptions.UnexpectedException;
import play.test.Fixtures;
import play.test.UnitTest;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

//import org.easymock.*;
public class MyTest extends UnitTest {
	// 每个方法都是一个过程，都会首先运行这个before方法
	@Before
	public void setup() {
		Fixtures.deleteAll();
		Logger.info("start");
	}

	@Test
	@Ignore
	public void testTras() {

	}

	@Test
	// @Ignore
	public void testTrasaction() throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		// 每个action都会开始一个事务
		// Interface used to interact with the entity manager factory for the
		// persistence unit
		assertNotNull(JPA.em().getTransaction());// 下面就没有事务了
		/*
		 * EntityManagerFactory test=sJPA.entityManagerFactory;
		 * if(test.isOpen()){ EntityManager manager=JPA.em();
		 * 
		 * OutboundVouch outVouch=new OutboundVouch("00000002", 123, "test");
		 * OutboundVouchSub vouchsub = new OutboundVouchSub(outVouch,
		 * outVouch.cReceiptMainID, BigInteger.valueOf(1), 1, 123, "TEST",
		 * BigDecimal.valueOf(100)); outVouch.addRowWithoutSave(vouchsub);
		 * DeliveryVouch vouch=new DeliveryVouch("00000001", "0", "01", "001");
		 * manager.persist(outVouch); //manager.flush();
		 * //manager.flush();//flush() without an active transaction is not
		 * going to work.
		 * //flush有两种模式；auto和commit(default),commit模式在事务提交时，自动flush //auto
		 * //JPA.em().getTransaction().commit(); Logger.debug("test");
		 * manager.persist(vouch); manager.flush();
		 * 
		 * }
		 */
		OutboundVouch outVouch = new OutboundVouch("00000003", 123, "test")
				.save();

		OutboundVouchSub vouchsub = new OutboundVouchSub(outVouch,
				outVouch.cReceiptMainID, BigInteger.valueOf(1), 1, 123, "TEST",
				BigDecimal.valueOf(100));

		outVouch.addRow(vouchsub);
		OutboundVouch temOutVouch = outVouch.findById(outVouch.cReceiptMainID);
		temOutVouch.cCusAddress = "sd";
		Class clazz = temOutVouch.getClass();
/*		while (true) {
			if (clazz.equals(JPABase.class)) {
				Field field = clazz.getField("willBeSaved");
				if (Modifier.isTransient(field.getModifiers())) {
					field.setAccessible(true);
					Logger.info("willBeSaved setAccessible");
				}
				Object o = field.get(temOutVouch);
				boolean bfield = ((Boolean) o).booleanValue();
				field.set(temOutVouch, !bfield);
				break;
			}
			clazz = clazz.getSuperclass();
		}*/

		OutboundVouch outVouch1 = new OutboundVouch("00000003", 123, "test")
				.save();

		OutboundVouchSub vouchsub1 = new OutboundVouchSub(outVouch,
				outVouch.cReceiptMainID, BigInteger.valueOf(1), 1, 123, "TEST",
				BigDecimal.valueOf(100));

		outVouch1.addRow(vouchsub1);
		/*
		 * Set<Field> fields = new HashSet<Field>(); // Class clazz =
		 * this.getClass();
		 * 
		 * for (Field field : fields) { field.setAccessible(true); if
		 * (Modifier.isTransient(field.getModifiers())) { continue; }
		 */
		// boolean btest = temOutVouch.willBeSaved;
		// ((JPABase)temOutVouch).willBeSaved=!;
		// JPA.em().flush();
		// temOutVouch.save();
		/*
		 * JPA.em().find(OutboundVouch.class, outVouch.cReceiptMainID);
		 * temOutVouch.saveWithoutFlush(); temOutVouch.cDefine1 = "test";
		 * PlayPlugin.postEvent("JPASupport.objectUpdated", temOutVouch);
		 * saveAndCascade(true, temOutVouch); JPA.em().flush();
		 */
		// temOutVouch.save();
	}

	private void saveAndCascade(boolean willBeSaved, OutboundVouch temOutVouch) {
		/*
		 * if (avoidCascadeSaveLoops.get().contains(this)) { return; } else {
		 * avoidCascadeSaveLoops.get().add(this); if (willBeSaved) {
		 * PlayPlugin.postEvent("JPASupport.objectUpdated", this); } }
		 */
		// Cascade save
		try {
			Set<Field> fields = new HashSet<Field>();
			Class clazz = temOutVouch.getClass();
			while (!clazz.equals(JPABase.class)) {
				Collections.addAll(fields, clazz.getDeclaredFields());
				clazz = clazz.getSuperclass();
			}
			for (Field field : fields) {
				field.setAccessible(true);
				if (Modifier.isTransient(field.getModifiers())) {
					continue;
				}
				boolean doCascade = false;

			}
		} catch (Exception e) {
			throw new UnexpectedException("During cascading save()", e);
		}
	}

	// Flush方法就是将改变刷新到数据库，当然JPA里面的内容也会相应更新。
	// 这样就可以通过find等query方法查询到相应的更改的内容
	// You first call persist operation on an entity and then before committing
	// you query for the same entity.
	// The provider detects that there was an operation called in this
	// transaction which would potentially change the results of query,
	// so it internally flushes without you calling flush.
	@Test
	@Ignore
	public void testFlush() {
		OutboundVouch outVouch = new OutboundVouch("00000002", 123, "test");
		OutboundVouchSub vouchsub = new OutboundVouchSub(outVouch,
				outVouch.cReceiptMainID, BigInteger.valueOf(1), 1, 123, "TEST",
				BigDecimal.valueOf(100));
		// DeliveryVouch vouch=new DeliveryVouch("00000001", "0", "01", "001");
		EntityManager manager = JPA.em();
		System.out.println("OutboundVouchSub count :"
				+ OutboundVouchSub.count());
		manager.persist(vouchsub);
		System.out.println("OutboundVouchSub count :"
				+ OutboundVouchSub.count());
		OutboundVouchSub testSub = OutboundVouchSub.findById(vouchsub.AutoID);
		// assertNull(testSub);
		manager.flush();
		OutboundVouchSub testSub1 = OutboundVouchSub.findById(vouchsub.AutoID);
		assertEquals(1, OutboundVouchSub.count());
		assertNotNull(testSub1);
	}

	@Test
	@Ignore
	public void testBatchMultiEntity() {
		EntityManager em = JPA.em();
		Long start = System.currentTimeMillis();
		// em.setFlushMode(FlushModeType.AUTO);
		for (int i = 0; i < 1000; i++) {
			System.out.println("s~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + i);
			OutboundVouch outVouch = new OutboundVouch("00000002", 123, "test");
			OutboundVouchSub vouchsub = new OutboundVouchSub(outVouch,
					outVouch.cReceiptMainID, BigInteger.valueOf(1), 1, 123,
					"TEST", BigDecimal.valueOf(100));
			outVouch.addRowWithoutSave(vouchsub);

			DeliveryVouch deliveryVouch = new DeliveryVouch("00000003", "0",
					"01", "001");
			DeliveryVouchSub deliveryVouchSub = new DeliveryVouchSub(
					deliveryVouch, deliveryVouch.cReceiptMainID,
					BigInteger.valueOf(1), "00001", "01",
					BigDecimal.valueOf(100));
			deliveryVouch.addRowWithoutSave(deliveryVouchSub);

			/*
			 * outVouch.save(); deliveryVouch.save();
			 */

			/*
			 * em.persist(outVouch); em.persist(deliveryVouch);
			 */
			outVouch.saveWithoutFlush();
			deliveryVouch.saveWithoutFlush();
			if (i % 1000 == 0) {
				em.flush();
				em.clear();
			}

		}
		Long end = System.currentTimeMillis();
		System.out.println("Took " + (end - start) + " milliseconds");
	}

	@Test
	@Ignore
	public void testBatch() {
		EntityManager em = JPA.em();
		Long start = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			System.out.println("s~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + i);
			Customer customer = new Customer("test");
			em.persist(customer);
		}
		Long end = System.currentTimeMillis();
		System.out.println("Took " + (end - start) + " milliseconds");
	}

	/*
	 * JPA.em().getTransaction().begin(); try{ // do some stuff // fetching,
	 * updating, deleting, whatever
	 * 
	 * JPA.em().getTransaction().commit(); } catch (Exception e) { // if an
	 * error occurs, rollback the transaction
	 * JPA.em().getTransaction().rollback(); } TRANSACTIONAL BLOCK ENDS // do
	 * some other stuff }
	 */
	@Test
	@Ignore
	public void testSessionTras() throws HibernateException {
		// Logger.log4j.de日志
		// org.hibernate.SessionFactory sessionFactoty =
		// HibernateSessionFactory;
		javax.persistence.EntityManager em = JPA.em();
		em.clear();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em
					.createQuery("from OutboundVouch where cstockcode=?");
			query.setParameter(0, "00000001");
			// OutboundVouch out1 = (OutboundVouch) query.();
			query = null;
			tx.commit();
		} catch (HibernateException e) {
			throw e;
		} finally {
			if (tx != null) {
				tx.commit();
			}
		}
	}

	@Test
	@Ignore
	public void testTrasaction1() {

		// 每个action都会开始一个事务

		// Interface used to interact with the entity manager factory for the
		// persistence unit

		JPA.em().getTransaction().commit();// 下面就没有事务了
		// JPA.em().clear();

		EntityManagerFactory test = JPA.entityManagerFactory;

		if (test.isOpen()) {

			EntityManager manager = JPA.em();

			manager.getTransaction().begin();

			OutboundVouch outVouch = new OutboundVouch("00000002", 123, "test");

			OutboundVouchSub vouchsub = new OutboundVouchSub(outVouch,

			outVouch.cReceiptMainID, BigInteger.valueOf(1), 1, 123, "TEST",

			BigDecimal.valueOf(100));

			outVouch.addRowWithoutSave(vouchsub);

			// DeliveryVouch vouch = new DeliveryVouch("00000001", "0",
			// "01","001");

			manager.persist(outVouch);

			// manager.flush();

			// manager.flush();//flush() without an active transaction is not
			// going to work.

			// flush有两种模式；auto和commit(default),commit模式在事务提交时，自动flush

			// auto

			// JPA.em().getTransaction().commit();

			// manager.persist(vouch);

			manager.flush();// 将sql写到数据库
			// manager.flush();//连续两次就是会重复Found two representations of same
			// collection: models.OutboundVouch.outboundVouchSubs
			// To save memory you must make sure that the session is cleared
			// regularly
			manager.clear();// 此处必须clear，清空persist context，清空内存
			manager.getTransaction().commit();

		}
	}

}