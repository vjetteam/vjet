/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.mod.internal.ui.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.mod.core.DLTKCore;
import org.eclipse.dltk.mod.core.IModelElement;


/**
 * Script-aware model provider.
 */
public final class ScriptModelProvider extends ModelProvider {

	/** The model provider id */
	public static final String DLTK_MODEL_PROVIDER_ID= "org.eclipse.dltk.mod.ui.modelProvider"; //$NON-NLS-1$

	/**
	 * Returns the resource associated with the corresponding model element.
	 * 
	 * @param element
	 *            the model element
	 * @return the associated resource, or <code>null</code>
	 */
	public static IResource getResource(final Object element) {
		IResource resource= null;
		if (element instanceof IModelElement) {
			resource= ((IModelElement) element).getResource();
		} else if (element instanceof IResource) {
			resource= (IResource) element;
		} else if (element instanceof IAdaptable) {
			final IAdaptable adaptable= (IAdaptable) element;
			final Object adapted= adaptable.getAdapter(IResource.class);
			if (adapted instanceof IResource)
				resource= (IResource) adapted;
		} else {
			final Object adapted= Platform.getAdapterManager().getAdapter(element, IResource.class);
			if (adapted instanceof IResource)
				resource= (IResource) adapted;
		}
		return resource;
	}

	/**
	 * Creates a newscriptmodel provider.
	 */
	public ScriptModelProvider() {
		// Used by the runtime
	}

	/**
	 * {@inheritDoc}
	 */
	public ResourceMapping[] getMappings(final IResource resource, final ResourceMappingContext context, final IProgressMonitor monitor) throws CoreException {
		final IModelElement element= DLTKCore.create(resource);
		if (element != null)
			return new ResourceMapping[] { DLTKElementResourceMapping.create(element)};
		final Object adapted= resource.getAdapter(ResourceMapping.class);
		if (adapted instanceof ResourceMapping)
			return new ResourceMapping[] { ((ResourceMapping) adapted)};
		return new ResourceMapping[] { new DLTKResourceMapping(resource)};
	}
}
