package com.example.aidl_server.aidl;

import com.example.aidl_server.entity.ComputerEntity;
interface IOnComputerArrivedListener {
	void onComputerArrived(in ComputerEntity computer);
}
