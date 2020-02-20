package cn.soa.service.impl.activity;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;

public class Test1 implements ExecutionListener  {

//	 private Expression fixedValue;
//
//	 private Expression dynamicValue;
	 private Expression type;

	 private Expression userVar;
	 
	 private Expression importVar;
	  
	@Override
	public void notify(DelegateExecution execution) throws Exception {	
		String key1  = (String) execution.getVariable("key1");
		System.out.println("-------key1-------");
		System.out.println(key1);
		String typeText  = type.getExpressionText();
		System.out.println("-------typeText-------");
		System.out.println(typeText);
		String userVarText  = userVar.getExpressionText();
		System.out.println("-------userVarText-------");
		System.out.println(userVarText);
		String typevalue  = type.getValue(execution).toString();
		System.out.println("-------typevalue-------");
		System.out.println(typevalue);
		String userVarvalue  = userVar.getValue(execution).toString();
		System.out.println("-------userVarvalue-------");
		System.out.println(userVarvalue);
		String importVarValue  = importVar.getValue(execution).toString();
		System.out.println("-------importVarValue-------");
		System.out.println(importVarValue);
		
		String role = "";
		if(importVarValue.equals("wo")) {
			role = "净化操作工";
		}else {
			role = "维修操作工";
		}
		
		execution.setVariable(userVarText, role);
	}

}
