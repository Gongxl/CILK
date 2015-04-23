package jobs;

import java.util.concurrent.Callable;

import api.Job;
import api.Space;

public class JobRunner<T> implements Callable<T> {
	private Job<T> job;
	private Space space;
	public JobRunner(Space space, Job<T> job) {
		this.job = job;
		this.space = space;
	}

	@Override
	public T call() throws Exception {
		this.space.addTask(job.toTask());
		return this.space.take();
	}
}
