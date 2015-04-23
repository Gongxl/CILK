package tasks;

import java.util.ArrayList;
import java.util.List;

import api.Closure;
import api.Task;

public class TaskFibonacci extends Task<Integer> {
	public static final long serialVersionUID = 227L;
	private int n;
	public TaskFibonacci(Closure<Integer> continuation, int slotIndex, int n) {
		super(continuation, slotIndex);
		List<Integer> argList = new ArrayList<Integer>();
		this.n = n;
		if(n >= 2) {
			argList.add(-1);
			argList.add(-1);
			this.closure = new Closure<Integer>(2, argList, this);
		} else {
			argList.add(n);
			this.closure = new Closure<Integer>(0, argList, this);
		}
		this.evolve();
	}
	
	@Override
	public Boolean call() throws Exception {
		int result;
		if(isReady()) {
			if(this.closure.getArgCount() == 1)
				result = this.closure.getArg(0);
			else result = this.closure.getArg(0) + this.closure.getArg(1);
			this.closure.putResult(result);
			return true;
		} else {
			this.spawn(new TaskFibonacci(this.closure, 0, n - 1));
			this.spawn(new TaskFibonacci(this.closure, 1, n - 2));
			return false;
		}
	}
}
