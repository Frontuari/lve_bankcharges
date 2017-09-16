package org.frontuari.component;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.base.IModelFactory;
import org.compiere.model.MEntityType;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.frontuari.info.FTUPluginFeatures;

/**
 * Generic Model Factory for Frontuari
 * @author Jorge Colmenarez, 2017-09-16 15:19, jcolmenarez@frontuari.com, Frontuari, C.A.
 *
 */
public class FTUModelFactory  implements IModelFactory {
	
	private final static CLogger log = CLogger.getCLogger(FTUModelFactory.class);
	private static CCache<String, Class<?>> cache = new CCache<String, Class<?>>("PO_Class", 20);
	
	private final static String prefixModel = "M";
	private final static String prefixModelDefault = "X_";

	@Override
	public Class<?> getClass(String tableName) {
		
		if (tableName == null)
			return null;

		Class<?> clazz = cache.get(tableName);

		if (clazz == null) {

			MTable table = MTable.get(Env.getCtx(), tableName);
			String entityType = table.getEntityType();
			
			Arrays.sort(FTUPluginFeatures.entityType);
			if (Arrays.binarySearch(FTUPluginFeatures.entityType, entityType)< 0)
				return null;

			MEntityType et = MEntityType.get(Env.getCtx(), entityType);
			String modelPackage = et.getModelPackage();

			String classNameFormat = "%s.%s%s";

			try {
				clazz = Class.forName(String.format(classNameFormat, modelPackage, prefixModel, tableName.replace("_", "")));
				cache.put(tableName, clazz);
			} catch (Exception e1) {
				try {
					clazz = Class.forName(String.format(classNameFormat, modelPackage, prefixModelDefault, tableName));
					cache.put(tableName, clazz);
				} catch (Exception e2) {
					if (log.isLoggable(Level.WARNING))
						log.warning(String.format("Plugin: %s -> Class not found for table: %s", FTUPluginFeatures.id, tableName));
				}
			}
		}

		return clazz;
	}

	@Override
	public PO getPO(String tableName, int Record_ID, String trxName) {

		Class<?> clazz = getClass(tableName);
		if (clazz == null)
			return null;

		PO model = null;
		Constructor<?> constructor = null;

		try {
			constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
			model = (PO) constructor.newInstance(new Object[] { Env.getCtx(), new Integer(Record_ID), trxName });
		} catch (Exception e) {
			if (log.isLoggable(Level.WARNING))
				log.warning(String.format("Plugin: %s -> Class can not be instantiated for table: %s", FTUPluginFeatures.id, tableName));
		}

		return model;
	}

	@Override
	public PO getPO(String tableName, ResultSet rs, String trxName) {

		Class<?> clazz = getClass(tableName);
		if (clazz == null)
			return null;

		PO model = null;
		Constructor<?> constructor = null;

		try {
			constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, ResultSet.class, String.class });
			model = (PO) constructor.newInstance(new Object[] { Env.getCtx(), rs, trxName });
		} catch (Exception e) {
			if (log.isLoggable(Level.WARNING))
				log.warning(String.format("Plugin: %s -> Class can not be instantiated for table: %s", FTUPluginFeatures.id, tableName));
		}

		return model;
	}

}
