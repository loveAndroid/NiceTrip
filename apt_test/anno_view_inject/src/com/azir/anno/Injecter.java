package com.azir.anno;

import android.view.View;

public interface Injecter<T> {
	public void inject(T target,View view);
}
