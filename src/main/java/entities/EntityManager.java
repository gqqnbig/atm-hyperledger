package entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.lang.reflect.Method;
import java.util.Map;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.File;

import org.hyperledger.fabric.shim.ChaincodeStub;
import com.owlike.genson.Genson;
import java.util.*;
import java.io.*;

public class EntityManager {

	private static final Genson genson = new Genson();

	public static ChaincodeStub stub;

	private static Map<String, List> AllInstance = new HashMap<String, List>();
	
	private static List<BankCard> BankCardInstances = new LinkedList<BankCard>();
	private static List<User> UserInstances = new LinkedList<User>();

	
	/* Put instances list into Map */
	static {
		AllInstance.put("BankCard", BankCardInstances);
		AllInstance.put("User", UserInstances);
	} 
		
	/* Save State */
	public static void save(File file) {
		
		try {
			
			ObjectOutputStream stateSave = new ObjectOutputStream(new FileOutputStream(file));
			
			stateSave.writeObject(BankCardInstances);
			stateSave.writeObject(UserInstances);
			
			stateSave.close();
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/* Load State */
	public static void load(File file) {
		
		try {
			
			ObjectInputStream stateLoad = new ObjectInputStream(new FileInputStream(file));
			
			try {
				
				BankCardInstances =  (List<BankCard>) stateLoad.readObject();
				AllInstance.put("BankCard", BankCardInstances);
				UserInstances =  (List<User>) stateLoad.readObject();
				AllInstance.put("User", UserInstances);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	/* create object */  
	public static Object createObject(String Classifer) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method createObjectMethod = c.getDeclaredMethod("create" + Classifer + "Object");
			return createObjectMethod.invoke(c);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* add object */  
	public static Object addObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectMethod = c.getDeclaredMethod("add" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) addObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}	
	
	/* add objects */  
	public static Object addObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectsMethod = c.getDeclaredMethod("add" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) addObjectsMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* Release object */
	public static boolean deleteObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) deleteObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/* Release objects */
	public static boolean deleteObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) deleteObjectMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}		 	
	
	 /* Get all objects belongs to same class */
	public static List getAllInstancesOf(String ClassName) {
			 return AllInstance.get(ClassName);
	}	

   /* Sub-create object */
	public static BankCard createBankCardObject() {
		BankCard o = new BankCard();
		return o;
	}
	
	public static boolean addBankCardObject(BankCard o) {
		List<BankCard> list = loadList(BankCard.class);
		if (list.add(o)) {
			String json = genson.serialize(list);
			stub.putStringState("BankCard", json);
			return true;
		} else
			return false;
	}
	
	public static boolean addBankCardObjects(List<BankCard> os) {
		return BankCardInstances.addAll(os);
	}
	
	public static boolean deleteBankCardObject(BankCard o) {
		List<BankCard> list = loadList(BankCard.class);
		if (list.remove(o)) {
			String json = genson.serialize(list);
			stub.putStringState("BankCard", json);
			return true;
		} else
			return false;
	}
	
	public static boolean deleteBankCardObjects(List<BankCard> os) {
		return BankCardInstances.removeAll(os);
	}
	public static User createUserObject() {
		User o = new User();
		return o;
	}
	
	public static boolean addUserObject(User o) {
		List<User> list = loadList(User.class);
		if (list.add(o)) {
			String json = genson.serialize(list);
			stub.putStringState("User", json);
			return true;
		} else
			return false;
	}
	
	public static boolean addUserObjects(List<User> os) {
		return UserInstances.addAll(os);
	}
	
	public static boolean deleteUserObject(User o) {
		List<User> list = loadList(User.class);
		if (list.remove(o)) {
			String json = genson.serialize(list);
			stub.putStringState("User", json);
			return true;
		} else
			return false;
	}
	
	public static boolean deleteUserObjects(List<User> os) {
		return UserInstances.removeAll(os);
	}
  

	public static <T> List<T> getAllInstancesOf(Class<T> clazz) {
		List<T> list = loadList(clazz);
		return list;
	}

	private static <T> List<T> loadList(Class<T> clazz) {
		String key = clazz.getSimpleName();
		List<T> list = AllInstance.get(key);
		if (list == null || list.size() == 0) {
			String json = stub.getStringState(key);
			System.out.printf("loadList %s: %s\n", key, json);
			if (json != null && Objects.equals(json, "") == false)
				list = GensonHelper.deserializeList(genson, json, clazz);
			else
				list = new LinkedList<>();
			AllInstance.put(key, list);
		}
		return list;
	}

	private static java.util.Random random;

	public static java.util.Random getRandom() {
		if (random == null)
			random = new Random(getStub().getTxTimestamp().toEpochMilli());
		return random;
	}

	public static String getGuid() {
		try {
			return UUID.nameUUIDFromBytes(Long.toString(getRandom().nextLong()).getBytes("UTF-8")).toString();
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}
	}

	public static ChaincodeStub getStub() {
		return stub;
	}

	public static void setStub(ChaincodeStub stub) {
		EntityManager.stub = stub;
		random = null;

		BankCardInstances = new LinkedList<>();
		UserInstances = new LinkedList<>();

		AllInstance.put("BankCard", BankCardInstances);
		AllInstance.put("User", UserInstances);
	}
}

