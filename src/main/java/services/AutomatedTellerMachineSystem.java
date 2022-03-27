package services;

import entities.*;
import java.util.List;
import java.time.LocalDate;


public interface AutomatedTellerMachineSystem {

	/* all system operations of the use case*/
	boolean depositFunds(float quantity) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean inputPassword(int password) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean inputCard(int cardid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean ejectCard() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean withdrawCash(int quantity) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	float printReceipt() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	float checkBalance() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean cardIdentification() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	boolean getPasswordValidated();
	void setPasswordValidated(boolean passwordvalidated);
	float getWithdrawedNumber();
	void setWithdrawedNumber(float withdrawednumber);
	BankCard getInputCard();
	void setInputCard(BankCard inputcard);
	boolean getCardIDValidated();
	void setCardIDValidated(boolean cardidvalidated);
	boolean getIsDeposit();
	void setIsDeposit(boolean isdeposit);
	boolean getIsWithdraw();
	void setIsWithdraw(boolean iswithdraw);
	float getDepositedNumber();
	void setDepositedNumber(float depositednumber);
	
	
	/* invariant checking function */
}
