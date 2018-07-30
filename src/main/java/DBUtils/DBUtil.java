package DBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.testng.Assert;

import beans.RHIDataSet;



public class DBUtil {
	
	public static Connection con;
	
	public static Statement createDBConnectionGetStatement() {
		Statement stmt = null;
		String DBServer = null;
		String DefaultSchema = null;
		
		DBServer = "den-signoff07-db01:3306";
		DefaultSchema = "ventana_densignoff07";
		String DBUName = "dev";
		
		String DBPassword = "dev";
		
		String connectionString = "jdbc:mysql://" + DBServer + "/" + DefaultSchema;
		try {
			Class.forName("com.mysql.jdbc.Driver");			
			con = DriverManager.getConnection(connectionString, DBUName, DBPassword);
			stmt = con.createStatement();
			System.out.println("the connection string is" + connectionString);
			System.out.println("connection to db is established");
		} catch (Exception e) {
			Assert.fail(Thread.currentThread().getStackTrace()[1].getMethodName() + " FAILED, Line No: "
					+ e.getStackTrace()[0].getLineNumber() + " And Exception is", e);
		}
		return stmt;
	}
	
	
	
	public static List<RHIDataSet> getListOfRHICustomers() {

		List<RHIDataSet> rhiCustomerData = new ArrayList<RHIDataSet>();
		String queryString = "SELECT  distinct ac.email as email,pi.value as cardHolderId,pt.first_name as firstName,"
				+ "pt.last_name as lastName,pt.date_of_birth as dateOfBirth FROM notifications n JOIN opportunity_account_recommendation_deliveries oard ON n.external_id = oard.public_identifier " 
				+"JOIN opportunity_account_recommendations oar ON oard.opportunity_account_recommendation_id = oar.id AND n.account_id = oar.account_id "
				+"JOIN opportunity_segments_recommendations osr ON oar.opportunity_recommendation_id = osr.opportunity_recommendation_id "
				+"JOIN employer_opportunity_segments eos ON osr.opportunity_segment_key = eos.opportunity_segment_key "
				+"JOIN employer_opportunities eo ON eos.employer_opportunity_id = eo.id "
				+"JOIN opportunities o ON eo.opportunity_key = o.opportunity_key "
				+"JOIN opportunity_families of ON o.opportunity_family_id = of.id "
				+"JOIN opportunity_recommendations_actions ora ON oar.opportunity_recommendation_id = ora.opportunity_recommendation_id "
				+"JOIN action_templates att ON att.action_id = ora.action_id "
				+"JOIN account_credentials ac ON n.account_id = ac.account_id "
				+"JOIN patients pt ON ac.`email` = pt.email "
				+"JOIN patient_identifiers pi ON pt.id = pi.`patient_id` "
				+"JOIN employer_and_insurance_company_configuration eicc ON pi.employer_id = eicc.employer_id "
				+"JOIN accounts a ON ac.account_id = a.id "
				+"WHERE  of.external_key = \"rhi\""
				+"AND att.template_name = \"redirect_token\" "
				+"AND att.template_value NOT LIKE \"%rcid%\" "
				+"AND pi.identifier_type = \"member_number\" AND eicc.rhi_enabled=\"1\" "
				+"AND a.employer_id IN (155) "
				+"ORDER BY n.created_at DESC, n.id DESC LIMIT 3;";
		
		System.out.println("query is :: "+queryString);
		ResultSet rs;
			try {
				
				rs = createDBConnectionGetStatement().executeQuery(queryString);
				while (rs.next()){	
					rhiCustomerData.add(new RHIDataSet(rs.getString("email"), 
							rs.getString("cardHolderId"),rs.getString("firstName"),rs.getString("lastName"),rs.getString("dateOfBirth")));
				}
				con.close();
			} catch (SQLException e) {
				Assert.fail(Thread.currentThread().getStackTrace()[1].getMethodName() + " FAILED, Line No: "
						+ e.getStackTrace()[0].getLineNumber() + " And Exception is", e);
			}
		System.out.println("rhiCustomerData : " + rhiCustomerData);
		return rhiCustomerData;

	}
	
	
	
	
	
	/**
	 * Store list of RHI customers.
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject storeListOfRHICustomers() {
		
		JSONObject obj = new JSONObject();
		try {
			Map<String, JSONArray> rhiCustomersSet = new LinkedHashMap<String, JSONArray>();
			
				JSONArray rhiCustomerSetArray = new JSONArray();
				getListOfRHICustomers().forEach((rhiCustomerData) -> {
					Map<String, String> rhiCustomerSetObj = new LinkedHashMap<String, String>();
					rhiCustomerSetObj.put("email", rhiCustomerData.email);
					if((rhiCustomerData.cardHolderId.length()) > 9){
						rhiCustomerSetObj.put("cardHolderId", (rhiCustomerData.cardHolderId).substring(3));
					}else{
						rhiCustomerSetObj.put("cardHolderId", rhiCustomerData.cardHolderId);	
					}
					rhiCustomerSetObj.put("firstName", rhiCustomerData.firstName);
					rhiCustomerSetObj.put("lastName", rhiCustomerData.lastName);
					rhiCustomerSetObj.put("dateOfBirth", rhiCustomerData.dateOfBirth);
					rhiCustomerSetArray.add(rhiCustomerSetObj);
				});

				rhiCustomersSet.put("members",rhiCustomerSetArray);
				obj = new JSONObject();
				obj.putAll(rhiCustomersSet);
			

		} catch (Exception e) {
			Assert.fail(Thread.currentThread().getStackTrace()[1].getMethodName() + " FAILED, Line No: "
					+ e.getStackTrace()[0].getLineNumber() + " And Exception is", e);
		}
		
		return obj;

	}

}
