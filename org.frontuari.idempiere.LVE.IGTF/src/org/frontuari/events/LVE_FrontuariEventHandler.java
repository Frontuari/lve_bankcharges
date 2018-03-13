package org.frontuari.events;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBankAccount;
import org.compiere.model.MDocType;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MPayment;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.osgi.service.event.Event;
import org.frontuari.model.MLVEIGTF;
import org.frontuari.model.X_LVE_IGTF;
/**
 * Event Handler to Payment for create IGTF Trx
 * @author Jorge Colmenarez, 2017-09-16, jcolmenarez@frontari.com, Frontari, C.A. www.frontuari.com
 *
 */
public class LVE_FrontuariEventHandler extends AbstractEventHandler{
	
	CLogger log = CLogger.getCLogger(LVE_FrontuariEventHandler.class);

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
				MBankAccount ba = new MBankAccount(po.getCtx(), pay.getC_BankAccount_ID(), po.get_TrxName());
				//	Get Document Type from Payment Object
				MDocType dt = new MDocType(po.getCtx(),pay.getC_DocType_ID(),po.get_TrxName());
				//	Check if Org Trx is Special Tax Payer for generate IGTF
				if(oi.get_ValueAsBoolean("IsSpecialTaxPayer")){
					//	Not Reversal Document 
					if(pay.getReversal_ID()==0
							//	Not Bank Account Type Cash Journal
							&& !pay.getC_BankAccount().getBankAccountType().equalsIgnoreCase(MBankAccount.BANKACCOUNTTYPE_Cash) 
							//	Not Check Return Document
							&& !dt.get_ValueAsBoolean("IsCheckReturn")){
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
							MLVEIGTF igtf = new MLVEIGTF(po.getCtx(),IGTF_ID,po.get_TrxName());  
							String sql ="SELECT * FROM LVE_IGTF_Exception "
									+ "WHERE LVE_IGTF_ID = ? " 
									+ "AND ((IsProprietaryTransfer = 'Y' AND EXISTS (SELECT 1 FROM AD_OrgInfo oi,C_BPartner bp,LCO_TaxIdType tp "
									+ "WHERE (tp.LCO_TaxIdType_ID = bp.LCO_TaxIdType_ID OR bp.LCO_TaxIdType_ID IS NULL) AND (COALESCE(tp.Name,'')||bp.TaxID)=REPLACE(oi.TaxID,'-','') "
									+ "AND bp.C_BPartner_ID = ?)) OR IsProprietaryTransfer = 'N') "
									+ "AND ((C_BPartner_ID = ? AND IsProprietaryTransfer = 'N') OR C_BPartner_ID IS NULL) "
									+ "AND (C_Charge_ID = ? OR C_Charge_ID IS NULL) "
									+ "AND (TenderType = ? OR TenderType IS NULL) "
									+ "AND (C_Bank_ID = ? OR C_Bank_ID IS NULL) "
									+ "AND (C_DocType_ID = ? OR C_DocType_ID IS NULL) ";
							PreparedStatement pst = null;
							try {
								pst = DB.prepareStatement(sql, null);
								pst.setInt(1, igtf.getLVE_IGTF_ID());
								pst.setInt(2, pay.getC_BPartner_ID());
								pst.setInt(3, pay.getC_BPartner_ID());
								pst.setInt(4, pay.getC_Charge_ID());
								pst.setString(5, pay.getTenderType());
								pst.setInt(6, pay.getC_BankAccount().getC_Bank_ID());
								pst.setInt(7, pay.getC_DocType_ID());
								ResultSet rs = pst.executeQuery();
								if (rs.next()) {
									if(igtf.get_ValueAsBoolean("IsReceiptApply")){
										MPayment igtfPayment = new MPayment(po.getCtx(), 0, po.get_TrxName());
										po.copyValues(pay, igtfPayment);
										igtfPayment.setAD_Org_ID(pay.getAD_Org_ID());
										igtfPayment.setC_DocType_ID(igtf.getC_DocType_ID());
										if(igtfPayment.getDescription() != null){
											igtfPayment.addDescription(igtf.getValue());
										}
										else{
											igtfPayment.setDescription(igtf.getValue());
										}
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
									else{
										log.warning("@C_Payment_ID@ "+pay.getDocumentNo()+" @exempt@ @of@ @LVE_IGTF_ID@");
									}
								}
								else{
									if(igtf.get_ValueAsBoolean("IsPayApply")){
										MPayment igtfPayment = new MPayment(po.getCtx(), 0, po.get_TrxName());
										po.copyValues(pay, igtfPayment); 
										igtfPayment.setAD_Org_ID(pay.getAD_Org_ID());
										igtfPayment.setC_DocType_ID(igtf.getC_DocType_ID());
										if(igtfPayment.getDescription() != null){
											igtfPayment.addDescription(igtf.getValue());
										}
										else{
											igtfPayment.setDescription(igtf.getValue());
										}
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
									else{
										log.warning("@C_Payment_ID@ "+pay.getDocumentNo()+" @exempt@ @of@ @LVE_IGTF_ID@");
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
				//	Generate POS commission when the POS is closed
				if(pay.isReceipt() 
						&& ba.get_ValueAsBoolean("IsAssignedtoPOS") 
						&& pay.getTenderType().equals(ba.get_ValueAsString("TenderTypePOS"))){
					MPayment commissionPay = new MPayment(po.getCtx(), 0, po.get_TrxName());
					po.copyValues(pay, commissionPay);
					commissionPay.setIsReceipt(false);
					commissionPay.setC_DocType_ID(ba.get_ValueAsInt("C_DocType_ID"));
					commissionPay.setTenderType(ba.get_ValueAsString("TenderType"));
					commissionPay.setC_Charge_ID(ba.get_ValueAsInt("C_Charge_ID"));
					//	Calculate Commission Amt
					BigDecimal Commission = (BigDecimal)ba.get_Value("Commission");
					BigDecimal CommissionAmt = pay.getPayAmt().multiply(Commission.divide(new BigDecimal(100))).setScale(pay.getC_Currency().getStdPrecision(),BigDecimal.ROUND_HALF_UP);
					commissionPay.setPayAmt(CommissionAmt);
					commissionPay.saveEx();
					if(commissionPay.processIt(MPayment.ACTION_Complete)){
						commissionPay.saveEx();
						pay.setRef_Payment_ID(commissionPay.getC_Payment_ID());
						pay.saveEx();
					}
					else{
						throw new AdempiereException(commissionPay.getProcessMsg());
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
