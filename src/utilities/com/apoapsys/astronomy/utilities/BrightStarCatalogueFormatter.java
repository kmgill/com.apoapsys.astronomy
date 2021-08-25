package com.apoapsys.astronomy.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

public class BrightStarCatalogueFormatter {
	
	
	public static void main(String[] args) {
		
		try {
			doWork();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	
	public static void doWork() throws Exception {
		String catalogueInPath = "catalogues/bright-star-catalog-couch.json";
		String catalogueOutPath = "catalogues/bright-star-catalog.json";
		
		InputStream in = BrightStarCatalogueFormatter.class.getClassLoader().getResourceAsStream(catalogueInPath);
		System.err.println("Reading source...");
		String jsonTxt = IOUtils.toString( in );
		
		System.err.println("Parsing from text...");
		JSONObject jsonObject = JSONObject.fromObject( jsonTxt ); 
		
		System.err.println("Extracting rows...");
		JSONArray rows = jsonObject.getJSONArray("rows");
		
		JSONArray newList = new JSONArray();
		System.err.println("Extracting data values into new list...");
		for (int i = 0; i < rows.size(); i++) {
			
			JSONObject row = rows.getJSONObject(i);
			JSONObject value = row.getJSONObject("value");
			value.discard("_rev");
			value.put("id", value.getString("_id"));
			value.discard("_id");
			value.discard("doc_type");
			newList.add(value);
		}
		
		
		System.err.println("Writing...");
		File f = new File("C:/jdem/repos/com.apoapsys.astronomy/src/resources/catalogues/bright-star-catalog.json");
		FileOutputStream out = new FileOutputStream(f);
		IOUtils.write(newList.toString(4), out);
		out.flush();
		out.close();
		
		
		//for (int i = 0; i < rows.size(); i++) {
		//	JSONObject row = rows.getJSONObject(i);
		//}
		
		
		
		
	}
	
}
