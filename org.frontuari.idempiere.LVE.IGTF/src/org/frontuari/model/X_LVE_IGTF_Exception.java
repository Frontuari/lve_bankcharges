/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.frontuari.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for LVE_IGTF_Exception
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_LVE_IGTF_Exception extends PO implements I_LVE_IGTF_Exception, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170916L;

    /** Standard Constructor */
    public X_LVE_IGTF_Exception (Properties ctx, int LVE_IGTF_Exception_ID, String trxName)
    {
      super (ctx, LVE_IGTF_Exception_ID, trxName);
      /** if (LVE_IGTF_Exception_ID == 0)
        {
			setIsProprietaryTransfer (false);
// N
			setLVE_IGTF_ID (0);
        } */
    }

    /** Load Constructor */
    public X_LVE_IGTF_Exception (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_LVE_IGTF_Exception[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_BankAccount getC_BankAccountFrom() throws RuntimeException
    {
		return (org.compiere.model.I_C_BankAccount)MTable.get(getCtx(), org.compiere.model.I_C_BankAccount.Table_Name)
			.getPO(getC_BankAccountFrom_ID(), get_TrxName());	}

	/** Set Bank Account From.
		@param C_BankAccountFrom_ID 
		Account at the Bank
	  */
	public void setC_BankAccountFrom_ID (int C_BankAccountFrom_ID)
	{
		if (C_BankAccountFrom_ID < 1) 
			set_Value (COLUMNNAME_C_BankAccountFrom_ID, null);
		else 
			set_Value (COLUMNNAME_C_BankAccountFrom_ID, Integer.valueOf(C_BankAccountFrom_ID));
	}

	/** Get Bank Account From.
		@return Account at the Bank
	  */
	public int getC_BankAccountFrom_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankAccountFrom_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BankAccount getC_BankAccountTo() throws RuntimeException
    {
		return (org.compiere.model.I_C_BankAccount)MTable.get(getCtx(), org.compiere.model.I_C_BankAccount.Table_Name)
			.getPO(getC_BankAccountTo_ID(), get_TrxName());	}

	/** Set Bank Account To.
		@param C_BankAccountTo_ID 
		Account at the Bank
	  */
	public void setC_BankAccountTo_ID (int C_BankAccountTo_ID)
	{
		if (C_BankAccountTo_ID < 1) 
			set_Value (COLUMNNAME_C_BankAccountTo_ID, null);
		else 
			set_Value (COLUMNNAME_C_BankAccountTo_ID, Integer.valueOf(C_BankAccountTo_ID));
	}

	/** Get Bank Account To.
		@return Account at the Bank
	  */
	public int getC_BankAccountTo_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankAccountTo_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException
    {
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_Name)
			.getPO(getC_Charge_ID(), get_TrxName());	}

	/** Set Charge.
		@param C_Charge_ID 
		Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID)
	{
		if (C_Charge_ID < 1) 
			set_Value (COLUMNNAME_C_Charge_ID, null);
		else 
			set_Value (COLUMNNAME_C_Charge_ID, Integer.valueOf(C_Charge_ID));
	}

	/** Get Charge.
		@return Additional document charges
	  */
	public int getC_Charge_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Proprietary Transfer.
		@param IsProprietaryTransfer Proprietary Transfer	  */
	public void setIsProprietaryTransfer (boolean IsProprietaryTransfer)
	{
		set_Value (COLUMNNAME_IsProprietaryTransfer, Boolean.valueOf(IsProprietaryTransfer));
	}

	/** Get Proprietary Transfer.
		@return Proprietary Transfer	  */
	public boolean isProprietaryTransfer () 
	{
		Object oo = get_Value(COLUMNNAME_IsProprietaryTransfer);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Tax on Large Financial Transactions Exceptions (ID).
		@param LVE_IGTF_Exception_ID Tax on Large Financial Transactions Exceptions (ID)	  */
	public void setLVE_IGTF_Exception_ID (int LVE_IGTF_Exception_ID)
	{
		if (LVE_IGTF_Exception_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LVE_IGTF_Exception_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LVE_IGTF_Exception_ID, Integer.valueOf(LVE_IGTF_Exception_ID));
	}

	/** Get Tax on Large Financial Transactions Exceptions (ID).
		@return Tax on Large Financial Transactions Exceptions (ID)	  */
	public int getLVE_IGTF_Exception_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_IGTF_Exception_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set LVE_IGTF_Exception_UU.
		@param LVE_IGTF_Exception_UU LVE_IGTF_Exception_UU	  */
	public void setLVE_IGTF_Exception_UU (String LVE_IGTF_Exception_UU)
	{
		set_Value (COLUMNNAME_LVE_IGTF_Exception_UU, LVE_IGTF_Exception_UU);
	}

	/** Get LVE_IGTF_Exception_UU.
		@return LVE_IGTF_Exception_UU	  */
	public String getLVE_IGTF_Exception_UU () 
	{
		return (String)get_Value(COLUMNNAME_LVE_IGTF_Exception_UU);
	}

	public org.frontuari.model.I_LVE_IGTF getLVE_IGTF() throws RuntimeException
    {
		return (org.frontuari.model.I_LVE_IGTF)MTable.get(getCtx(), org.frontuari.model.I_LVE_IGTF.Table_Name)
			.getPO(getLVE_IGTF_ID(), get_TrxName());	}

	/** Set Tax on Large Financial Transactions (ID).
		@param LVE_IGTF_ID Tax on Large Financial Transactions (ID)	  */
	public void setLVE_IGTF_ID (int LVE_IGTF_ID)
	{
		if (LVE_IGTF_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LVE_IGTF_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LVE_IGTF_ID, Integer.valueOf(LVE_IGTF_ID));
	}

	/** Get Tax on Large Financial Transactions (ID).
		@return Tax on Large Financial Transactions (ID)	  */
	public int getLVE_IGTF_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_IGTF_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}