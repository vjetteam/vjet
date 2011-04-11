/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.mod.internal.ui;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.dltk.mod.internal.ui.search.DLTKSearchPageScoreComputer;
import org.eclipse.dltk.mod.internal.ui.search.SearchUtil;
import org.eclipse.search.ui.ISearchPageScoreComputer;



/**
 * Adapter factory to support basic UI operations for markers.
 */
public class MarkerAdapterFactory implements IAdapterFactory {

	private static Class[] PROPERTIES= new Class[0];
	

	private Object fSearchPageScoreComputer;
	
	public Class[] getAdapterList() {
		updateLazyLoadedAdapters();
		return PROPERTIES;
	}
	
	public Object getAdapter(Object element, Class key) {
		updateLazyLoadedAdapters();
		if (fSearchPageScoreComputer != null && ISearchPageScoreComputer.class.equals(key))
			return fSearchPageScoreComputer;
		return null;
	}

	private void updateLazyLoadedAdapters() {
		if (fSearchPageScoreComputer == null && SearchUtil.isSearchPlugInActivated())
			createSearchPageScoreComputer();
	}
	
	private void createSearchPageScoreComputer() {
		fSearchPageScoreComputer= new DLTKSearchPageScoreComputer();
		PROPERTIES= new Class[] {ISearchPageScoreComputer.class};
	}
}
