package org.frontuari.events;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MPayment;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.osgi.service.event.Event;

public class FTUEventHandler extends AbstractEventHandler{
	
	CLogger log = CLogger.getCLogger(FTUEventHandler.class);

	@Override
	protected void doHandleEvent(Event event) {
		// TODO Auto-generated method stub
		
		PO po = getPO(event);
		
		if(po instanceof MPayment){
			MPayment pay = (MPayment)po;
			log.warning("-------------Docno: "+pay.getDocumentNo()+ " Topic: "+event.getTopic());
		}
		
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		registerTableEvent(IEventTopics.PO_AFTER_CHANGE, MPayment.Table_Name);
	}

}
