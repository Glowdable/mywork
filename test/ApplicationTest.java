import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.junit.Before;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

/*import org.json.JSONException;  
 import org.json.JSONObject;  
 import org.json.JSONTokener; */

public class ApplicationTest extends FunctionalTest {

	// 每个方法都是一个过程，都会首先运行这个before方法
	@Before
	public void setup() {
		Fixtures.deleteAll();
		// createTestData();
	}

	@Test
	// @Ignore
	public void testThatIndexPageWorks() {
		Response response = GET("/");
		assertIsOk(response);
		assertContentType("text/html", response);
		assertCharset(play.Play.defaultWebEncoding, response);
	}

	@Test
	// @Ignore
	public void testOutBoundVouchGet() {

		// createTestData();//写在同一个方法里，到达controller的时候，数据可能没有保存
		Response response = GET("/OutboundVouch/show/1");
		assertIsOk(response);
		// assertEquals(Integer.valueOf(302),response.status);//The HTTP
		// response status code 302 Found is a common way of performing a
		// redirection.
		assertContentType("application/json", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		System.out.println(response.out.toString());// 返回的json字符串，在控制台查看
	}

	@Test
	// @Ignore
	public void testOutBoundVouchPost() {

		List<OutboundVouch> outVouchs = createTestData();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation() // 不导出实体中没有用@Expose注解的属性
																				// (必须加这个)
				.serializeNulls().setDateFormat("yyyy-MM-dd")// 时间转化为特定格式
																// HH:mm:ss:SSS
				.setPrettyPrinting() // 对json结果格式化.
				.setVersion(1.0).create();

		String strJson = gson.toJson(outVouchs);

		Response response = POST("/OutboundVouch/save", "application/json",
				strJson);
		assertIsOk(response);
		// assertEquals(Integer.valueOf(302),response.status);//The HTTP
		// response status code 302 Found is a common way of performing a
		// redirection.
		assertContentType("text/plain", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		System.out.println(response.out.toString());// 返回的保存成功（单据数量）或者失败，
	}

	@Test
	// @Ignore
	public void testOutBoundVouchPut() {
		List<OutboundVouch> outVouchs = createTestData();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation() // 不导出实体中没有用@Expose注解的属性
																				// (必须加这个)
				.serializeNulls().setDateFormat("yyyy-MM-dd")// 时间转化为特定格式
																// HH:mm:ss:SSS
				.setPrettyPrinting() // 对json结果格式化.
				.setVersion(1.0).create();

		String strJson = gson.toJson(outVouchs);

		Response response = PUT("/OutboundVouch/update", "application/json",
				strJson);
		assertIsOk(response);
		// assertEquals(Integer.valueOf(302),response.status);//The HTTP
		// response status code 302 Found is a common way of performing a
		// redirection.
		assertContentType("text/plain", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		System.out.println(response.out.toString());// 返回的更新成功（单据数量）或者失败，
	}

	@Test
	// @Ignore
	public void testOutBoundVouchDelete() {
		Response response = DELETE("/OutboundVouch/delete/1");
		assertIsOk(response);
		// assertEquals(Integer.valueOf(302),response.status);//The HTTP
		// response status code 302 Found is a common way of performing a
		// redirection.
		assertContentType("text/plain", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		System.out.println(response.out.toString());// 返回的更新成功（单据数量）或者失败，
	}

	// 生成json的数据，不能save
	private List<OutboundVouch> createTestData() {
		/*
		 * List<OutboundVouch> outVouchList = new ArrayList<OutboundVouch>();
		 * OutboundVouch outVouch = new OutboundVouch("00000001", 123, "test");
		 * 
		 * OutboundVouchSub vouchsub = new OutboundVouchSub(outVouch,
		 * outVouch.cReceiptMainID, BigInteger.valueOf(1), 1, 123, "TEST",
		 * BigDecimal.valueOf(100));
		 * 
		 * outVouch.addRowWithoutSave(vouchsub);
		 * 
		 * outVouchList.add(outVouch);
		 */
		OutboundVouch outVouch = new OutboundVouch("00000001", 0, null);
		OutboundVouchSub outvouchsub = new OutboundVouchSub(outVouch,
				outVouch.cReceiptMainID, BigInteger.valueOf(1), 0, 0, "001",
				BigDecimal.valueOf(10));
		;
		outVouch.addRowWithoutSave(outvouchsub);

		List<OutboundVouch> outVouchList = new ArrayList<OutboundVouch>();
		outVouchList.add(outVouch);
		/*
		 * // 第二张单据 OutboundVouch outVouchSnd = new OutboundVouch("00000002",
		 * 123, "test");
		 * 
		 * OutboundVouchSub vouchSubSnd = new OutboundVouchSub(outVouchSnd,
		 * outVouch.cReceiptMainID, BigInteger.valueOf(1), 1, 123, "TEST",
		 * BigDecimal.valueOf(100)); outVouchSnd.addRowWithoutSave(vouchSubSnd);
		 * 
		 * outVouchList.add(outVouchSnd);
		 */
		return outVouchList;
	}
}