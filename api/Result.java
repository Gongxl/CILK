package api;

import java.io.Serializable;

/**
 *
 * @author Mingrui Lyu
 * @param <T> type of return value of corresponding Task.
 */
public class Result implements Serializable
{
    public final Closure closure;
    public final long taskRunTime;
    public Result(Closure closure, long taskRunTime)
    {
        assert closure != null;
        assert taskRunTime >= 0;
        this.closure = closure;
        this.taskRunTime = taskRunTime;
    }
    
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( getClass() );
        stringBuilder.append( "\n\tExecution time:\n\t" ).append( taskRunTime );
        stringBuilder.append( "\n\tReturn value:\n\t" ).append( closure.toString() );
        return stringBuilder.toString();
    }
}