package com.stdnull.runmap.common;

/**
 * 耗时任务抽象
 * @param <T>
 */
public abstract class CFAsyncTask<T> {
    public abstract T onTaskExecuted(Object... params);

    public abstract void onTaskFinished(T result);
}