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
import com.owlike.genson.Genson;

public class ThirdPartyServicesImpl implements ThirdPartyServices, Serializable {
	private static final Genson genson = new Genson();
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	

	
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
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
