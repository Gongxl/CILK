package tasks;

import java.rmi.RemoteException;

import api.Space;
import api.Task;

/**
 * TaskFibonacci extends from the Task abstract class. It calculates the given
 * Fibonacci Number.
 * 
 * @author Gongxl
 *
 */
public class TaskFibonacci extends Task<Integer> {
	public static final long serialVersionUID = 227L;
	private int n;

	/**
	 * Constructor. If the Fibonacci number to be calculated is bigger than 2,
	 * put -1 to this task's argument list and set the number of missing
	 * arguments to 2. Otherwise, put the input number in the argument slot and
	 * set the number of missing arguments to -1.
	 * 
	 * @param space
	 *            the Space where the tasks are put into.
	 * @param parentId
	 *            the ID of the successor.
	 * @param slotIndex
	 *            the position where the missing argument belongs to.
	 * @param n
	 *            the nth Fibonacci number to be calculated.
	 */
	public TaskFibonacci(Space space, int parentId, int slotIndex, int n) {
		super(space, parentId, slotIndex);
		this.n = n;
		if (n >= 2) {
			this.argList.add(WAITING_ANSWER);
			this.argList.add(WAITING_ANSWER);
			this.missingArgCount = 2;
		} else {
			this.argList.add(n);
			this.missingArgCount = -1;
		}
	}

	/**
	 * When the number of the missing arguments is -1, set the argument in the
	 * first slot of the closure as the result. 
	 * When the number of the missing
	 * arguments is 0, set the sum of the two arguments in the closure as the
	 * result. 
	 * In other cases, spawn two child tasks F(n-1) and F(n-2).
	 */
	@Override
	public void run() throws RemoteException {
		if (this.missingArgCount <= 0) {
			int result;
			if (this.missingArgCount == -1)
				result = this.getArg(0);
			else
				result = this.getArg(0) + this.getArg(1);
			this.feedback(result);
		} else {
			int parentId = this.space.suspendTask(this);
			this.spawn(new TaskFibonacci(this.space, parentId, 0, n - 1));
			this.spawn(new TaskFibonacci(this.space, parentId, 1, n - 2));
		}
	}
}
