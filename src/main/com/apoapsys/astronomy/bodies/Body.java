package com.apoapsys.astronomy.bodies;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class Body {
	
	private String name;
	private BodyTypeEnum bodyType;
	private List<Body> orbitingBodies = Lists.newArrayList();
	private List<Ring> rings = Lists.newArrayList();
	private Body parent;
	private Map<String, String> extendedProperties = Maps.newHashMap();
	
	public Body() {

	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Body getParent() {
		return parent;
	}

	public void setParent(Body parent) {
		this.parent = parent;
	}

	public BodyTypeEnum getBodyType() {
		return bodyType;
	}

	public void setBodyType(BodyTypeEnum bodyType) {
		this.bodyType = bodyType;
	}
	
	public void addOrbitingBody(Body body) {
		orbitingBodies.add(body);
	}
	
	public List<Body> getOrbitingBodies() {
		return orbitingBodies;
	}

	public void setOrbitingBodies(List<Body> orbitingBodies) {
		this.orbitingBodies = orbitingBodies;
	}
	
	public void addRing(Ring ring) {
		rings.add(ring);
	}

	public List<Ring> getRings() {
		return rings;
	}

	public void setRings(List<Ring> rings) {
		this.rings = rings;
	}

	public void setExtendedProperty(String key, String value) {
		extendedProperties.put(key, value);
	}
	
	public String getExtendedProperty(String key) {
		return extendedProperties.get(key);
	}
	
	public Map<String, String> getExtendedProperties() {
		return extendedProperties;
	}

	public void setExtendedProperties(Map<String, String> extendedProperties) {
		this.extendedProperties = extendedProperties;
	}
	
	
	
}
