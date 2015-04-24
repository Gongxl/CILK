package system;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import api.Space;
import api.Task;

public class ComputerImpl extends UnicastRemoteObject implements Computer {
	public static final long serialVersionUID = 227L; 	
	private long startTime;
	private long endTime;
	
	public ComputerImpl() throws RemoteException {
		super();
	}
	
	public static void main(String[] args) {
		if(System.getSecurityManager() == null)
			System.setSecurityManager(new SecurityManager());
		
		try {
			Computer computer = new ComputerImpl();
			String url = "rmi://" + args[0] + ":" + Space.PORT + "/"
					+ Space.SERVICE_NAME;
			Space space = (Space) Naming.lookup(url);
			space.register(computer);
		} catch (Exception e) {
			System.out.println("ComputeEngine Exception");
			e.printStackTrace();
		}
		System.out.println("Computer is running");
	}
	
	@Override
	public void exit() throws RemoteException {			
		System.exit(0);
	}

	@Override
	public <T> long executeTask(Task<T> task) throws RemoteException {
		this.startTime = System.currentTimeMillis();
		System.out.println("Executing Task: " + task);
		task.run();
		this.endTime = System.currentTimeMillis();
		return this.endTime - this.startTime;
		
	}
}
