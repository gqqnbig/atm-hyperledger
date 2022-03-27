package services;

import entities.*;
import java.util.List;
import java.time.LocalDate;


public interface ManageBankCardCRUDService {

	/* all system operations of the use case*/
	boolean createBankCard(int cardid, CardStatus cardstatus, CardCatalog catalog, int password, float balance) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	BankCard queryBankCard(int cardid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean modifyBankCard(int cardid, CardStatus cardstatus, CardCatalog catalog, int password, float balance) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean deleteBankCard(int cardid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
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
