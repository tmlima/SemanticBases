package br.pucrs.inf.pln.semanticbases.contopt.domain;

import java.io.Serializable;
import java.util.List;

public class Synset implements Serializable {

	private int id;
	private List<Word> words;
	
	public Synset(int synsetId, List<Word> words) {
		this.id = synsetId;
		this.words = words;
	}

	public int getId() {
		return id;
	}
	
	public void addWord(String word, double weight) {
		this.words.add(new Word(word, weight));
	}
	
	public List<Word> getWords() { 
		return words;
	}
}
