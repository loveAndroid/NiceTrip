package azir.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Test {

	public static void main(String[] args) throws Exception {
		Class<?> name = Class.forName("azir.proxy.Real");
		Object newInstance = name.newInstance();
		InvocationHandler h = new InvocationHandlerEx(newInstance);
		Object instance = Proxy.newProxyInstance(name.getClassLoader(), name.getInterfaces(), h);
		Method method = instance.getClass().getMethod("get");
		Object invoke = method.invoke(instance);
		System.out.println(invoke);
		
		method = newInstance.getClass().getMethod("get2");
		invoke = method.invoke(newInstance);
		System.out.println(invoke);
	}

}
