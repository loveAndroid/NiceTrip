package com.example.aidl;
import com.example.aidl.MyListener;
interface IComputer {
	void registerListener(MyListener listener);
}
