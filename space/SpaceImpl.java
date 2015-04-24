package space;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import system.Computer;
import system.ComputerProxy;
import api.Job;
import api.Space;
import api.Task;

public class SpaceImpl extends UnicastRemoteObject implements Space {
	private BlockingQueue<Task> taskQueue;
	private HashMap<Integer, Task> waitingQueue;
	private BlockingQueue resultQueue;
	private List<ComputerProxy> computerList;
	
	public SpaceImpl()  throws RemoteException {
		this.taskQueue = new LinkedBlockingQueue<Task>();
		this.computerList = new ArrayList<ComputerProxy>();
		this.waitingQueue = new HashMap<Integer, Task>();
		this.resultQueue = new LinkedBlockingQueue<Task>();
	}

	@Override
	public void exit() throws RemoteException {
	}

	@Override
	public void register(Computer computer) throws RemoteException {
		ComputerProxy computerProxy = new ComputerProxy(this, computer);
		this.computerList.add(computerProxy);
		computerProxy.start();
	}

	@Override
	public <T> Task<T> fetchTask() throws RemoteException, InterruptedException {
		return this.taskQueue.take();
	}


	public static void main(String[] args) throws RemoteException, NotBoundException {
		Space space = null;
		if(System.getSecurityManager() == null)
			System.setSecurityManager(new SecurityManager());
		try {
			space = new SpaceImpl();
			Registry registry = LocateRegistry.createRegistry(Space.PORT);
			registry.rebind(Space.SERVICE_NAME, space);
			System.out.println("Space in on, waiting for connection ...");
		} catch (Exception e) {
			System.out.println("Space Exception");
			e.printStackTrace();
		}
	}

	@Override
	public <T> void issueTask(Task<T> task) throws RemoteException {
		try {
			this.taskQueue.put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	synchronized public <T>  int suspendTask(Task<T> task) throws RemoteException {
		int id = this.waitingQueue.size();
		this.waitingQueue.put(id, task);
		return id;
	}

	@Override
	public <T> void insertArg(T arg, int id, int slotIndex)
			throws RemoteException {
		Task task = null;
		synchronized(this) {
			task = this.waitingQueue.get(id);
			task.insertArg(arg, slotIndex);	
		}
		if(task.isReady())
			this.issueTask(task);
	}

	@Override
	public <T> T take() throws RemoteException, InterruptedException {
		return (T) resultQueue.take();
	}

	@Override
	public <T> void setupResult(T result) throws RemoteException, InterruptedException {
		this.resultQueue.put(result);
	}

	@Override
	public <T> void startJob(Job<T> job) throws RemoteException,
			InterruptedException {
		this.taskQueue.put(job.toTask(this));		
	}
}
