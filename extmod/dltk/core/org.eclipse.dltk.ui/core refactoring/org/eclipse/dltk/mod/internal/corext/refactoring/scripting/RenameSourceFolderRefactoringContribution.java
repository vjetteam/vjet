/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.mod.internal.corext.refactoring.scripting;

import org.eclipse.dltk.mod.internal.corext.refactoring.ScriptRefactoringContribution;
import org.eclipse.dltk.mod.internal.corext.refactoring.rename.RenameSourceFolderProcessor;
import org.eclipse.dltk.mod.internal.corext.refactoring.rename.ScriptRenameRefactoring;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;


/**
 * Refactoring contribution for the rename source folder refactoring.
 * 
	 *
 */
public final class RenameSourceFolderRefactoringContribution extends ScriptRefactoringContribution {

	/**
	 * {@inheritDoc}
	 */
	public Refactoring createRefactoring(final RefactoringDescriptor descriptor) {
		return new ScriptRenameRefactoring(new RenameSourceFolderProcessor(null));
	}
}
