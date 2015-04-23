package clients;

import java.awt.BorderLayout;
import java.awt.Container;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import jobs.JobRunner;
import space.SpaceImpl;
import api.Job;
import api.Space;
/**
 *
 * @author Peter Cappello
 * @param <T>
 * return type the Task that this Client executes.
 */
public class Client<T> extends JFrame {
	protected T taskReturnValue;
	private long clientStartTime;
	private Job<T> job;
	private Space space;
	public Client(final String title, final String domainName,
			final Job<T> job) throws RemoteException, NotBoundException,
			MalformedURLException {
		this.job = job;
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		String url = "rmi://" + domainName + ":" + Space.PORT + "/"
				+ Space.SERVICE_NAME;
		this.space = (Space) Naming.lookup(url);	
	}

	public void begin() {
		clientStartTime = System.nanoTime();
	}

	public void end() {
		Logger.getLogger(Client.class.getCanonicalName()).log(Level.INFO,
				"Client time: {0} ms.",
				(System.nanoTime() - clientStartTime) / 1000000);
	}

	public void add(final JLabel jLabel) {
		final Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(new JScrollPane(jLabel), BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

	public T runTask() throws RemoteException {
		final long taskStartTime = System.nanoTime();
		JobRunner<T> jobRunner = new JobRunner<T>(this.space, this.job);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<T> future = executor.submit(jobRunner);
		T value = null;
		try {
			value = future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}		
		System.out.println("Final result" + value);
		final long taskRunTime = (System.nanoTime() - taskStartTime) / 1000000;
		Logger.getLogger(Client.class.getCanonicalName()).log(Level.INFO,
				"Task {0}Task time: {1} ms.",
				new Object[] { job, taskRunTime });
		return value;
	}
}