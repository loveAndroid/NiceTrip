/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\Code\\workspace\\aidl_client\\src\\com\\example\\aidl_server\\aidl\\IOnComputerArrivedListener.aidl
 */
package com.example.aidl_server.aidl;
public interface IOnComputerArrivedListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.aidl_server.aidl.IOnComputerArrivedListener
{
private static final java.lang.String DESCRIPTOR = "com.example.aidl_server.aidl.IOnComputerArrivedListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.aidl_server.aidl.IOnComputerArrivedListener interface,
 * generating a proxy if needed.
 */
public static com.example.aidl_server.aidl.IOnComputerArrivedListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.aidl_server.aidl.IOnComputerArrivedListener))) {
return ((com.example.aidl_server.aidl.IOnComputerArrivedListener)iin);
}
return new com.example.aidl_server.aidl.IOnComputerArrivedListener.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onComputerArrived:
{
data.enforceInterface(DESCRIPTOR);
com.example.aidl_server.entity.ComputerEntity _arg0;
if ((0!=data.readInt())) {
_arg0 = com.example.aidl_server.entity.ComputerEntity.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onComputerArrived(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.aidl_server.aidl.IOnComputerArrivedListener
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void onComputerArrived(com.example.aidl_server.entity.ComputerEntity computer) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((computer!=null)) {
_data.writeInt(1);
computer.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onComputerArrived, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onComputerArrived = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void onComputerArrived(com.example.aidl_server.entity.ComputerEntity computer) throws android.os.RemoteException;
}
