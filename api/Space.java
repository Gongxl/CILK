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
    
    <T> T take() throws RemoteException, InterruptedException;
    
    void exit() throws RemoteException;
    
    void register(Computer computer) throws RemoteException;
    
    <T> void issueTask(Task<T> task) throws RemoteException;
    
    <T> int suspendTask(Task<T> task) throws RemoteException;
    
    <T> void insertArg(T arg, int id, int slotIndex) throws RemoteException;
    
    <T> Task<T> fetchTask() throws RemoteException, InterruptedException;    
    
    <T> void setupResult(T result) throws RemoteException, InterruptedException;
    
    <T> void startJob(Job<T> job) throws RemoteException, InterruptedException; 
}