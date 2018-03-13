package org.frontuari.factories;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;
import org.frontuari.process.BankTransfer;
import org.frontuari.process.CreateCheckReturn;

public class LVE_FrontuariProcessFactory implements IProcessFactory {

	@Override
	public ProcessCall newProcessInstance(String className) {
		// TODO Auto-generated method stub
		
		if(className.equals("org.frontuari.process.CreateCheckReturn"))
			return new CreateCheckReturn();
		
		if(className.equals("org.frontuari.process.BankTransfer"))
			return new BankTransfer();
		
		return null;
	}

}
