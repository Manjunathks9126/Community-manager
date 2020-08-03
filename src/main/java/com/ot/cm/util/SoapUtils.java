package com.ot.cm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoapUtils {
	//private static final TGLoggerIF logger = CPSCfgManager.getLogger();
	private static final Logger logger = LoggerFactory.getLogger(SoapUtils.class);
	private String organization = "system";
	private String instance = "default";
	private String userid = "";
	private String password = "";
	private String protocol = "https";
	private String domain = "";
	private int port = 443;
	private String strUrl;
	
	

	public String getStrUrl() {
		return strUrl;
	}

	public void setStrUrl(String strUrl) {
		this.strUrl = strUrl;
	}	

	private String bpmInstanceId = null;

	public String getOrganization() {
		return organization;
	}

	// public String getInstance() {
	// return instance;
	// }

	public String getUserid() {
		return userid;
	}

	public String getPassword() {
		return password;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getDomain() {
		return domain;
	}

	public int getPort() {
		return port;
	}

	public String getBpmInstanceId() {
		return bpmInstanceId;
	}

	public String getSamlArtifact() {
		return samlArtifact;
	}

	private String samlArtifact = "";

	private static final String LOGIN_TEMPLATE = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"> "
			+ " <SOAP:Header> "
			+ "  <wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"> "
			+ "   <wsse:UsernameToken xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"> "
			+ "    <wsse:Username>%s</wsse:Username> " + "    <wsse:Password>%s</wsse:Password> "
			+ "   </wsse:UsernameToken></wsse:Security> " + " </SOAP:Header> " + " <SOAP:Body> "
			+ "  <samlp:Request xmlns:samlp=\"urn:oasis:names:tc:SAML:1.0:protocol\" MajorVersion=\"1\" MinorVersion=\"1\" IssueInstant=\"%s\" RequestID=\"%s\"> "
			+ "   <samlp:AuthenticationQuery> "
			+ "    <saml:Subject xmlns:saml=\"urn:oasis:names:tc:SAML:1.0:assertion\"> "
			+ "     <saml:NameIdentifier Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\">%s</saml:NameIdentifier> "
			+ "    </saml:Subject> " + "   </samlp:AuthenticationQuery> " + "  </samlp:Request> " + " </SOAP:Body> "
			+ "</SOAP:Envelope> ";
	
	

	public static final String PS_SOAP_API_URL_TEMPLATE = "%s://%s:%d/home/%s/com.eibus.web.soap.Gateway.wcp?organization=&fb2deforg=true&messageOptions=0";

	public static final String WSAPI_SEND_MESSAGE = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"> "
			+ "<SOAP:Header/> " + "<SOAP:Body> "
			+ "<SendMessage xmlns=\"http://schemas.cordys.com/bpm/execution/1.0\"> " + "<receiver>%s</receiver> "
			+ "<message overwrite=\"false\">%s</message> " + "</SendMessage> " + "</SOAP:Body> " + "</SOAP:Envelope> ";
	private static final String GET_STATUS_REQUEST="<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP:Body>" + 
			"<GetProcessInstances xmlns=\"http://schemas.cordys.com/pim/queryinstancedata/1.0\">" + 
			"<Query xmlns=\"http://schemas.cordys.com/cql/1.0\">" + 
			"<Select>" + 
			"<QueryableObject>PROCESS_INSTANCE</QueryableObject>" + 
			"<Field>INSTANCE_ID</Field>" + 
			"<Field>PROCESS_NAME</Field>" + 
			"<Field>DESCRIPTION</Field>" + 
			"<Field>START_TIME</Field>" + 
			"<Field>END_TIME</Field>" + 
			"<Field>STATUS</Field>" + 
			"<Field>TYPE</Field>" + 
			"<Field>PROCESS_TYPE</Field>" +
			"<Field>ERROR_TEXT</Field>" +
			"</Select>" + 
			"<Filters>" + 
			"<EQ field=\"INSTANCE_ID\">" + 
			"<Value>%s</Value>" + 
			"</EQ>" + 
			"</Filters>" + 
			"<Cursor numRows=\"5\" position=\"0\" />" + 
			"</Query>" + 
			"</GetProcessInstances></SOAP:Body></SOAP:Envelope>";

	public static final String COMPLEX_POC_EXEC_RESULT_MESSAGE = "<RemoteExecutionResult xmlns=\"http://schemas.cordys.com/default\"> "
			+ "<ResultCode xmlns=\"http://schemas.cordys.com/default\">%s</ResultCode> "
			+ "<ErrorCode xmlns=\"http://schemas.cordys.com/default\">%s</ErrorCode> "
			+ "<ErrorDetails xmlns=\"http://schemas.cordys.com/default\">%s</ErrorDetails> "
			+ "</RemoteExecutionResult>";

	public static final String ORCHESTRATOR_MESSAGE = "<OrchestratorInput xmlns=\"http://schemas.cordys.com/default\"> "
			+ "<StartItinerary xmlns=\"http://schemas.cordys.com/default\"> " + "<BPMName>%s</BPMName> "
			+ "</StartItinerary> " + "<ResumeItinerary xmlns=\"http://schemas.cordys.com/default\"> "
			+ "<InstanceId>%s</InstanceId> " + "<Action>%d</Action> " + "</ResumeItinerary> " + "</OrchestratorInput>";

	static final String WSAPI_EXECUTE_PROCESS =

			"<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"> " + "<SOAP:Body> "
					+ "<ExecuteProcess type=\"\" xmlns=\"http://schemas.cordys.com/bpm/execution/1.0\"> "
					+ "<type>definition</type> " + "<receiver>%s</receiver> " + "<source>CPS User</source>" + "<message>%s</message> "
					+ "</ExecuteProcess> " + "</SOAP:Body> " + "</SOAP:Envelope> ";

	static final String REMOTE_EXECUTE_RESULT = "<RemoteExecutionResult xmlns=\"http://schemas.cordys.com/default\"> "
			+ "<ResultCode xmlns=\"http://schemas.cordys.com/default\">%s</ResultCode> "
			+ "<ErrorCode xmlns=\"http://schemas.cordys.com/default\">%s</ErrorCode> "
			+ "<ErrorDetails xmlns=\"http://schemas.cordys.com/default\">%s</ErrorDetails> "
			+ "</RemoteExecutionResult> ";
	public static final String PS_SOAP_LOGIN_FAULT_MESSAGE = "The username or password you entered is incorrect";
	public static final String PS_SOAP_FAULT_MESSAGE_START="		<faultstring				xml:lang=\"en-US\">";
	public static final String PS_SOAP_FAULT_MESSAGE_END="</faultstring>";

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public void setSamlArtifact(String samlArtifact) {
		this.samlArtifact = samlArtifact;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private static String sendHttpRequest(String strUrl, boolean post, Map<String, String> attributes, String body) throws Exception {
		
		int responseCode = 0;
		
		String response = sendHttpRequestInternal(strUrl, post, attributes, body);

		responseCode = Integer.parseInt(response.substring(0, 3));

		if (responseCode != HttpURLConnection.HTTP_OK) {
			if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
				if (response.indexOf("Unable to bind an artifact") >= 0) {
					// Echo.login(true);
					response = sendHttpRequestInternal(strUrl, post, attributes, body);
				}
			}
		}		
		return response.substring(4);
	}

	private static String sendHttpRequestInternal(String strUrl, boolean post, Map<String, String> attributes, String body) throws Exception {

		if(logger.isDebugEnabled()){
			logger.debug("sendHttpRequestInternal()", "Start");
		}
		
		Set<String> keys = attributes.keySet();
		URL url = null;
		HttpURLConnection conn = null;
		OutputStreamWriter out = null;

		try {
			logger.debug("sendHttpRequestInternal()", "request body  : " + body);
			url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();

			logger.debug("sendHttpRequestInternal()", "HttpURLConnection  : " + conn);

			url = new URL(strUrl);
			conn.setDoInput(true);
			if (post) {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			} else
				conn.setRequestMethod("GET");

			for (String key : keys) {
				conn.setRequestProperty(key, attributes.get(key));
			}

			if (body != null && !body.isEmpty()) {
				out = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
				out.write(body);
				out.close();
				out = null;
			}

			int responseCode = conn.getResponseCode();		

			BufferedReader br = null;

			if (responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_MULT_CHOICE)
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			else
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

			String output;
			String strResponse = "";

			while ((output = br.readLine()) != null) {
				strResponse += output;
			}

			conn.disconnect();
			conn = null;
			
			if(logger.isDebugEnabled()){
				logger.debug("sendHttpRequestInternal()", "outPut  : " + String.format("%d:%s", responseCode, strResponse));
			}
			
			return String.format("%d:%s", responseCode, strResponse);

		} catch (IOException e) {
			if(logger.isErrorEnabled()){
				logger.error("sendHttpRequestInternal() - IO Error", e);
			}
			throw e;
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("sendHttpRequestInternal()", e);
			}
			throw e;
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
				}
			if (conn != null)
				conn.disconnect();
		}		
		
	}

	public static String extractTagContent(String xml, String tagIdentifier) {
		String content = "";
		
		int tagPos = xml.indexOf(tagIdentifier);
		System.out.println("xml.indexOf(tagIdentifier) : "+xml.indexOf(tagIdentifier));
		if (tagPos > -1) {
			tagPos = xml.indexOf('>', tagPos);
			if (tagPos > -1) {
				content = xml.substring(tagPos + 1, xml.indexOf('<', tagPos));
			}
		}else {			
			tagPos = xml.indexOf(PS_SOAP_FAULT_MESSAGE_START);			
			System.out.println("xml.indexOf(PS_SOAP_FAULT_MESSAGE_START) : "+tagPos);
			if (tagPos > -1) {
				tagPos = xml.indexOf(PS_SOAP_FAULT_MESSAGE_END);
				System.out.println("xml.indexOf(PS_SOAP_FAULT_MESSAGE_END) : "+tagPos);
				if (tagPos > -1) {
					content = xml.substring(xml.indexOf(PS_SOAP_FAULT_MESSAGE_START) + 1, xml.indexOf(PS_SOAP_FAULT_MESSAGE_END));
				}			
			}
			content =content+PS_SOAP_FAULT_MESSAGE_END;
		}
		
		return content;
	}
	
	public static String extractResponseContent(String xml, String tagIdentifier) {
		
		String content = "";
		int PRETTY_PRINT_INDENT_FACTOR = 4;
		int i=xml.indexOf("<PROCESS_INSTANCE>");		
		int j=xml.indexOf("</PROCESS_INSTANCE>");
		j="</PROCESS_INSTANCE>".length()+j;
		if(i>-1) {
			String output=	xml.substring(i, j);
			JSONObject xmlJSONObj = XML.toJSONObject(output);
			content = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);

			System.out.println("output : "+output);
			return content;
		}else {
			content= "No data found for the input : ";
		}
		
		/*int tagPos = xml.indexOf(tagIdentifier);
		System.out.println("xml.indexOf(tagIdentifier) : "+xml.indexOf(tagIdentifier));
		if (tagPos > -1) {
			tagPos = xml.indexOf('>', tagPos);
			if (tagPos > -1) {
				content = xml.substring(tagPos + 1, xml.indexOf('<', tagPos));
			}
		}else {			
			tagPos = xml.indexOf(PS_SOAP_FAULT_MESSAGE_START);			
			System.out.println("xml.indexOf(PS_SOAP_FAULT_MESSAGE_START) : "+tagPos);
			if (tagPos > -1) {
				tagPos = xml.indexOf(PS_SOAP_FAULT_MESSAGE_END);
				System.out.println("xml.indexOf(PS_SOAP_FAULT_MESSAGE_END) : "+tagPos);
				if (tagPos > -1) {
					content = xml.substring(xml.indexOf(PS_SOAP_FAULT_MESSAGE_START) + 1, xml.indexOf(PS_SOAP_FAULT_MESSAGE_END));
				}			
			}
			content =content+PS_SOAP_FAULT_MESSAGE_END;
		}*/
		
		return content;
	}

	public void readContext(ServletContext context) {
		if (context != null) {
			domain = context.getInitParameter("domain");
			organization = context.getInitParameter("organization");
			protocol = context.getInitParameter("protocol");
			instance = context.getInitParameter("instance");
			userid = context.getInitParameter("userid");
			password = context.getInitParameter("password");
			port = Integer.parseInt(context.getInitParameter("port"));
		} else {
			// logger.error("Read Context: context is null.");
			System.out.println("Read Context: context is null.");
		}
	}

	public String login() throws Exception{
		synchronized (samlArtifact) {
			if (samlArtifact == null || samlArtifact.isEmpty()) {
				try {
					samlArtifact = _login();
					if(samlArtifact != null && !samlArtifact.isEmpty()) {						
						if(logger.isDebugEnabled()){
							logger.debug("_login", "samlArtifact : " + samlArtifact);
						}
					}
				} catch (Exception e) {
					if(e instanceof java.net.UnknownHostException){
						throw new Exception(e.getMessage());
					}else {
						throw new Exception(e.getMessage());
					}
				}
			}
		}
		return samlArtifact;
	}

	private String _login() throws IOException,Exception {
		String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ").format(new Date());
		String guid = UUID.randomUUID().toString();

		String soapRequest = String.format(LOGIN_TEMPLATE, userid, password, currentDate, guid, userid);
		Map<String, String> attributes = new HashMap<String, String>();

		attributes.put("Content-Type", "text/xml; charset=UTF-8");
		attributes.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		attributes.put("User-Agent", "EchoServer");
		attributes.put("Accept-Language", "en-US,en;q=0.8");

		String strResponse = sendHttpRequest(strUrl, true, attributes, soapRequest);
	//	logger.debug(SoapUtils.class, "_login", "strResponse : " + strResponse);
		if (strResponse == null || strResponse.isEmpty())
			return "";

		return extractTagContent(strResponse, "<samlp:AssertionArtifact");
	}

	public static String sendMessage(String strUrl, String samlArt, String strRecipient, String payload) throws Exception{

		String soapRequest = String.format(WSAPI_SEND_MESSAGE, strRecipient, payload);

		Map<String, String> attributes = new HashMap<String, String>();

		attributes.put("Content-Type", "text/xml; charset=UTF-8");
		attributes.put("Accept-Type", "*/*");
		attributes.put("User-Agent", "EchoServer");
		attributes.put("Accept-Language", "en-US,en;q=0.8");
		attributes.put("SAMLart", samlArt);
		if(logger.isDebugEnabled()){
			logger.debug("_login", "soapRequest : " + soapRequest);
		}
		// String strUrl = String.format(PS_SOAP_API_URL_TEMPLATE,protocol,
		// domain, port, organization, organization, instance,
		// domain.substring(domain.indexOf('.')+1));
		String strResponse = sendHttpRequest(strUrl, true, attributes, soapRequest);
		if(logger.isDebugEnabled()){
			logger.debug("_login", "strResponse : " + strResponse);
		}
		if (strResponse == null || strResponse.isEmpty())
			return null;

		String instanceId = extractTagContent(strResponse, "<instance_id>");
		if (instanceId != null && !instanceId.isEmpty())
			return instanceId;
		return null;
	}

	public static String executeProcess(String strUrl, String samlArt, String bpmName, String payload) throws Exception {
		String strResponse = null;
		String soapRequest =null;
		try {
			if(bpmName==null || bpmName.isEmpty()) {
				System.out.println("payload : "+payload);
				/*replace with work flow id in the soap request*/
				soapRequest = String.format(GET_STATUS_REQUEST, payload, payload);
			}else {
				soapRequest = String.format(WSAPI_EXECUTE_PROCESS, bpmName, payload);
			}
			System.out.println("payload : "+payload);
			logger.debug("executeProcess", "soapRequest : " + soapRequest);
			
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("Content-Type", "text/xml; charset=UTF-8");
			attributes.put("Accept-Type", "*/*");
			attributes.put("User-Agent", "EchoServer");
			attributes.put("Accept-Language", "en-US,en;q=0.8");
			attributes.put("SAMLart", samlArt);
			
			strResponse = sendHttpRequest(strUrl, true, attributes, soapRequest);

		} catch (Exception e) {
			logger.error("executeProcess", e);
			e.printStackTrace();
			if(e instanceof java.net.UnknownHostException){
			throw new Exception(e.getMessage());
			}else {
				throw new Exception(e.getMessage());
			}
		}

		return strResponse;
	}

}
