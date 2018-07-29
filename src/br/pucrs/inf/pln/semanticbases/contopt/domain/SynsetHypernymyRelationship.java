package br.pucrs.inf.pln.semanticbases.contopt.domain;

import java.io.Serializable;

public class SynsetHypernymyRelationship implements Serializable {

	private int synsetId;
	private int hypernymSynsetId;
	private double weight;
	
	public SynsetHypernymyRelationship(int synsetId, int hypernymSynsetId, double weight) {
		this.synsetId = synsetId;
		this.hypernymSynsetId = hypernymSynsetId;
		this.weight = weight;
	}

	public int getSynsetId() {
		return synsetId;
	}

	public int getHypernymSynsetId() {
		return hypernymSynsetId;
	}

	public double getWeight() {
		return weight;
	}
}
