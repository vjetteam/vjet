/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.jst.declaration;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ebayopensource.dsf.jst.IJstLib;
import org.ebayopensource.dsf.jst.IJstRefType;
import org.ebayopensource.dsf.jst.IJstType;


public class JstCache {

	private List<JstType> m_temp = new ArrayList<JstType>();
	private Map<String,JstType> m_types = new HashMap<String,JstType>();
	private Map<String,JstRefType> m_refTypes = new HashMap<String,JstRefType>();
	private Map<String,JstPackage> m_pkgs = new HashMap<String,JstPackage>();
	private Map<String, IJstLib> m_lib = new HashMap<String, IJstLib>();
	private Map<String, String> m_typeSymbolMapping = new HashMap<String, String>();
	private Map<IJstType, IJstRefType> m_typeRefTypeMap = new HashMap<IJstType, IJstRefType>();

	
	//
	// Singleton
	//
	private static JstCache s_instance = new JstCache();
	private JstCache(){}
	public static JstCache getInstance(){
		return s_instance;
	}
	
	//
	// API
	//
	public synchronized void addLib(IJstLib lib){
		if (lib == null){
			return;
		}
		for (JstType jst : lib.getAllTypes(true)) {
			addType(jst, true);
		}
	}
	
	public synchronized void addTypeSymbolMapping(String shortName, String longName) {
		m_typeSymbolMapping.put(shortName, longName);
	}
	
	public synchronized String getTypeSymbolMapping(String shortName) {
		return m_typeSymbolMapping.get(shortName);
	}
	
	/**
	 * Add given type to cache. However, if type with same name already exists and
	 * rtnExisting is true, do not add but return the existing type.
	 * @param type JstType the type to add to cache
	 * @param rtnExisting boolean 
	 * @return JstType
	 */
	public synchronized JstType addType(JstType type, boolean rtnExisting){
		if (type == null){
			return null;
		}
		if (rtnExisting){
			JstType existing = getType(type.getName());
			if (existing != null){
				return existing;
			}
		}
		addType(type.getName(), type);
		return type;
	}
	
	/**
	 * Add given type to cache. It may override type with same name in the cache.
	 * @param type JstType
	 * @return boolean
	 */
	public synchronized boolean addType(JstType type){
		if(!type.getAlias().equals(type.getName())){
			addType(type.getAlias(), type);
		}
		return addType(type.getName(), type);
	}
	
	public synchronized boolean addType(String key, JstType type){
		if (type == null){
			return false;
		}
		
		if (key == null){
			m_temp.add(type);
			return true;
		}
		
		//Do not add local types to cache
		if (type.isLocalType()){
			return false;
		}

		m_types.put(key, type);
		return true;
	}
	
	public synchronized boolean addOType(JstType oType) {
		if (oType == null){
			return false;
		}
		
		m_types.put(oType.getName(), oType);
		return true;
	}
	
	public synchronized JstType getType(String fullName, boolean create){
		JstType jstType = getType(fullName);
		if (jstType != null){
			return jstType;
		}
		if (create){
			jstType = new JstType(fullName);
			addType(jstType);
			return jstType;
		}
		return null;
	}
	
	public synchronized JstType getType(String fullName){
		
		if (fullName == null){
			return null;
		}
		
		JstType type = m_refTypes.get(fullName);
		if (type != null){
			return type;
		}
		
		type = m_types.get(fullName);
		if (type != null){
			return type;
		}
		
		for (JstType t: m_temp){
			if (fullName.equals(t.getName())){
				addType(fullName, t);
				m_temp.remove(t);
				return t;
			}
		}
		
		
		// TODO remove this causes issues with user defined types and lib types
		for (IJstLib lib : m_lib.values()) {
			type = lib.getType(fullName, true);
			if (type != null) {
				return type;
			}
		}
		
		return null;
	}
	
	public synchronized JstType getType(JstPackage pkg, String typeName){
		String fullName;
		if (pkg == null){
			fullName = typeName;
		}
		else {
			fullName = pkg.getName() + "." + typeName;
		}
		return getType(fullName);
	}
	
	public synchronized boolean addRefType(JstRefType type){
		return addRefType(type.getName(), type);
	}
	
	public synchronized boolean addRefType(String key, JstRefType type){
		if ("Void".equals(type.getSimpleName())){
			m_refTypes.put("void", type);
			m_refTypes.put(key, type);
			return true;
		}
		
		if (type.isArray()){// TODO replace this temp fix for array
			String arrayKey = type.getSimpleName();
			for (int i = 0; i < type.getDimensions(); i++) {
				arrayKey += "[]";
			}
			m_refTypes.put(arrayKey, type);
		} else {
			m_refTypes.put(key, type);
		}

		return true;
	}
	
	public synchronized JstRefType getRefType(String fullName){
		JstRefType refType = m_refTypes.get(fullName);
		if (refType != null){
			return refType;
		}
		IJstType jstType;
		for (IJstLib lib : m_lib.values()) {
			jstType = lib.getType(fullName, true);
			if (jstType != null && jstType instanceof JstRefType) {
				return (JstRefType)jstType;
			}
		}
		return null;
	}
	
	public synchronized JstType getRefType(JstPackage pkg, String typeName){
		String fullName;
		if (pkg == null){
			fullName = typeName;
		}
		else {
			fullName = pkg.getName() + "." + typeName;
		}
		return getRefType(fullName);
	}
	
	public synchronized boolean addTypeRefType(IJstRefType type) {
		if (type == null){
			return false;
		}
		m_typeRefTypeMap.put(type.getReferencedNode(), type);
		return true;
	}
	
	public IJstRefType getTypeRefType(IJstType type){
		return m_typeRefTypeMap.get(type);
	}
	
	public synchronized boolean addPackage(JstPackage pkg){
		if (pkg == null || m_pkgs.containsKey(pkg.getName())){
			return false;
		}
		m_pkgs.put(pkg.getName(), pkg);
		return true;
	}
	
	public synchronized JstPackage getPackage(String fullName){
		return m_pkgs.get(fullName);
	}
	
	public synchronized boolean removeJstType(JstType type){
		String key = null;
		if (m_types.containsValue(type)){
			for (Entry<String,JstType> entry: m_types.entrySet()){
				if (entry.getValue() == type){
					key = entry.getKey();
					break;
				}
			}
			if (key != null){
				return m_types.remove(key) != null;
			}
		}
		if (m_refTypes.containsValue(type)){
			for (Entry<String,JstRefType> entry: m_refTypes.entrySet()){
				if (entry.getValue() == type){
					key = entry.getKey();
					break;
				}
			}
			if (key != null){
				return m_refTypes.remove(key) != null;
			}
		}
		if (m_typeRefTypeMap.containsKey(type)){
			IJstType mapKey = null;
			for (Entry<IJstType,IJstRefType> entry: m_typeRefTypeMap.entrySet()){
				if (entry.getKey() == type){
					mapKey = entry.getKey();
					break;
				}
			}
			if (mapKey != null){
				return m_typeRefTypeMap.remove(mapKey) != null;
			}
		}
		return false;
	}
	
	
	public void printTypes(PrintStream p){
		p.println("ref types in cache");
		for(IJstType t : m_refTypes.values()){
			p.println(t.getName());
		}
		p.println("types in cache");
		for(IJstType t : m_types.values()){
			p.println(t.getName());
		}
		
		p.println("types in temp");
		for(IJstType t : m_temp){
			p.println(t.getName());
		}
		
		p.println("type in lib");
		for (IJstLib lib : m_lib.values()) {
			
			for(JstType t : lib.getAllTypes(true)){
				p.println(t.getName());			
			}
			
		}
		
	}
	
	public synchronized void clear(){
		m_types.clear();
		m_refTypes.clear();
		m_temp.clear();
		m_lib.clear();
		m_pkgs.clear();
		m_typeSymbolMapping.clear();
		m_typeRefTypeMap.clear();
		
	}
}
