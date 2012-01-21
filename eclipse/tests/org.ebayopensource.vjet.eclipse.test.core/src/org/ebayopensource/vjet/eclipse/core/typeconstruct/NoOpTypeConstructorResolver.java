/**
 * 
 */
package org.ebayopensource.vjet.eclipse.core.typeconstruct;

import java.util.List;

import org.ebayopensource.dsf.jst.IJstType;
import org.ebayopensource.dsf.jst.declaration.JstFactory;
import org.ebayopensource.dsf.jst.declaration.JstType;
import org.ebayopensource.dsf.jst.token.IExpr;
import org.ebayopensource.dsf.jstojava.resolver.ITypeConstructorResolver;

/**
 * @author paragraval
 * 
 */
public class NoOpTypeConstructorResolver implements ITypeConstructorResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ebayopensource.dsf.jstojava.resolver.ITypeConstructorResolver#resolve
	 * (java.util.List)
	 */
	@Override
	public IJstType resolve(List<IExpr> args) {
		// creating JstType
		String typeName = args.get(0).toExprText();
		typeName = trimQuotes(typeName);
		JstType jstType = JstFactory.getInstance()
				.createJstType(typeName, true);
		return jstType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ebayopensource.dsf.jstojava.resolver.ITypeConstructorResolver#getGroupId
	 * ()
	 */
	@Override
	public String getGroupId() {
		return "test";
	}

	/**
	 * If the given string is prefixed and suffixed with either ' or " then
	 * truncates them.
	 * 
	 * @param input
	 * @return
	 */
	private String trimQuotes(String input) {
		if (input == null) {
			return "";
		}
		if (input.startsWith("'") || input.startsWith("\"")) {
			if (input.length() > 1) {
				input = input.substring(1);
			} else {
				input = "";
			}
		}
		if (input.endsWith("'") || input.endsWith("\"")) {
			if (input.length() > 1) {
				input = input.substring(0, input.length() - 1);
			} else {
				input = "";
			}
		}
		return input;
	}

}
