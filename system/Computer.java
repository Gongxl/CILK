package system;

import java.rmi.Remote;
import java.rmi.RemoteException;

import api.Task;

public interface Computer extends Remote {
	<T> long executeTask(Task<T> t) throws RemoteException;
	void exit() throws RemoteException;
}
