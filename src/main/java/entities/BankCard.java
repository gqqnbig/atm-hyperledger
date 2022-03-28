package entities;

import services.impl.StandardOPs;

import java.util.*;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;
import org.hyperledger.fabric.contract.annotation.*;
import com.owlike.genson.annotation.*;

@DataType()
public class BankCard implements Serializable {
	public Object getPK() {
		return getCardID();
	}
	
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
	@JsonProperty
	private Object BelongedUserPK;
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
	@JsonIgnore
	public User getBelongedUser() {
		if (BelongedUser == null) {
			if (BelongedUserPK instanceof Long)
				BelongedUserPK = Math.toIntExact((long) BelongedUserPK);
			BelongedUser = EntityManager.getUserByPK(BelongedUserPK);
		}
		return BelongedUser;
	}	
	
	public void setBelongedUser(User user) {
		this.BelongedUser = user;
		this.BelongedUserPK = user.getPK();
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


	public void prepareClone(HashSet<Object> prepared) {
		if (prepared.contains(this))
			return;
		prepared.add(this);
		if (getBelongedUser() != null)
			getBelongedUser().prepareClone(prepared);

	}
}
