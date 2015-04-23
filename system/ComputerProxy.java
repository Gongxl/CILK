package system;

import java.rmi.RemoteException;
import api.Result;
import api.Space;
import api.Task;
import api.Closure;

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
			Result result = null;
			Task task = null;
			try {
				synchronized(this) {
					task = space.fetchTask();
//					if(!this.running) {
//						space.addTask(task);
//						return;
//					}
					result = computer.executeTask(task);
				}

				// After calling executeTask, the closure of the 'executed' 
				// task is converted:
				// 1.	if task is successfully executed, the closure 
				// 	  	becomes a READY closure, it contains the calculated
				//		result;
				// 2.	if not, the closure becomes a WAITING closure,
				// 		it will later inherited by its child tasks as
				//		a continuation, waiting to be filled when its
				//		child tasks finish.
				// 3.   if task is executed and its continuation is null,
				// 		its closure will become FINAL
				if(result.closure.getType() == Closure.Type.WAITING)
					space.issueTask(task.getSubtaskList());
				else if(result.closure.getType() == Closure.Type.FINISH){
					// feedback the result to task's continuation
					task.feedback(result.closure.getAnswer());
					// check if the task's continuation has zero missing 
					// arguments now. We only need to check the continuation
					// at this moment. If there is no missing args, the
					// parent task would have been executed. 
					if(task.isParentReady())
						space.resumeTask(task.getParent());
				} else { // closure is final
					
				}
			} catch (RemoteException e) {
				e.printStackTrace();
				try {
					space.addTask(task);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
}
