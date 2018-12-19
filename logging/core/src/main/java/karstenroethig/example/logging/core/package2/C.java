package karstenroethig.example.logging.core.package2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C
{
	private static final Log log = LogFactory.getLog(C.class);

	Integer t;
	Integer oldT;

	public void setTemperature(Integer temperature)
	{
		oldT = t;
		t = temperature;

		log.debug(String.format("Temperature set to %s. Old temperature was %s.", t, oldT));

		if (temperature.intValue() > 50)
			log.info("Temperature has risen above 50 degrees.");
	}
}
