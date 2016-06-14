package com.example.aidl.manual;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public abstract class AidlImpl extends Binder implements AidlInterface {

	@Override
	public IBinder asBinder() {
		return this;
	}

	public AidlImpl() {
		System.out.println("impl()");
		this.attachInterface(this, DESCRIPTOR);
	}

	@Override
	protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

		switch (code) {
		case INTERFACE_TRANSACTION:
			reply.writeString(DESCRIPTOR);
			return true;
		case transaction_add:
			System.out.println("impl  ontransact add");
			data.enforceInterface(DESCRIPTOR);
			int x = data.readInt();
			int y = data.readInt();

			int add = this.add(x, y);
			reply.writeInt(add);
			return true;
		case transaction_register_listener:
			data.enforceInterface(DESCRIPTOR);
			com.example.aidl.manual.AidlListener _arg0;
			_arg0 = com.example.aidl.manual.Stub.asInterface(data.readStrongBinder());
			this.registerListener(_arg0);
			reply.writeNoException();
			return true;
		}
		return super.onTransact(code, data, reply, flags);
	}

	public static AidlInterface asInterface(IBinder obj) {
		System.out.println("impl as interface");
		if ((obj == null)) {
			return null;
		}
		android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
		if (((iin != null) && (iin instanceof AidlImpl))) {
			return ((AidlImpl) iin);
		}
		return new AidlImpl.Proxy(obj);
	}

	static class Proxy implements AidlInterface {

		private IBinder mRemote;

		public Proxy(IBinder obj) {
			mRemote = obj;
		}

		@Override
		public IBinder asBinder() {
			System.out.println("proxy asbinder");
			return mRemote;
		}

		@Override
		public int add(int x, int y) {

			System.out.println("proxy add(x,y)");
			Parcel data = Parcel.obtain();
			Parcel reply = Parcel.obtain();
			int result = 0;
			try {
				data.writeInterfaceToken(DESCRIPTOR);
				data.writeInt(x);
				data.writeInt(y);
				mRemote.transact(transaction_add, data, reply, 0);
				result = reply.readInt();
			} catch (RemoteException e) {
				e.printStackTrace();
			} finally {
				reply.recycle();
				data.recycle();
			}
			return result;
		}

		public java.lang.String getInterfaceDescriptor() {
			return DESCRIPTOR;
		}

		@Override
		public void registerListener(AidlListener listener) {

			android.os.Parcel _data = android.os.Parcel.obtain();
			android.os.Parcel _reply = android.os.Parcel.obtain();
			try {
				_data.writeInterfaceToken(DESCRIPTOR);
				_data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
				try {
					mRemote.transact(transaction_register_listener, _data, _reply, 0);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				_reply.readException();
			} finally {
				_reply.recycle();
				_data.recycle();
			}

		}

	}

}
