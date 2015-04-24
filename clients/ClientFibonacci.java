package clients;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import jobs.JobFibonacci;

public class ClientFibonacci extends Client<Integer> {
	public ClientFibonacci(String domainName)
			throws RemoteException, NotBoundException, MalformedURLException {
		super("Client Fibonacci", domainName, new JobFibonacci(16));
	}

	public static void main(String[] args) throws Exception {
		System.setSecurityManager(new SecurityManager());
		final ClientFibonacci client = new ClientFibonacci(args[0]);
		client.begin();
		final int value = client.runTask();
		System.out.println("F(16) is "+ value);
		client.end();
	}
}
