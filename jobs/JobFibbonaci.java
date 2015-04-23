package jobs;

import api.Job;
import api.Task;

public class JobFibbonaci implements Job<Integer>{
	private int n;
	public JobFibbonaci(int n) {
		this.n = n;
	}
	
	@Override
	public Task<Integer> toTask() {
		return null;
	}
}
