package org.frontuari.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.MClient;
import org.compiere.model.MCurrency;

public class MLVEIGTF extends X_LVE_IGTF {

	private static final long serialVersionUID = -4748321544982518978L;

	public MLVEIGTF(Properties ctx, int LVE_IGTF_ID, String trxName) {
		super(ctx, LVE_IGTF_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MLVEIGTF(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public BigDecimal calculateTax(BigDecimal Amount){
		BigDecimal TaxAmt = new BigDecimal(0); 
		//	Support for Round Amount ITF
		MClient client = new MClient(getCtx(),getAD_Client_ID(),get_TrxName());
		MCurrency cur = new MCurrency(getCtx(), client.getC_Currency_ID(), get_TrxName());
		TaxAmt = Amount.multiply(getRate().divide(new BigDecimal(100))).setScale(cur.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
		return TaxAmt;
	}

}
