package tn.gov.nashville.beans;

import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import tn.gov.nashville.dao.LoginDAO;
import tn.gov.nashville.service.IdentityProofingService2Client;
import tn.gov.nashville.ws.identityproofing.SharedCromerrException;
import org.apache.log4j.Logger;

@ManagedBean
@SessionScoped
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterWithTNGov implements Serializable {
	
	public static Logger logger = Logger.getLogger(RegisterWithTNGov.class);

	private static final long serialVersionUID = 1094801825228386361L;
	
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zipcode;
	private Date dateofBirth;
	private String last4ssn;
	private String phonenumber;
	private boolean lexisNexisVerified;
	

	@ManagedProperty(value="#{login}") 
	private Login login;
	
	
	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Date getDateofBirth() {
		return dateofBirth;
	}

	public void setDateofBirth(Date dateofBirth) {
		this.dateofBirth = dateofBirth;
	}

	public String getLast4ssn() {
		return last4ssn;
	}

	public void setLast4ssn(String last4ssn) {
		this.last4ssn = last4ssn;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	
	 public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}
	
	

	public boolean isLexisNexisVerified() {
		return lexisNexisVerified;
	}

	public void setLexisNexisVerified(boolean lexisNexisVerified) {
		this.lexisNexisVerified = lexisNexisVerified;
	}

	public String validateLexisNexis() {
		 
		
		 IdentityProofingService2Client identityProofingService2Client= new IdentityProofingService2Client();
		
			
			try {
				logger.info(" calling Crommerr services  ..... "  );
					boolean varifiedCustomer = identityProofingService2Client.callCrommerServices(this);
					// TODO  update user database
					boolean dbUpdate = false;
					if(varifiedCustomer)
					 dbUpdate = LoginDAO.insertOrUpdateUser(this);
					
					if (varifiedCustomer && dbUpdate) 
					{
						
						FacesMessage msg = new FacesMessage("Congratulation, we have sucessfully validated your Information.");
						msg.setSeverity(FacesMessage.SEVERITY_INFO);
						FacesContext.getCurrentInstance().addMessage("form:sucessDashMsg", msg);	
						
						
						return "tdecDashboard";	
					}
					else{
						
						FacesMessage msg = new FacesMessage("We are unable to validate your Credentials. Please contact HelpDesk");
						msg.setSeverity(FacesMessage.SEVERITY_FATAL);
						FacesContext.getCurrentInstance().addMessage("form:lexisNexisMsg", msg);	
						
						return "laxisNexisForm";	
					}
						
				} catch (Exception e) 
					{
				
					
					FacesMessage msg = new FacesMessage("We encountered the System Error. Please try again. If the problem persist please contact HelpDesk (888) 891-TDEC (8332) ");
					msg.setSeverity(FacesMessage.SEVERITY_FATAL);
					FacesContext.getCurrentInstance().addMessage("form:lexisNexisMsg", msg);	
					
					
					System.out.println("Exception: "+e.getMessage());
					

					return "laxisNexisForm";
					}
		 
		
		// return "laxisNexisForm";
	}

	
}
