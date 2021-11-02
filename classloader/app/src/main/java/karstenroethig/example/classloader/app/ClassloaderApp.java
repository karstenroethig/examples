package karstenroethig.example.classloader.app;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import karstenroethig.example.classloader.core.FooBarUtils;
import karstenroethig.example.classloader.core.IFooBar;
import karstenroethig.example.classloader.core.IResult;

public class ClassloaderApp
{
	public static void main(String[] args) throws Exception
	{
		FooBarUtils.doSomething("app");

		File jar1 = new File("../dep1/target/example-classloader-dep1-0.0.1-SNAPSHOT.jar");
		String className1 = "karstenroethig.example.classloader.dep1.FooBar1";

		File jar2 = new File("../dep2/target/example-classloader-dep2-0.0.1-SNAPSHOT.jar");
		String className2 = "karstenroethig.example.classloader.dep2.FooBar2";

		if (!jar1.exists())
		{
			throw new IllegalArgumentException(jar1.toPath() + " does not exist");
		}

		if (!jar2.exists())
		{
			throw new IllegalArgumentException(jar2.toPath() + " does not exist");
		}

		System.out.println(getResult(jar1, className1));
		System.out.println(getResult(jar2, className2));
		System.out.println(getResult_(jar1, className1));

		FooBarUtils.doSomething("app");
	}

	private static int getResult(File jarFile, String className)
			throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException
	{
		try (URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {jarFile.toURI().toURL()}))
		{
			Class<?> clazz = classLoader.loadClass(className);
			Object instance = clazz.newInstance();
			IFooBar foobar = (IFooBar) instance;
			IResult result = foobar.foobar();

			return result.getResult();
		}
	}

	private static int getResult_(File jarFile, String className)
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, IOException
	{
		try (URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {jarFile.toURI().toURL()}))
		{
			Class<?> clazz = classLoader.loadClass(className);
			Method method = clazz.getDeclaredMethod("foobar");
			Object instance = clazz.newInstance();
			Object result = method.invoke(instance);

			return ((IResult)result).getResult();
		}
	}
}
