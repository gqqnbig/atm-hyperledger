package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;
import org.hyperledger.fabric.contract.annotation.*;

@DataType()
public class BankCard implements Serializable {
	
	/* all primary attributes */
	@Property()
	private int cardID;
	@Property()
	private CardStatus cardStatus;
	@Property()
	private CardCatalog catalog;
	@Property()
	private int password;
	@Property()
	private float balance;
	
	/* all references */
	private User BelongedUser; 
	
	/* all get and set functions */
	public int getCardID() {
		return cardID;
	}	
	
	public void setCardID(int cardid) {
		this.cardID = cardid;
	}
	public CardStatus getCardStatus() {
		return cardStatus;
	}	
	
	public void setCardStatus(CardStatus cardstatus) {
		this.cardStatus = cardstatus;
	}
	public CardCatalog getCatalog() {
		return catalog;
	}	
	
	public void setCatalog(CardCatalog catalog) {
		this.catalog = catalog;
	}
	public int getPassword() {
		return password;
	}	
	
	public void setPassword(int password) {
		this.password = password;
	}
	public float getBalance() {
		return balance;
	}	
	
	public void setBalance(float balance) {
		this.balance = balance;
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
		
		if (balance >= 0) {
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
