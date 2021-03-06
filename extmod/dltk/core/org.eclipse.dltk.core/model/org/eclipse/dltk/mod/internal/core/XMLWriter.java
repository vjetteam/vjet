/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.mod.internal.core;
import java.io.Writer;

import org.eclipse.dltk.mod.compiler.util.GenericXMLWriter;
import org.eclipse.dltk.mod.core.IScriptProject;
import org.eclipse.dltk.mod.internal.core.util.Util;


class XMLWriter extends GenericXMLWriter {

	public XMLWriter(Writer writer, IScriptProject project, boolean printXmlVersion) {
		super(writer, Util.getLineSeparator((String) null, project), printXmlVersion);
	}
}
