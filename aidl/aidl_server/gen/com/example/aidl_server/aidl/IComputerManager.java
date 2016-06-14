/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\Code\\workspace\\aidl_server\\src\\com\\example\\aidl_server\\aidl\\IComputerManager.aidl
 */
package com.example.aidl_server.aidl;
public interface IComputerManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.aidl_server.aidl.IComputerManager
{
private static final java.lang.String DESCRIPTOR = "com.example.aidl_server.aidl.IComputerManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.aidl_server.aidl.IComputerManager interface,
 * generating a proxy if needed.
 */
public static com.example.aidl_server.aidl.IComputerManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.aidl_server.aidl.IComputerManager))) {
return ((com.example.aidl_server.aidl.IComputerManager)iin);
}
return new com.example.aidl_server.aidl.IComputerManager.Stub.Proxy(obj);
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
case TRANSACTION_addComputer:
{
data.enforceInterface(DESCRIPTOR);
com.example.aidl_server.entity.ComputerEntity _arg0;
if ((0!=data.readInt())) {
_arg0 = com.example.aidl_server.entity.ComputerEntity.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.addComputer(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getComputerList:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<com.example.aidl_server.entity.ComputerEntity> _result = this.getComputerList();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_registerUser:
{
data.enforceInterface(DESCRIPTOR);
com.example.aidl_server.aidl.IOnComputerArrivedListener _arg0;
_arg0 = com.example.aidl_server.aidl.IOnComputerArrivedListener.Stub.asInterface(data.readStrongBinder());
this.registerUser(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unRegisterUser:
{
data.enforceInterface(DESCRIPTOR);
com.example.aidl_server.aidl.IOnComputerArrivedListener _arg0;
_arg0 = com.example.aidl_server.aidl.IOnComputerArrivedListener.Stub.asInterface(data.readStrongBinder());
this.unRegisterUser(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.aidl_server.aidl.IComputerManager
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
@Override public void addComputer(com.example.aidl_server.entity.ComputerEntity computer) throws android.os.RemoteException
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
mRemote.transact(Stub.TRANSACTION_addComputer, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.util.List<com.example.aidl_server.entity.ComputerEntity> getComputerList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.example.aidl_server.entity.ComputerEntity> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getComputerList, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.example.aidl_server.entity.ComputerEntity.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void registerUser(com.example.aidl_server.aidl.IOnComputerArrivedListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerUser, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unRegisterUser(com.example.aidl_server.aidl.IOnComputerArrivedListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unRegisterUser, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_addComputer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getComputerList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_registerUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_unRegisterUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public void addComputer(com.example.aidl_server.entity.ComputerEntity computer) throws android.os.RemoteException;
public java.util.List<com.example.aidl_server.entity.ComputerEntity> getComputerList() throws android.os.RemoteException;
public void registerUser(com.example.aidl_server.aidl.IOnComputerArrivedListener listener) throws android.os.RemoteException;
public void unRegisterUser(com.example.aidl_server.aidl.IOnComputerArrivedListener listener) throws android.os.RemoteException;
}
