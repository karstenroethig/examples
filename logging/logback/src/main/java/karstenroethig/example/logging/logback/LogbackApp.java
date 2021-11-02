package karstenroethig.example.logging.logback;

import karstenroethig.example.logging.core.package1.A;
import karstenroethig.example.logging.core.package2.B;
import karstenroethig.example.logging.core.package2.C;

public class LogbackApp
{
	public static void main(String[] args)
	{
		Integer i1 = 36;
		Integer i2 = 56;

		A a = new A();

		a.setTemperature(i1);
		a.setTemperature(i2);

		B b = new B();

		b.setTemperature(i1);
		b.setTemperature(i2);

		C c = new C();

		c.setTemperature(i1);
		c.setTemperature(i2);
	}
}
