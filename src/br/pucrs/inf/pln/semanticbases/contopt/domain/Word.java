package br.pucrs.inf.pln.semanticbases.contopt.domain;

import java.io.Serializable;

public class Word implements Serializable {

	private String word;
	private double weight;
	
	public Word(String word, double weight) {
		this.word = word;
		this.weight = weight;
	}

	public String getWord() {
		return word;
	}

	public double getWeight() {
		return weight;
	}	
}
