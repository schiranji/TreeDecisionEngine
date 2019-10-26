package com.techsavy.de.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.expression.Expression;

public class XlsRuleData implements Serializable {
	private static final long serialVersionUID = 6420372477843175352L;
	private String ruleName;
	private List<Expression> expressions = new ArrayList<>();
	private String decision;
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public List<Expression> getExpressions() {
		return expressions;
	}
	public void setExpressions(List<Expression> expressions) {
		this.expressions = expressions;
	}
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	
}
