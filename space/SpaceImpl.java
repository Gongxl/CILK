package space;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import system.Computer;
import system.ComputerProxy;
import api.Job;
import api.Space;
import api.Task;

public class SpaceImpl implements Space {
	private BlockingQueue<Task> taskQueue;
	private List<ComputerProxy> computerList;
	public SpaceImpl()  throws RemoteException {
		this.taskQueue = new LinkedBlockingQueue<Task>();
		this.computerList = new ArrayList<ComputerProxy>();
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
	public <T> void issueTask(List<Task<T>> newTaskList) {
		for(Task<T> task : newTaskList)
			try {
				this.taskQueue.put(task);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	@Override
	public <T> Task<T> fetchTask() {
		try {
			return this.taskQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T> void addTask(Task<T> task) {
		try {
			this.taskQueue.put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public <T> void resumeTask(Task<T> task) {
		try {
			this.taskQueue.put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public <T> T take() throws RemoteException {
		this
	}

}
