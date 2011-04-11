/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.mod.internal.corext.refactoring.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.mod.core.ISourceModule;
import org.eclipse.dltk.mod.internal.corext.refactoring.changes.SourceModuleChange;
import org.eclipse.ltk.core.refactoring.TextChange;


/**
 * A <code>TextChangeManager</code> manages associations between <code>ISourceModule</code>
 * or <code>IFile</code> and <code>TextChange</code> objects.
 */
public class TextChangeManager {
	
	private Map/*<ISourceModule, TextChange>*/ fMap= new HashMap(10);
	
	private final boolean fKeepExecutedTextEdits;
	
	public TextChangeManager() {
		this(false);
	}

	public TextChangeManager(boolean keepExecutedTextEdits) {
		fKeepExecutedTextEdits= keepExecutedTextEdits;
	}
	
	/**
	 * Adds an association between the given compilation unit and the passed
	 * change to this manager.
	 * 
	 * @param cu the compilation unit (key)
	 * @param change the change associated with the compilation unit
	 */
	public void manage(ISourceModule cu, TextChange change) {
		fMap.put(cu, change);
	}
	
	/**
	 * Returns the <code>TextChange</code> associated with the given compilation unit.
	 * If the manager does not already manage an association it creates a one.
	 * 
	 * @param cu the compilation unit for which the text buffer change is requested
	 * @return the text change associated with the given compilation unit. 
	 */
	public TextChange get(ISourceModule cu) {
		TextChange result= (TextChange)fMap.get(cu);
		if (result == null) {
			result= new SourceModuleChange(cu.getElementName(), cu);
			result.setKeepPreviewEdits(fKeepExecutedTextEdits);
			fMap.put(cu, result);
		}
		return result;
	}
	
	/**
	 * Removes the <tt>TextChange</tt> managed under the given key
	 * <code>unit<code>.
	 * 
	 * @param unit the key determining the <tt>TextChange</tt> to be removed.
	 * @return the removed <tt>TextChange</tt>.
	 */
	public TextChange remove(ISourceModule unit) {
		return (TextChange)fMap.remove(unit);
	}
	
	/**
	 * Returns all text changes managed by this instance.
	 * 
	 * @return all text changes managed by this instance
	 */
	public TextChange[] getAllChanges(){
		Set cuSet= fMap.keySet();
		ISourceModule[] cus= (ISourceModule[]) cuSet.toArray(new ISourceModule[cuSet.size()]);
		// sort by cu name:
		Arrays.sort(cus, new Comparator() {
			public int compare(Object o1, Object o2) {
				String name1= ((ISourceModule) o1).getElementName();
				String name2= ((ISourceModule) o2).getElementName();
				return name1.compareTo(name2);
			}
		});
		
		TextChange[] textChanges= new TextChange[cus.length];
		for (int i= 0; i < cus.length; i++) {
			textChanges[i]= (TextChange) fMap.get(cus[i]);
		}
		return textChanges;
	}

	/**
	 * Returns all compilation units managed by this instance.
	 * 
	 * @return all compilation units managed by this instance
	 */	
	public ISourceModule[] getAllSourceModules(){
		return (ISourceModule[]) fMap.keySet().toArray(new ISourceModule[fMap.keySet().size()]);
	}
	
	/**
	 * Clears all associations between resources and text changes.
	 */
	public void clear() {
		fMap.clear();
	}

	/**
	 * Returns if any text changes are managed for the specified compilation unit.
	 * 
	 * @param cu the compilation unit
	 * @return <code>true</code> if any text changes are managed for the specified compilation unit and <code>false</code> otherwise
	 */		
	public boolean containsChangesIn(ISourceModule cu){
		return fMap.containsKey(cu);
	}
}

