package azir.proxy.cgproxy;

import java.util.List;

public class Test {
	
	public static void main(String[] args) throws Exception {
		
//		AClassProxy ct = new AClassProxy(new AClass());  
//		AClass aClass = (AClass) ct.getProxy(AClass.class);  
//		System.out.println(aClass.get());
		
		Class<?> cls = Class.forName("java.util.ArrayList");
		AClassProxy ct = new AClassProxy(cls.newInstance());
		
		java.util.List proxy = (List) ct.getProxy(cls);
		
		System.out.println(proxy.getClass());
		
		proxy.add("asdf");
		
		Object object = proxy.get(0);
		System.out.println(object);
		
	}
	
}	
