package org.frontuari.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MCharge;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MPayment;
import org.compiere.model.MQuery;
import org.compiere.model.PrintInfo;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class CreateCheckReturn extends SvrProcess {
	private int p_C_BPartner_ID;
	private int p_Receipt_ID;
	private int p_C_BankAccount_ID;
	private int p_C_DocType_ID;
	private int p_C_DocTypeTarget_ID;
	private int p_C_Charge_ID;
	private Timestamp p_DateTrx;
	private Timestamp p_DateAcct;
	private boolean p_PrintDirect;
	private MInvoice debitNoteCHR;
	private MPayment pay;
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		ProcessInfoParameter[] params = getParameter();
		
		for (ProcessInfoParameter parameter : params) {
		       String name = parameter.getParameterName();
		       if (parameter.getParameter() == null)
		         continue;
		       if (name.equalsIgnoreCase("C_BPartner_ID"))
		         p_C_BPartner_ID = parameter.getParameterAsInt();
		       else if (name.equalsIgnoreCase("CheckNo"))
		         p_Receipt_ID = parameter.getParameterAsInt();
		       else if (name.equals("C_BankAccount_ID"))
		    	   p_C_BankAccount_ID = parameter.getParameterAsInt();
		       else if (name.equals("C_DocType_ID"))
		    	   p_C_DocType_ID = parameter.getParameterAsInt();
		       else if (name.equalsIgnoreCase("DateAcct"))
		    	   p_DateAcct = ((Timestamp)parameter.getParameter());
		       else if (name.equalsIgnoreCase("C_DocTypeTarget_ID"))
		    	   p_C_DocTypeTarget_ID = parameter.getParameterAsInt();
		       else if (name.equalsIgnoreCase("C_Charge_ID"))
		    	   p_C_Charge_ID = parameter.getParameterAsInt();
		       else if (name.equalsIgnoreCase("DateTrx"))
		    	   p_DateTrx = ((Timestamp)parameter.getParameter());
		       else if(name.equalsIgnoreCase("IsPrinted"))
		    	   p_PrintDirect = parameter.getParameterAsBoolean();
		       else
		         log.log(Level.SEVERE, "Unknown Parameter:" + name);
		     }
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		//	create a Payment
		createPayment(p_C_BankAccount_ID, p_C_BPartner_ID, p_Receipt_ID, p_C_DocType_ID, p_DateAcct);
		//	create Debit Note for Check Return
		createDebitNoteCHR(p_C_BPartner_ID, p_Receipt_ID, p_C_DocTypeTarget_ID, p_C_Charge_ID, p_DateTrx);
	    //	Set Value into Object pay for created Allocation
		createAssignment(p_DateTrx, debitNoteCHR, pay); 
		
		if(p_PrintDirect)
			printDocuments(debitNoteCHR.getC_Invoice_ID(), p_C_DocTypeTarget_ID);
		
		addLog(pay.getC_Payment_ID(),pay.getDateTrx(),pay.getPayAmt(),pay.getDocumentNo(),pay.get_Table_ID(),pay.getC_Payment_ID());
		addLog(debitNoteCHR.getC_Invoice_ID(), debitNoteCHR.getDateInvoiced(), debitNoteCHR.getGrandTotal(), debitNoteCHR.getDocumentNo(), debitNoteCHR.get_Table_ID(), debitNoteCHR.getC_Invoice_ID());
	   return debitNoteCHR.getDocumentNo();
	}
	
	public void createPayment(int BankAccount, int BPartner, int Receipt_ID, int DocType, Timestamp DateTrx) throws AdempiereUserError {
		
		MPayment receipt = new MPayment(getCtx(),Receipt_ID,get_TrxName());
		
		pay = new MPayment(getCtx(), 0, get_TrxName());
		pay.setDocumentNo(receipt.getCheckNo());
		pay.setCheckNo(receipt.getCheckNo());
		pay.setDateTrx(DateTrx);
		pay.setDateAcct(DateTrx);
		pay.setC_DocType_ID(DocType);
		pay.setC_BankAccount_ID(BankAccount);
		pay.setC_BPartner_ID(BPartner);
		pay.setC_Currency_ID(receipt.getC_Currency_ID());
		pay.setPayAmt(receipt.getPayAmt());
		pay.setTenderType("K");
		pay.saveEx(get_TrxName());
		
		if(pay.getPayAmt().compareTo(BigDecimal.ZERO)==0){
			rollback();
			throw new AdempiereException("@IsError@ @C_Payment_ID@: @Amt@ "+pay.getPayAmt()+" @NotValid@");
		}
		else{
			if(pay.processIt(MPayment.ACTION_Complete))
				pay.saveEx(get_TrxName());
			else{
				rollback();
				throw new AdempiereException(pay.getProcessMsg()); 
			}
		}
	}
	
	public void createDebitNoteCHR(int BPartner, int Receipt_ID, int DocType, int Charge, Timestamp DateTrx) throws AdempiereUserError {
		//	Create Debit Note
		debitNoteCHR = new MInvoice(getCtx(), 0, get_TrxName());
		debitNoteCHR.setC_BPartner_ID(BPartner);
		debitNoteCHR.setC_DocTypeTarget_ID(DocType);
		debitNoteCHR.setDateInvoiced(DateTrx);
		debitNoteCHR.setDateAcct(DateTrx);
		//	get Charge Object
		MCharge charge = new MCharge(getCtx(), Charge, get_TrxName());
		//	Get Invoices Affected
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COALESCE(al.C_Invoice_ID,p.C_Invoice_ID) AS C_Invoice_ID,al.Lines "
			+ "FROM C_Payment p "
			+ "LEFT JOIN (SELECT al.C_Invoice_ID,al.Amount,al.C_Payment_ID,cal.Lines FROM C_AllocationHdr ah "
			+ "INNER JOIN C_AllocationLine al ON al.C_AllocationHdr_ID = ah.C_AllocationHdr_ID "
			+ "INNER JOIN (SELECT C_AllocationHdr_ID,COUNT(C_AllocationLine_ID) AS Lines FROM C_AllocationLine "
			+ "GROUP BY 1) cal ON al.C_AllocationHdr_ID = cal.C_AllocationHdr_ID "
			+ "WHERE ah.DocStatus = 'CO') al ON p.C_Payment_ID = al.C_Payment_ID "
			+ "WHERE p.DocStatus = 'CO' AND p.TenderType IN ('K','A')"
			+ "AND p.C_Payment_ID = ? "); 
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql.toString(), get_TrxName());
			ps.setInt(1, Receipt_ID);
			rs = ps.executeQuery();
			while (rs.next())
			{
				//	get Invoice Affected Object
				MInvoice invoice = new MInvoice(getCtx(),rs.getInt("C_Invoice_ID"),get_TrxName());
				debitNoteCHR.setC_Currency_ID(invoice.getC_Currency_ID());
				debitNoteCHR.setM_PriceList_ID(invoice.getM_PriceList_ID());
				debitNoteCHR.setSalesRep_ID(invoice.getSalesRep_ID());
				
				debitNoteCHR.saveEx(get_TrxName());
				//	Create Line
				MInvoiceLine debitNoteCHRline = new MInvoiceLine(debitNoteCHR);
				
				debitNoteCHRline.setC_Invoice_ID(debitNoteCHR.getC_Invoice_ID());
				debitNoteCHRline.setC_Charge_ID(Charge);
				debitNoteCHRline.setQty(BigDecimal.ONE);
				debitNoteCHRline.setPrice(charge.getChargeAmt().divide(rs.getBigDecimal("Lines")));
				debitNoteCHRline.set_ValueOfColumn("LVE_InvoiceAffected_ID",invoice.getC_Invoice_ID());
				
				debitNoteCHRline.saveEx(get_TrxName());
				
				// Valid Grand Total of Debit Adjust
				if(debitNoteCHRline.getLineNetAmt().compareTo(BigDecimal.ZERO)==0){
					rollback();
					throw new AdempiereException("@IsError@ @C_Invoice_ID@: @Amt@ "+debitNoteCHRline.getLineNetAmt()+" @NotValid@");
				}
			}
		} catch (SQLException e) {
			addLog(e.getMessage());
		} finally {
			DB.close(rs, ps);
			rs = null; ps = null;
		}
		
		if(debitNoteCHR.processIt(MInvoice.DOCACTION_Complete)){
			debitNoteCHR.saveEx();
			//	Set Debit Note into Receipt
			MPayment receipt = new MPayment(getCtx(), Receipt_ID, get_TrxName());
			receipt.set_ValueOfColumn("LVE_InvoiceAffected_ID", debitNoteCHR.getC_Invoice_ID());
			receipt.saveEx();
		}
		else {
			rollback();
			throw new AdempiereException(debitNoteCHR.getProcessMsg());
		}
	}
	 
	public void createAssignment(Timestamp DateTrx, MInvoice invoice, MPayment payment) throws AdempiereUserError
	{
		MAllocationHdr AHeader = new MAllocationHdr(getCtx(), 0, get_TrxName());
		
		AHeader.setDateTrx(DateTrx);
		AHeader.setDateAcct(DateTrx);
		AHeader.setC_Currency_ID(invoice.getC_Currency_ID());
		
		AHeader.saveEx(get_TrxName());
		
		MAllocationLine ALine = new MAllocationLine(AHeader);
		
		ALine.setC_BPartner_ID(invoice.getC_BPartner_ID());
		ALine.setC_Invoice_ID(invoice.getC_Invoice_ID());
		ALine.setC_Payment_ID(payment.getC_Payment_ID());
		
		if (payment.isReceipt())
			ALine.setAmount(payment.getPayAmt());
		else
			ALine.setAmount(payment.getPayAmt().negate());
		
		ALine.setOverUnderAmt(payment.getPayAmt().add(invoice.getGrandTotal()));
		
		ALine.saveEx(get_TrxName());
		
		if(AHeader.processIt(MAllocationHdr.ACTION_Complete))
			AHeader.saveEx(get_TrxName());
		else{
			rollback();
			throw new AdempiereException(AHeader.getProcessMsg());
		}
		
		payment.setIsAllocated(true);
		payment.saveEx(get_TrxName());
	}
		
	/**
	 * Print Document
	 * @return void
	 */
	private void printDocuments(int Record_ID, int DocType_ID) {
		//	Get Document Type
		MDocType m_DocType = MDocType.get(getCtx(), DocType_ID);
		//	Check Print Format
		if(m_DocType.getAD_PrintFormat_ID() == 0) {
			log.warning(Msg.parseTranslation(getCtx(), 
				"@NoDocPrintFormat@ @AD_Table_ID@=@C_Invoice_ID@"));
		}
		//	Get Print Format
		MPrintFormat f = MPrintFormat.get(getCtx(), m_DocType.getAD_PrintFormat_ID(), false);
		
		if(f != null) {
			MQuery q = new MQuery(MInvoice.Table_Name);
			q.addRestriction(MInvoice.Table_Name + "_ID", "=", Record_ID);
			PrintInfo i = new PrintInfo(Msg.translate(getCtx(), 
					MInvoice.Table_Name + "_ID"), MInvoice.Table_ID, Record_ID);
			
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i, get_TrxName());
			//	Print
			//	Direct Print
			re.print();
		}
	}
}
