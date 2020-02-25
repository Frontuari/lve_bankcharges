package org.frontuari.component;

import org.adempiere.base.IPaymentExporterFactory;
import org.compiere.util.PaymentExport;
import org.frontuari.utils.B_BanescoPE;
import org.frontuari.utils.B_BanplusPE;
import org.frontuari.utils.B_ExteriorPE;
import org.frontuari.utils.B_MercantilPE;
import org.frontuari.utils.B_ProvincialPE;
import org.frontuari.utils.B_VenezuelaPE;

public class LVE_FrontuariPaymentExporterFactory implements IPaymentExporterFactory {

	@Override
	public PaymentExport newPaymentExporterInstance(String className) {
		
		if(className.equals("org.frontuari.utils.B_BanescoPE"))
			return new B_BanescoPE();
		
		if(className.equals("org.frontuari.utils.B_BanplusPE"))
			return new B_BanplusPE();
		
		if(className.equals("org.frontuari.utils.B_ExteriorPE"))
			return new B_ExteriorPE();
		
		if(className.equals("org.frontuari.utils.B_MercantilPE"))
			return new B_MercantilPE();
		
		if(className.equals("org.frontuari.utils.B_ProvincialPE"))
			return new B_ProvincialPE();
		
		if(className.equals("org.frontuari.utils.B_VenezuelaPE"))
			return new B_VenezuelaPE();
		
		if(className.equals("org.frontuari.utils.B_BanplusPE"))
			return new B_BanplusPE();
		
		if(className.equals("org.frontuari.utils.B_BanplusPE"))
			return new B_BanplusPE();
		
		return null;
	}

}
