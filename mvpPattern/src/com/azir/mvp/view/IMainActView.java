package com.azir.mvp.view;

import com.azir.mvp.bean.Message;

/**
 * get a message from netWork or local: if get, show message ,otherwise show
 * error info
 */
public interface IMainActView {

	public void showLoding();

	public void ShowMessage(boolean isValid, Message msg);
}
