package karstenroethig.example.classloader.dep2;

import karstenroethig.example.classloader.core.IResult;

public class Result2 implements IResult
{
	private int result = 0;

	public void setResult(int result)
	{
		this.result = result;
	}

	@Override
	public int getResult()
	{
		return result;
	}
}
