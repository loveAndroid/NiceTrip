package azir.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvocationHandlerEx implements InvocationHandler {

	private Object mObj;

	public InvocationHandlerEx(Object obj) {
		this.mObj = obj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String name = method.getName();
		if (name.equals("add")) {
			args[1] = "invoke";
			method.invoke(mObj, args);
		}

		if (name.equals("get")) {
			return "lkajsdlkfjs";
		}

		if (name.equals("get2")) {
			return -1;
		}

		return method.invoke(mObj, args);
	}

}
