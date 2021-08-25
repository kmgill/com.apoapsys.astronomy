package com.apoapsys.astronomy.bodies;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.EllipticalOrbit;
import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.rotation.Rotation;
import com.google.common.collect.Lists;

public class MajorBodiesLoader {
	
	private static String MAJOR_BODIES_JSON_PATH = "major_bodies.json";
	
	
	private static List<Body> MAJOR_BODIES_LIST = Lists.newArrayList();
	
	private static Ring parseJsonRing(JSONObject jsonObject) {
		Ring ring = new Ring();

		if (jsonObject.containsKey("innerRadius")) {
			ring.setInnerRadius(jsonObject.getDouble("innerRadius"));
		}
		
		if (jsonObject.containsKey("outterRadius")) {
			ring.setOutterRadius(jsonObject.getDouble("outterRadius"));
		}
		
		if (jsonObject.containsKey("rotation")) {
			JSONArray rotationArray = jsonObject.getJSONArray("rotation");
			
			if (rotationArray != null && rotationArray.size() > 3) {
				Vector rotation = new Vector();
				rotation.x = MathExt.radians(rotationArray.getDouble(0));
				rotation.y = MathExt.radians(rotationArray.getDouble(1));
				rotation.z = MathExt.radians(rotationArray.getDouble(2));
				ring.setOrientation(rotation);
			}
		}
		
		return ring;
	}
	
	private static Ephemeris parseJsonEphemeris(JSONObject jsonObject) {
		
		Ephemeris ephemeris = new Ephemeris();

		if (jsonObject.containsKey("argOfPeriapsis")) {
			ephemeris.argOfPeriapsis = Angle.fromDegrees(jsonObject.getDouble("argOfPeriapsis"));
		}
		
		if (jsonObject.containsKey("period")) {
			ephemeris.period = jsonObject.getDouble("period");
		}
		
		if (jsonObject.containsKey("ascendingNode")) {
			ephemeris.ascendingNode = Angle.fromDegrees(jsonObject.getDouble("ascendingNode"));
		}
		
		if (jsonObject.containsKey("eccentricity")) {
			ephemeris.eccentricity = jsonObject.getDouble("eccentricity");
		}
		
		if (jsonObject.containsKey("periapsisDistance")) {
			ephemeris.pericenterDistance = jsonObject.getDouble("periapsisDistance");
		}
		
		if (jsonObject.containsKey("semiMajorAxis")) {
			ephemeris.semiMajorAxis = jsonObject.getDouble("semiMajorAxis");
		}
		
		if (jsonObject.containsKey("meanAnomalyAtEpoch")) {
			ephemeris.meanAnomalyAtEpoch = jsonObject.getDouble("meanAnomalyAtEpoch");
		}
		
		if (jsonObject.containsKey("epoch")) {
			ephemeris.epoch = jsonObject.getDouble("epoch");
		}
		
		if (jsonObject.containsKey("inclination")) {
			ephemeris.inclination = Angle.fromDegrees(jsonObject.getDouble("inclination"));
		}
		
		return ephemeris;
	}
	
	private static Orbit<?> initOrbitClass(String orbitClass) throws MajorBodiesLoadException {
		
		Orbit<?> orbit = null;
		
		try {
			Class<?> clazz = MajorBodiesLoader.class.getClassLoader().loadClass(orbitClass);
			orbit = (Orbit<?>) clazz.newInstance();
		} catch (Exception ex) {
			throw new MajorBodiesLoadException("Error loading specified orbit implementation class: " + orbitClass, ex);
		}
		
		return orbit;
		
	}
	
	private static Rotation initRotationClass(String customRotationClassName) throws MajorBodiesLoadException {
		Rotation rotation = null;
		
		try {
			Class<?> clazz = MajorBodiesLoader.class.getClassLoader().loadClass(customRotationClassName);
			rotation = (Rotation) clazz.newInstance();
		} catch (Exception ex) {
			throw new MajorBodiesLoadException("Error loading specified rotation implementation class: " + customRotationClassName, ex);
		}
		
		return rotation;
	}
	
	private static Body parseJsonBody(JSONObject jsonObject) throws MajorBodiesLoadException {
		

		double escapeVelocity = jsonObject.getDouble("escapeVelocity");
		double momentOfInertia = jsonObject.getDouble("momentOfInertia");
		String symbol = jsonObject.getString("symbol");
		String customOrbitClassName = jsonObject.containsKey("customOrbit") ? jsonObject.getString("customOrbit") : null;
		double radius = jsonObject.getDouble("radius");
		int id = jsonObject.getInt("id");
		double meanDensity = jsonObject.getDouble("meanDensity");
		double surfaceGravity = jsonObject.getDouble("surfaceGravity");
		boolean lightEmitting = jsonObject.getBoolean("lightEmitting");
		String name = jsonObject.getString("name");
		double flattening = jsonObject.getDouble("flattening");
		String customRotationClassName = jsonObject.getString("customRotation");
		String type = jsonObject.getString("type");
		double mass = jsonObject.containsKey("mass") ? jsonObject.getDouble("mass") : 0.0;
		
		double siderealRotPeriod = jsonObject.containsKey("siderealRotPeriod") ? jsonObject.getDouble("siderealRotPeriod") : 0.0;
		double obliquity = jsonObject.containsKey("obliquity") ? jsonObject.getDouble("obliquity") : 0.0;
		
		BodyTypeEnum bodyType = BodyTypeEnum.valueOf(type);
		
		OrbitingBody body = new OrbitingBody();
		body.setName(name);
		body.setId(id);
		body.setSurfaceGravity(surfaceGravity);
		body.setBodyType(bodyType);
		body.setEscapeVelocity(escapeVelocity);
		body.setFlattening(flattening);
		body.setMeanDensity(meanDensity);
		body.setMomentOfInertia(momentOfInertia);
		body.setRadius(radius);
		body.setMass(mass);
		body.setSiderealRotationalPeriod(siderealRotPeriod);
		body.setObliquityToTheOrbit(obliquity);
		body.setExtendedProperty("symbol", symbol);
		body.setExtendedProperty("lightEmitting", ""+lightEmitting);
		
		if (jsonObject.containsKey("ring")) {
			JSONObject ringJsonObject = jsonObject.getJSONObject("ring");
			if (ringJsonObject != null) {
				Ring ring = parseJsonRing(ringJsonObject);
				body.addRing(ring);
			}
		}
		
		Ephemeris ephemeris = null;
		if (jsonObject.containsKey("elements")) {
			JSONObject ephemerisJsonObject = jsonObject.getJSONObject("elements");
			if (ephemerisJsonObject != null) {
				ephemeris = parseJsonEphemeris(ephemerisJsonObject);
				body.setEphemeris(ephemeris);
			}
		}
		
		
		if (customRotationClassName != null && !customRotationClassName.isEmpty() && !"null".equals(customRotationClassName)) {
			Rotation rotation = initRotationClass(customRotationClassName);
			body.setRotation(rotation);
		}
		
		if (customOrbitClassName != null && !customOrbitClassName.isEmpty() && !"null".equals(customOrbitClassName)) {
			Orbit<?> orbit = initOrbitClass(customOrbitClassName);
			body.setOrbit(orbit);
		} else if (ephemeris != null) {
			EllipticalOrbit orbit = new EllipticalOrbit(ephemeris);
			body.setOrbit(orbit);
		}
		
		
		if (jsonObject.containsKey("moons")) {
			JSONArray moonJsonArray = jsonObject.getJSONArray("moons");
			if (moonJsonArray != null) {
				
				for (int i = 0; i < moonJsonArray.size(); i++) {
					JSONObject moonJsonObject = moonJsonArray.getJSONObject(i);
					Body moon = parseJsonBody(moonJsonObject);
					moon.setParent(body);
					body.addOrbitingBody(moon);
				}
				
			}
		}
		
		return body;
	}
	
	// Load as a List for now... 
	// TODO: A simple linear process for now. Make this better. A lot better.
	// TODO: A more robust database
	public static List<Body> load() throws MajorBodiesLoadException {
		
		if (MAJOR_BODIES_LIST != null && MAJOR_BODIES_LIST.size() > 0) {
			return MAJOR_BODIES_LIST;
		}
		
		List<Body> bodies = Lists.newArrayList();
		
		InputStream in = MajorBodiesLoader.class.getClassLoader().getResourceAsStream(MAJOR_BODIES_JSON_PATH);
		String jsonTxt = null;
		
		try {
			jsonTxt = IOUtils.toString( in );
		} catch (IOException ex) {
			throw new MajorBodiesLoadException("Error reading input stream", ex);
		}
		
		JSONArray jsonArray = JSONArray.fromObject(jsonTxt);
		
		for(int i = 0; i < jsonArray.size(); i++) {
			JSONObject object = jsonArray.getJSONObject(i);
			Body body = parseJsonBody(object);
			if (body != null) {
				bodies.add(body);
			}
		}
		
		MAJOR_BODIES_LIST.addAll(bodies);
		
		return bodies;
	}
	
}
