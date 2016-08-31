package com.example.injectactivity.util;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;


import android.content.Context;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * 动态加载补丁包
 * 
 * @author alex_mahao
 * 
 */
public class FixDexUtils {

	public static void loadFixDex(Context context, File targetFile) {
		doDexInject(context, targetFile);
	}

	private static void doDexInject(Context context, File fileDir) {
		// .dex 的加载需要一个临时目录
		String optimizeDir = context.getDir("opt_dex_", Context.MODE_PRIVATE).getAbsolutePath();
		File fopt = new File(optimizeDir);
		if (!fopt.exists())
			fopt.mkdirs();
		// 根据.dex 文件创建对应的DexClassLoader 类
		DexClassLoader classLoader = new DexClassLoader(fileDir.getAbsolutePath(), fopt.getAbsolutePath(), null,
				context.getClassLoader());
		// 注入
		inject(classLoader, context);
	}

	private static void inject(DexClassLoader classLoader, Context context) {

		PathClassLoader pathLoader = (PathClassLoader) context.getClassLoader();
		try {
			Object dexElements = combineArray(getDexElements(getPathList(classLoader)),
					getDexElements(getPathList(pathLoader)));
			Object pathList = getPathList(pathLoader);
			setField(pathList, pathList.getClass(), "dexElements", dexElements);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setField(Object obj, Class<?> cl, String field, Object value) throws NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field localField = cl.getDeclaredField(field);
		ReflectAccelerator.setValue(localField, obj, value);
	}

	private static Object getPathList(Object baseDexClassLoader) throws IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException, ClassNotFoundException {
		return getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
	}

	private static Object getField(Object obj, Class<?> cl, String field) throws NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field localField = cl.getDeclaredField(field);
		return ReflectAccelerator.getValue(localField, obj);
	}

	private static Object getDexElements(Object paramObject) throws IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException {
		return getField(paramObject, paramObject.getClass(), "dexElements");
	}

	private static Object combineArray(Object arrayLhs, Object arrayRhs) {

		Class<?> localClass = arrayLhs.getClass().getComponentType();
		int i = Array.getLength(arrayLhs);
		int j = i + Array.getLength(arrayRhs);
		Object result = Array.newInstance(localClass, j);
		for (int k = 0; k < j; ++k) {
			if (k < i) {
				Array.set(result, k, Array.get(arrayLhs, k));
			} else {
				Array.set(result, k, Array.get(arrayRhs, k - i));
			}
		}
		return result;
	}
}
