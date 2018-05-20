package br.pucrs.inf.pln.semanticbases;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class SemanticBasesTests {

	@Test
	void serializeDesserializeBase() throws Exception {
		RelationsFile relations = new RelationsFile();
		BaseSemantica semanticBase = mockBaseSemantica();
		relations.Write("", semanticBase);
		
		BaseSemantica semanticBaseDeserialized = relations.Load("");
		assertTrue(sameMap(semanticBase.Hiponimos, semanticBaseDeserialized.Hiponimos));
		assertTrue(sameMap(semanticBase.Sinonimos, semanticBaseDeserialized.Sinonimos));
		
		new File(RelationsFile.FILE_NAME).delete();
	}
	
	private BaseSemantica mockBaseSemantica() {
		BaseSemantica base = new BaseSemantica();
		base.Hiponimos = mockHiponimos();
		base.Sinonimos = mockSinonimos();
		return base;
	}
	
	private Map<String, String> mockHiponimos() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Branco", "Cor");
		map.put("Verde", "Cor");
		map.put("Azul", "Cor");
		return map;
	}
	
	private Map<String, String> mockSinonimos() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Casa", "Residência");
		map.put("Casa", "Moradia");
		return map;
	}
	
	private Boolean sameMap(Map<String, String> map1, Map<String, String> map2) {
		for (String key : map1.keySet()) {
			if (!map2.containsKey(key)) {
				return false;
			} else {
				if (!map1.get(key).equals(map2.get(key)))
					return false;				
			}
		}
		return true;		
	}
}
