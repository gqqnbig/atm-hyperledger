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
public class ManageBankCardCRUDServiceImpl implements ManageBankCardCRUDService, Serializable, ContractInterface {
	private static final Genson genson = new Genson();
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ManageBankCardCRUDServiceImpl() {
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
		AutomatedTellerMachineSystem automatedtellermachinesystem_service = (AutomatedTellerMachineSystem) ServiceManager.getAllInstancesOf(AutomatedTellerMachineSystem.class).get(0);
		automatedtellermachinesystem_service.setPasswordValidated(PasswordValidated);
		automatedtellermachinesystem_service.setWithdrawedNumber(WithdrawedNumber);
		automatedtellermachinesystem_service.setInputCard(InputCard);
		automatedtellermachinesystem_service.setCardIDValidated(CardIDValidated);
		automatedtellermachinesystem_service.setIsDeposit(IsDeposit);
		automatedtellermachinesystem_service.setIsWithdraw(IsWithdraw);
		automatedtellermachinesystem_service.setDepositedNumber(DepositedNumber);
	}
	
	/* Generate buiness logic according to functional requirement */
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean createBankCard(final Context ctx, int cardid, String cardstatus, String catalog, int password, float balance) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = createBankCard(cardid, genson.deserialize(cardstatus, CardStatus.class), genson.deserialize(catalog, CardCatalog.class), password, balance);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean createBankCard(int cardid, CardStatus cardstatus, CardCatalog catalog, int password, float balance) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get bankcard
		BankCard bankcard = null;
		//no nested iterator --  iterator: any previous:any
		for (BankCard ban : (List<BankCard>)EntityManager.getAllInstancesOf(BankCard.class))
		{
			if (ban.getCardID() == cardid)
			{
				bankcard = ban;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(bankcard) == true) 
		{ 
			/* Logic here */
			BankCard ban = null;
			ban = (BankCard) EntityManager.createObject("BankCard");
			ban.setCardID(cardid);
			ban.setCardStatus(cardstatus);
			ban.setCatalog(catalog);
			ban.setPassword(password);
			ban.setBalance(balance);
			EntityManager.addObject("BankCard", ban);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			ban.getCardID() == cardid
			 && 
			ban.getCardStatus() == cardstatus
			 && 
			ban.getCatalog() == catalog
			 && 
			ban.getPassword() == password
			 && 
			ban.getBalance() == balance
			 && 
			StandardOPs.includes(((List<BankCard>)EntityManager.getAllInstancesOf(BankCard.class)), ban)
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
		//all relevant vars : ban
		//all relevant entities : BankCard
	} 
	 
	static {opINVRelatedEntity.put("createBankCard", Arrays.asList("BankCard"));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public BankCard queryBankCard(final Context ctx, int cardid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = queryBankCard(cardid);
		return res;
	}

	@SuppressWarnings("unchecked")
	public BankCard queryBankCard(int cardid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get bankcard
		BankCard bankcard = null;
		//no nested iterator --  iterator: any previous:any
		for (BankCard ban : (List<BankCard>)EntityManager.getAllInstancesOf(BankCard.class))
		{
			if (ban.getCardID() == cardid)
			{
				bankcard = ban;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(bankcard) == false) 
		{ 
			/* Logic here */
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			refresh(); return bankcard;
		}
		else
		{
			throw new PreconditionException();
		}
	} 
	 
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean modifyBankCard(final Context ctx, int cardid, String cardstatus, String catalog, int password, float balance) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = modifyBankCard(cardid, genson.deserialize(cardstatus, CardStatus.class), genson.deserialize(catalog, CardCatalog.class), password, balance);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean modifyBankCard(int cardid, CardStatus cardstatus, CardCatalog catalog, int password, float balance) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get bankcard
		BankCard bankcard = null;
		//no nested iterator --  iterator: any previous:any
		for (BankCard ban : (List<BankCard>)EntityManager.getAllInstancesOf(BankCard.class))
		{
			if (ban.getCardID() == cardid)
			{
				bankcard = ban;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(bankcard) == false) 
		{ 
			/* Logic here */
			bankcard.setCardID(cardid);
			bankcard.setCardStatus(cardstatus);
			bankcard.setCatalog(catalog);
			bankcard.setPassword(password);
			bankcard.setBalance(balance);
			
			
			refresh();
			// post-condition checking
			if (!(bankcard.getCardID() == cardid
			 && 
			bankcard.getCardStatus() == cardstatus
			 && 
			bankcard.getCatalog() == catalog
			 && 
			bankcard.getPassword() == password
			 && 
			bankcard.getBalance() == balance
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
		//all relevant vars : bankcard
		//all relevant entities : BankCard
	} 
	 
	static {opINVRelatedEntity.put("modifyBankCard", Arrays.asList("BankCard"));}
	
	
	@Transaction(intent = Transaction.TYPE.SUBMIT)
	public boolean deleteBankCard(final Context ctx, int cardid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		ChaincodeStub stub = ctx.getStub();
		EntityManager.setStub(stub);

		var res = deleteBankCard(cardid);
		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean deleteBankCard(int cardid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get bankcard
		BankCard bankcard = null;
		//no nested iterator --  iterator: any previous:any
		for (BankCard ban : (List<BankCard>)EntityManager.getAllInstancesOf(BankCard.class))
		{
			if (ban.getCardID() == cardid)
			{
				bankcard = ban;
				break;
			}
				
			
		}
		/* previous state in post-condition*/

		/* check precondition */
		if (StandardOPs.oclIsundefined(bankcard) == false && StandardOPs.includes(((List<BankCard>)EntityManager.getAllInstancesOf(BankCard.class)), bankcard)) 
		{ 
			/* Logic here */
			EntityManager.deleteObject("BankCard", bankcard);
			
			
			refresh();
			// post-condition checking
			if (!(StandardOPs.excludes(((List<BankCard>)EntityManager.getAllInstancesOf(BankCard.class)), bankcard)
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
		//all relevant vars : bankcard
		//all relevant entities : BankCard
	} 
	 
	static {opINVRelatedEntity.put("deleteBankCard", Arrays.asList("BankCard"));}
	
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
