package services.impl;

import services.*;
import entities.*;

import java.util.*;
import java.time.LocalDate;
import java.util.function.Predicate;
import java.io.Serializable;
import java.util.function.BooleanSupplier;

import org.apache.commons.lang3.SerializationUtils;

import org.hyperledger.fabric.shim.*;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.contract.*;
import com.owlike.genson.Genson;

@Contract
public class AutomatedTellerMachineSystemImpl implements AutomatedTellerMachineSystem, Serializable, ContractInterface {
	private static final Genson genson = new Genson();
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public AutomatedTellerMachineSystemImpl() {
		services = new ThirdPartyServicesImpl();
	}

				
	
	/* Generate buiness logic according to functional requirement */
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean depositFunds(final Context ctx, float quantity) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = depositFunds(quantity);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean depositFunds(float quantity) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
		/* service reference */
		this.prepareClone(new HashSet<>());
		AutomatedTellerMachineSystemImpl Pre_this = SerializationUtils.clone(this);
		/* service temp attribute */
		/* objects in definition */

		/* check precondition */
		if (this.getPasswordValidated() == true && this.getCardIDValidated() == true && StandardOPs.oclIsundefined(this.getInputCard()) == false && quantity >= 100) 
		{ 
			/* Logic here */
			this.getInputCard().setBalance(this.getInputCard().getBalance()+quantity);
			this.setIsDeposit(true);
			this.setDepositedNumber(quantity);
			
			
			;
			// post-condition checking
			if (!(this.getInputCard().getBalance() == Pre_this.getInputCard().getBalance()+quantity
			 && 
			this.getIsDeposit() == true
			 && 
			this.getDepositedNumber() == quantity
			 &&
			EntityManager.saveModified(BankCard.class)
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
		//all relevant vars : this
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("depositFunds", Arrays.asList(""));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean inputPassword(final Context ctx, int password) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = inputPassword(password);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean inputPassword(int password) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (this.getCardIDValidated() == true && StandardOPs.oclIsundefined(this.getInputCard()) == false) 
		{ 
			/* Logic here */
			if (this.getInputCard().getPassword() == password)
			{
				this.setPasswordValidated(true);
				
				;
				// post-condition checking
				if (!((this.getInputCard().getPassword() == password ? this.getPasswordValidated() == true
				 && 
				true : this.getPasswordValidated() == false
				 && 
				true))) {
					throw new PostconditionException();
				}
				
				//return code
				;
				return true;
			}
			else
			{
			 	this.setPasswordValidated(false);
			 	
			 	;
			 	// post-condition checking
			 	if (!((this.getInputCard().getPassword() == password ? this.getPasswordValidated() == true
			 	 && 
			 	true : this.getPasswordValidated() == false
			 	 && 
			 	true))) {
			 		throw new PostconditionException();
			 	}
			 	
			 	//return code
			 	;
			 	return false;
			}
			
			
			
		
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : this
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("inputPassword", Arrays.asList(""));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean inputCard(final Context ctx, int cardid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = inputCard(cardid);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean inputCard(int cardid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get bc
		BankCard bc = null;
		//no nested iterator --  iterator: any previous:any
		for (BankCard c : (List<BankCard>)EntityManager.getAllInstancesOf(BankCard.class))
		{
			if (c.getCardID() == cardid)
			{
				bc = c;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (true) 
		{ 
			/* Logic here */
			if ((StandardOPs.oclIsundefined(bc) == false))
			{
				this.setCardIDValidated(true);
				this.setInputCard(bc);
				
				;
				// post-condition checking
				if (!(((StandardOPs.oclIsundefined(bc) == false) ? this.getCardIDValidated() == true
				 && 
				this.getInputCard() == bc
				 && 
				true : this.getCardIDValidated() == false
				 && 
				true))) {
					throw new PostconditionException();
				}
				
				//return code
				;
				return true;
			}
			else
			{
			 	this.setCardIDValidated(false);
			 	
			 	;
			 	// post-condition checking
			 	if (!(((StandardOPs.oclIsundefined(bc) == false) ? this.getCardIDValidated() == true
			 	 && 
			 	this.getInputCard() == bc
			 	 && 
			 	true : this.getCardIDValidated() == false
			 	 && 
			 	true))) {
			 		throw new PostconditionException();
			 	}
			 	
			 	//return code
			 	;
			 	return false;
			}
			
			
			
		
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : this
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("inputCard", Arrays.asList(""));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean ejectCard(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = ejectCard();
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean ejectCard() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (this.getPasswordValidated() == true && this.getCardIDValidated() == true && StandardOPs.oclIsundefined(this.getInputCard()) == false) 
		{ 
			/* Logic here */
			this.setInputCard(null);
			this.setPasswordValidated(false);
			this.setCardIDValidated(false);
			this.setIsWithdraw(false);
			this.setIsDeposit(false);
			this.setWithdrawedNumber(0);
			this.setDepositedNumber(0);

			System.out.println(getInputCard());

			;
			// post-condition checking
			if (!(this.getInputCard() == null
			 && 
			this.getPasswordValidated() == false
			 && 
			this.getCardIDValidated() == false
			 && 
			this.getIsWithdraw() == false
			 && 
			this.getIsDeposit() == false
			 && 
			this.getWithdrawedNumber() == 0
			 && 
			this.getDepositedNumber() == 0
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
		//all relevant vars : this
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("ejectCard", Arrays.asList(""));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean withdrawCash(final Context ctx, int quantity) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = withdrawCash(quantity);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean withdrawCash(int quantity) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
		/* service reference */
		this.prepareClone(new HashSet<>());
		AutomatedTellerMachineSystemImpl Pre_this = SerializationUtils.clone(this);
		/* service temp attribute */
		/* objects in definition */

		/* check precondition */
		if (this.getPasswordValidated() == true && this.getCardIDValidated() == true && StandardOPs.oclIsundefined(this.getInputCard()) == false && this.getInputCard().getBalance() >= quantity) 
		{ 
			/* Logic here */
			this.getInputCard().setBalance(this.getInputCard().getBalance()-quantity);
			this.setWithdrawedNumber(quantity);
			this.setIsWithdraw(true);
			
			
			;
			// post-condition checking
			if (!(this.getInputCard().getBalance() == Pre_this.getInputCard().getBalance()-quantity
			 && 
			this.getWithdrawedNumber() == quantity
			 && 
			this.getIsWithdraw() == true
			 &&
			EntityManager.saveModified(BankCard.class)
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
		//all relevant vars : this
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("withdrawCash", Arrays.asList(""));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public float printReceipt(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = printReceipt();
		return res;
	}

	@SuppressWarnings("unchecked")
	public float printReceipt() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (this.getCardIDValidated() == true && this.getPasswordValidated() == true && StandardOPs.oclIsundefined(this.getInputCard()) == false) 
		{ 
			/* Logic here */
			if (this.getIsWithdraw() == true)
			{
				
				;
				// post-condition checking
				if (!((this.getIsWithdraw() == true ? true : (this.getIsDeposit() == true ? true : true)))) {
					throw new PostconditionException();
				}
				
				//return code
				;
				return this.getWithdrawedNumber();
			}
			else
			{
			 	if (this.getIsDeposit() == true)
			 	{
			 		
			 		;
			 		// post-condition checking
			 		if (!((this.getIsWithdraw() == true ? true : (this.getIsDeposit() == true ? true : true)))) {
			 			throw new PostconditionException();
			 		}
			 		
			 		//return code
			 		;
			 		return this.getDepositedNumber();
			 	}
			 	else
			 	{
			 	 	
			 	 	;
			 	 	// post-condition checking
			 	 	if (!((this.getIsWithdraw() == true ? true : (this.getIsDeposit() == true ? true : true)))) {
			 	 		throw new PostconditionException();
			 	 	}
			 	 	
			 	 	//return code
			 	 	;
			 	 	return 0;
			 	}
			}
			
			
			
		
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : this
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("printReceipt", Arrays.asList(""));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public float checkBalance(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = checkBalance();
		return res;
	}

	@SuppressWarnings("unchecked")
	public float checkBalance() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (this.getPasswordValidated() == true && this.getCardIDValidated() == true && StandardOPs.oclIsundefined(this.getInputCard()) == false) 
		{ 
			/* Logic here */
			
			
			;
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			;				
			return this.getInputCard().getBalance();
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : this
		//all relevant entities : 
	} 
	 
	static {opINVRelatedEntity.put("checkBalance", Arrays.asList(""));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public void nullTest(final Context ctx) {
		String json = genson.serialize(null);
		ctx.getStub().putStringState("AutomatedTellerMachineSystemImpl.InputCardPK", json);
	}


	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean cardIdentification(final Context ctx) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = cardIdentification();
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean cardIdentification() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/

		/* check precondition */
		if (true) 
		{ 
			/* Logic here */
			
			
			;
			// post-condition checking
			if (!(true)) {
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
	} 
	 
	
	
	
	
	/* temp property for controller */
	private Boolean PasswordValidated;
	private Float WithdrawedNumber;
	private Object InputCardPK;
	private BankCard InputCard;
	private Boolean CardIDValidated;
	private Boolean IsDeposit;
	private Boolean IsWithdraw;
	private Float DepositedNumber;

	/* all get and set functions for temp property*/
	public boolean getPasswordValidated() {
		if (PasswordValidated == null)
			PasswordValidated = genson.deserialize(EntityManager.getStub().getStringState("system.PasswordValidated"), Boolean.class);
		if (PasswordValidated != null)
			return PasswordValidated;
		else
			return false;
	}

	public void setPasswordValidated(boolean passwordvalidated) {
		EntityManager.getStub().putStringState("system.PasswordValidated", genson.serialize(passwordvalidated));
		this.PasswordValidated = passwordvalidated;
	}

	public float getWithdrawedNumber() {
		if (WithdrawedNumber == null)
			WithdrawedNumber = genson.deserialize(EntityManager.getStub().getStringState("system.WithdrawedNumber"), Float.class);
		if (WithdrawedNumber != null)
			return WithdrawedNumber;
		else
			return 0;
	}

	public void setWithdrawedNumber(float withdrawednumber) {
		EntityManager.getStub().putStringState("system.WithdrawedNumber", genson.serialize(withdrawednumber));
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
		if (CardIDValidated == null)
			CardIDValidated = genson.deserialize(EntityManager.getStub().getStringState("system.CardIDValidated"), Boolean.class);
		if (CardIDValidated != null)
			return CardIDValidated;
		else
			return false;
	}

	public void setCardIDValidated(boolean cardidvalidated) {
		this.CardIDValidated = cardidvalidated;
		EntityManager.getStub().putStringState("system.CardIDValidated", genson.serialize(cardidvalidated));
	}

	public boolean getIsDeposit() {
		if (IsDeposit == null)
			IsDeposit = genson.deserialize(EntityManager.getStub().getStringState("system.IsDeposit"), Boolean.class);
		if (IsDeposit != null)
			return IsDeposit;
		else
			return false;
	}

	public void setIsDeposit(boolean isdeposit) {
		EntityManager.getStub().putStringState("system.IsDeposit", genson.serialize(isdeposit));
		this.IsDeposit = isdeposit;
	}

	public boolean getIsWithdraw() {
		if (IsWithdraw == null)
			IsWithdraw = genson.deserialize(EntityManager.getStub().getStringState("system.IsWithdraw"), Boolean.class);
		if (IsWithdraw != null)
			return IsWithdraw;
		else
			return false;
	}

	public void setIsWithdraw(boolean iswithdraw) {
		EntityManager.getStub().putStringState("system.IsWithdraw", genson.serialize(iswithdraw));
		this.IsWithdraw = iswithdraw;
	}

	public float getDepositedNumber() {
		if (DepositedNumber == null)
			DepositedNumber = genson.deserialize(EntityManager.getStub().getStringState("system.DepositedNumber"), Float.class);
		if (DepositedNumber != null)
			return DepositedNumber;
		else
			return 0;
	}

	public void setDepositedNumber(float depositednumber) {
		EntityManager.getStub().putStringState("system.DepositedNumber", genson.serialize(depositednumber));
		this.DepositedNumber = depositednumber;
	}
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());


	public void prepareClone(HashSet<Object> prepared) {
		if (prepared.contains(this))
			return;
		prepared.add(this);
		if (getInputCard() != null)
			getInputCard().prepareClone(prepared);
	}
}
