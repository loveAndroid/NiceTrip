package azir.proxy.cgproxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class AClassProxy implements MethodInterceptor {

	private Object mObj;

	public AClassProxy(Object obj) {
		this.mObj = obj;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		
//		if(method.getName().equals("get")) {
//			return "intercept";
//		}
		
		return method.invoke(mObj, args);
	}

	private Enhancer enhancer = new Enhancer();

	public Object getProxy(Class clazz) {
		// 生成指定类对象的子类,也就是重写类中的业务函数，在重写中加入intercept()函数而已。
		enhancer.setSuperclass(clazz);
		// 这里是回调函数，enhancer中肯定有个MethodInterceptor属性。
		// 回调函数是在setSuperclass中的那些重写的方法中调用---猜想
		enhancer.setCallback(this);
		// 创建这个子类对象
		return enhancer.create();
	}

}
