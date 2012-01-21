package org.ebayopensource.vjet.eclipse.core.typeconstruct;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.ebayopensource.dsf.jst.IJstParseController;
import org.ebayopensource.dsf.jst.IJstType;
import org.ebayopensource.dsf.jst.IScriptUnit;
import org.ebayopensource.dsf.jst.ts.JstTypeSpaceMgr;
import org.ebayopensource.dsf.jstojava.controller.JstParseController;
import org.ebayopensource.dsf.jstojava.loader.DefaultJstTypeLoader;
import org.ebayopensource.dsf.jstojava.parser.VjoParser;
import org.ebayopensource.dsf.ts.event.type.AddTypeEvent;
import org.ebayopensource.dsf.ts.type.TypeName;
import org.ebayopensource.vjet.eclipse.core.test.VjetModelTestsPlugin;
import org.ebayopensource.vjo.lib.TsLibLoader;
import org.junit.Test;
import org.osgi.framework.Bundle;

public class TypeConstructResolverExtensionTests {

	@Test
	public void testResolverExtension() throws Exception {

		// scaffolding
		IJstParseController pc = new JstParseController(new VjoParser());
		JstTypeSpaceMgr ts = new JstTypeSpaceMgr(pc, new DefaultJstTypeLoader());
		ts.initialize();
		TsLibLoader.loadDefaultLibs(ts);

		Bundle bundle = VjetModelTestsPlugin.getDefault().getBundle();
		URL urlFooDefineTypeJS = bundle
				.getEntry("fixtures/typeconstruct/foodefinetype.js");
		System.out.println(urlFooDefineTypeJS);

		URL urlUsingFooDefineTypeJS = bundle
				.getEntry("fixtures/typeconstruct/usingfoodefinetype.js");

		System.out.println(urlUsingFooDefineTypeJS);
		// bootstrap types
		addTypeToTS(pc, ts, urlFooDefineTypeJS, "test", "Foo");

		// user defined type
		addTypeToTS(pc, ts, urlUsingFooDefineTypeJS, "test", "a.b.c.D");

		// reading from type space
		IJstType exttype = ts.getTypeSpace().getType(
				new TypeName("test", "Foo"));
		assertEquals("Foo", exttype.getName());
		IJstType type = ts.getTypeSpace().getType(
				new TypeName("test", "a.b.c.D"));
		assertEquals("a.b.c.D", type.getName());

	}

	private void addTypeToTS(IJstParseController pc, JstTypeSpaceMgr ts,
			URL file, String groupName, String typeName) {
		IScriptUnit su1 = pc.parseAndResolve(groupName, typeName,
				VjoParser.getContent(file));
		ts.processEvent(new AddTypeEvent<IJstType>(new TypeName(groupName, su1
				.getType().getName()), su1.getType()));
	}

}
