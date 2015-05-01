package com.classPackage.utils;

import com.classPackage.dbUtils.DBHelper;

import javax.servlet.ServletContext;

public class ServletUtils {
	private static final String CLASS_PACKAGE_DB = "DBHelper";

	public static DBHelper getClassPackageDB(ServletContext servletContext) {
		if (servletContext.getAttribute(CLASS_PACKAGE_DB) == null) {
			servletContext.setAttribute(CLASS_PACKAGE_DB, com.classPackage.dbUtils.DBHelper.getInstance());
		}

		return (DBHelper) servletContext.getAttribute(CLASS_PACKAGE_DB);
	}
}
