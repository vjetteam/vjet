/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.mod.internal.ui.util;

import org.eclipse.dltk.mod.core.IProjectFragment;
import org.eclipse.dltk.mod.core.search.MethodNameMatch;
import org.eclipse.dltk.mod.core.search.TypeNameMatch;
import org.eclipse.dltk.mod.internal.ui.DLTKUIMessages;
import org.eclipse.dltk.mod.ui.DLTKPluginImages;
import org.eclipse.dltk.mod.ui.IDLTKUILanguageToolkit;
import org.eclipse.dltk.mod.ui.ScriptElementLabels;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class MethodNameMatchLabelProvider extends LabelProvider {

	public static final int SHOW_FULLYQUALIFIED = 0x01;
	public static final int SHOW_PACKAGE_POSTFIX = 0x02;
	public static final int SHOW_PACKAGE_ONLY = 0x04;
	public static final int SHOW_ROOT_POSTFIX = 0x08;
	public static final int SHOW_TYPE_ONLY = 0x10;
	public static final int SHOW_TYPE_CONTAINER_ONLY = 0x20;
	public static final int SHOW_POST_QUALIFIED = 0x40;

	private static final Image METHOD_ICON = DLTKPluginImages
			.get(DLTKPluginImages.IMG_METHOD_PUBLIC);
	private static final Image ANNOTATION_ICON = DLTKPluginImages
			.get(DLTKPluginImages.IMG_OBJS_ANNOTATION);
	// private static final Image INTERFACE_ICON=
	// DLTKPluginImages.get(DLTKPluginImages.IMG_OBJS_INTERFACE);
	private static final Image PKG_ICON = DLTKPluginImages
			.get(DLTKPluginImages.IMG_OBJS_PACKAGE);

	private int fFlags;
	private IDLTKUILanguageToolkit fToolkit;

	public MethodNameMatchLabelProvider(int flags,
			IDLTKUILanguageToolkit toolkit) {
		fFlags = flags;
		this.fToolkit = toolkit;
	}

	private boolean isSet(int flag) {
		return (fFlags & flag) != 0;
	}

	private String getPackageName(String packName) {
		if (packName.length() == 0)
			return DLTKUIMessages.TypeInfoLabelProvider_default_package;
		else
			return packName;
	}

	/*
	 * non java-doc
	 * 
	 * @see ILabelProvider#getText
	 */
	public String getText(Object element) {
		if (!(element instanceof TypeNameMatch))
			return super.getText(element);

		TypeNameMatch typeRef = (TypeNameMatch) element;
		StringBuffer buf = new StringBuffer();
		if (isSet(SHOW_TYPE_ONLY)) {
			buf.append(typeRef.getSimpleTypeName());
		} else if (isSet(SHOW_TYPE_CONTAINER_ONLY)) {
			String containerName = typeRef.getTypeContainerName();
			buf.append(getPackageName(containerName));
		} else if (isSet(SHOW_PACKAGE_ONLY)) {
			String packName = typeRef.getPackageName();
			buf.append(getPackageName(packName));
		} else {
			if (isSet(SHOW_FULLYQUALIFIED)) {
				buf.append(typeRef.getFullyQualifiedName());
			} else if (isSet(SHOW_POST_QUALIFIED)) {
				buf.append(typeRef.getSimpleTypeName());
				String containerName = typeRef.getTypeContainerName();
				if (containerName != null && containerName.length() > 0) {
					buf.append(ScriptElementLabels.CONCAT_STRING);
					buf.append(containerName);
				}
			} else {
				buf.append(typeRef.getTypeQualifiedName());
			}

			if (isSet(SHOW_PACKAGE_POSTFIX)) {
				buf.append(ScriptElementLabels.CONCAT_STRING);
				String packName = typeRef.getPackageName();
				buf.append(getPackageName(packName));
			}
		}
		if (isSet(SHOW_ROOT_POSTFIX)) {
			buf.append(ScriptElementLabels.CONCAT_STRING);
			IProjectFragment root = typeRef.getProjectFragment();
			ScriptElementLabels labels = this.fToolkit.getScriptElementLabels();
			labels.getProjectFragmentLabel(root,
					ScriptElementLabels.ROOT_QUALIFIED, buf);
		}
		return buf.toString();
	}

	/*
	 * non java-doc
	 * 
	 * @see ILabelProvider#getImage
	 */
	public Image getImage(Object element) {
		if (!(element instanceof MethodNameMatch))
			return super.getImage(element);

		if (isSet(SHOW_TYPE_CONTAINER_ONLY)) {
			MethodNameMatch methodRef = (MethodNameMatch) element;
			if (methodRef.getPackageName().equals(
					methodRef.getMethodContainerName()))
				return PKG_ICON;

			return METHOD_ICON;

		} else if (isSet(SHOW_PACKAGE_ONLY)) {
			return PKG_ICON;
		} else {
			return METHOD_ICON;
		}
	}
}
