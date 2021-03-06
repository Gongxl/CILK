package api;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Task is an encapsulation of some computation. It is implements Serializable
 * interface since this object will be serialized and deserialized in the RMI
 * procedure.
 * 
 * @author Gongxl
 *
 * @param <V>
 */
public abstract class Task<V> implements Serializable {
	protected int slotIndex;
	protected int parentId;
	protected List<V> argList;
	protected int missingArgCount;
	protected Space space;

	protected static final int WAITING_ANSWER = -1;
	public static final int NO_PARENT = -1;

	/**
	 * Execute tasks.
	 * 
	 * @throws RemoteException
	 *             occurs if there is a communication problem or the remote
	 *             service is not responding.
	 */
	abstract public void run() throws RemoteException;

	/**
	 * Constructor
	 * 
	 * @param space
	 *            the Space where the tasks are put into.
	 * @param parentId
	 *            the ID of the successor.
	 * @param slotIndex
	 *            the position where the missing argument belongs to.
	 */
	public Task(Space space, int parentId, int slotIndex) {
		this.slotIndex = slotIndex;
		this.parentId = parentId;
		this.space = space;
		this.argList = new ArrayList<V>();
	}

	/**
	 * Set successor's ID.
	 * 
	 * @param parentId
	 *            the ID of the successor.
	 */
	public void setId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * Check whether the task is ready to be executed.
	 * 
	 * @return true if the number of missing arguments in the closure is zero.
	 *         Otherwise, the task is not ready.
	 */
	public boolean isReady() {
		return this.missingArgCount <= 0 ? true : false;
	}

	/**
	 * Generate the child task and put it into TaskQueue, which is in the space.
	 * 
	 * @param task
	 *            the child task.
	 * @throws RemoteException
	 *             occurs if there is a communication problem or the remote
	 *             service is not responding.
	 */
	public void spawn(Task<V> task) throws RemoteException {
		this.space.issueTask(task);
	}

	/**
	 * Space set the appropriate input element of the successor task. If the
	 * task is the root task, put the result in the result queue.
	 * 
	 * @param result
	 *            produced by the current task is the input of its successor
	 *            task.
	 * @throws RemoteException
	 *             occurs if there is a communication problem or the remote
	 *             service is not responding.
	 */
	public void feedback(V result) throws RemoteException {
		if (this.parentId == NO_PARENT)
			try {
				this.space.setupResult(result);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		else
			this.space.insertArg(result, this.parentId, this.slotIndex);
	}

	/**
	 * Get the argument in the corresponding slot of the closure.
	 * 
	 * @param index
	 *            the slot's index in the closure.
	 * @return the argument of the corresponding slot.
	 */
	public V getArg(int index) {
		if (index > argList.size() - 1 || index < 0)
			return null;
		else
			return argList.get(index);
	}

	/**
	 * Get the number of arguments that already received by the successor.
	 * 
	 * @return the size of the argument list.
	 */
	public int getArgCount() {
		return this.argList.size();
	}

	/**
	 * Insert the argument to the slot where it belongs to and decrease the
	 * number of the missing arguments.
	 * 
	 * @param arg
	 *            the input of current task's successor.
	 * @param index
	 *            the index of the slot where the argument should insert.
	 */
	public void insertArg(V arg, int index) {
		this.argList.set(index, arg);
		assert this.missingArgCount > 0;
		this.missingArgCount--;
	}

}
