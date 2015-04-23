package clients;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import tasks.TaskFibNum;

public class ClientFibNum extends Client<Integer> {
	private static final int NUMBER = 16;

	public ClientFibNum(String serverDomain) throws RemoteException,
			NotBoundException, MalformedURLException {
		super("Fibonacci number", serverDomain, new TaskFibNum(NUMBER));
	}

	public static void main(String[] args) throws Exception {
		System.setSecurityManager(new SecurityManager());
		final ClientFibNum client = new ClientFibNum(args[0]);
		client.begin();
		final int value = client.runTask();
		System.out.println("The result for F(16) is "+String.valueOf(value));
		client.end();
	}
}
