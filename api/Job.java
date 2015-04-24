package api;

public interface Job<T> {

	public Task<T> toTask(Space space);
}
