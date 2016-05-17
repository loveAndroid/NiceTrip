package com.azir.anno;

import android.app.Activity;
import android.view.View;

public class InjectUtils {

	public static void injecter(Activity act) {
		String simpleName = act.getClass().getSimpleName();
		Injecter<Object> injecter = getInjecter(act.getPackageName(), simpleName);
		injecter.inject(act, act.getWindow().getDecorView());
	}

	@SuppressWarnings("unchecked")
	private static Injecter<Object> getInjecter(String pkgname, String className) {

		try {
			Class<?> cls = Class.forName(pkgname + "." + className + "Proxy");
			return (Injecter<Object>) cls.newInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T castView(View view) {
		try {
			return (T) view;
		} catch (ClassCastException e) {
			throw new IllegalStateException("", e);
		}
	}

}
