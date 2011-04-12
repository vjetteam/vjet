/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
/* 
 * $Id: ITypeInstanceMembers1.java.java, Jun 21, 2009, 12:20:41 AM, liama. Exp$:
 * Copyright (c) 2006-2009 Ebay Technologies. All Rights Reserved.
 * This software program and documentation are copyrighted by Ebay
 * Technologies.
 */
package org.ebayopensource.dsf.jst.validation.vjo.vjoPro.samples.fundamentals;
import com.ebay.junitnexgen.category.ModuleInfo;

import static com.ebay.junitnexgen.category.Category.Groups.FAST;
import static com.ebay.junitnexgen.category.Category.Groups.P3;
import static com.ebay.junitnexgen.category.Category.Groups.UNIT;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.ebayopensource.dsf.jsgen.shared.ids.VjoSyntaxProbIds;
import org.ebayopensource.dsf.jsgen.shared.validation.vjo.VjoSemanticProblem;
import org.ebayopensource.dsf.jst.validation.vjo.VjoValidationBaseTester;
import com.ebay.junitnexgen.category.Category;
import com.ebay.junitnexgen.category.Description;

/**
 * ITypeInstanceMembers1.java
 * 
 * @author <a href="mailto:liama@ebay.com">liama</a>
 * @since JDK 1.5
 */
@Category( { P3, FAST, UNIT })
@ModuleInfo(value="DsfPrebuild",subModuleId="JsToJava")
public class ITypeInstanceMembers1 extends VjoValidationBaseTester {

    @Before
    public void setUp() {
        expectProblems.clear();
        expectProblems.add(createNewProblem(
                VjoSyntaxProbIds.ITypeAllowsOnlyPublicModifier, 2, 0));
        expectProblems.add(createNewProblem(
                VjoSyntaxProbIds.ITypeAllowsOnlyPublicModifier, 6, 0));
        expectProblems.add(createNewProblem(
                VjoSyntaxProbIds.ITypeWithInstanceProperty, 3, 0));
        expectProblems.add(createNewProblem(
                VjoSyntaxProbIds.ITypeWithInstanceProperty, 6, 0));
        expectProblems.add(createNewProblem(
                VjoSyntaxProbIds.ITypeWithInstanceProperty, 2, 0));
        expectProblems.add(createNewProblem(
                VjoSyntaxProbIds.ITypeWithInstanceProperty, 5, 0));
    }

    @Test
    @Category( { P3, FAST, UNIT })
    @Description("To test VjoPro project false positive")
    public void testITypeInstanceMembers1() {
        List<VjoSemanticProblem> problems = getVjoSemanticProblem(
                "vjoPro.samples.fundamentals.", "ITypeInstanceMembers1.js",
                this.getClass());
        assertProblemEquals(expectProblems, problems);
    }
}