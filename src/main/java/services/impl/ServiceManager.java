package services.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import services.*;
	
public class ServiceManager {
	
	private static Map<String, List> AllServiceInstance = new HashMap<String, List>();
	
	private static List<AutomatedTellerMachineSystem> AutomatedTellerMachineSystemInstances = new LinkedList<AutomatedTellerMachineSystem>();
	private static List<ManageBankCardCRUDService> ManageBankCardCRUDServiceInstances = new LinkedList<ManageBankCardCRUDService>();
	private static List<ManageUserCRUDService> ManageUserCRUDServiceInstances = new LinkedList<ManageUserCRUDService>();
	private static List<ThirdPartyServices> ThirdPartyServicesInstances = new LinkedList<ThirdPartyServices>();
	
	static {
		AllServiceInstance.put("AutomatedTellerMachineSystem", AutomatedTellerMachineSystemInstances);
		AllServiceInstance.put("ManageBankCardCRUDService", ManageBankCardCRUDServiceInstances);
		AllServiceInstance.put("ManageUserCRUDService", ManageUserCRUDServiceInstances);
		AllServiceInstance.put("ThirdPartyServices", ThirdPartyServicesInstances);
	} 
	
	public static List getAllInstancesOf(String ClassName) {
			 return AllServiceInstance.get(ClassName);
	}	
	
	public static AutomatedTellerMachineSystem createAutomatedTellerMachineSystem() {
		AutomatedTellerMachineSystem s = new AutomatedTellerMachineSystemImpl();
		AutomatedTellerMachineSystemInstances.add(s);
		return s;
	}
	public static ManageBankCardCRUDService createManageBankCardCRUDService() {
		ManageBankCardCRUDService s = new ManageBankCardCRUDServiceImpl();
		ManageBankCardCRUDServiceInstances.add(s);
		return s;
	}
	public static ManageUserCRUDService createManageUserCRUDService() {
		ManageUserCRUDService s = new ManageUserCRUDServiceImpl();
		ManageUserCRUDServiceInstances.add(s);
		return s;
	}
	public static ThirdPartyServices createThirdPartyServices() {
		ThirdPartyServices s = new ThirdPartyServicesImpl();
		ThirdPartyServicesInstances.add(s);
		return s;
	}
}	
