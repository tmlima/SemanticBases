package br.pucrs.inf.pln.semanticbases;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RelationsFile {
	
	public static final String  FILE_NAME = "Ontology.dat";
	
    public BaseSemantica Load(String caminho) throws Exception {
        BaseSemantica ontology = new BaseSemantica();
    	try {
            FileInputStream file = new FileInputStream(caminho + FILE_NAME);
            ObjectInputStream objectreader = new ObjectInputStream(file);
            ontology = (BaseSemantica) objectreader.readObject();
            objectreader.close();
            return ontology;    		
    	} catch (Exception e) {
    		throw new Exception("Erro ao carregar [" + caminho + "]: " + e.getMessage(), e);
    	}
    }

    public void Write(String caminho, BaseSemantica dbs) throws Exception {
    	try {
            FileOutputStream file = new FileOutputStream(caminho + FILE_NAME);
            ObjectOutputStream objectreader = new ObjectOutputStream(file);
            objectreader.writeObject(dbs);
            objectreader.close();
    	} catch (Exception e) {
    		throw new Exception("Erro ao serializar arquivo [" + caminho + "]: " + e.getMessage(), e);
    	}
    }
}
