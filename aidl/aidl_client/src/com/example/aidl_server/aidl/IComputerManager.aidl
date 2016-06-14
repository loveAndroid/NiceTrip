package com.example.aidl_server.aidl;

import java.util.List;

import com.example.aidl_server.entity.ComputerEntity;
import com.example.aidl_server.aidl.IOnComputerArrivedListener;

interface IComputerManager {
	 void addComputer(in ComputerEntity computer);
     List<ComputerEntity> getComputerList();
     void registerUser(IOnComputerArrivedListener listener);
     void unRegisterUser(IOnComputerArrivedListener listener);
}
