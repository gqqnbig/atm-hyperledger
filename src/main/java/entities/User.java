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
public class User implements Serializable {
	public Object getPK() {
		return getUserID();
	}
	
	/* all primary attributes */
	@Property()
	private int userID;
	@Property()
	private String name;
	@Property()
	private String address;
	
	/* all references */
	private List<BankCard> OwnedCard = new LinkedList<BankCard>(); 
	
	/* all get and set functions */
	public int getUserID() {
		return userID;
	}	
	
	public void setUserID(int userid) {
		this.userID = userid;
	}
	public String getName() {
		return name;
	}	
	
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}	
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	/* all functions for reference*/
	public List<BankCard> getOwnedCard() {
		return OwnedCard;
	}	
	
	public void addOwnedCard(BankCard bankcard) {
		this.OwnedCard.add(bankcard);
	}
	
	public void deleteOwnedCard(BankCard bankcard) {
		this.OwnedCard.remove(bankcard);
	}
	

	/* invarints checking*/
	public boolean User_UniqueUserID() {
		
		if (StandardOPs.isUnique(((List<User>)EntityManager.getAllInstancesOf("User")), "UserID")) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (User_UniqueUserID()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("User_UniqueUserID"));

}
