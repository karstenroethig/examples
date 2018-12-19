package karstenroethig.example.classloader.dep2;

import karstenroethig.example.classloader.core.FooBarUtils;
import karstenroethig.example.classloader.core.IFooBar;
import karstenroethig.example.classloader.core.IResult;

public class FooBar2 implements IFooBar
{
	@Override
	public IResult foobar()
	{
		FooBarUtils.doSomething("dep2");

		Result2 result = new Result2();
		result.setResult(43);

		return result;
	}
}
