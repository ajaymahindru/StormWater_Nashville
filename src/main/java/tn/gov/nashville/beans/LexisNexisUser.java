package tn.gov.nashville.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import tn.gov.nashville.dao.LoginDAO;
import tn.gov.nashville.service.LoginServiceClient;
import tn.gov.nashville.util.SessionUtils;

@ManagedBean
@SessionScoped
@JsonIgnoreProperties(ignoreUnknown = true)
public class LexisNexisUser implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
	
	@JsonProperty("userId")
	private String userId;

	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("lastName")
	private String lastName;
	@JsonProperty("mailingAddress1")
	private String mailingAddress1;
	@JsonProperty("mailingAddress2")
	private String mailingAddress2;
	@JsonProperty("city")
	private String city;
	@JsonProperty("state")
	private String state;
	@JsonProperty("zip")
	private String zip;
	
	//6785435796
	@JsonProperty("phone")
	private String phone;	
	@JsonProperty("ssnLast4")
	private String ssnLast4;
	
	//1972-07-08T00:00:00
	@JsonProperty("dateofBirth")
	private String dateofBirth;
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getMailingAddress1() {
		return mailingAddress1;
	}

	public void setMailingAddress1(String mailingAddress1) {
		this.mailingAddress1 = mailingAddress1;
	}

	public String getMailingAddress2() {
		return mailingAddress2;
	}

	public void setMailingAddress2(String mailingAddress2) {
		this.mailingAddress2 = mailingAddress2;
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

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSsnLast4() {
		return ssnLast4;
	}

	public void setSsnLast4(String ssnLast4) {
		this.ssnLast4 = ssnLast4;
	}

	public String getDateofBirth() {
		return dateofBirth;
	}

	public void setDateofBirth(String dateofBirth) {
		this.dateofBirth = dateofBirth;
	}

	
}
