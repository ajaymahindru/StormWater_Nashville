package tn.gov.nashville.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import tn.gov.nashville.dao.LoginDAO;
import tn.gov.nashville.service.LoginServiceClient;
import tn.gov.nashville.util.SessionUtils;
import org.apache.log4j.Logger;

@ManagedBean
@SessionScoped
@JsonIgnoreProperties(ignoreUnknown = true)
public class Login implements Serializable {
	
	public static Logger logger = Logger.getLogger(Login.class);
	
 //  private static final long serialVersionUID = 1094801825228386363L;
	@JsonProperty("password")
	private String pwd;
	@JsonProperty("message")
	private String msg;
	@JsonProperty("username")
	private String user;
	@JsonProperty("uniqueId")
	private String uniqueId;
	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("lastName")
	private String lastName;
	@JsonIgnore
	private boolean userExist;
		
	

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	


	public boolean isUserExist() {
		return userExist;
	}

	public void setUserExist(boolean userExist) {
		this.userExist = userExist;
	}

	public String validateUsernamePassword() {
		
		logger.info(" Entered validateUsernamePassword  function ..... ");  
		LoginServiceClient loginServiceClient= new LoginServiceClient();
		boolean valid1 =  false;
		if (this.getUser()== null ||this.getUser().equals("") || this.getPwd() == null || this.getPwd().equals("")){
			FacesContext.getCurrentInstance().addMessage("password",new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Incorrect Username and Passowrd",
					"Please enter correct username and Password"));
		return "login";	
		}
	
		logger.info(" Calling SSO loginService Client Function callLoginService ..... ");  
		Login loginReturnedValuesbySSO = loginServiceClient.callLoginService(this);
		
		this.setUniqueId(loginReturnedValuesbySSO.getUniqueId());	
		this.setFirstName(loginReturnedValuesbySSO.getFirstName());
		this.setLastName(loginReturnedValuesbySSO.getLastName());
		
		
		if( loginReturnedValuesbySSO.getUniqueId()!= null || this.getUniqueId() == ""  ) {
			try{
				int uniqueId = Integer.parseInt(loginReturnedValuesbySSO.getUniqueId());
				if(uniqueId !=0){
					logger.info(" Got the UniqueID from SSO..... "); 
					valid1 = true;
					logger.info(" Making a call to the database with SSO information ..... "); 
				 valid1 = LoginDAO.validate(loginReturnedValuesbySSO);
				 this.setUserExist(loginReturnedValuesbySSO.isUserExist());
					if (valid1) 
					{
						
						FacesMessage msg = new FacesMessage("Congratulation, Sucessfully validated your Information with STATE of TN.");
						msg.setSeverity(FacesMessage.SEVERITY_INFO);
						FacesContext.getCurrentInstance().addMessage("form:sucessDashMsg", msg);	
						
						
						
						return "tdecDashboard";			
						
					}
					else{
						
						return "laxisNexisForm";	
					}
				}
					
			}catch(NumberFormatException exception){
				
				/*FacesContext.getCurrentInstance().addMessage("password",new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Incorrect Username and Passowrd",
						"Please enter correct username and Password"));
				*/
				
				
				FacesMessage msg = new FacesMessage("Please check your Credentials");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage("form:eventsrc", msg);
				
				/*FacesMessage msg = new FacesMessage("Failed", "This networkId is already exists.");
  				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
  				FacesContext.getCurrentInstance().addMessage("userform:existuser", msg);
  				*/
				exception.printStackTrace();
			}		
			
			
		}
		FacesMessage msg = new FacesMessage("Incorrect Credentials");
		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		FacesContext.getCurrentInstance().addMessage("form:eventsrc", msg);	
		
		
				
			return "login";	
			
		}
		 
		
		//validate login
	/*	public String validateUsernamePassword() {
			boolean valid = LoginDAO.validate(user, pwd);
			if (valid) {
				HttpSession session = SessionUtils.getSession();
				session.setAttribute("username", user);
				return "admin";
			} else {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Incorrect Username and Passowrd",
								"Please enter correct username and Password"));
				return "login";
			}
		}
         */
		
		
		
		 
//		}

//	public static HttpSession getSession() {
//		return (HttpSession) FacesContext.getCurrentInstance()
//				.getExternalContext().getSession(false);
//	}

	public String logout() {
		
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		
		return "login";
	}

}
