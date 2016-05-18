package com.azir.mvp.presenter;

import com.azir.mvp.bean.Message;
import com.azir.mvp.view.IMainActView;

public class MsgPersenter implements IMsgPresenter {

	private IMainActView actView;

	public MsgPersenter(IMainActView actView) {
		this.actView = actView;
	}

	@Override
	public void loadMessage() {
		// Message message = new Message();
		// message.setInfo("this is message info");
		// message.setTitle("this is message title");

		actView.showLoding();

		Message message = null;

		if (message != null) {
			actView.ShowMessage(true, message);
		} else {
			actView.ShowMessage(false, null);
		}

	}

}
