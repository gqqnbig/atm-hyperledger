package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class BankCard implements Serializable {
	
	/* all primary attributes */
	private int CardID;
	private CardStatus CardStatus;
	private CardCatalog Catalog;
	private int Password;
	private float Balance;
	
	/* all references */
	private User BelongedUser; 
	
	/* all get and set functions */
	public int getCardID() {
		return CardID;
	}	
	
	public void setCardID(int cardid) {
		this.CardID = cardid;
	}
	public CardStatus getCardStatus() {
		return CardStatus;
	}	
	
	public void setCardStatus(CardStatus cardstatus) {
		this.CardStatus = cardstatus;
	}
	public CardCatalog getCatalog() {
		return Catalog;
	}	
	
	public void setCatalog(CardCatalog catalog) {
		this.Catalog = catalog;
	}
	public int getPassword() {
		return Password;
	}	
	
	public void setPassword(int password) {
		this.Password = password;
	}
	public float getBalance() {
		return Balance;
	}	
	
	public void setBalance(float balance) {
		this.Balance = balance;
	}
	
	/* all functions for reference*/
	public User getBelongedUser() {
		return BelongedUser;
	}	
	
	public void setBelongedUser(User user) {
		this.BelongedUser = user;
	}			
	

	/* invarints checking*/
	public boolean BankCard_UniqueCardID() {
		
		if (StandardOPs.isUnique(((List<BankCard>)EntityManager.getAllInstancesOf("BankCard")), "CardID")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean BankCard_BalanceGreatAndEqualZero() {
		
		if (Balance >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (BankCard_UniqueCardID() && BankCard_BalanceGreatAndEqualZero()) {
			return true;
		} else {
			return false;
		}
	}	
	
	//check invariant by inv name
	public boolean checkInvariant(String INVname) {
		
		try {
			Method m = this.getClass().getDeclaredMethod(INVname);
			return (boolean) m.invoke(this);
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	
	}	
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("BankCard_UniqueCardID","BankCard_BalanceGreatAndEqualZero"));

}
