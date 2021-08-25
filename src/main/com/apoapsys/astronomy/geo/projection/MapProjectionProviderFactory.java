package com.apoapsys.astronomy.geo.projection;


import com.apoapsys.astronomy.geo.exception.MapProjectionException;
import com.apoapsys.astronomy.logging.Log;
import com.apoapsys.astronomy.logging.Logging;

public class MapProjectionProviderFactory
{
	@SuppressWarnings("unused")
	private static Log log = Logging.getLog(MapProjectionProviderFactory.class);
	
	protected MapProjectionProviderFactory()
	{
		
	}
	

	protected static MapProjection _getMapProjection(MapProjectionEnum projectionEnum, double north, double south, double east, double west, double width, double height) throws MapProjectionException
	{
		if (projectionEnum == null) {
			throw new MapProjectionException("Map projection provider is null.");
		}
		
		MapProjection mapProjectionInstance = null;
		
		try {
			mapProjectionInstance = projectionEnum.provider().newInstance();
		} catch (Exception ex) {
			throw new MapProjectionException("Error trying to create new map projection instance: " + ex.getMessage(), ex);
		} 
		
		if (mapProjectionInstance == null) {
			throw new MapProjectionException("Map projection instance is null following dynamic create.");
		}

		mapProjectionInstance.setUp(north, south, east, west, width, height);
		
		
		return mapProjectionInstance;
	}
}
