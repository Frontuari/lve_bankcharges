package org.frontuari.events;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MPayment;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.osgi.service.event.Event;
import org.frontuari.model.MLVE_IGTF;
import org.frontuari.model.X_LVE_IGTF;
/**
 * Event Handler to Payment for create IGTF Trx
 * @author Jorge Colmenarez, 2017-09-16, jcolmenarez@frontari.com, Frontari, C.A. www.frontuari.com
 *
 */
public class FTUEventHandler extends AbstractEventHandler{
	
	CLogger log = CLogger.getCLogger(FTUEventHandler.class);

	@Override
	protected void initialize() {
		
		registerTableEvent(IEventTopics.DOC_AFTER_COMPLETE, MPayment.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSECORRECT, MPayment.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSEACCRUAL, MPayment.Table_Name);
	}

	@Override
	protected void doHandleEvent(Event event) {
		
		PO po = getPO(event);
		String type = event.getTopic();
		log.info(po.get_TableName() + " Type: " + type);
		//	Get events for create IGTF from Payment after complete
		if(po instanceof MPayment){
			if(type.equalsIgnoreCase(IEventTopics.DOC_AFTER_COMPLETE)){
				MPayment pay = (MPayment)po;
				MOrgInfo oi = MOrgInfo.get(po.getCtx(), pay.getAD_Org_ID(),po.get_TrxName());
				//	Check if Org Trx is Special Tax Payer
				if(oi.get_ValueAsBoolean("IsSpecialTaxPayer")){
					//	Not Reversal Document 
					if(pay.getReversal_ID()==0){
						//	Build Where Clause
						String whereclause = "ValidFrom <= ? ";
						if(pay.isReceipt()){
							whereclause = whereclause+"AND IsReceiptApply = 'Y' ";
						}else{
							whereclause = whereclause+"AND IsPayApply = 'Y' ";
						}
						//	Get last IGTF valid
						int IGTF_ID = new Query(po.getCtx(), X_LVE_IGTF.Table_Name, whereclause, po.get_TrxName())
								.setOnlyActiveRecords(true)
								.setParameters(pay.getDateTrx())
								.setOrderBy("ValidFrom DESC")
								.firstId();
						if(IGTF_ID > 0){
							MLVE_IGTF igtf = new MLVE_IGTF(po.getCtx(),IGTF_ID,po.get_TrxName());  
							String sql ="SELECT * FROM LVE_IGTF_Exception "
									+ "WHERE LVE_IGTF_ID = ? "
									+ "AND (C_BankAccountFrom_ID = ? OR C_BankAccountFrom_ID IS NULL) "
									+ "AND (C_BPartner_ID = ? OR C_BPartner_ID IS NULL) "
									+ "AND (C_Charge_ID = ? OR C_Charge_ID IS NULL) ";
							PreparedStatement pst = null;
							try {
								pst = DB.prepareStatement(sql, null);
								pst.setInt(1, igtf.getLVE_IGTF_ID());
								pst.setInt(2, pay.getC_BankAccount_ID());
								pst.setInt(3, pay.getC_BPartner_ID());
								pst.setInt(4, pay.getC_Charge_ID());
								ResultSet rs = pst.executeQuery();
								if (rs.next()) {
									log.warning("payment / receipt "+pay.getDocumentNo()+" exempt from tax on large financial transactions");
								}
								else{
									MPayment igtfPayment = new MPayment(po.getCtx(), 0, po.get_TrxName());
									po.copyValues(pay, igtfPayment);	
									igtfPayment.setC_DocType_ID(igtf.getC_DocType_ID());
									igtfPayment.setDescription(igtf.getValue());
									igtfPayment.setC_Charge_ID(igtf.getC_Charge_ID());
									igtfPayment.setTenderType(igtf.getTenderType());
									igtfPayment.setPayAmt(igtf.calculateTax(pay.getPayAmt()));
									igtfPayment.saveEx();
									if(igtfPayment.processIt(MPayment.ACTION_Complete)){
										igtfPayment.saveEx();
										pay.setRef_Payment_ID(igtfPayment.getC_Payment_ID());
										pay.saveEx();
									}
									else{
										throw new AdempiereException(igtfPayment.getProcessMsg());
									}
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
					    	finally{
					    		DB.close(pst);
					    		pst = null;
					    	}
						}
					}
				}
				
			}
			//	Get events for reverse IGTF from Payment after reverse correct or accrual
			else if(type.equalsIgnoreCase(IEventTopics.DOC_AFTER_REVERSECORRECT) 
					|| type.equalsIgnoreCase(IEventTopics.DOC_AFTER_REVERSEACCRUAL)){
				MPayment pay = (MPayment)po;
				//	Check if payment have allocated a IGFT
				if(pay.getRef_Payment_ID()!=0){
					MPayment igtfPayment = new MPayment(po.getCtx(), pay.getRef_Payment_ID(), po.get_TrxName());
					//	Reverse Correct or Reverse Accrual IGTF
					String DocAction = type.equalsIgnoreCase(IEventTopics.DOC_AFTER_REVERSECORRECT) ? MPayment.ACTION_Reverse_Correct : MPayment.ACTION_Reverse_Accrual;
					if(igtfPayment.processIt(DocAction)){
						igtfPayment.saveEx();
					}
					else{
						throw new AdempiereException(igtfPayment.getProcessMsg());
					}
				}
			}
		}
		
	}

}
