package com.techsavy.de.processor.sample3.domain;

import com.techsavy.de.domain.DecisionEngineRequest;

public class DecisionEngineRequestXls extends DecisionEngineRequest {
	private static final long serialVersionUID = 4562601397789368017L;
	public int fico = 820;
	public String suits = "Y";
	public int delinquencies = 5;

	public int getFico() {
		return fico;
	}

	public void setFico(int fico) {
		this.fico = fico;
	}

	public String getSuits() {
		return suits;
	}

	public void setSuits(String suits) {
		this.suits = suits;
	}

	public int getDelinquencies() {
		return delinquencies;
	}

	public void setDelinquencies(int delinquencies) {
		this.delinquencies = delinquencies;
	}
}
