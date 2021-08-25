package com.apoapsys.astronomy.utilities.nhats;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.jcouchdb.document.BaseDocument;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AsteroidRecordDocument extends BaseDocument {
	
	private String name;
	private Map<String, String> jplProperties = Maps.newHashMap();
	private Map<String, Double> elements = Maps.newHashMap();
	private List<Map<String, BigDecimal>> vectors = Lists.newArrayList();
	
	public AsteroidRecordDocument() {
		
	}
	
	public AsteroidRecordDocument(String name) {
		this.name = name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setJplProperties(Map<String, String> jplProperties) {
		this.jplProperties = jplProperties;
	}
	
	public Map<String, String> getJplProperties() {
		return jplProperties;
	}

	public Map<String, Double> getElements() {
		return elements;
	}

	public void setElements(Map<String, Double> elements) {
		this.elements = elements;
	}

	public List<Map<String, BigDecimal>> getVectors() {
		return vectors;
	}

	public void setVectors(List<Map<String, BigDecimal>> vectors) {
		this.vectors = vectors;
	}
	
	
}
