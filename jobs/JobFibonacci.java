package jobs;

import java.io.Serializable;

import tasks.TaskFibonacci;
import api.Job;
import api.Space;
import api.Task;

public class JobFibonacci implements Job<Integer>, Serializable{
	public static final long serialVersionUID = 227L;
	private int n;
	public JobFibonacci(int n) {
		this.n = n;
	}
	@Override
	public Task<Integer> toTask(Space space) {
		return new TaskFibonacci(space, Task.NO_PARENT, Task.NO_PARENT, n);
	}
	@Override
	public String toString() {
		
		return "JobFibonacci: " + this.n;
	}	
}
