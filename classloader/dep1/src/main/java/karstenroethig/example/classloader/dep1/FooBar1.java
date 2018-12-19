package karstenroethig.example.classloader.dep1;

import karstenroethig.example.classloader.core.FooBarUtils;
import karstenroethig.example.classloader.core.IFooBar;
import karstenroethig.example.classloader.core.IResult;

public class FooBar1 implements IFooBar
{
	@Override
	public IResult foobar()
	{
		FooBarUtils.doSomething("dep1");

		return new Result1(42);
	}
}
