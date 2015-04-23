package system;

import java.rmi.Remote;
import java.rmi.RemoteException;

import api.Closure;
import api.Result;
import api.Task;

public interface Computer extends Remote {
	<T> Result executeTask(Task<T> t) throws RemoteException;
	void exit() throws RemoteException;
}
