package api;

import java.util.ArrayList;
import java.util.List;

public class Closure <T> {
	private List<T> argList;
	private int missingArgCount;
	public final Task<T> task;
	private Type type;
	public enum Type {WAITING, READY, FINAL}
	
	public Closure(int missingArgCount, List<T> argList, Task<T> task) {
		this.task = task;
		this.missingArgCount = missingArgCount;
		this.argList = argList;
	}
	
	public T getArg(int index) {
		if(index > argList.size() - 1 || index <= 0)
			return null;
		else return argList.get(index);
	}
	
	public int getArgCount() {
		return this.argList.size();
	}
	
	public void insertArg(T arg, int index) {
		this.argList.set(index, arg);
		assert this.missingArgCount > 0;
		this.missingArgCount --;
	}
	
	public int getMissingArgCount() {
		return this.missingArgCount;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public T getResult() {
		assert this.type == Type.READY;
		return this.argList.get(0);
	}
	
	public void putResult(T result) {
		this.argList.set(1, result);
	}
}