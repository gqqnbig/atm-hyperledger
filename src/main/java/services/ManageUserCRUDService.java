package services;

import entities.*;
import java.util.List;
import java.time.LocalDate;


public interface ManageUserCRUDService {

	/* all system operations of the use case*/
	boolean createUser(int userid, String name, String address) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	User queryUser(int userid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean modifyUser(int userid, String name, String address) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean deleteUser(int userid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
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
