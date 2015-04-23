package api;

import java.util.concurrent.Callable;

public interface Job<T> {
	public Task<T> toTask();
}
