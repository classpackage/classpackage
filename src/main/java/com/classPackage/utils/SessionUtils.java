package com.classPackage.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    private static final String EMAIL = "email";
    private static final String SERIAL_NUM = "serialNum";
    private static final String IS_AUTORIZED = "isAutorized"; 
    //maybe save email
    
    public static String getUserEmail(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(EMAIL) : null;
        
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
    
    public static void setUserEmail(HttpServletRequest request, String email) {
        HttpSession session = request.getSession(true);

        session.setAttribute(EMAIL, email);
    }
    
    //-----------------------------------------------------------------------------------------------//
    
    public static String getUserSerialNum(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(SERIAL_NUM) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
    
    public static void setUserSerialNum(HttpServletRequest request, String serialNum) {
        HttpSession session = request.getSession(true);

        session.setAttribute(SERIAL_NUM, serialNum);
    }
    
    //---------------------------------------------------------------------------------------------//
    
    public static String getIsUserAutorized(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(IS_AUTORIZED) : null;
        System.out.println("isAutorized is: "+ sessionAttribute.toString());
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
    
    public static void setIsUserAutorized(HttpServletRequest request, String isAutorized) {
        HttpSession session = request.getSession(true);

        session.setAttribute(IS_AUTORIZED, isAutorized);
    }
    
    
    //---------------------------------------------------------------------------------------------//
    
    public static void setSessionActiveTime(HttpServletRequest request) {
    	HttpSession session = request.getSession(true);
    	session.setMaxInactiveInterval(60*60*24*14); //2 weeks
    	
    }
    
    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
    
    //-------------------------------------------------------------------------------------------//
    
    
    
}
