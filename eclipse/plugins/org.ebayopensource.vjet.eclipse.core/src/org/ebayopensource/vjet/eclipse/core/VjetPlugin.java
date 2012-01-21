/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.vjet.eclipse.core;

import java.util.Collection;

import org.ebayopensource.dsf.jstojava.controller.JstParseController;
import org.ebayopensource.dsf.jstojava.resolver.TypeConstructorRegistry;
import org.ebayopensource.vjet.eclipse.core.builder.TypeSpaceBuilder;
import org.ebayopensource.vjet.eclipse.core.parser.VjoParserToJstAndIType;
import org.ebayopensource.vjet.eclipse.core.ts.EclipseTypeSpaceLoader;
import org.ebayopensource.vjet.eclipse.core.ts.JstLibResolver;
import org.ebayopensource.vjet.eclipse.core.ts.TypeSpaceLoadJob;
import org.ebayopensource.vjet.eclipse.core.typeconstruct.TypeConstructResolverExtension;
import org.ebayopensource.vjet.eclipse.core.typeconstruct.TypeConstructResolverExtensionRegistry;
import org.ebayopensource.vjet.eclipse.core.validation.DefaultValidator;
import org.ebayopensource.vjet.eclipse.internal.core.SerBuildPathEntry;
import org.ebayopensource.vjo.lib.IResourceResolver;
import org.ebayopensource.vjo.lib.LibManager;
import org.ebayopensource.vjo.tool.typespace.TypeSpaceMgr;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.dltk.mod.core.IAccessRule;
import org.eclipse.dltk.mod.core.IBuildpathAttribute;
import org.eclipse.dltk.mod.core.IBuildpathEntry;
import org.eclipse.dltk.mod.core.IProjectFragment;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class VjetPlugin extends Plugin {

	// The shared instance
	private static VjetPlugin plugin;

	// The plug-in ID
	public static final String PLUGIN_ID = "org.ebayopensource.vjet.eclipse.core";
	public static final String VJO_SUBFIX = ".js";
	public final static String VJETVALIDATION = "VJETVALIDATION";
	private TypeSpaceMgr m_typeSpaceMgr = TypeSpaceMgr.getInstance();
	public static final String SDK_CONTAINER = "org.ebayopensource.vjet.eclipse.core"
			+ ".SDK_CONTAINER";
	public static final String ID_DEFAULT_SDK = "DEFUALT_SDK";
	public static final String JS_DEFAULT_SDK = "org.ebayopensource.vjet.eclipse.core.JSNATIVE_CONTAINER";
	public static final String JS_DEFAULT_SDK_LABEL = "JS Native Types";
	public static final String DES_VJET_SDK = "VJET SDK";

	public static final String JSNATIVESDK_ID = PLUGIN_ID
			+ ".JSNATIVE_CONTAINER";

	public static final String BROWSERSDK_LABEL = "Browser SDK";

	public static final String BROWSERSDK_ID = "org.ebayopensource.vjet.eclipse.core.BROWSER_CONTAINER";

	public static final String VJOLIB_ID = "org.ebayopensource.vjet.eclipse.core.VJO_CONTAINER";

	public static final String VJOLIB_LABEL = "VJO LIB";

	public static final String BUILDER_ID = PLUGIN_ID + ".builder";

	public static final boolean DEBUG = Boolean
			.valueOf(Platform.getDebugOption("org.eclipse.dltk.mod.core/debug")).booleanValue(); //$NON-NLS-1$

	public static final boolean DEBUG_SCRIPT_BUILDER = Boolean
			.valueOf(
					Platform.getDebugOption("org.ebayopensource.vjet.eclipse.core/debugScriptBuilder")).booleanValue(); //$NON-NLS-1$

	public static final boolean TRACE_SCRIPT_BUILDER = Boolean
			.valueOf(
					Platform.getDebugOption("org.ebayopensource.vjet.eclipse.core/traceScriptBuilder")).booleanValue(); //$NON-NLS-1$

	public static final boolean TRACE_TYPESPACE = Boolean
			.valueOf(
					Platform.getDebugOption("org.ebayopensource.vjet.eclipse.core/typespace")).booleanValue(); //$NON-NLS-1$

	public static final boolean TRACE_PARSER = Boolean
			.valueOf(
					Platform.getDebugOption("org.ebayopensource.vjet.eclipse.core/traceParser")).booleanValue(); //$NON-NLS-1$

	private TypeSpaceLoadJob m_loadJob = new TypeSpaceLoadJob();

	private EclipseTypeSpaceLoader loader = new EclipseTypeSpaceLoader();

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static VjetPlugin getDefault() {
		return plugin;
	}

	/**
	 * The constructor
	 */
	public VjetPlugin() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		IResourceResolver jstLibResolver = JstLibResolver.getInstance()
				.setSdkEnvironment(PiggyBackClassPathUtil.getSdkEnvironment());

		LibManager.getInstance().setResourceResolver(jstLibResolver);

		m_typeSpaceMgr.setTypeLoader(loader);
		JstParseController controller = VjoParserToJstAndIType
				.getJstParseController();
		m_typeSpaceMgr.init(controller);

		TypeSpaceBuilder.addGroupEventListeners(m_typeSpaceMgr.getController()
				.getJstTypeSpaceMgr());

		if (VjetPlugin.TRACE_TYPESPACE) {
			addTraceGroupEventListeners();
		}

		setPluginInstance(this);
		DefaultValidator.getValidator();
		m_loadJob.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				loader.setStarted(true);
			}
		});
		
		initTypeCostructorRegistry();
		
		m_loadJob.schedule();
		
	}

	private void initTypeCostructorRegistry() {

		TypeConstructorRegistry registry = TypeConstructorRegistry
				.getInstance();

		TypeConstructResolverExtensionRegistry extensionRegistry = new TypeConstructResolverExtensionRegistry();
		Collection<TypeConstructResolverExtension> extensions = extensionRegistry
				.getResolverExtensions();
		for (TypeConstructResolverExtension extension : extensions) {
			try {
				registry.addResolver(extension.getKey(),
						extension.createResolver());
			} catch (CoreException e) {
				VjetPlugin
						.getDefault()
						.getLog()
						.log(new Status(IStatus.ERROR, VjetPlugin.PLUGIN_ID,
								"Error intializing the " + extension.toString()
										+ " resolver.", e));
			}
		}
		
	}
	
	private void addTraceGroupEventListeners() {
		TypeSpaceBuilder.addGroupTraceEventListeners(m_typeSpaceMgr
				.getController().getJstTypeSpaceMgr());
	}

	// Modify by Oliver, 2009-12-01, fix findbugs bug.
	private static void setPluginInstance(VjetPlugin pluginPar) {
		plugin = pluginPar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		setPluginInstance(null);
		super.stop(context);
		loader.setStarted(false);
	}

	public static void error(String message) {
		error(message, IStatus.ERROR);
	}

	public static void error(String message, int status) {
		plugin.getLog().log(
				new Status(status, PLUGIN_ID, IStatus.OK, message, null));
	}

	public static void error(String message, Throwable t) {
		error(message, t, IStatus.ERROR);
	}

	public static void error(String message, Throwable t, int status) {
		plugin.getLog().log(
				new Status(status, PLUGIN_ID, IStatus.OK, message, t));
	}

	public static IBuildpathEntry newSdkLibraryEntry(IPath path,
			IAccessRule[] accessRules, IBuildpathAttribute[] extraAttributes,
			IPath[] include, IPath[] exclude, boolean isExported,
			boolean externalLib) {

		if (path == null || path.segment(0) == null)
			Assert.isTrue(false, "Library path cannot be null"); //$NON-NLS-1$
		return new SerBuildPathEntry(IProjectFragment.K_BINARY,
				IBuildpathEntry.BPE_LIBRARY, path, isExported, include, // inclusion
				// patterns
				exclude, // exclusion patterns
				accessRules, false, // no access rules to combine
				extraAttributes, externalLib);
	}

}
