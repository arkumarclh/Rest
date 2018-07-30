package Tests;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

public class HttpTest {

	String endpointurl = "http://localhost:8080/SampleProject/restservices/productcatalog/addCatalog";
	private String rhiService = "9540/rhiservice";
	private String hostName = "http://den-signoff08:";
	
	@Test
	public void test01() throws IOException, ParseException{
		JSONParser parser = new JSONParser();
		//String payload = Utils.HttpHelperClasses.returnJSONPayload();
		String payload = Utils.HttpHelper.getJSONObjFromFile();
		String response = Utils.HttpHelper.getResponse(payload,endpointurl,"POST");
		//Assert.assertEquals(actual, expected);
		JSONObject object = (JSONObject) parser.parse(response);
		
	}
	
	//http://den-signoff08:9540/rhiservice/anthem/care-gaps
	
	@Test
	public void test02() throws IOException{
		String url = hostName+rhiService+"/anthem/care-gaps";
		String payload = DBUtils.DBUtil.storeListOfRHICustomers().toString();
		Utils.HttpHelper.getResponse(payload,url,"POST");
		
	}
}
