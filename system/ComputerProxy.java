package system;

import java.rmi.RemoteException;

import api.Space;
import api.Task;

public class ComputerProxy extends Thread {
	private Computer computer;
	private Space space;
	private boolean running;
	public ComputerProxy(Space space, Computer computer) {
		this.space = space;
		this.computer = computer;
		this.running = false;
	}
	@Override
	public void run() {
		super.run();
		System.out.println("Start a new proxy!");
		this.running = true;
		while(this.running) {
			Task task = null;
			try {
				task = this.space.fetchTask();
				long time = computer.executeTask(task);
				System.out.println("Task running time: " + time);
			} catch (RemoteException | InterruptedException e) {
				e.printStackTrace();
				try {
					this.space.issueTask(task);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				this.running = false;
			}
		}
	}
	
}
