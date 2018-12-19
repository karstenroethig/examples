package karstenroethig.example.logging.core.package2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class B
{
	private static final Logger log = LoggerFactory.getLogger(B.class);

	Integer t;
	Integer oldT;

	public void setTemperature(Integer temperature)
	{
		oldT = t;
		t = temperature;

		log.debug("Temperature set to {}. Old temperature was {}.", t, oldT);

		if (temperature.intValue() > 50)
			log.info("Temperature has risen above 50 degrees.");
	}
}
