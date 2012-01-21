/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.jst.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ebayopensource.dsf.jst.BaseJstNode;
import org.ebayopensource.dsf.jst.IJstNode;
import org.ebayopensource.dsf.jst.IJstRefType;
import org.ebayopensource.dsf.jst.IJstType;
import org.ebayopensource.dsf.jst.declaration.JstParamType;
import org.ebayopensource.dsf.jst.declaration.JstTypeWithArgs;
import org.ebayopensource.dsf.jst.term.JstIdentifier;
import org.ebayopensource.dsf.jst.token.IExpr;
import org.ebayopensource.dsf.jst.token.IStmt;
import org.ebayopensource.dsf.jst.traversal.IJstNodeVisitor;
import com.ebay.kernel.util.Z;

public class MtdInvocationExpr extends BaseJstNode implements IExpr, IStmt {
	
	private static final long serialVersionUID = 1L;
	
	private IExpr m_mtdIdentifier;
	private IExpr m_qualifyExpr;
	private List<IExpr> m_args;
	private IJstType m_resultType;
	
	//
	// Constructor
	//
	public MtdInvocationExpr(final String name, final IExpr...args){
		this(new JstIdentifier(name), args);
	}

	public MtdInvocationExpr(final JstIdentifier name, final IExpr...args){
		this((IExpr)name, args);
	}

	public MtdInvocationExpr(final IExpr mtdIdentifier, final IExpr...args){		
		m_mtdIdentifier = mtdIdentifier;
		addChild(mtdIdentifier);
		if (args.length > 0){
			m_args = getArgs(true);
			for (IExpr arg : args){
				m_args.add(arg);
				addChild(arg);
			}
		}
	}
	
	//
	// Satisfy IExpr, IStmt
	//
	public IJstType getResultType(){
		
		if (m_resultType != null){
			return m_resultType;
		}

		if (m_mtdIdentifier != null){
			IJstType type = m_mtdIdentifier.getResultType();
			if (type instanceof JstParamType){
				if (m_qualifyExpr != null){
					type = m_qualifyExpr.getResultType();
					if (type instanceof JstTypeWithArgs){
						type = ((JstTypeWithArgs)type).getArgType();
					}
				}
			}
			
			if (type instanceof IJstRefType) {
				return ((IJstRefType)type);
			}
		}
		
		return null;
	}
	
	public String toExprText(){
		StringBuilder sb = new StringBuilder();
		String q = null;
		if (m_qualifyExpr != null){
			q = m_qualifyExpr.toExprText();
		}
		String m = null;
		if (m_mtdIdentifier != null){
			m = m_mtdIdentifier.toExprText();
		}
		if (q != null){
			sb.append(q);
		}
		if (q != null && m != null){
			sb.append(".");
		}
		if (m != null){
			sb.append(m);
		}
		sb.append("(");
		if (m_args != null){
			for (int i=0; i<m_args.size(); i++){
				if (m_args.get(i) == null){
					continue;
				}
				if (i > 0){
					sb.append(",");
				}
				sb.append(m_args.get(i).toExprText());
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	public String toStmtText(){
		return toExprText() + ";";
	}
	
	//
	// API
	//
	public IExpr getMethodIdentifier(){
		return m_mtdIdentifier;
	}
	
	public MtdInvocationExpr setQualifyExpr(IExpr qualifyExpr){
		removeChild(m_qualifyExpr);
		addChild(qualifyExpr);
		m_qualifyExpr = qualifyExpr;
		return this;
	}
	
	/**
	 * QualifyExpr is the expression that returns the type on which the method is invoked, 
	 * as in as in qualify_expr.mtd(args).
	 * @return qualify expression, or null of there is none
	 */
	public IExpr getQualifyExpr(){
		return m_qualifyExpr;
	}
	
	public List<IExpr> getArgs(){
		if (m_args == null){
			return Collections.emptyList();
		}
		else {
			return Collections.unmodifiableList(m_args);
		}
	}
	
	public void setArg(int index, IExpr arg){
		if (index < 0 || arg == null){
			return; // TODO throw exception?
		}
		if (m_args == null || m_args.size() < index + 1){
			return; // TODO throw exception?
		}
		m_args.set(index, arg);
	}
	
	public void setArgs(List<IExpr> args){
		if (m_args != null){
			for (IExpr arg: m_args){
				removeChild(arg);
			}
		}
		if (null != args && args.size() > 0){
			m_args = new ArrayList<IExpr>();
			for (IExpr arg : args){
				m_args.add(arg);
				addChild(arg);
			}
		}
	}
	
	public MtdInvocationExpr addArg(final IExpr expr){
		assert expr != null : "expr canoot be null";
		getArgs(true).add(expr);
		addChild(expr);
		return this;
	}
	
	public void setResultType(IJstType resultType){
		m_resultType = resultType;
	}
	
	/**
	 * lookup the method being invoked
	 * @return IJstMethod
	 */
	public IJstNode getMethod() {
		if (m_mtdIdentifier instanceof JstIdentifier) {
			return ((JstIdentifier)m_mtdIdentifier).getJstBinding();
		}else if (m_mtdIdentifier instanceof FieldAccessExpr) {
			return ((FieldAccessExpr)m_mtdIdentifier).getName().getJstBinding();
		}
		return null;			
	}	

	@Override
	public void accept(IJstNodeVisitor visitor){
		visitor.visit(this);
	}
	
	@Override
	public String toString(){
		Z z = new Z();
		z.format("m_funcName", m_mtdIdentifier);
		if (m_args != null){
			for (IExpr p : m_args){
				if(p!=null){
					z.format("arg", p.toString());
				}
			}
		}
		if(m_resultType!=null){
			z.format("m_resultType", m_resultType);
		}
		return z.toString();
	}
	
	//
	// Private
	//
	private List<IExpr> getArgs(boolean create){
		if (m_args == null && create){
			m_args = new ArrayList<IExpr>();
		}
		return m_args;
	}
}
