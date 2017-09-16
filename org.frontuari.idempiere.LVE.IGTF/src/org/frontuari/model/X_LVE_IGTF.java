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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for LVE_IGTF
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_LVE_IGTF extends PO implements I_LVE_IGTF, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170916L;

    /** Standard Constructor */
    public X_LVE_IGTF (Properties ctx, int LVE_IGTF_ID, String trxName)
    {
      super (ctx, LVE_IGTF_ID, trxName);
      /** if (LVE_IGTF_ID == 0)
        {
			setC_Charge_ID (0);
			setIsPayApply (false);
// N
			setIsReceiptApply (false);
// N
			setLVE_IGTF_ID (0);
			setName (null);
			setRate (Env.ZERO);
// 0
			setTenderType (null);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
// SYSDATE
        } */
    }

    /** Load Constructor */
    public X_LVE_IGTF (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LVE_IGTF[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Applicable in Payments.
		@param IsPayApply Applicable in Payments	  */
	public void setIsPayApply (boolean IsPayApply)
	{
		set_Value (COLUMNNAME_IsPayApply, Boolean.valueOf(IsPayApply));
	}

	/** Get Applicable in Payments.
		@return Applicable in Payments	  */
	public boolean isPayApply () 
	{
		Object oo = get_Value(COLUMNNAME_IsPayApply);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Applicable in Receipts.
		@param IsReceiptApply Applicable in Receipts	  */
	public void setIsReceiptApply (boolean IsReceiptApply)
	{
		set_Value (COLUMNNAME_IsReceiptApply, Boolean.valueOf(IsReceiptApply));
	}

	/** Get Applicable in Receipts.
		@return Applicable in Receipts	  */
	public boolean isReceiptApply () 
	{
		Object oo = get_Value(COLUMNNAME_IsReceiptApply);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

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

	/** Set LVE_IGTF_UU.
		@param LVE_IGTF_UU LVE_IGTF_UU	  */
	public void setLVE_IGTF_UU (String LVE_IGTF_UU)
	{
		set_Value (COLUMNNAME_LVE_IGTF_UU, LVE_IGTF_UU);
	}

	/** Get LVE_IGTF_UU.
		@return LVE_IGTF_UU	  */
	public String getLVE_IGTF_UU () 
	{
		return (String)get_Value(COLUMNNAME_LVE_IGTF_UU);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Rate.
		@param Rate 
		Rate or Tax or Exchange
	  */
	public void setRate (BigDecimal Rate)
	{
		set_Value (COLUMNNAME_Rate, Rate);
	}

	/** Get Rate.
		@return Rate or Tax or Exchange
	  */
	public BigDecimal getRate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** TenderType AD_Reference_ID=214 */
	public static final int TENDERTYPE_AD_Reference_ID=214;
	/** Credit Card = C */
	public static final String TENDERTYPE_CreditCard = "C";
	/** Check = K */
	public static final String TENDERTYPE_Check = "K";
	/** Direct Deposit = A */
	public static final String TENDERTYPE_DirectDeposit = "A";
	/** Direct Debit = D */
	public static final String TENDERTYPE_DirectDebit = "D";
	/** Account = T */
	public static final String TENDERTYPE_Account = "T";
	/** Cash = X */
	public static final String TENDERTYPE_Cash = "X";
	/** Set Tender type.
		@param TenderType 
		Method of Payment
	  */
	public void setTenderType (String TenderType)
	{

		set_Value (COLUMNNAME_TenderType, TenderType);
	}

	/** Get Tender type.
		@return Method of Payment
	  */
	public String getTenderType () 
	{
		return (String)get_Value(COLUMNNAME_TenderType);
	}

	/** Set Valid from.
		@param ValidFrom 
		Valid from including this date (first day)
	  */
	public void setValidFrom (Timestamp ValidFrom)
	{
		set_Value (COLUMNNAME_ValidFrom, ValidFrom);
	}

	/** Get Valid from.
		@return Valid from including this date (first day)
	  */
	public Timestamp getValidFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidFrom);
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}