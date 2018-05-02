package tn.gov.nashville.service;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;

import tn.gov.nashville.beans.Login;
import tn.gov.nashville.beans.RegisterWithTNGov;
import tn.gov.nashville.dao.LoginDAO;
import tn.gov.nashville.ws.identityproofing.IdentityProofingFullUserType;
import tn.gov.nashville.ws.identityproofing.IdentityProofingService2;
import tn.gov.nashville.ws.identityproofing.IdentityProofingService2Service;
import tn.gov.nashville.ws.identityproofing.SharedCromerrException;
import tn.gov.nashville.ws.identityproofing.UserType;

public class IdentityProofingService2Client {

	public static Logger logger = Logger.getLogger(IdentityProofingService2Client.class);
	
	  public boolean callCrommerServices(RegisterWithTNGov register) throws SharedCromerrException {
		IdentityProofingService2 identityProofingServiceTWO =
				new IdentityProofingService2Service().getIdentityProofingService2Port();
		
		//  *** WARNING*** This is the NAAS account credentials created, No one is allowed to Copy it 
		//  *** WARNING*** and use it for any purposes without the written permission of Vicky Hutchings or Ajay Mahindru */
		logger.info(" calling Crommerr services to get the Security Token  ..... "  );
		String adminId = "ajay.mahindru@tn.gov";
		
		String credential = "CDXscstntam12";
		String securityToken = identityProofingServiceTWO.authenticate(adminId, credential);
		logger.info(" got the  Security Token sucess  ..... " +securityToken);
		boolean validate = false;

		String dataflow  = "test-esa";			
		try{
			UserType  userId1 = new UserType();
		
			userId1.setUserId(register.getLogin().getUniqueId());
			userId1.setFirstName(register.getLogin().getFirstName());
			userId1.setLastName(register.getLogin().getLastName());
			logger.info(" calling Crommerr services to get the Activity ID  ..... "  );
			String activityid = identityProofingServiceTWO.createActivity(securityToken, dataflow, userId1, null);
			
			logger.info(" got the  Activity ID  ..... " +activityid );
			//System.out.println("++++++++++++++++++++uniqueActivityId :"+activityid);
			/*ZonedDateTime zonedDateTime = ZonedDateTime.now()
					.withYear(register.getDateofBirth().getYear())
					.withMonth(register.getDateofBirth().getMonth())
					.withDayOfMonth(register.getDateofBirth().getDate());*/
			ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant((register.getDateofBirth().toInstant()), ZoneId.systemDefault());
					
					
			
			GregorianCalendar gcal = GregorianCalendar.from(zonedDateTime);
			XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			
			IdentityProofingFullUserType identityProofingFullUserType = new IdentityProofingFullUserType();
			identityProofingFullUserType.setUserId(register.getLogin().getUniqueId());
			identityProofingFullUserType.setFirstName(register.getLogin().getFirstName());
			identityProofingFullUserType.setLastName(register.getLogin().getLastName());
			identityProofingFullUserType.setMailingAddress1(register.getAddress1());
			identityProofingFullUserType.setMailingAddress2(register.getAddress2());
			identityProofingFullUserType.setCity(register.getCity());
			identityProofingFullUserType.setState(register.getState());
			identityProofingFullUserType.setZip(register.getZipcode());
			identityProofingFullUserType.setDateOfBirth(xcal);
			identityProofingFullUserType.setPhone(register.getPhonenumber());
			identityProofingFullUserType.setSSNLast4(register.getLast4ssn());
			
			logger.info(" making a call to the LEXIS NEXIS webservice ..... "  );
			identityProofingServiceTWO.createRequest(securityToken, activityid, identityProofingFullUserType);
			logger.info("call to the LEXIS NEXIS webservice  Sucess ..... "  );
			
			logger.info("calling to the database  to Update the lexis Nexis..... "  );
			register.setLexisNexisVerified(true);
			logger.info("call to the database Success to set the Flag to TRUE..... "  );
			
			validate= true;
		}catch(SharedCromerrException  | DatatypeConfigurationException ex){
		//	catch(SharedCromerrException  ex){
			if(ex instanceof SharedCromerrException){
				SharedCromerrException sharedCromerrException = (SharedCromerrException)ex;
				
				logger.warn("Failed to validate with Lexis Nexis Fault code:>>> "+ sharedCromerrException.getFaultInfo().getErrorCode());
			//	System.out.println("Fault code: "+sharedCromerrException.getFaultInfo().getErrorCode());
				
				logger.warn("Validation Failed  with Lexis Nexis Fault Description:>>> "+ sharedCromerrException.getFaultInfo().getDescription());
			//	System.out.println("Fault Description: "+sharedCromerrException.getFaultInfo().getDescription());
				
			}else{
				logger.warn("Failed to validate with Lexis Nexis Fault Exceptions Occured:>>> "+ex.getMessage());
			}
		}		
	
		 
		return validate;	 
	}

}
