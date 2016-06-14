package com.example.aidl.manual;


public abstract class Stub extends android.os.Binder implements AidlListener {

	public Stub() {
		this.attachInterface(this, DESCRIPTOR);
	}

	public static AidlListener asInterface(android.os.IBinder obj) {
		if ((obj == null)) {
			return null;
		}
		android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
		if (((iin != null) && (iin instanceof AidlListener))) {
			return ((AidlListener) iin);
		}
		return new Stub.Proxy(obj);
	}

	@Override
	public android.os.IBinder asBinder() {
		return this;
	}

	@Override
	public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
			throws android.os.RemoteException {
		switch (code) {
		case INTERFACE_TRANSACTION: {
			reply.writeString(DESCRIPTOR);
			return true;
		}
		case TRANSACTION_onChange: {
			data.enforceInterface(DESCRIPTOR);
			this.onChange();
			reply.writeNoException();
			return true;
		}
		}
		return super.onTransact(code, data, reply, flags);
	}

	static class Proxy implements AidlListener {
		private android.os.IBinder mRemote;

		Proxy(android.os.IBinder remote) {
			mRemote = remote;
		}

		@Override
		public android.os.IBinder asBinder() {
			return mRemote;
		}

		public java.lang.String getInterfaceDescriptor() {
			return DESCRIPTOR;
		}

		@Override
		public void onChange() throws android.os.RemoteException {
			android.os.Parcel _data = android.os.Parcel.obtain();
			android.os.Parcel _reply = android.os.Parcel.obtain();
			try {
				_data.writeInterfaceToken(DESCRIPTOR);
				mRemote.transact(Stub.TRANSACTION_onChange, _data, _reply, 0);
				_reply.readException();
			} finally {
				_reply.recycle();
				_data.recycle();
			}
		}
	}
}