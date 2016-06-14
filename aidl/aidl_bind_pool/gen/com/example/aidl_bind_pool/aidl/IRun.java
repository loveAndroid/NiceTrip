/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\Code\\workspace\\aidl_bind_pool\\src\\com\\example\\aidl_bind_pool\\aidl\\IRun.aidl
 */
package com.example.aidl_bind_pool.aidl;
public interface IRun extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.aidl_bind_pool.aidl.IRun
{
private static final java.lang.String DESCRIPTOR = "com.example.aidl_bind_pool.aidl.IRun";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.aidl_bind_pool.aidl.IRun interface,
 * generating a proxy if needed.
 */
public static com.example.aidl_bind_pool.aidl.IRun asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.aidl_bind_pool.aidl.IRun))) {
return ((com.example.aidl_bind_pool.aidl.IRun)iin);
}
return new com.example.aidl_bind_pool.aidl.IRun.Stub.Proxy(obj);
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
case TRANSACTION_getRandom:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.getRandom(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.aidl_bind_pool.aidl.IRun
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
@Override public int getRandom(int x) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(x);
mRemote.transact(Stub.TRANSACTION_getRandom, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getRandom = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public int getRandom(int x) throws android.os.RemoteException;
}
