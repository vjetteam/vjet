/**
 * 
 */
package org.ebayopensource.vjet.eclipse.core.typeconstruct;

import org.ebayopensource.dsf.jstojava.resolver.ITypeConstructorResolver;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Represents an extension for
 * {@link TypeConstructResolverExtensionRegistry#EXTENSION_POINT_ID} extension
 * point.
 * 
 * @author paragraval
 * 
 */
public class TypeConstructResolverExtension {

	public static final String ATT_NAME = "name"; //$NON-NLS-1$
	public static final String ATT_KEY = "key"; //$NON-NLS-1$
	public static final String TAG_DESCRIPTION = "description"; //$NON-NLS-1$
	public static final String ATT_CLASS = "class"; //$NON-NLS-1$

	private final String name;
	private final String key;
	private String description;
	private String executableClass;

	private final IConfigurationElement configurationElement;

	/**
	 * Default constructor
	 */
	public TypeConstructResolverExtension(IConfigurationElement element) {

		this.configurationElement = element;

		this.name = configurationElement.getAttribute(ATT_NAME);
		Assert.isLegal(this.name != null);

		this.key = configurationElement.getAttribute(ATT_KEY);
		Assert.isLegal(this.key != null);

		this.executableClass = configurationElement.getAttribute(ATT_CLASS);
		Assert.isLegal(executableClass != null);
	}

	/**
	 * Returns human-readable name of this resolver.
	 * 
	 * @return human-readable name of this resolver
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the key to which this extension is mapped.
	 * 
	 * @return the key to which this extension is mapped
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Returns description of this resolver, if available.
	 * 
	 * @return description of this resolver, if available
	 */
	public String getDescription() {
		if (description == null) {
			IConfigurationElement[] children = configurationElement
					.getChildren(TAG_DESCRIPTION);
			if (children.length > 0) {
				description = children[0].getValue();
			}
		}
		return description;
	}

	/**
	 * Returns a new instance of {@link ITypeConstructorResolver} as described
	 * by the 'class' attribute.
	 * 
	 * @see IConfigurationElement#createExecutableExtension(String)
	 * 
	 * @return a new instance of {@link ITypeConstructorResolver}
	 * @throws CoreException
	 *             if an instance of the executable extension could not be
	 *             created for any reason
	 */
	public ITypeConstructorResolver createResolver() throws CoreException {
		ITypeConstructorResolver typeConstructResolver = (ITypeConstructorResolver) configurationElement
				.createExecutableExtension(ATT_CLASS);
		return typeConstructResolver;
	}

	@Override
	public String toString() {
		String value = name + " [key: " + key + " , class: " + executableClass
				+ "]";
		return value;
	}

}
