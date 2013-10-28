package controllers;

import play.*;
import play.mvc.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
//It seems we could make a better parser for Map<String,Object> which converts the Object to a primitive wrapper, list, or map.  Right now, we just get an error back.
//"Type information is unavailable, and the target object is not a primitive"
//Fixed in GSON 2.0.so i used lastest gson version 2.2.4
import models.*;

public class OutBoundVouchCRUD extends Controller {

	public static void index() {
		render();
	}

	public static void createOutVouch(String body) {
		DeliveryVouch deliveryVouch = createTestDeliveryData();
		// 上面为发货单的数据准备，以便实现--出库单保存的时候，反写发货单数量、金额

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation() // 不导出实体中没有用@Expose注解的属性
																				// (必须加这个)s
				.serializeNulls().setDateFormat("yyyy-MM-dd")// 时间转化为特定格式
																// HH:mm:ss:SSS
				.setPrettyPrinting() // 对json结果格式化.
				.setVersion(1.0).create();

		List<OutboundVouch> lstOutVouch = gson.fromJson(body,
				new TypeToken<List<OutboundVouch>>() {
				}.getType());

		long beforeOutboundCount = OutboundVouch.count();
		System.out.println("OutboundVouch count:" + OutboundVouch.count());
		System.out
				.println("OutboundVouchSub count:" + OutboundVouchSub.count());
		for (OutboundVouch vouch : lstOutVouch) {
			vouch.cDLReceiptMainID = deliveryVouch.cReceiptMainID;
			vouch.cDLCode = deliveryVouch.cDLCode;
			vouch.outboundVouchSubs.get(0).cDLReceiptDetailID = deliveryVouch.deliveryvouchsubs
					.get(0).AutoID;
//			vouch.outboundVouchSubs.get(0).cDLReceiptMainID = deliveryVouch.deliveryvouchsubs
//					.get(0).DLID;
			// 上面为关联准备
			vouch.save();
		}
		long afterOutboundCount = OutboundVouch.count();
		System.out.println("OutboundVouch count:" + OutboundVouch.count());
		System.out
				.println("OutboundVouchSub count:" + OutboundVouchSub.count());
		long internal = afterOutboundCount - beforeOutboundCount;
		if (internal != (long) 0) {
			// 根据ID在数据库中获取新的对象，以便更新
			DeliveryVouchSub deliveryVouchSub = DeliveryVouchSub
					.findById(deliveryVouch.deliveryvouchsubs.get(0).AutoID);
			deliveryVouchSub.iQuantity = deliveryVouch.deliveryvouchsubs.get(0).iQuantity
					.add(lstOutVouch.get(0).outboundVouchSubs.get(0).iQuantity);
			deliveryVouchSub.iNatMoney = deliveryVouchSub.iQuantity
					.multiply(deliveryVouchSub.iUnitPrice);
			deliveryVouch.save();
			renderText("Save Success " + internal
					+ " vouchs! and back to update DeliveryVouch success!");
		} else {
			renderText("Save OutboundVouch Fail!");
		}
		// index();//302
	}

	public static void readOutVouch() {
		createTestData();// 保证测试时有数据

		String id = params.get("id");
		long lId = Long.parseLong(id);
		// OutboundVouch outVouch = OutboundVouch.find("cStockCode",
		// "00000001").first();
		OutboundVouch outVouch = OutboundVouch.find("cReceiptMainID", lId)
				.first();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation() // 不导出实体中没有用@Expose注解的属性
																				// (必须加这个)
				.serializeNulls().setDateFormat("yyyy-MM-dd")// 时间转化为特定格式
																// HH:mm:ss:SSS
				.setPrettyPrinting() // 对json结果格式化.
				.setVersion(1.0).create();

		String strJson = gson.toJson(outVouch);
		// index();//302
		//renderJSON(strJson);
		renderJSON(outVouch);
	}

	public static void updateOutVouch(String body) {

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation() // 不导出实体中没有用@Expose注解的属性
																				// (必须加这个)s
				.serializeNulls().setDateFormat("yyyy-MM-dd")// 时间转化为特定格式
																// HH:mm:ss:SSS
				.setPrettyPrinting() // 对json结果格式化.
				.setVersion(1.0).create();

		List<OutboundVouch> lstOutVouch = gson.fromJson(body,
				new TypeToken<List<OutboundVouch>>() {
				}.getType());

		for (OutboundVouch vouch : lstOutVouch) {
			OutboundVouch temvouch = OutboundVouch
					.findById(vouch.cReceiptMainID);
			if (null == temvouch) {// 数据库中没有这个ID，重新保存
				vouch.save();
			} else {// 数据库中有这个数据，更新,此处只是实现模拟demo，暂时没有考虑其它
				temvouch.delete();
				vouch.save();
			}

		}

		renderText("Update Success!");

		// index();//302
	}

	public static void deleteOutVouch(String body) {
		createTestData();// 保证测试时有数据

		String id = params.get("id");
		long lId = Long.parseLong(id);
		OutboundVouch outVouch = OutboundVouch.findById(lId);
		long lSubId = outVouch.outboundVouchSubs.get(0).AutoID;
		outVouch.delete();
		OutboundVouch outTemVouch = OutboundVouch.findById(lId);
		if (null != outTemVouch) {
			renderText("Delete Fail!");
		} else {
			OutboundVouchSub outVouchSub = OutboundVouchSub.findById(lSubId);
			if (null == outVouchSub) {
				renderText("Delete Success and Delete Sub Entity Mapbyed!");// 子实体也必须级联删除
			}
		}
		// index();//302
	}

	private static void createTestData() {
		OutboundVouch outVouch = new OutboundVouch("00000001", 123, "test")
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

		outVouch.addRow(vouchsub);
		outVouch.addRow(vouchsub1);
		outVouch.addRow(vouchsub2);
	}

	private static DeliveryVouch createTestDeliveryData() {
		DeliveryVouch vouch = new DeliveryVouch("00000001", "0", "01", "001")
				.save();
		DeliveryVouchSub vouchsub = new DeliveryVouchSub(vouch,
				vouch.cReceiptMainID, BigInteger.valueOf(1), "001", "computer",
				BigDecimal.valueOf(10));
		vouchsub.iUnitPrice = new BigDecimal(10);
		vouch.addRow(vouchsub);
		return vouch;
	}

}