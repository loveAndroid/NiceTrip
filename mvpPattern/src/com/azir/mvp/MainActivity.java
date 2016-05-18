package com.azir.mvp;

import qingfengmy.mvp1.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.azir.mvp.bean.Message;
import com.azir.mvp.presenter.IMsgPresenter;
import com.azir.mvp.presenter.MsgPersenter;
import com.azir.mvp.view.IMainActView;

public class MainActivity extends Activity implements OnClickListener, IMainActView {

	private IMsgPresenter msgPersenter;
	public Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button = (Button) findViewById(R.id.save);
		button.setOnClickListener(this);

		msgPersenter = new MsgPersenter(this);
	}

	@Override
	public void onClick(View v) {
		msgPersenter.loadMessage();
	}

	@Override
	public void showLoding() {
		System.out.println("showLoading...");
	}

	@Override
	public void ShowMessage(boolean isValid, Message msg) {
		if (isValid) {
			System.out.println("msg info = " + msg.toString());
		} else {
			System.out.println("msg info = null");
		}
	}

}
