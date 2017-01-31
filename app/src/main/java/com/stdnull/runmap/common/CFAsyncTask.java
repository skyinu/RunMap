package com.stdnull.runmap.common;

public abstract class CFAsyncTask<T> {
    public abstract T onTaskExecuted(Object... params);

    public abstract void onTaskFinished(T result);
}