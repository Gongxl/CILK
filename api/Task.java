package api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import api.Closure.Type;

public abstract class Task<V> implements Serializable, Callable<Boolean> 
{
	protected Closure<V> closure;
	protected Closure<V> continuation;
	private int slotIndex;
	protected ArrayList<Task> subtaskList;
	public Task(Closure<V> continuation) {
		this.closure = null;
		this.continuation = continuation;
		this.evolve();
	}
	@Override
	abstract public Boolean call() throws Exception;	
	public void spawn(Task task){
		if(this.subtaskList == null)
			this.subtaskList = new ArrayList<Task>();
		this.subtaskList.add(task);
	}
	
	public List<Task> getSubtaskList() {
		return this.subtaskList;
	}
	
	protected void evolve() {
		assert this.closure != null;
		if(this.isReady()) {
			if(this.continuation == null)
				this.closure.setType(Type.FINAL);
			else this.closure.setType(Type.READY);
		} else this.closure.setType(Type.WAITING);
	}
	
	public Task<V> getParent() {
		return this.continuation.task;
	}
	
	public Closure<V> getClosure() {
		return this.closure;
	}
	
	public boolean isParentReady() {
		return (this.continuation.getMissingArgCount() == 0);
	}
	
	protected boolean isReady() {
		return (this.closure.getMissingArgCount() == 0) ? true : false;
	}
	
	public void feedback(V result) {
		assert this.continuation != null;
		this.continuation.insertArg(result, this.slotIndex);
	}
}
