/**
 * 
 */
package org.ebayopensource.vjet.eclipse.core.typeconstruct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.ebayopensource.dsf.jstojava.resolver.ITypeConstructorResolver;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

/**
 * @author paragraval
 * 
 */
public class TypeConstructResolverRegistryTest {

	/**
	 * Test the {@link TypeConstructResolverExtensionRegistry} to verify
	 * extensions discovery and initialization for the
	 * {@link TypeConstructResolverExtensionRegistry#EXTENSION_POINT_ID}.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testRegistryExtension() throws CoreException {

		String key = "Foo::defineType";

		TypeConstructResolverExtensionRegistry extensionRegistry = new TypeConstructResolverExtensionRegistry();
		TypeConstructResolverExtension resolverExtension = extensionRegistry
				.getResolverExtension(key);

		// assert the extension is NonNull
		assertNotNull("No extension found for '" + key + "' key.",
				resolverExtension);

		// assert the name is "ExtTest::defineResolver"
		assertEquals("Incorrect resolver extension found.",
				resolverExtension.getName(), key + "_Resolver");

		ITypeConstructorResolver resolver = resolverExtension.createResolver();
		// assert the resolver is NonNull
		assertNotNull("No type constructor resolver found for '" + key
				+ "' key.", resolver);

	}

}
