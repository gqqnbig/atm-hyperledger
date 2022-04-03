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
import org.hyperledger.fabric.shim.*;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.contract.*;
import com.owlike.genson.Genson;

@Contract
public class ManageUserCRUDServiceImpl implements ManageUserCRUDService, Serializable, ContractInterface {
	private static final Genson genson = new Genson();
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ManageUserCRUDServiceImpl() {
		services = new ThirdPartyServicesImpl();
	}

	
	//Shared variable from system services
	
	/* Shared variable from system services and get()/set() methods */
	private boolean PasswordValidated;
	private float WithdrawedNumber;
	private Object InputCardPK;
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

	private Object getInputCardPK() {
		if (InputCardPK == null)
			InputCardPK = genson.deserialize(EntityManager.getStub().getStringState("system.InputCardPK"), Integer.class);
		return InputCardPK;
	}

	private void setInputCardPK(Object inputcardPK) {
		String json = genson.serialize(inputcardPK);
		EntityManager.getStub().putStringState("system.InputCardPK", json);
		//If we set inputcardPK to null,  getInputCardPK() thinks this fields is not initialized, thus will read the old value from chain.
		if (inputcardPK != null)
			this.InputCardPK = inputcardPK;
		else
			this.InputCardPK = EntityManager.getGuid();
	}

	public BankCard getInputCard() {
		if (InputCard == null)
			InputCard = EntityManager.getBankCardByPK(getInputCardPK());
		return InputCard;
	}

	public void setInputCard(BankCard inputcard) {
		if (inputcard != null)
			setInputCardPK(inputcard.getPK());
		else
			setInputCardPK(null);
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
	
	
	/* Generate buiness logic according to functional requirement */
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean createUser(final Context ctx, int userid, String name, String address) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = createUser(userid, name, address);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean createUser(int userid, String name, String address) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User use : (List<User>)EntityManager.getAllInstancesOf(User.class))
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
			
			
			;
			// post-condition checking
			if (!(true && 
			use.getUserID() == userid
			 && 
			use.getName() == name
			 && 
			use.getAddress() == address
			 && 
			StandardOPs.includes(((List<User>)EntityManager.getAllInstancesOf(User.class)), use)
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			;				
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
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public User queryUser(final Context ctx, int userid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = queryUser(userid);
		return res;
	}

	@SuppressWarnings("unchecked")
	public User queryUser(int userid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User use : (List<User>)EntityManager.getAllInstancesOf(User.class))
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
			
			
			;
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			; return user;
		}
		else
		{
			throw new PreconditionException();
		}
	} 
	 
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean modifyUser(final Context ctx, int userid, String name, String address) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = modifyUser(userid, name, address);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean modifyUser(int userid, String name, String address) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User use : (List<User>)EntityManager.getAllInstancesOf(User.class))
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
			
			
			;
			// post-condition checking
			if (!(user.getUserID() == userid
			 && 
			user.getName() == name
			 && 
			user.getAddress() == address
			 && 
			EntityManager.saveModified(User.class)
			 &&
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			;				
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
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean deleteUser(final Context ctx, int userid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = deleteUser(userid);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean deleteUser(int userid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User use : (List<User>)EntityManager.getAllInstancesOf(User.class))
		{
			if (use.getUserID() == userid)
			{
				user = use;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(user) == false && StandardOPs.includes(((List<User>)EntityManager.getAllInstancesOf(User.class)), user)) 
		{ 
			/* Logic here */
			EntityManager.deleteObject("User", user);
			
			
			;
			// post-condition checking
			if (!(StandardOPs.excludes(((List<User>)EntityManager.getAllInstancesOf(User.class)), user)
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			;				
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
