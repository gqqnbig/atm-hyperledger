package services.impl;

import services.*;
import entities.*;
import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.Arrays;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import org.apache.commons.lang3.SerializationUtils;
import java.util.Iterator;

public class ManageUserCRUDServiceImpl implements ManageUserCRUDService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ManageUserCRUDServiceImpl() {
		services = new ThirdPartyServicesImpl();
	}

	
	//Shared variable from system services
	
	/* Shared variable from system services and get()/set() methods */
	private boolean PasswordValidated;
	private float WithdrawedNumber;
	private BankCard InputCard;
	private boolean CardIDValidated;
	private boolean IsDeposit;
	private boolean IsWithdraw;
	private float DepositedNumber;
			
	/* all get and set functions for temp property*/
	public boolean getPasswordValidated() {
		return PasswordValidated;
	}	
	
	public void setPasswordValidated(boolean passwordvalidated) {
		this.PasswordValidated = passwordvalidated;
	}
	public float getWithdrawedNumber() {
		return WithdrawedNumber;
	}	
	
	public void setWithdrawedNumber(float withdrawednumber) {
		this.WithdrawedNumber = withdrawednumber;
	}
	public BankCard getInputCard() {
		return InputCard;
	}	
	
	public void setInputCard(BankCard inputcard) {
		this.InputCard = inputcard;
	}
	public boolean getCardIDValidated() {
		return CardIDValidated;
	}	
	
	public void setCardIDValidated(boolean cardidvalidated) {
		this.CardIDValidated = cardidvalidated;
	}
	public boolean getIsDeposit() {
		return IsDeposit;
	}	
	
	public void setIsDeposit(boolean isdeposit) {
		this.IsDeposit = isdeposit;
	}
	public boolean getIsWithdraw() {
		return IsWithdraw;
	}	
	
	public void setIsWithdraw(boolean iswithdraw) {
		this.IsWithdraw = iswithdraw;
	}
	public float getDepositedNumber() {
		return DepositedNumber;
	}	
	
	public void setDepositedNumber(float depositednumber) {
		this.DepositedNumber = depositednumber;
	}
				
	
	
	/* Generate inject for sharing temp variables between use cases in system service */
	public void refresh() {
		AutomatedTellerMachineSystem automatedtellermachinesystem_service = (AutomatedTellerMachineSystem) ServiceManager.getAllInstancesOf("AutomatedTellerMachineSystem").get(0);
		automatedtellermachinesystem_service.setPasswordValidated(PasswordValidated);
		automatedtellermachinesystem_service.setWithdrawedNumber(WithdrawedNumber);
		automatedtellermachinesystem_service.setInputCard(InputCard);
		automatedtellermachinesystem_service.setCardIDValidated(CardIDValidated);
		automatedtellermachinesystem_service.setIsDeposit(IsDeposit);
		automatedtellermachinesystem_service.setIsWithdraw(IsWithdraw);
		automatedtellermachinesystem_service.setDepositedNumber(DepositedNumber);
	}
	
	/* Generate buiness logic according to functional requirement */
	@SuppressWarnings("unchecked")
	public boolean createUser(int userid, String name, String address) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User use : (List<User>)EntityManager.getAllInstancesOf("User"))
		{
			if (use.getUserID() == userid)
			{
				user = use;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(user) == true) 
		{ 
			/* Logic here */
			User use = null;
			use = (User) EntityManager.createObject("User");
			use.setUserID(userid);
			use.setName(name);
			use.setAddress(address);
			EntityManager.addObject("User", use);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			use.getUserID() == userid
			 && 
			use.getName() == name
			 && 
			use.getAddress() == address
			 && 
			StandardOPs.includes(((List<User>)EntityManager.getAllInstancesOf("User")), use)
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//string parameters: [name, address]
		//all relevant vars : use
		//all relevant entities : User
	} 
	 
	static {opINVRelatedEntity.put("createUser", Arrays.asList("User"));}
	
	@SuppressWarnings("unchecked")
	public User queryUser(int userid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User use : (List<User>)EntityManager.getAllInstancesOf("User"))
		{
			if (use.getUserID() == userid)
			{
				user = use;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(user) == false) 
		{ 
			/* Logic here */
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			refresh(); return user;
		}
		else
		{
			throw new PreconditionException();
		}
	} 
	 
	
	@SuppressWarnings("unchecked")
	public boolean modifyUser(int userid, String name, String address) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User use : (List<User>)EntityManager.getAllInstancesOf("User"))
		{
			if (use.getUserID() == userid)
			{
				user = use;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(user) == false) 
		{ 
			/* Logic here */
			user.setUserID(userid);
			user.setName(name);
			user.setAddress(address);
			
			
			refresh();
			// post-condition checking
			if (!(user.getUserID() == userid
			 && 
			user.getName() == name
			 && 
			user.getAddress() == address
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//string parameters: [name, address]
		//all relevant vars : user
		//all relevant entities : User
	} 
	 
	static {opINVRelatedEntity.put("modifyUser", Arrays.asList("User"));}
	
	@SuppressWarnings("unchecked")
	public boolean deleteUser(int userid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User use : (List<User>)EntityManager.getAllInstancesOf("User"))
		{
			if (use.getUserID() == userid)
			{
				user = use;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(user) == false && StandardOPs.includes(((List<User>)EntityManager.getAllInstancesOf("User")), user)) 
		{ 
			/* Logic here */
			EntityManager.deleteObject("User", user);
			
			
			refresh();
			// post-condition checking
			if (!(StandardOPs.excludes(((List<User>)EntityManager.getAllInstancesOf("User")), user)
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : user
		//all relevant entities : User
	} 
	 
	static {opINVRelatedEntity.put("deleteUser", Arrays.asList("User"));}
	
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
