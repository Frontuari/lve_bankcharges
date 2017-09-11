package org.frontuari.factories;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;
import org.frontuari.process.CreateCheckReturn;

public class FTUProcessFactory implements IProcessFactory {

	@Override
	public ProcessCall newProcessInstance(String className) {
		// TODO Auto-generated method stub
		
		if(className.equals("org.frontuari.process.CreateCheckReturn"))
			return new CreateCheckReturn();
		
		return null;
	}

}
