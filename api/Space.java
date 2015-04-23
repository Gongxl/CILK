package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import system.Computer;

/**
 *
 * @author Mingrui Lyu
 */
public interface Space extends Remote 
{
    public static int PORT = 8001;
    public static String SERVICE_NAME = "Space";     
    
    <T> T take() throws RemoteException;
    
    void exit() throws RemoteException;
    
    void register(Computer computer) throws RemoteException;
    
    <T> void issueTask(List<Task<T>> taskList);
    
    <T> void resumeTask(Task<T> task);
    
    <T> Task<T> fetchTask();
    
    <T> void addTask(Task<T> task)  throws RemoteException;
}