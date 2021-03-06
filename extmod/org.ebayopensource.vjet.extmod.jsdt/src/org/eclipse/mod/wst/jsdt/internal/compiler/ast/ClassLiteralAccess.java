/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.mod.wst.jsdt.internal.compiler.ast;


import org.eclipse.mod.wst.jsdt.core.ast.IASTNode;
import org.eclipse.mod.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.mod.wst.jsdt.internal.compiler.flow.FlowContext;
import org.eclipse.mod.wst.jsdt.internal.compiler.flow.FlowInfo;
//import org.eclipse.mod.wst.jsdt.internal.compiler.impl.Constant;
//import org.eclipse.mod.wst.jsdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.mod.wst.jsdt.internal.compiler.lookup.BlockScope;
//import org.eclipse.mod.wst.jsdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.mod.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class ClassLiteralAccess extends Expression  {

	public TypeReference type;
	public TypeBinding targetType;

	public ClassLiteralAccess(int sourceEnd, TypeReference type) {
		this.type = type;
		type.bits |= IgnoreRawTypeCheck; // no need to worry about raw type usage
		this.sourceStart = type.sourceStart;
		this.sourceEnd = sourceEnd;
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		return flowInfo;
	}

	public StringBuffer printExpression(int indent, StringBuffer output) {

		return type.print(0, output).append(".class"); //$NON-NLS-1$
	}

	public TypeBinding resolveType(BlockScope scope) {
		return null;
		
//		constant = Constant.NotAConstant;
//		if ((targetType = type.resolveType(scope, true /* check bounds*/)) == null)
//			return null;
//
//		if (targetType.isArrayType()
//			&& ((ArrayBinding) targetType).leafComponentType == TypeBinding.VOID) {
//			scope.problemReporter().cannotAllocateVoidArray(this);
//			return null;
//		}
//		ReferenceBinding classType = scope.getJavaLangClass();
//		    this.resolvedType = classType;
//		
//		return this.resolvedType;
	}

	public void traverse(
		ASTVisitor visitor,
		BlockScope blockScope) {

		if (visitor.visit(this, blockScope)) {
			type.traverse(visitor, blockScope);
		}
		visitor.endVisit(this, blockScope);
	}
	public int getASTType() {
		return IASTNode.CLASS_LITERAL_ACCESS;
	
	}
}
