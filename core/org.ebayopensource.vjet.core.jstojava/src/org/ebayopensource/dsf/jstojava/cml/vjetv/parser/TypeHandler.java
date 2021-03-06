/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
/* 
 * $Id: TypeHandler.java, Jan 25, 2010, 10:44:46 PM, liama. Exp$
 *
 * Copyright (c) 2006-2009 Ebay Technologies. All Rights Reserved.
 * This software program and documentation are copyrighted by Ebay 
 * Technologies.
 */
package org.ebayopensource.dsf.jstojava.cml.vjetv.parser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Class/Interface description
 * 
 * @author <a href="mailto:liama@ebay.com">liama</a>
 * @since JDK 1.5
 */
public class TypeHandler {
    /**
     * Returns the <code>Object</code> of type <code>obj</code> with the
     * value of <code>str</code>.
     * 
     * @param str
     *            the command line value
     * @param obj
     *            the type of argument
     * @return The instance of <code>obj</code> initialised with the value of
     *         <code>str</code>.
     */
    public static Object createValue(String str, Object obj)
            throws ParseException {
        return createValue(str, (Class) obj);
    }

    /**
     * Returns the <code>Object</code> of type <code>clazz</code> with the
     * value of <code>str</code>.
     * 
     * @param str
     *            the command line value
     * @param clazz
     *            the type of argument
     * @return The instance of <code>clazz</code> initialised with the value
     *         of <code>str</code>.
     */
    public static Object createValue(String str, Class clazz)
            throws ParseException {
        if (PatternOptionBuilder.STRING_VALUE == clazz) {
            return str;
        } else if (PatternOptionBuilder.OBJECT_VALUE == clazz) {
            return createObject(str);
        } else if (PatternOptionBuilder.NUMBER_VALUE == clazz) {
            return createNumber(str);
        } else if (PatternOptionBuilder.DATE_VALUE == clazz) {
            return createDate(str);
        } else if (PatternOptionBuilder.CLASS_VALUE == clazz) {
            return createClass(str);
        } else if (PatternOptionBuilder.FILE_VALUE == clazz) {
            return createFile(str);
        } else if (PatternOptionBuilder.EXISTING_FILE_VALUE == clazz) {
            return createFile(str);
        } else if (PatternOptionBuilder.FILES_VALUE == clazz) {
            return createFiles(str);
        } else if (PatternOptionBuilder.URL_VALUE == clazz) {
            return createURL(str);
        } else {
            return null;
        }
    }

    /**
     * Create an Object from the classname and empty constructor.
     * 
     * @param classname
     *            the argument value
     * @return the initialised object, or null if it couldn't create the Object.
     */
    public static Object createObject(String classname) throws ParseException {
        Class cl = null;

        try {
            cl = Class.forName(classname);
        } catch (ClassNotFoundException cnfe) {
            throw new ParseException("Unable to find the class: " + classname);
        }

        Object instance = null;

        try {
            instance = cl.newInstance();
        } catch (Exception e) {
            throw new ParseException(e.getClass().getName()
                    + "; Unable to create an instance of: " + classname);
        }

        return instance;
    }

    /**
     * Create a number from a String. If a . is present, it creates a Double,
     * otherwise a Long.
     * 
     * @param str
     *            the value
     * @return the number represented by <code>str</code>, if
     *         <code>str</code> is not a number, null is returned.
     */
    public static Number createNumber(String str) throws ParseException {
        try {
            if (str.indexOf('.') != -1) {
                return Double.valueOf(str);
            } else {
                return Long.valueOf(str);
            }
        } catch (NumberFormatException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Returns the class whose name is <code>classname</code>.
     * 
     * @param classname
     *            the class name
     * @return The class if it is found, otherwise return null
     */
    public static Class createClass(String classname) throws ParseException {
        try {
            return Class.forName(classname);
        } catch (ClassNotFoundException e) {
            throw new ParseException("Unable to find the class: " + classname);
        }
    }

    /**
     * Returns the date represented by <code>str</code>.
     * 
     * @param str
     *            the date string
     * @return The date if <code>str</code> is a valid date string, otherwise
     *         return null.
     */
    public static Date createDate(String str) throws ParseException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Returns the URL represented by <code>str</code>.
     * 
     * @param str
     *            the URL string
     * @return The URL is <code>str</code> is well-formed, otherwise return
     *         null.
     */
    public static URL createURL(String str) throws ParseException {
        try {
            return new URL(str);
        } catch (MalformedURLException e) {
            throw new ParseException("Unable to parse the URL: " + str);
        }
    }

    /**
     * Returns the File represented by <code>str</code>.
     * 
     * @param str
     *            the File location
     * @return The file represented by <code>str</code>.
     */
    public static File createFile(String str) throws ParseException {
        return new File(str);
    }

    /**
     * Returns the File[] represented by <code>str</code>.
     * 
     * @param str
     *            the paths to the files
     * @return The File[] represented by <code>str</code>.
     */
    public static File[] createFiles(String str) throws ParseException {
        // to implement/port:
        // return FileW.findFiles(str);
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
