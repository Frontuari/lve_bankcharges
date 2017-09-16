package org.frontuari.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

public class MLVE_IGTF extends X_LVE_IGTF {

	private static final long serialVersionUID = -4748321544982518978L;

	public MLVE_IGTF(Properties ctx, int LVE_IGTF_ID, String trxName) {
		super(ctx, LVE_IGTF_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MLVE_IGTF(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public BigDecimal calculateTax(BigDecimal Amount){
		BigDecimal TaxAmt = new BigDecimal(0); 
		TaxAmt = Amount.multiply(getRate().divide(new BigDecimal(100)));
		return TaxAmt;
	}

}
