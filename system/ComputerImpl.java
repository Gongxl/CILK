package system;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import api.Closure;
import api.Result;
import api.Space;
import api.Task;

public class ComputerImpl implements Computer {
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
	public <T> Result executeTask(Task<T> t) throws RemoteException {
		this.startTime = System.currentTimeMillis();
		System.out.println("Executing Task: " + t);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Boolean> future = executor.submit(t);
		try {
			future.get();
			this.endTime = System.currentTimeMillis();
			return new Result(t.getClosure(), this.endTime - this.startTime);
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
