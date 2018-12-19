package karstenroethig.example.classloader.dep1;

import karstenroethig.example.classloader.core.IResult;

public class Result1 implements IResult
{
	private int result;

	public Result1(int result)
	{
		this.result = result;
	}

	@Override
	public int getResult()
	{
		return result;
	}
}
