package tn.gov.nashville.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.apache.log4j.Logger;

import tn.gov.nashville.beans.Login;
import tn.gov.nashville.beans.RegisterWithTNGov;
import tn.gov.nashville.util.DataConnect;


public class LoginDAO {
	
	public static Logger logger = Logger.getLogger(LoginDAO.class);
	
	String LexisNexisVarification;

	public String getLexisNexisVarification() {
		return LexisNexisVarification;
	}

	public void setLexisNexisVarification(String lexisNexisVarification) {
		LexisNexisVarification = lexisNexisVarification;
	}

	public static boolean validate(Login  loginReturnedValuesbySSO) {
		
		logger.info(" Inside validate function..... "); 
		Connection con = null;
		PreparedStatement ps = null;
		boolean valid = false;
		String uniqueIdLoginService = loginReturnedValuesbySSO.getUniqueId();

		try {
			
			logger.info(" Making a call to the database using unique ID ..... "); 
			con = DataConnect.getConnection();   
			logger.info(" database connection Sucess..... "); 
		    String query = "select  LEXISNEXISVERIFIED from BG_CROMERR.CRO_USERINFO_LEXISNEXIS where USERID = '" + uniqueIdLoginService+"'";
		    ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			

			if (rs.next()) {
				loginReturnedValuesbySSO.setUserExist(true);
				String lexisNexis = rs.getString("LEXISNEXISVERIFIED");
				logger.info(" sql query execute sucessfully Got the reults back based on Unique ID of SSO..... " +query);
				 if (lexisNexis.equalsIgnoreCase("True") )				
				{
					 valid = true;	
				}
			
			}
		} catch (SQLException ex) {
			
			logger.error(" Making a call to the database using unique ID FAILED ..... " + ex.getMessage());
			//System.out.println("Login error -->" + ex.getMessage());
			
		} finally {
			DataConnect.close(con);
		
		}
		return valid;
	}
	
	public static boolean insertUser(RegisterWithTNGov  register) {
		
		
		Connection con = null;
		PreparedStatement ps = null;
		String uniqueId = register.getLogin().getUniqueId();
		String firstName = register.getLogin().getFirstName();
		String lastName = register.getLogin().getLastName();
		
		String address1 = register.getAddress1();
		String address2 = register.getAddress2();
		String city = register.getCity();
		
		String state = register.getState();
		String zipcode = register.getZipcode();
		String phonenumber = register.getPhonenumber();
		String last4ssn = register.getLast4ssn();
		ZonedDateTime dob = ZonedDateTime.ofInstant((register.getDateofBirth().toInstant()),ZoneId.systemDefault());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String dateString = dob.format(formatter);
		String emailID = register.getLogin().getUser(); 
		String lastupdated = ZonedDateTime.now().format(formatter);
		logger.info(" got the value from the LexisNexis Form  ..... ");
		boolean lexisnexisVerified = register.isLexisNexisVerified();
		

		try {
			con = DataConnect.getConnection();   
			
			/*
		    String query = "INSERT INTO BG_CROMERR.CRO_USERINFO_LEXISNEXIS (SECURITYTOKEN,ACTIVITYID,USERID,FIRSTNAME,LASTNAME,MIDDLEINITIAL,MAILINGADDRESS1,MAILINGADDRESS2,CITY,STATE,ZIP,PHONE,SSNLAST4,DATEOFBIRTH,LEXISNEXISVERIFIED,EMAILADDRESS,LASTUPDATED)"
		    		+ ""
		    		+ " VALUES('"+ "www"+ "','" +"www"+ "',"+ "'" +uniqueId+"'" +"," +"'" +firstName+"'" +"," +"'" +lastName+"'" +"," +""+","   +address1+"," +address2+ ","+city+"," +state +"," +zipcode+"," +phonenumber+ "," +last4ssn+"," +dateString+"," +"'"+lexisnexisVerified+"'"+ "," +"'" +emailID+"','"+lastupdated+"')";
		    */
			
			
			String query = "INSERT INTO BG_CROMERR.CRO_USERINFO_LEXISNEXIS (SECURITYTOKEN,ACTIVITYID,USERID,FIRSTNAME,LASTNAME,MIDDLEINITIAL,MAILINGADDRESS1,MAILINGADDRESS2,CITY,STATE,ZIP,PHONE,SSNLAST4,DATEOFBIRTH,LEXISNEXISVERIFIED,EMAILADDRESS,LASTUPDATED)"
					+ "" + " VALUES('" + "www" + "','" + "www" + "'," + "'" + uniqueId + "'" + "," + "'" + firstName + "'" + "," + "'" + lastName + "'" + ",'" + "" + "','" + address1 + "','" + address2
					+ "','" + city + "','" + state + "','" + zipcode + "','" + phonenumber + "','" + last4ssn + "','" + dateString + "'," + "'" + lexisnexisVerified + "'" + "," + "'" + emailID
					+ "','" + lastupdated + "')";
			
		    ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			}
		} catch (SQLException ex) {
			System.out.println("Error While Inserting Data -->" + ex.getMessage());
			return false;
		} finally {
			DataConnect.close(con);
		}
		return false;
	}
	
	
	public static boolean updateUser(RegisterWithTNGov  register) {
		Connection con = null;
		PreparedStatement ps = null;
		String uniqueId = register.getLogin().getUniqueId();
		String firstName = register.getLogin().getFirstName();
		String lastName = register.getLogin().getLastName();
		String emailID = register.getLogin().getUser();
		String address1 = register.getAddress1();
		String address2 = register.getAddress2();
		String city = register.getCity();
		String last4ssn = register.getLast4ssn();
		String state = register.getState();
		String zipcode = register.getZipcode();
		String phonenumber = register.getPhonenumber();
		ZonedDateTime dob = ZonedDateTime.ofInstant((register.getDateofBirth().toInstant()),ZoneId.systemDefault());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String dateString = dob.format(formatter);
		boolean lexisnexisVerified = register.isLexisNexisVerified();
		String lastupdated = ZonedDateTime.now().format(formatter);
		
		
		try {
			con = DataConnect.getConnection();   
			String query  ="";
			query = "UPDATE BG_CROMERR.CRO_USERINFO_LEXISNEXIS SET SECURITYTOKEN = 'www'"
					+ ", ACTIVITYID= 'www'"
							+ ",USERID ='"+uniqueId +"'"
							+ ",FIRSTNAME ='"+firstName +"'"
							+ ",LASTNAME ='"+lastName +"'"
							+ ",MIDDLEINITIAL = ' ' "
									+ ",MAILINGADDRESS1 ='"+ address1+"'"
									+ ",MAILINGADDRESS2 ='"+ address2+"'"
									+ ",CITY ='"+ city+"'"
									+ ",STATE ='"+ state+"'"
									+ ",ZIP ='"+zipcode+"'"
									+ ",PHONE ='"+ phonenumber+"'"
									+ ",SSNLAST4 ='"+ last4ssn+"'"
									+ ",DATEOFBIRTH ='"+ dateString+"'"
									+ ",LEXISNEXISVERIFIED ='"+ lexisnexisVerified+"'"
									+ ",EMAILADDRESS ='"+ emailID+"'"
									+ ",LASTUPDATED ='"+lastupdated+"'"
									+ " WHERE USERID ='"+ uniqueId+"'";
		    ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			}
		} catch (SQLException ex) {
			System.out.println("Error While Inserting Data -->" + ex.getMessage());
			return false;
		} finally {
			DataConnect.close(con);
		}
		return false;
	}
	
	public static boolean insertOrUpdateUser(RegisterWithTNGov  register){
		if(register.getLogin().isUserExist())
			return updateUser(register);
		else
			return insertUser(register);
	}
}