package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TabPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Tooltip;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.File;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.time.LocalDate;
import java.util.LinkedList;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import gui.supportclass.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import services.*;
import services.impl.*;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Method;

import entities.*;

public class PrototypeController implements Initializable {


	DateTimeFormatter dateformatter;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		automatedtellermachinesystem_service = ServiceManager.createAutomatedTellerMachineSystem();
		managebankcardcrudservice_service = ServiceManager.createManageBankCardCRUDService();
		manageusercrudservice_service = ServiceManager.createManageUserCRUDService();
		thirdpartyservices_service = ServiceManager.createThirdPartyServices();
				
		this.dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
	   	 //prepare data for contract
	   	 prepareData();
	   	 
	   	 //generate invariant panel
	   	 genereateInvairantPanel();
	   	 
		 //Actor Threeview Binding
		 actorTreeViewBinding();
		 
		 //Generate
		 generatOperationPane();
		 genereateOpInvariantPanel();
		 
		 //prilimariry data
		 try {
			DataFitService.fit();
		 } catch (PreconditionException e) {
			// TODO Auto-generated catch block
		 	e.printStackTrace();
		 }
		 
		 //generate class statistic
		 classStatisicBingding();
		 
		 //generate object statistic
		 generateObjectTable();
		 
		 //genereate association statistic
		 associationStatisicBingding();

		 //set listener 
		 setListeners();
	}
	
	/**
	 * deepCopyforTreeItem (Actor Generation)
	 */
	TreeItem<String> deepCopyTree(TreeItem<String> item) {
		    TreeItem<String> copy = new TreeItem<String>(item.getValue());
		    for (TreeItem<String> child : item.getChildren()) {
		        copy.getChildren().add(deepCopyTree(child));
		    }
		    return copy;
	}
	
	/**
	 * check all invariant and update invariant panel
	 */
	public void invairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}				
			}
			
			for (Entry<String, Label> inv : service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	/**
	 * check op invariant and update op invariant panel
	 */		
	public void opInvairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : op_entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
			for (Entry<String, Label> inv : op_service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 
	*	generate op invariant panel 
	*/
	public void genereateOpInvariantPanel() {
		
		opInvariantPanel = new HashMap<String, VBox>();
		op_entity_invariants_label_map = new LinkedHashMap<String, Label>();
		op_service_invariants_label_map = new LinkedHashMap<String, Label>();
		
		VBox v;
		List<String> entities;
		v = new VBox();
		
		//entities invariants
		entities = AutomatedTellerMachineSystemImpl.opINVRelatedEntity.get("inputCard");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("inputCard" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("AutomatedTellerMachineSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("inputCard", v);
		
		v = new VBox();
		
		//entities invariants
		entities = AutomatedTellerMachineSystemImpl.opINVRelatedEntity.get("inputPassword");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("inputPassword" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("AutomatedTellerMachineSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("inputPassword", v);
		
		v = new VBox();
		
		//entities invariants
		entities = AutomatedTellerMachineSystemImpl.opINVRelatedEntity.get("printReceipt");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("printReceipt" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("AutomatedTellerMachineSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("printReceipt", v);
		
		v = new VBox();
		
		//entities invariants
		entities = AutomatedTellerMachineSystemImpl.opINVRelatedEntity.get("checkBalance");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("checkBalance" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("AutomatedTellerMachineSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("checkBalance", v);
		
		v = new VBox();
		
		//entities invariants
		entities = AutomatedTellerMachineSystemImpl.opINVRelatedEntity.get("ejectCard");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("ejectCard" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("AutomatedTellerMachineSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("ejectCard", v);
		
		v = new VBox();
		
		//entities invariants
		entities = AutomatedTellerMachineSystemImpl.opINVRelatedEntity.get("withdrawCash");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("withdrawCash" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("AutomatedTellerMachineSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("withdrawCash", v);
		
		v = new VBox();
		
		//entities invariants
		entities = AutomatedTellerMachineSystemImpl.opINVRelatedEntity.get("depositFunds");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("depositFunds" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("AutomatedTellerMachineSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("depositFunds", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageBankCardCRUDServiceImpl.opINVRelatedEntity.get("createBankCard");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("createBankCard" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageBankCardCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("createBankCard", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageBankCardCRUDServiceImpl.opINVRelatedEntity.get("queryBankCard");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("queryBankCard" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageBankCardCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("queryBankCard", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageBankCardCRUDServiceImpl.opINVRelatedEntity.get("modifyBankCard");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("modifyBankCard" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageBankCardCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("modifyBankCard", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageBankCardCRUDServiceImpl.opINVRelatedEntity.get("deleteBankCard");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("deleteBankCard" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageBankCardCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("deleteBankCard", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageUserCRUDServiceImpl.opINVRelatedEntity.get("createUser");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("createUser" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageUserCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("createUser", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageUserCRUDServiceImpl.opINVRelatedEntity.get("queryUser");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("queryUser" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageUserCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("queryUser", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageUserCRUDServiceImpl.opINVRelatedEntity.get("modifyUser");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("modifyUser" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageUserCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("modifyUser", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageUserCRUDServiceImpl.opINVRelatedEntity.get("deleteUser");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("deleteUser" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageUserCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("deleteUser", v);
		
		v = new VBox();
		
		//entities invariants
		entities = AutomatedTellerMachineSystemImpl.opINVRelatedEntity.get("cardIdentification");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("cardIdentification" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("AutomatedTellerMachineSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("cardIdentification", v);
		
		
	}
	
	
	/*
	*  generate invariant panel
	*/
	public void genereateInvairantPanel() {
		
		service_invariants_label_map = new LinkedHashMap<String, Label>();
		entity_invariants_label_map = new LinkedHashMap<String, Label>();
		
		//entity_invariants_map
		VBox v = new VBox();
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			Label l = new Label(inv.getKey());
			l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			service_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		//entity invariants
		for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
			
			String INVname = inv.getKey();
			Label l = new Label(INVname);
			if (INVname.contains("AssociationInvariants")) {
				l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #099b17 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			} else {
				l.setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
			}	
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			entity_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		ScrollPane scrollPane = new ScrollPane(v);
		scrollPane.setFitToWidth(true);
		all_invariant_pane.setMaxHeight(850);
		
		all_invariant_pane.setContent(scrollPane);
	}	
	
	
	
	/* 
	*	mainPane add listener
	*/
	public void setListeners() {
		 mainPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
			 
			 	if (newTab.getText().equals("System State")) {
			 		System.out.println("refresh all");
			 		refreshAll();
			 	}
		    
		    });
	}
	
	
	//checking all invariants
	public void checkAllInvariants() {
		
		invairantPanelUpdate();
	
	}	
	
	//refresh all
	public void refreshAll() {
		
		invairantPanelUpdate();
		classStatisticUpdate();
		generateObjectTable();
	}
	
	
	//update association
	public void updateAssociation(String className) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber();
		}
		
	}
	
	public void updateAssociation(String className, int index) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber(index);
		}
		
	}	
	
	public void generateObjectTable() {
		
		allObjectTables = new LinkedHashMap<String, TableView>();
		
		TableView<Map<String, String>> tableBankCard = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableBankCard_CardID = new TableColumn<Map<String, String>, String>("CardID");
		tableBankCard_CardID.setMinWidth("CardID".length()*10);
		tableBankCard_CardID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("CardID"));
		    }
		});	
		tableBankCard.getColumns().add(tableBankCard_CardID);
		TableColumn<Map<String, String>, String> tableBankCard_CardStatus = new TableColumn<Map<String, String>, String>("CardStatus");
		tableBankCard_CardStatus.setMinWidth("CardStatus".length()*10);
		tableBankCard_CardStatus.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("CardStatus"));
		    }
		});	
		tableBankCard.getColumns().add(tableBankCard_CardStatus);
		TableColumn<Map<String, String>, String> tableBankCard_Catalog = new TableColumn<Map<String, String>, String>("Catalog");
		tableBankCard_Catalog.setMinWidth("Catalog".length()*10);
		tableBankCard_Catalog.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Catalog"));
		    }
		});	
		tableBankCard.getColumns().add(tableBankCard_Catalog);
		TableColumn<Map<String, String>, String> tableBankCard_Password = new TableColumn<Map<String, String>, String>("Password");
		tableBankCard_Password.setMinWidth("Password".length()*10);
		tableBankCard_Password.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Password"));
		    }
		});	
		tableBankCard.getColumns().add(tableBankCard_Password);
		TableColumn<Map<String, String>, String> tableBankCard_Balance = new TableColumn<Map<String, String>, String>("Balance");
		tableBankCard_Balance.setMinWidth("Balance".length()*10);
		tableBankCard_Balance.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Balance"));
		    }
		});	
		tableBankCard.getColumns().add(tableBankCard_Balance);
		
		//table data
		ObservableList<Map<String, String>> dataBankCard = FXCollections.observableArrayList();
		List<BankCard> rsBankCard = EntityManager.getAllInstancesOf("BankCard");
		for (BankCard r : rsBankCard) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("CardID", String.valueOf(r.getCardID()));
			unit.put("CardStatus", String.valueOf(r.getCardStatus()));
			unit.put("Catalog", String.valueOf(r.getCatalog()));
			unit.put("Password", String.valueOf(r.getPassword()));
			unit.put("Balance", String.valueOf(r.getBalance()));

			dataBankCard.add(unit);
		}
		
		tableBankCard.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableBankCard.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("BankCard", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableBankCard.setItems(dataBankCard);
		allObjectTables.put("BankCard", tableBankCard);
		
		TableView<Map<String, String>> tableUser = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableUser_UserID = new TableColumn<Map<String, String>, String>("UserID");
		tableUser_UserID.setMinWidth("UserID".length()*10);
		tableUser_UserID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("UserID"));
		    }
		});	
		tableUser.getColumns().add(tableUser_UserID);
		TableColumn<Map<String, String>, String> tableUser_Name = new TableColumn<Map<String, String>, String>("Name");
		tableUser_Name.setMinWidth("Name".length()*10);
		tableUser_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableUser.getColumns().add(tableUser_Name);
		TableColumn<Map<String, String>, String> tableUser_Address = new TableColumn<Map<String, String>, String>("Address");
		tableUser_Address.setMinWidth("Address".length()*10);
		tableUser_Address.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Address"));
		    }
		});	
		tableUser.getColumns().add(tableUser_Address);
		
		//table data
		ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
		List<User> rsUser = EntityManager.getAllInstancesOf("User");
		for (User r : rsUser) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("UserID", String.valueOf(r.getUserID()));
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");
			if (r.getAddress() != null)
				unit.put("Address", String.valueOf(r.getAddress()));
			else
				unit.put("Address", "");

			dataUser.add(unit);
		}
		
		tableUser.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableUser.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("User", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableUser.setItems(dataUser);
		allObjectTables.put("User", tableUser);
		

		
	}
	
	/* 
	* update all object tables with sub dataset
	*/ 
	public void updateBankCardTable(List<BankCard> rsBankCard) {
			ObservableList<Map<String, String>> dataBankCard = FXCollections.observableArrayList();
			for (BankCard r : rsBankCard) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("CardID", String.valueOf(r.getCardID()));
				unit.put("CardStatus", String.valueOf(r.getCardStatus()));
				unit.put("Catalog", String.valueOf(r.getCatalog()));
				unit.put("Password", String.valueOf(r.getPassword()));
				unit.put("Balance", String.valueOf(r.getBalance()));
				dataBankCard.add(unit);
			}
			
			allObjectTables.get("BankCard").setItems(dataBankCard);
	}
	public void updateUserTable(List<User> rsUser) {
			ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
			for (User r : rsUser) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("UserID", String.valueOf(r.getUserID()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				if (r.getAddress() != null)
					unit.put("Address", String.valueOf(r.getAddress()));
				else
					unit.put("Address", "");
				dataUser.add(unit);
			}
			
			allObjectTables.get("User").setItems(dataUser);
	}
	
	/* 
	* update all object tables
	*/ 
	public void updateBankCardTable() {
			ObservableList<Map<String, String>> dataBankCard = FXCollections.observableArrayList();
			List<BankCard> rsBankCard = EntityManager.getAllInstancesOf("BankCard");
			for (BankCard r : rsBankCard) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("CardID", String.valueOf(r.getCardID()));
				unit.put("CardStatus", String.valueOf(r.getCardStatus()));
				unit.put("Catalog", String.valueOf(r.getCatalog()));
				unit.put("Password", String.valueOf(r.getPassword()));
				unit.put("Balance", String.valueOf(r.getBalance()));
				dataBankCard.add(unit);
			}
			
			allObjectTables.get("BankCard").setItems(dataBankCard);
	}
	public void updateUserTable() {
			ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
			List<User> rsUser = EntityManager.getAllInstancesOf("User");
			for (User r : rsUser) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("UserID", String.valueOf(r.getUserID()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				if (r.getAddress() != null)
					unit.put("Address", String.valueOf(r.getAddress()));
				else
					unit.put("Address", "");
				dataUser.add(unit);
			}
			
			allObjectTables.get("User").setItems(dataUser);
	}
	
	public void classStatisicBingding() {
	
		 classInfodata = FXCollections.observableArrayList();
	 	 bankcard = new ClassInfo("BankCard", EntityManager.getAllInstancesOf("BankCard").size());
	 	 classInfodata.add(bankcard);
	 	 user = new ClassInfo("User", EntityManager.getAllInstancesOf("User").size());
	 	 classInfodata.add(user);
	 	 
		 class_statisic.setItems(classInfodata);
		 
		 //Class Statisic Binding
		 class_statisic.getSelectionModel().selectedItemProperty().addListener(
				 (observable, oldValue, newValue) ->  { 
				 										 //no selected object in table
				 										 objectindex = -1;
				 										 
				 										 //get lastest data, reflect updateTableData method
				 										 try {
												 			 Method updateob = this.getClass().getMethod("update" + newValue.getName() + "Table", null);
												 			 updateob.invoke(this);			 
												 		 } catch (Exception e) {
												 		 	 e.printStackTrace();
												 		 }		 										 
				 	
				 										 //show object table
				 			 				 			 TableView obs = allObjectTables.get(newValue.getName());
				 			 				 			 if (obs != null) {
				 			 				 				object_statics.setContent(obs);
				 			 				 				object_statics.setText("All Objects " + newValue.getName() + ":");
				 			 				 			 }
				 			 				 			 
				 			 				 			 //update association information
							 			 				 updateAssociation(newValue.getName());
				 			 				 			 
				 			 				 			 //show association information
				 			 				 			 ObservableList<AssociationInfo> asso = allassociationData.get(newValue.getName());
				 			 				 			 if (asso != null) {
				 			 				 			 	association_statisic.setItems(asso);
				 			 				 			 }
				 			 				 		  });
	}
	
	public void classStatisticUpdate() {
	 	 bankcard.setNumber(EntityManager.getAllInstancesOf("BankCard").size());
	 	 user.setNumber(EntityManager.getAllInstancesOf("User").size());
		
	}
	
	/**
	 * association binding
	 */
	public void associationStatisicBingding() {
		
		allassociationData = new HashMap<String, ObservableList<AssociationInfo>>();
		
		ObservableList<AssociationInfo> BankCard_association_data = FXCollections.observableArrayList();
		AssociationInfo BankCard_associatition_BelongedUser = new AssociationInfo("BankCard", "User", "BelongedUser", false);
		BankCard_association_data.add(BankCard_associatition_BelongedUser);
		
		allassociationData.put("BankCard", BankCard_association_data);
		
		ObservableList<AssociationInfo> User_association_data = FXCollections.observableArrayList();
		AssociationInfo User_associatition_OwnedCard = new AssociationInfo("User", "BankCard", "OwnedCard", true);
		User_association_data.add(User_associatition_OwnedCard);
		
		allassociationData.put("User", User_association_data);
		
		
		association_statisic.getSelectionModel().selectedItemProperty().addListener(
			    (observable, oldValue, newValue) ->  { 
	
							 		if (newValue != null) {
							 			 try {
							 			 	 if (newValue.getNumber() != 0) {
								 				 //choose object or not
								 				 if (objectindex != -1) {
									 				 Class[] cArg = new Class[1];
									 				 cArg[0] = List.class;
									 				 //reflect updateTableData method
										 			 Method updateob = this.getClass().getMethod("update" + newValue.getTargetClass() + "Table", cArg);
										 			 //find choosen object
										 			 Object selectedob = EntityManager.getAllInstancesOf(newValue.getSourceClass()).get(objectindex);
										 			 //reflect find association method
										 			 Method getAssociatedObject = selectedob.getClass().getMethod("get" + newValue.getAssociationName());
										 			 List r = new LinkedList();
										 			 //one or mulity?
										 			 if (newValue.getIsMultiple() == true) {
											 			 
											 			r = (List) getAssociatedObject.invoke(selectedob);
										 			 }
										 			 else {
										 				r.add(getAssociatedObject.invoke(selectedob));
										 			 }
										 			 //invoke update method
										 			 updateob.invoke(this, r);
										 			  
										 			 
								 				 }
												 //bind updated data to GUI
					 				 			 TableView obs = allObjectTables.get(newValue.getTargetClass());
					 				 			 if (obs != null) {
					 				 				object_statics.setContent(obs);
					 				 				object_statics.setText("Targets Objects " + newValue.getTargetClass() + ":");
					 				 			 }
					 				 		 }
							 			 }
							 			 catch (Exception e) {
							 				 e.printStackTrace();
							 			 }
							 		}
					 		  });
		
	}	
	
	

    //prepare data for contract
	public void prepareData() {
		
		//definition map
		definitions_map = new HashMap<String, String>();
		definitions_map.put("inputCard", "bc:BankCard = BankCard.allInstance()->any(c:BankCard | c.CardID = cardid)\r\r\n");
		definitions_map.put("createBankCard", "bankcard:BankCard = BankCard.allInstance()->any(ban:BankCard | ban.CardID = cardid)\r\r\n");
		definitions_map.put("queryBankCard", "bankcard:BankCard = BankCard.allInstance()->any(ban:BankCard | ban.CardID = cardid)\r\r\n");
		definitions_map.put("modifyBankCard", "bankcard:BankCard = BankCard.allInstance()->any(ban:BankCard | ban.CardID = cardid)\r\r\n");
		definitions_map.put("deleteBankCard", "bankcard:BankCard = BankCard.allInstance()->any(ban:BankCard | ban.CardID = cardid)\r\r\n");
		definitions_map.put("createUser", "user:User = User.allInstance()->any(use:User | use.UserID = userid)\r\r\n");
		definitions_map.put("queryUser", "user:User = User.allInstance()->any(use:User | use.UserID = userid)\r\r\n");
		definitions_map.put("modifyUser", "user:User = User.allInstance()->any(use:User | use.UserID = userid)\r\r\n");
		definitions_map.put("deleteUser", "user:User = User.allInstance()->any(use:User | use.UserID = userid)\r\r\n");
		
		//precondition map
		preconditions_map = new HashMap<String, String>();
		preconditions_map.put("inputCard", "true");
		preconditions_map.put("inputPassword", "self.CardIDValidated = true and\nself.InputCard.oclIsUndefined() = false\n");
		preconditions_map.put("printReceipt", "self.CardIDValidated = true and\nself.PasswordValidated = true and\nself.InputCard.oclIsUndefined() = false\n");
		preconditions_map.put("checkBalance", "self.PasswordValidated = true and\nself.CardIDValidated = true and\nself.InputCard.oclIsUndefined() = false\n");
		preconditions_map.put("ejectCard", "self.PasswordValidated = true and\nself.CardIDValidated = true and\nself.InputCard.oclIsUndefined() = false\n");
		preconditions_map.put("withdrawCash", "self.PasswordValidated = true and\nself.CardIDValidated = true and\nself.InputCard.oclIsUndefined() = false and\nself.InputCard.Balance >= quantity\n");
		preconditions_map.put("depositFunds", "self.PasswordValidated = true and\nself.CardIDValidated = true and\nself.InputCard.oclIsUndefined() = false and\nquantity >= 100\n");
		preconditions_map.put("createBankCard", "bankcard.oclIsUndefined() = true");
		preconditions_map.put("queryBankCard", "bankcard.oclIsUndefined() = false");
		preconditions_map.put("modifyBankCard", "bankcard.oclIsUndefined() = false");
		preconditions_map.put("deleteBankCard", "bankcard.oclIsUndefined() = false and\nBankCard.allInstance()->includes(bankcard)\n");
		preconditions_map.put("createUser", "user.oclIsUndefined() = true");
		preconditions_map.put("queryUser", "user.oclIsUndefined() = false");
		preconditions_map.put("modifyUser", "user.oclIsUndefined() = false");
		preconditions_map.put("deleteUser", "user.oclIsUndefined() = false and\nUser.allInstance()->includes(user)\n");
		preconditions_map.put("cardIdentification", "true");
		
		//postcondition map
		postconditions_map = new HashMap<String, String>();
		postconditions_map.put("inputCard", "if(bc.oclIsUndefined() = false)\nthen\nself.CardIDValidated = true and\nself.InputCard = bc and\nresult = true\nelse\nself.CardIDValidated = false and\nresult = false\nendif");
		postconditions_map.put("inputPassword", "ifself.InputCard.Password = password\nthen\nself.PasswordValidated = true and\nreturn = true\nelse\nself.PasswordValidated = false and\nreturn = false\nendif");
		postconditions_map.put("printReceipt", "ifself.IsWithdraw = true\nthen\nresult = self.WithdrawedNumber\nelse\nif\nself.IsDeposit = true\nthen\nresult = self.DepositedNumber\nelse\nresult = 0\nendif\nendif");
		postconditions_map.put("checkBalance", "result = self.InputCard.Balance");
		postconditions_map.put("ejectCard", "self.InputCard = null and\nself.PasswordValidated = false and\nself.CardIDValidated = false and\nself.IsWithdraw = false and\nself.IsDeposit = false and\nself.WithdrawedNumber = 0 and\nself.DepositedNumber = 0 and\nresult = true\n");
		postconditions_map.put("withdrawCash", "self.InputCard.Balance = self.InputCard.Balance@pre - quantity and\nself.WithdrawedNumber = quantity and\nself.IsWithdraw = true and\nresult = true\n");
		postconditions_map.put("depositFunds", "self.InputCard.Balance = self.InputCard.Balance@pre + quantity and\nself.IsDeposit = true and\nself.DepositedNumber = quantity and\nresult = true\n");
		postconditions_map.put("createBankCard", "let ban:BankCard inban.oclIsNew() and\nban.CardID = cardid and\nban.CardStatus = cardstatus and\nban.Catalog = catalog and\nban.Password = password and\nban.Balance = balance and\nBankCard.allInstance()->includes(ban) and\nresult = true\n");
		postconditions_map.put("queryBankCard", "result = bankcard");
		postconditions_map.put("modifyBankCard", "bankcard.CardID = cardid and\nbankcard.CardStatus = cardstatus and\nbankcard.Catalog = catalog and\nbankcard.Password = password and\nbankcard.Balance = balance and\nresult = true\n");
		postconditions_map.put("deleteBankCard", "BankCard.allInstance()->excludes(bankcard) and\nresult = true\n");
		postconditions_map.put("createUser", "let use:User inuse.oclIsNew() and\nuse.UserID = userid and\nuse.Name = name and\nuse.Address = address and\nUser.allInstance()->includes(use) and\nresult = true\n");
		postconditions_map.put("queryUser", "result = user");
		postconditions_map.put("modifyUser", "user.UserID = userid and\nuser.Name = name and\nuser.Address = address and\nresult = true\n");
		postconditions_map.put("deleteUser", "User.allInstance()->excludes(user) and\nresult = true\n");
		postconditions_map.put("cardIdentification", "result = true");
		
		//service invariants map
		service_invariants_map = new LinkedHashMap<String, String>();
		
		//entity invariants map
		entity_invariants_map = new LinkedHashMap<String, String>();
		entity_invariants_map.put("BankCard_UniqueCardID"," BankCard . allInstance() -> isUnique ( b : BankCard | b . CardID )");
		entity_invariants_map.put("BankCard_BalanceGreatAndEqualZero"," Balance >= 0");
		entity_invariants_map.put("User_UniqueUserID"," User . allInstance() -> isUnique ( u : User | u . UserID )");
		
	}
	
	public void generatOperationPane() {
		
		 operationPanels = new LinkedHashMap<String, GridPane>();
		
		 // ==================== GridPane_inputCard ====================
		 GridPane inputCard = new GridPane();
		 inputCard.setHgap(4);
		 inputCard.setVgap(6);
		 inputCard.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> inputCard_content = inputCard.getChildren();
		 Label inputCard_cardid_label = new Label("cardid:");
		 inputCard_cardid_label.setMinWidth(Region.USE_PREF_SIZE);
		 inputCard_content.add(inputCard_cardid_label);
		 GridPane.setConstraints(inputCard_cardid_label, 0, 0);
		 
		 inputCard_cardid_t = new TextField();
		 inputCard_content.add(inputCard_cardid_t);
		 inputCard_cardid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(inputCard_cardid_t, 1, 0);
		 operationPanels.put("inputCard", inputCard);
		 
		 // ==================== GridPane_inputPassword ====================
		 GridPane inputPassword = new GridPane();
		 inputPassword.setHgap(4);
		 inputPassword.setVgap(6);
		 inputPassword.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> inputPassword_content = inputPassword.getChildren();
		 Label inputPassword_password_label = new Label("password:");
		 inputPassword_password_label.setMinWidth(Region.USE_PREF_SIZE);
		 inputPassword_content.add(inputPassword_password_label);
		 GridPane.setConstraints(inputPassword_password_label, 0, 0);
		 
		 inputPassword_password_t = new TextField();
		 inputPassword_content.add(inputPassword_password_t);
		 inputPassword_password_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(inputPassword_password_t, 1, 0);
		 operationPanels.put("inputPassword", inputPassword);
		 
		 // ==================== GridPane_printReceipt ====================
		 GridPane printReceipt = new GridPane();
		 printReceipt.setHgap(4);
		 printReceipt.setVgap(6);
		 printReceipt.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> printReceipt_content = printReceipt.getChildren();
		 Label printReceipt_label = new Label("This operation is no intput parameters..");
		 printReceipt_label.setMinWidth(Region.USE_PREF_SIZE);
		 printReceipt_content.add(printReceipt_label);
		 GridPane.setConstraints(printReceipt_label, 0, 0);
		 operationPanels.put("printReceipt", printReceipt);
		 
		 // ==================== GridPane_checkBalance ====================
		 GridPane checkBalance = new GridPane();
		 checkBalance.setHgap(4);
		 checkBalance.setVgap(6);
		 checkBalance.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> checkBalance_content = checkBalance.getChildren();
		 Label checkBalance_label = new Label("This operation is no intput parameters..");
		 checkBalance_label.setMinWidth(Region.USE_PREF_SIZE);
		 checkBalance_content.add(checkBalance_label);
		 GridPane.setConstraints(checkBalance_label, 0, 0);
		 operationPanels.put("checkBalance", checkBalance);
		 
		 // ==================== GridPane_ejectCard ====================
		 GridPane ejectCard = new GridPane();
		 ejectCard.setHgap(4);
		 ejectCard.setVgap(6);
		 ejectCard.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> ejectCard_content = ejectCard.getChildren();
		 Label ejectCard_label = new Label("This operation is no intput parameters..");
		 ejectCard_label.setMinWidth(Region.USE_PREF_SIZE);
		 ejectCard_content.add(ejectCard_label);
		 GridPane.setConstraints(ejectCard_label, 0, 0);
		 operationPanels.put("ejectCard", ejectCard);
		 
		 // ==================== GridPane_withdrawCash ====================
		 GridPane withdrawCash = new GridPane();
		 withdrawCash.setHgap(4);
		 withdrawCash.setVgap(6);
		 withdrawCash.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> withdrawCash_content = withdrawCash.getChildren();
		 Label withdrawCash_quantity_label = new Label("quantity:");
		 withdrawCash_quantity_label.setMinWidth(Region.USE_PREF_SIZE);
		 withdrawCash_content.add(withdrawCash_quantity_label);
		 GridPane.setConstraints(withdrawCash_quantity_label, 0, 0);
		 
		 withdrawCash_quantity_t = new TextField();
		 withdrawCash_content.add(withdrawCash_quantity_t);
		 withdrawCash_quantity_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(withdrawCash_quantity_t, 1, 0);
		 operationPanels.put("withdrawCash", withdrawCash);
		 
		 // ==================== GridPane_depositFunds ====================
		 GridPane depositFunds = new GridPane();
		 depositFunds.setHgap(4);
		 depositFunds.setVgap(6);
		 depositFunds.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> depositFunds_content = depositFunds.getChildren();
		 Label depositFunds_quantity_label = new Label("quantity:");
		 depositFunds_quantity_label.setMinWidth(Region.USE_PREF_SIZE);
		 depositFunds_content.add(depositFunds_quantity_label);
		 GridPane.setConstraints(depositFunds_quantity_label, 0, 0);
		 
		 depositFunds_quantity_t = new TextField();
		 depositFunds_content.add(depositFunds_quantity_t);
		 depositFunds_quantity_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(depositFunds_quantity_t, 1, 0);
		 operationPanels.put("depositFunds", depositFunds);
		 
		 // ==================== GridPane_createBankCard ====================
		 GridPane createBankCard = new GridPane();
		 createBankCard.setHgap(4);
		 createBankCard.setVgap(6);
		 createBankCard.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> createBankCard_content = createBankCard.getChildren();
		 Label createBankCard_cardid_label = new Label("cardid:");
		 createBankCard_cardid_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBankCard_content.add(createBankCard_cardid_label);
		 GridPane.setConstraints(createBankCard_cardid_label, 0, 0);
		 
		 createBankCard_cardid_t = new TextField();
		 createBankCard_content.add(createBankCard_cardid_t);
		 createBankCard_cardid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createBankCard_cardid_t, 1, 0);
		 Label createBankCard_cardstatus_label = new Label("cardstatus:");
		 createBankCard_cardstatus_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBankCard_content.add(createBankCard_cardstatus_label);
		 GridPane.setConstraints(createBankCard_cardstatus_label, 0, 1);
		 
		 createBankCard_cardstatus_cb = new ChoiceBox();
createBankCard_cardstatus_cb.getItems().add("NORMAL");
createBankCard_cardstatus_cb.getItems().add("SUSPEND");
createBankCard_cardstatus_cb.getItems().add("CANNEL");
		 createBankCard_cardstatus_cb.getSelectionModel().selectFirst();
		 createBankCard_content.add(createBankCard_cardstatus_cb);
		 GridPane.setConstraints(createBankCard_cardstatus_cb, 1, 1);
		 Label createBankCard_catalog_label = new Label("catalog:");
		 createBankCard_catalog_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBankCard_content.add(createBankCard_catalog_label);
		 GridPane.setConstraints(createBankCard_catalog_label, 0, 2);
		 
		 createBankCard_catalog_cb = new ChoiceBox();
createBankCard_catalog_cb.getItems().add("CREDIT");
createBankCard_catalog_cb.getItems().add("DESPOSIT");
		 createBankCard_catalog_cb.getSelectionModel().selectFirst();
		 createBankCard_content.add(createBankCard_catalog_cb);
		 GridPane.setConstraints(createBankCard_catalog_cb, 1, 2);
		 Label createBankCard_password_label = new Label("password:");
		 createBankCard_password_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBankCard_content.add(createBankCard_password_label);
		 GridPane.setConstraints(createBankCard_password_label, 0, 3);
		 
		 createBankCard_password_t = new TextField();
		 createBankCard_content.add(createBankCard_password_t);
		 createBankCard_password_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createBankCard_password_t, 1, 3);
		 Label createBankCard_balance_label = new Label("balance:");
		 createBankCard_balance_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBankCard_content.add(createBankCard_balance_label);
		 GridPane.setConstraints(createBankCard_balance_label, 0, 4);
		 
		 createBankCard_balance_t = new TextField();
		 createBankCard_content.add(createBankCard_balance_t);
		 createBankCard_balance_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createBankCard_balance_t, 1, 4);
		 operationPanels.put("createBankCard", createBankCard);
		 
		 // ==================== GridPane_queryBankCard ====================
		 GridPane queryBankCard = new GridPane();
		 queryBankCard.setHgap(4);
		 queryBankCard.setVgap(6);
		 queryBankCard.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> queryBankCard_content = queryBankCard.getChildren();
		 Label queryBankCard_cardid_label = new Label("cardid:");
		 queryBankCard_cardid_label.setMinWidth(Region.USE_PREF_SIZE);
		 queryBankCard_content.add(queryBankCard_cardid_label);
		 GridPane.setConstraints(queryBankCard_cardid_label, 0, 0);
		 
		 queryBankCard_cardid_t = new TextField();
		 queryBankCard_content.add(queryBankCard_cardid_t);
		 queryBankCard_cardid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(queryBankCard_cardid_t, 1, 0);
		 operationPanels.put("queryBankCard", queryBankCard);
		 
		 // ==================== GridPane_modifyBankCard ====================
		 GridPane modifyBankCard = new GridPane();
		 modifyBankCard.setHgap(4);
		 modifyBankCard.setVgap(6);
		 modifyBankCard.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> modifyBankCard_content = modifyBankCard.getChildren();
		 Label modifyBankCard_cardid_label = new Label("cardid:");
		 modifyBankCard_cardid_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBankCard_content.add(modifyBankCard_cardid_label);
		 GridPane.setConstraints(modifyBankCard_cardid_label, 0, 0);
		 
		 modifyBankCard_cardid_t = new TextField();
		 modifyBankCard_content.add(modifyBankCard_cardid_t);
		 modifyBankCard_cardid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyBankCard_cardid_t, 1, 0);
		 Label modifyBankCard_cardstatus_label = new Label("cardstatus:");
		 modifyBankCard_cardstatus_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBankCard_content.add(modifyBankCard_cardstatus_label);
		 GridPane.setConstraints(modifyBankCard_cardstatus_label, 0, 1);
		 
		 modifyBankCard_cardstatus_cb = new ChoiceBox();
modifyBankCard_cardstatus_cb.getItems().add("NORMAL");
modifyBankCard_cardstatus_cb.getItems().add("SUSPEND");
modifyBankCard_cardstatus_cb.getItems().add("CANNEL");
		 modifyBankCard_cardstatus_cb.getSelectionModel().selectFirst();
		 modifyBankCard_content.add(modifyBankCard_cardstatus_cb);
		 GridPane.setConstraints(modifyBankCard_cardstatus_cb, 1, 1);
		 Label modifyBankCard_catalog_label = new Label("catalog:");
		 modifyBankCard_catalog_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBankCard_content.add(modifyBankCard_catalog_label);
		 GridPane.setConstraints(modifyBankCard_catalog_label, 0, 2);
		 
		 modifyBankCard_catalog_cb = new ChoiceBox();
modifyBankCard_catalog_cb.getItems().add("CREDIT");
modifyBankCard_catalog_cb.getItems().add("DESPOSIT");
		 modifyBankCard_catalog_cb.getSelectionModel().selectFirst();
		 modifyBankCard_content.add(modifyBankCard_catalog_cb);
		 GridPane.setConstraints(modifyBankCard_catalog_cb, 1, 2);
		 Label modifyBankCard_password_label = new Label("password:");
		 modifyBankCard_password_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBankCard_content.add(modifyBankCard_password_label);
		 GridPane.setConstraints(modifyBankCard_password_label, 0, 3);
		 
		 modifyBankCard_password_t = new TextField();
		 modifyBankCard_content.add(modifyBankCard_password_t);
		 modifyBankCard_password_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyBankCard_password_t, 1, 3);
		 Label modifyBankCard_balance_label = new Label("balance:");
		 modifyBankCard_balance_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBankCard_content.add(modifyBankCard_balance_label);
		 GridPane.setConstraints(modifyBankCard_balance_label, 0, 4);
		 
		 modifyBankCard_balance_t = new TextField();
		 modifyBankCard_content.add(modifyBankCard_balance_t);
		 modifyBankCard_balance_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyBankCard_balance_t, 1, 4);
		 operationPanels.put("modifyBankCard", modifyBankCard);
		 
		 // ==================== GridPane_deleteBankCard ====================
		 GridPane deleteBankCard = new GridPane();
		 deleteBankCard.setHgap(4);
		 deleteBankCard.setVgap(6);
		 deleteBankCard.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> deleteBankCard_content = deleteBankCard.getChildren();
		 Label deleteBankCard_cardid_label = new Label("cardid:");
		 deleteBankCard_cardid_label.setMinWidth(Region.USE_PREF_SIZE);
		 deleteBankCard_content.add(deleteBankCard_cardid_label);
		 GridPane.setConstraints(deleteBankCard_cardid_label, 0, 0);
		 
		 deleteBankCard_cardid_t = new TextField();
		 deleteBankCard_content.add(deleteBankCard_cardid_t);
		 deleteBankCard_cardid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(deleteBankCard_cardid_t, 1, 0);
		 operationPanels.put("deleteBankCard", deleteBankCard);
		 
		 // ==================== GridPane_createUser ====================
		 GridPane createUser = new GridPane();
		 createUser.setHgap(4);
		 createUser.setVgap(6);
		 createUser.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> createUser_content = createUser.getChildren();
		 Label createUser_userid_label = new Label("userid:");
		 createUser_userid_label.setMinWidth(Region.USE_PREF_SIZE);
		 createUser_content.add(createUser_userid_label);
		 GridPane.setConstraints(createUser_userid_label, 0, 0);
		 
		 createUser_userid_t = new TextField();
		 createUser_content.add(createUser_userid_t);
		 createUser_userid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createUser_userid_t, 1, 0);
		 Label createUser_name_label = new Label("name:");
		 createUser_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 createUser_content.add(createUser_name_label);
		 GridPane.setConstraints(createUser_name_label, 0, 1);
		 
		 createUser_name_t = new TextField();
		 createUser_content.add(createUser_name_t);
		 createUser_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createUser_name_t, 1, 1);
		 Label createUser_address_label = new Label("address:");
		 createUser_address_label.setMinWidth(Region.USE_PREF_SIZE);
		 createUser_content.add(createUser_address_label);
		 GridPane.setConstraints(createUser_address_label, 0, 2);
		 
		 createUser_address_t = new TextField();
		 createUser_content.add(createUser_address_t);
		 createUser_address_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createUser_address_t, 1, 2);
		 operationPanels.put("createUser", createUser);
		 
		 // ==================== GridPane_queryUser ====================
		 GridPane queryUser = new GridPane();
		 queryUser.setHgap(4);
		 queryUser.setVgap(6);
		 queryUser.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> queryUser_content = queryUser.getChildren();
		 Label queryUser_userid_label = new Label("userid:");
		 queryUser_userid_label.setMinWidth(Region.USE_PREF_SIZE);
		 queryUser_content.add(queryUser_userid_label);
		 GridPane.setConstraints(queryUser_userid_label, 0, 0);
		 
		 queryUser_userid_t = new TextField();
		 queryUser_content.add(queryUser_userid_t);
		 queryUser_userid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(queryUser_userid_t, 1, 0);
		 operationPanels.put("queryUser", queryUser);
		 
		 // ==================== GridPane_modifyUser ====================
		 GridPane modifyUser = new GridPane();
		 modifyUser.setHgap(4);
		 modifyUser.setVgap(6);
		 modifyUser.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> modifyUser_content = modifyUser.getChildren();
		 Label modifyUser_userid_label = new Label("userid:");
		 modifyUser_userid_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyUser_content.add(modifyUser_userid_label);
		 GridPane.setConstraints(modifyUser_userid_label, 0, 0);
		 
		 modifyUser_userid_t = new TextField();
		 modifyUser_content.add(modifyUser_userid_t);
		 modifyUser_userid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyUser_userid_t, 1, 0);
		 Label modifyUser_name_label = new Label("name:");
		 modifyUser_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyUser_content.add(modifyUser_name_label);
		 GridPane.setConstraints(modifyUser_name_label, 0, 1);
		 
		 modifyUser_name_t = new TextField();
		 modifyUser_content.add(modifyUser_name_t);
		 modifyUser_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyUser_name_t, 1, 1);
		 Label modifyUser_address_label = new Label("address:");
		 modifyUser_address_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyUser_content.add(modifyUser_address_label);
		 GridPane.setConstraints(modifyUser_address_label, 0, 2);
		 
		 modifyUser_address_t = new TextField();
		 modifyUser_content.add(modifyUser_address_t);
		 modifyUser_address_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyUser_address_t, 1, 2);
		 operationPanels.put("modifyUser", modifyUser);
		 
		 // ==================== GridPane_deleteUser ====================
		 GridPane deleteUser = new GridPane();
		 deleteUser.setHgap(4);
		 deleteUser.setVgap(6);
		 deleteUser.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> deleteUser_content = deleteUser.getChildren();
		 Label deleteUser_userid_label = new Label("userid:");
		 deleteUser_userid_label.setMinWidth(Region.USE_PREF_SIZE);
		 deleteUser_content.add(deleteUser_userid_label);
		 GridPane.setConstraints(deleteUser_userid_label, 0, 0);
		 
		 deleteUser_userid_t = new TextField();
		 deleteUser_content.add(deleteUser_userid_t);
		 deleteUser_userid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(deleteUser_userid_t, 1, 0);
		 operationPanels.put("deleteUser", deleteUser);
		 
		 // ==================== GridPane_cardIdentification ====================
		 GridPane cardIdentification = new GridPane();
		 cardIdentification.setHgap(4);
		 cardIdentification.setVgap(6);
		 cardIdentification.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> cardIdentification_content = cardIdentification.getChildren();
		 Label cardIdentification_label = new Label("This operation is no intput parameters..");
		 cardIdentification_label.setMinWidth(Region.USE_PREF_SIZE);
		 cardIdentification_content.add(cardIdentification_label);
		 GridPane.setConstraints(cardIdentification_label, 0, 0);
		 operationPanels.put("cardIdentification", cardIdentification);
		 
	}	

	public void actorTreeViewBinding() {
		
		TreeItem<String> treeRootcustomer = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_withdrawCash = new TreeItem<String>("withdrawCash");
			subTreeRoot_withdrawCash.getChildren().addAll(Arrays.asList(
					
				 	new TreeItem<String>("inputCard"),
				 	new TreeItem<String>("inputPassword"),
				 	new TreeItem<String>("withdrawCash"),
				 	new TreeItem<String>("printReceipt"),
				 	new TreeItem<String>("ejectCard")
				 	));
			TreeItem<String> subTreeRoot_checkBalance = new TreeItem<String>("checkBalance");
			subTreeRoot_checkBalance.getChildren().addAll(Arrays.asList(
					
				 	new TreeItem<String>("inputCard"),
				 	new TreeItem<String>("inputPassword"),
				 	new TreeItem<String>("checkBalance"),
				 	new TreeItem<String>("ejectCard")
				 	));
			TreeItem<String> subTreeRoot_depositFunds = new TreeItem<String>("depositFunds");
			subTreeRoot_depositFunds.getChildren().addAll(Arrays.asList(
					
				 	new TreeItem<String>("inputCard"),
				 	new TreeItem<String>("inputPassword"),
				 	new TreeItem<String>("depositFunds"),
				 	new TreeItem<String>("printReceipt"),
				 	new TreeItem<String>("ejectCard")
				 	));
		
		treeRootcustomer.getChildren().addAll(Arrays.asList(
			new TreeItem<String>("withdrawCash"),
			new TreeItem<String>("checkBalance"),
			new TreeItem<String>("depositFunds")
			));
		
		treeRootcustomer.setExpanded(true);

		actor_treeview_customer.setShowRoot(false);
		actor_treeview_customer.setRoot(treeRootcustomer);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_customer.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootbankclerk = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_manageBankCard = new TreeItem<String>("manageBankCard");
			subTreeRoot_manageBankCard.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("createBankCard"),
					 	new TreeItem<String>("queryBankCard"),
					 	new TreeItem<String>("modifyBankCard"),
					 	new TreeItem<String>("deleteBankCard")
				 		));	
			TreeItem<String> subTreeRoot_manageUser = new TreeItem<String>("manageUser");
			subTreeRoot_manageUser.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("createUser"),
					 	new TreeItem<String>("queryUser"),
					 	new TreeItem<String>("modifyUser"),
					 	new TreeItem<String>("deleteUser")
				 		));	
		
		treeRootbankclerk.getChildren().addAll(Arrays.asList(
			subTreeRoot_manageBankCard,
			subTreeRoot_manageUser
			));
		
		treeRootbankclerk.setExpanded(true);

		actor_treeview_bankclerk.setShowRoot(false);
		actor_treeview_bankclerk.setRoot(treeRootbankclerk);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_bankclerk.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
	}

	/**
	*    Execute Operation
	*/
	@FXML
	public void execute(ActionEvent event) {
		
		switch (clickedOp) {
		case "inputCard" : inputCard(); break;
		case "inputPassword" : inputPassword(); break;
		case "printReceipt" : printReceipt(); break;
		case "checkBalance" : checkBalance(); break;
		case "ejectCard" : ejectCard(); break;
		case "withdrawCash" : withdrawCash(); break;
		case "depositFunds" : depositFunds(); break;
		case "createBankCard" : createBankCard(); break;
		case "queryBankCard" : queryBankCard(); break;
		case "modifyBankCard" : modifyBankCard(); break;
		case "deleteBankCard" : deleteBankCard(); break;
		case "createUser" : createUser(); break;
		case "queryUser" : queryUser(); break;
		case "modifyUser" : modifyUser(); break;
		case "deleteUser" : deleteUser(); break;
		case "cardIdentification" : cardIdentification(); break;
		
		}
		
		System.out.println("execute buttion clicked");
		
		//checking relevant invariants
		opInvairantPanelUpdate();
	}

	/**
	*    Refresh All
	*/		
	@FXML
	public void refresh(ActionEvent event) {
		
		refreshAll();
		System.out.println("refresh all");
	}		
	
	/**
	*    Save All
	*/			
	@FXML
	public void save(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save State to File");
		fileChooser.setInitialFileName("*.state");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showSaveDialog(stage);
		
		if (file != null) {
			System.out.println("save state to file " + file.getAbsolutePath());				
			EntityManager.save(file);
		}
	}
	
	/**
	*    Load All
	*/			
	@FXML
	public void load(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open State File");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showOpenDialog(stage);
		
		if (file != null) {
			System.out.println("choose file" + file.getAbsolutePath());
			EntityManager.load(file); 
		}
		
		//refresh GUI after load data
		refreshAll();
	}
	
	
	//precondition unsat dialog
	public void preconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Precondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}
	
	//postcondition unsat dialog
	public void postconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Postcondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}

	public void thirdpartyServiceUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("third party service is exception");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}		
	
	
	public void inputCard() {
		
		System.out.println("execute inputCard");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: inputCard in service: AutomatedTellerMachineSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(automatedtellermachinesystem_service.inputCard(
			Integer.valueOf(inputCard_cardid_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void inputPassword() {
		
		System.out.println("execute inputPassword");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: inputPassword in service: AutomatedTellerMachineSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(automatedtellermachinesystem_service.inputPassword(
			Integer.valueOf(inputPassword_password_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void printReceipt() {
		
		System.out.println("execute printReceipt");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: printReceipt in service: AutomatedTellerMachineSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(automatedtellermachinesystem_service.printReceipt(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void checkBalance() {
		
		System.out.println("execute checkBalance");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: checkBalance in service: AutomatedTellerMachineSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(automatedtellermachinesystem_service.checkBalance(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void ejectCard() {
		
		System.out.println("execute ejectCard");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: ejectCard in service: AutomatedTellerMachineSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(automatedtellermachinesystem_service.ejectCard(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void withdrawCash() {
		
		System.out.println("execute withdrawCash");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: withdrawCash in service: AutomatedTellerMachineSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(automatedtellermachinesystem_service.withdrawCash(
			Integer.valueOf(withdrawCash_quantity_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void depositFunds() {
		
		System.out.println("execute depositFunds");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: depositFunds in service: AutomatedTellerMachineSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(automatedtellermachinesystem_service.depositFunds(
			Float.valueOf(depositFunds_quantity_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void createBankCard() {
		
		System.out.println("execute createBankCard");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: createBankCard in service: ManageBankCardCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managebankcardcrudservice_service.createBankCard(
			Integer.valueOf(createBankCard_cardid_t.getText()),
			CardStatus.valueOf(createBankCard_cardstatus_cb.getSelectionModel().getSelectedItem().toString()),
			CardCatalog.valueOf(createBankCard_catalog_cb.getSelectionModel().getSelectedItem().toString()),
			Integer.valueOf(createBankCard_password_t.getText()),
			Float.valueOf(createBankCard_balance_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void queryBankCard() {
		
		System.out.println("execute queryBankCard");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: queryBankCard in service: ManageBankCardCRUDService ");
		
		try {
			//invoke op with parameters
				BankCard r = managebankcardcrudservice_service.queryBankCard(
				Integer.valueOf(queryBankCard_cardid_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableBankCard = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableBankCard_CardID = new TableColumn<Map<String, String>, String>("CardID");
				tableBankCard_CardID.setMinWidth("CardID".length()*10);
				tableBankCard_CardID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("CardID"));
				    }
				});	
				tableBankCard.getColumns().add(tableBankCard_CardID);
				TableColumn<Map<String, String>, String> tableBankCard_CardStatus = new TableColumn<Map<String, String>, String>("CardStatus");
				tableBankCard_CardStatus.setMinWidth("CardStatus".length()*10);
				tableBankCard_CardStatus.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("CardStatus"));
				    }
				});	
				tableBankCard.getColumns().add(tableBankCard_CardStatus);
				TableColumn<Map<String, String>, String> tableBankCard_Catalog = new TableColumn<Map<String, String>, String>("Catalog");
				tableBankCard_Catalog.setMinWidth("Catalog".length()*10);
				tableBankCard_Catalog.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Catalog"));
				    }
				});	
				tableBankCard.getColumns().add(tableBankCard_Catalog);
				TableColumn<Map<String, String>, String> tableBankCard_Password = new TableColumn<Map<String, String>, String>("Password");
				tableBankCard_Password.setMinWidth("Password".length()*10);
				tableBankCard_Password.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Password"));
				    }
				});	
				tableBankCard.getColumns().add(tableBankCard_Password);
				TableColumn<Map<String, String>, String> tableBankCard_Balance = new TableColumn<Map<String, String>, String>("Balance");
				tableBankCard_Balance.setMinWidth("Balance".length()*10);
				tableBankCard_Balance.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Balance"));
				    }
				});	
				tableBankCard.getColumns().add(tableBankCard_Balance);
				
				ObservableList<Map<String, String>> dataBankCard = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("CardID", String.valueOf(r.getCardID()));
					unit.put("CardStatus", String.valueOf(r.getCardStatus()));
					unit.put("Catalog", String.valueOf(r.getCatalog()));
					unit.put("Password", String.valueOf(r.getPassword()));
					unit.put("Balance", String.valueOf(r.getBalance()));
					dataBankCard.add(unit);
				
				
				tableBankCard.setItems(dataBankCard);
				operation_return_pane.setContent(tableBankCard);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void modifyBankCard() {
		
		System.out.println("execute modifyBankCard");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: modifyBankCard in service: ManageBankCardCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managebankcardcrudservice_service.modifyBankCard(
			Integer.valueOf(modifyBankCard_cardid_t.getText()),
			CardStatus.valueOf(modifyBankCard_cardstatus_cb.getSelectionModel().getSelectedItem().toString()),
			CardCatalog.valueOf(modifyBankCard_catalog_cb.getSelectionModel().getSelectedItem().toString()),
			Integer.valueOf(modifyBankCard_password_t.getText()),
			Float.valueOf(modifyBankCard_balance_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void deleteBankCard() {
		
		System.out.println("execute deleteBankCard");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: deleteBankCard in service: ManageBankCardCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managebankcardcrudservice_service.deleteBankCard(
			Integer.valueOf(deleteBankCard_cardid_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void createUser() {
		
		System.out.println("execute createUser");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: createUser in service: ManageUserCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageusercrudservice_service.createUser(
			Integer.valueOf(createUser_userid_t.getText()),
			createUser_name_t.getText(),
			createUser_address_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void queryUser() {
		
		System.out.println("execute queryUser");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: queryUser in service: ManageUserCRUDService ");
		
		try {
			//invoke op with parameters
				User r = manageusercrudservice_service.queryUser(
				Integer.valueOf(queryUser_userid_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableUser = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableUser_UserID = new TableColumn<Map<String, String>, String>("UserID");
				tableUser_UserID.setMinWidth("UserID".length()*10);
				tableUser_UserID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("UserID"));
				    }
				});	
				tableUser.getColumns().add(tableUser_UserID);
				TableColumn<Map<String, String>, String> tableUser_Name = new TableColumn<Map<String, String>, String>("Name");
				tableUser_Name.setMinWidth("Name".length()*10);
				tableUser_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
				    }
				});	
				tableUser.getColumns().add(tableUser_Name);
				TableColumn<Map<String, String>, String> tableUser_Address = new TableColumn<Map<String, String>, String>("Address");
				tableUser_Address.setMinWidth("Address".length()*10);
				tableUser_Address.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Address"));
				    }
				});	
				tableUser.getColumns().add(tableUser_Address);
				
				ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("UserID", String.valueOf(r.getUserID()));
					if (r.getName() != null)
						unit.put("Name", String.valueOf(r.getName()));
					else
						unit.put("Name", "");
					if (r.getAddress() != null)
						unit.put("Address", String.valueOf(r.getAddress()));
					else
						unit.put("Address", "");
					dataUser.add(unit);
				
				
				tableUser.setItems(dataUser);
				operation_return_pane.setContent(tableUser);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void modifyUser() {
		
		System.out.println("execute modifyUser");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: modifyUser in service: ManageUserCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageusercrudservice_service.modifyUser(
			Integer.valueOf(modifyUser_userid_t.getText()),
			modifyUser_name_t.getText(),
			modifyUser_address_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void deleteUser() {
		
		System.out.println("execute deleteUser");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: deleteUser in service: ManageUserCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageusercrudservice_service.deleteUser(
			Integer.valueOf(deleteUser_userid_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void cardIdentification() {
		
		System.out.println("execute cardIdentification");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: cardIdentification in service: AutomatedTellerMachineSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(automatedtellermachinesystem_service.cardIdentification(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}




	//select object index
	int objectindex;
	
	@FXML
	TabPane mainPane;

	@FXML
	TextArea log;
	
	@FXML
	TreeView<String> actor_treeview_customer;
	@FXML
	TreeView<String> actor_treeview_bankclerk;

	@FXML
	TextArea definition;
	@FXML
	TextArea precondition;
	@FXML
	TextArea postcondition;
	@FXML
	TextArea invariants;
	
	@FXML
	TitledPane precondition_pane;
	@FXML
	TitledPane postcondition_pane;
	
	//chosen operation textfields
	List<TextField> choosenOperation;
	String clickedOp;
		
	@FXML
	TableView<ClassInfo> class_statisic;
	@FXML
	TableView<AssociationInfo> association_statisic;
	
	Map<String, ObservableList<AssociationInfo>> allassociationData;
	ObservableList<ClassInfo> classInfodata;
	
	AutomatedTellerMachineSystem automatedtellermachinesystem_service;
	ManageBankCardCRUDService managebankcardcrudservice_service;
	ManageUserCRUDService manageusercrudservice_service;
	ThirdPartyServices thirdpartyservices_service;
	
	ClassInfo bankcard;
	ClassInfo user;
		
	@FXML
	TitledPane object_statics;
	Map<String, TableView> allObjectTables;
	
	@FXML
	TitledPane operation_paras;
	
	@FXML
	TitledPane operation_return_pane;
	
	@FXML
	TitledPane all_invariant_pane;
	
	@FXML
	TitledPane invariants_panes;
	
	Map<String, GridPane> operationPanels;
	Map<String, VBox> opInvariantPanel;
	
	//all textfiled or eumntity
	TextField inputCard_cardid_t;
	TextField inputPassword_password_t;
	TextField withdrawCash_quantity_t;
	TextField depositFunds_quantity_t;
	TextField createBankCard_cardid_t;
	ChoiceBox createBankCard_cardstatus_cb;
	ChoiceBox createBankCard_catalog_cb;
	TextField createBankCard_password_t;
	TextField createBankCard_balance_t;
	TextField queryBankCard_cardid_t;
	TextField modifyBankCard_cardid_t;
	ChoiceBox modifyBankCard_cardstatus_cb;
	ChoiceBox modifyBankCard_catalog_cb;
	TextField modifyBankCard_password_t;
	TextField modifyBankCard_balance_t;
	TextField deleteBankCard_cardid_t;
	TextField createUser_userid_t;
	TextField createUser_name_t;
	TextField createUser_address_t;
	TextField queryUser_userid_t;
	TextField modifyUser_userid_t;
	TextField modifyUser_name_t;
	TextField modifyUser_address_t;
	TextField deleteUser_userid_t;
	
	HashMap<String, String> definitions_map;
	HashMap<String, String> preconditions_map;
	HashMap<String, String> postconditions_map;
	HashMap<String, String> invariants_map;
	LinkedHashMap<String, String> service_invariants_map;
	LinkedHashMap<String, String> entity_invariants_map;
	LinkedHashMap<String, Label> service_invariants_label_map;
	LinkedHashMap<String, Label> entity_invariants_label_map;
	LinkedHashMap<String, Label> op_entity_invariants_label_map;
	LinkedHashMap<String, Label> op_service_invariants_label_map;
	

	
}
