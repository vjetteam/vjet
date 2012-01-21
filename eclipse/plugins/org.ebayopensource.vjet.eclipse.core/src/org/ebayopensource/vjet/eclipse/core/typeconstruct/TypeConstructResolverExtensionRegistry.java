/**
 * 
 */
package org.ebayopensource.vjet.eclipse.core.typeconstruct;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.dsf.jstojava.resolver.ITypeConstructorResolver;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

/**
 * A registry of {@link ITypeConstructorResolver}.
 * <p>
 * This is not available as singleton but can be used by creating
 * <code>new</code> instance every time. This helps avoiding problems due to
 * dynamic nature of plug-ins (e.g. if a plug-in contributing extension goes
 * away).
 * 
 * @author paragraval
 */
public class TypeConstructResolverExtensionRegistry {

	public static final String EXTENSION_POINT_ID = "org.ebayopensource.vjet.eclipse.core.typeconstruct"; //$NON-NLS-1$

	private static final String TAG_RESOLVER = "resolver"; //$NON-NLS-1$

	private Map<String, TypeConstructResolverExtension> resolversMap;
	private boolean isInitialized = false;

	/**
	 * Default constructor
	 */
	public TypeConstructResolverExtensionRegistry() {
		resolversMap = new HashMap<String, TypeConstructResolverExtension>();
	}

	/**
	 * Returns a collection of extensions registered as a type constructor
	 * resolver.
	 * 
	 * @return collection of extensions registered as a type constructor
	 *         resolver
	 */
	public Collection<TypeConstructResolverExtension> getResolverExtensions() {
		if (isInitialized) {
			return resolversMap.values();
		}

		// initialize the extensions
		IExtensionPoint typeConstructResolverExtPoint = Platform
				.getExtensionRegistry().getExtensionPoint(EXTENSION_POINT_ID);

		if (typeConstructResolverExtPoint == null) {
			return resolversMap.values();
		}

		IExtension[] typeConstructResolverExtensions = typeConstructResolverExtPoint
				.getExtensions();

		for (IExtension typeConstructResolverExtension : typeConstructResolverExtensions) {

			IConfigurationElement[] configElements = typeConstructResolverExtension
					.getConfigurationElements();

			for (IConfigurationElement element : configElements) {

				if (element == null) {
					continue;
				}
				String elementName = element.getName();
				if (elementName.equals(TAG_RESOLVER)) {
					readTypeConstructResolverExtension(element);
				}

			}

		}

		return resolversMap.values();
	}

	private void readTypeConstructResolverExtension(
			IConfigurationElement element) {
		TypeConstructResolverExtension extension = new TypeConstructResolverExtension(
				element);
		resolversMap.put(extension.getKey(), extension);

	}

	/**
	 * Returns reference to {@link TypeConstructResolverExtension} based on
	 * given key, if available, otherwise returns <code>null</code>.
	 * 
	 * @param key
	 *            a key to which the resolver is registered
	 * @return reference to {@link TypeConstructResolverExtension} based on
	 *         given key, if available, otherwise returns <code>null</code>.
	 */
	public TypeConstructResolverExtension getResolverExtension(String key) {
		// To make sure that the registry is initialized
		getResolverExtensions();
		return resolversMap.get(key);
	}

	/**
	 * For the given <code>key</code>, finds the corresponding
	 * {@link TypeConstructResolverExtension} and if available, creates and
	 * returns the reference to {@link ITypeConstructorResolver}.
	 * <p>
	 * Returns <code>null</code>, if no extension found based on given key.
	 * 
	 * @param key
	 *            a key to which the resolver is registered
	 * @return reference to {@link ITypeConstructorResolver} based on given key,
	 *         if available, otherwise returns <code>null</code>.
	 * @throws CoreException
	 */
	public ITypeConstructorResolver getResolver(String key)
			throws CoreException {
		TypeConstructResolverExtension extension = getResolverExtension(key);
		if (extension != null) {
			return extension.createResolver();
		} else {
			return null;
		}
	}
}
