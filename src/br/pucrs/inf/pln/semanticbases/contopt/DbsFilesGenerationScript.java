package br.pucrs.inf.pln.semanticbases.contopt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.pucrs.inf.pln.semanticbases.contopt.domain.Synset;
import br.pucrs.inf.pln.semanticbases.contopt.domain.SynsetHypernymyRelationship;
import br.pucrs.inf.pln.semanticbases.contopt.domain.Word;

public class DbsFilesGenerationScript {

	private static Map<Integer,Synset> synsets = new HashMap<Integer, Synset>();
	private static Map<String, List<Integer>> synsetsByWord = new HashMap<String, List<Integer>>();
	private static Map<Integer, List<SynsetHypernymyRelationship>> hypernymsBySynset = new HashMap<Integer, List<SynsetHypernymyRelationship>>();
	
	public static void main(String[] args) {
		generateDbsFiles("contopt_0.1_r2_c0.0.txt");
	}
	
	public static void generateDbsFiles(String contoPtFilePath) {
		BufferedReader bufferedReader = null;
		InputStreamReader reader = null;
    	try {
    		reader = new InputStreamReader(new FileInputStream(contoPtFilePath), "UTF-8");
    		bufferedReader = new BufferedReader(reader);
    		
    		String line;
    		int lineCount = 0;
    	    while ((line = bufferedReader.readLine()) != null) {
    	    	lineCount++;
    	    	System.out.print("\rProcessing line " + lineCount);
    	    	if (line.contains(": nome :")) {
    	    		Synset s = createSynset(line);
    	    		synsets.put(s.getId(),  s);
    	    	} else if (line.contains(" HIPERONIMO_DE ")) {
    	    		SynsetHypernymyRelationship relation = createSynsetHypernymRelation(line);
    	    		if (hypernymsBySynset.containsKey(relation.getSynsetId())) {
    	    			List<SynsetHypernymyRelationship> relations = hypernymsBySynset.get(relation.getSynsetId());
    	    			relations.add(relation);
    	    			hypernymsBySynset.put(relation.getSynsetId(), relations);
    	    		} else {
    	    			hypernymsBySynset.put(relation.getSynsetId(), new ArrayList<SynsetHypernymyRelationship>() {{ add(relation); }});
    	    		}
    	    	}
    	    }
    	    System.out.print("\n");
    	    
    	    System.out.println("Creating synsets by word index");
    	    synsetsByWord = createSynsetsByWord();
    	    System.out.println("Creating synsets by word index [done]");

    	    System.out.println("Writing files");
    	    writeFile(synsets, "synsets");
    	    writeFile(synsetsByWord, "synsetsbyword");
    	    writeFile(hypernymsBySynset, "hypernymsbysynset");
    	    System.out.println("Writing files [done]");

    	    System.out.println("Script complete");
    	} catch (Exception e) {
    		System.err.println(e);
    	} finally {
    		try {
	    		if (bufferedReader != null)
	    			bufferedReader.close();
	    		
	    		if (reader != null)
	    			reader.close();
    		} catch (Exception e) {
        		System.err.println(e);
    		}
    	}
	}
	
	private static Synset createSynset(String line) throws Exception {
		final Pattern pattern = Pattern.compile("(\\d+) : nome : (.*)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
        	int id = Integer.parseInt(matcher.group(1));
        	String wordsWithWeights = matcher.group(2);
        	return new Synset(id, parseWords(wordsWithWeights));
        } else {
        	throw new Exception("Match not found when parsing [" + line + "]");
        }
	}

	private static List<Word> parseWords(String wordsWithWeights) throws Exception {
		final Pattern pattern = Pattern.compile("(.+)\\(((\\d+).(\\d+))\\)", Pattern.UNICODE_CHARACTER_CLASS);	
		List<Word> words = new ArrayList<Word>();
		for (String s : wordsWithWeights.split(";")) {
			Matcher matcher = pattern.matcher(s);
			if (matcher.find())
				words.add(new Word(matcher.group(1), Double.parseDouble(matcher.group(2))));
			else
				throw new Exception("Match not found when parsing [" + s + "]");
		}
		return words;
	}
	
	private static SynsetHypernymyRelationship createSynsetHypernymRelation(String line) throws Exception {
		try {
			String[] relationshipAndWeight = line.split(":");
			String[] synsetsId = relationshipAndWeight[0].split("HIPERONIMO_DE");
			double weight = Double.parseDouble(relationshipAndWeight[1].trim());
			int synsetId = Integer.parseInt(synsetsId[1].trim());
			int hypernymSynsetId = Integer.parseInt(synsetsId[0].trim());

			return new SynsetHypernymyRelationship(synsetId, hypernymSynsetId, weight);
		} catch (Exception e) {
			throw new Exception("Error when parsing line [" + line + "]: " + e.getMessage());
		}
	}

	private static Map<String, List<Integer>> createSynsetsByWord() {
		Map<String, List<Integer>> synsetsByWord = new HashMap<String, List<Integer>>();
		for (Synset s : synsets.values()) {
			for (Word w : s.getWords()) {
				if (synsetsByWord.containsKey(w.getWord())) {
				 List<Integer> words = synsetsByWord.get(w.getWord());
				 words.add(s.getId());
				 synsetsByWord.put(w.getWord(), words);
				} else {
					synsetsByWord.put(w.getWord(), new ArrayList<Integer>() {{ add(s.getId()); }});
				}
			}
		}
		return synsetsByWord;
	}
	
	private static void writeFile(Object object, String fileName) throws Exception {
    	try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream objectreader = new ObjectOutputStream(file);
            objectreader.writeObject(object);
            objectreader.close();
    	} catch (Exception e) {
    		throw new Exception("Error when serializing [" + fileName + "]: " + e.getMessage(), e);
    	}
	}
}
