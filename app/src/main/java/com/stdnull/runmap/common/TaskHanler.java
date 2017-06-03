package com.stdnull.runmap.common;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * 任务处理类
 * Created by chen on 2017/1/28.
 */

public class TaskHanler {
    private static TaskHanler mInsatnce= new TaskHanler();
    private Looper mLooper;
    private HandlerThread mTaskThread;
    private Handler mTaskHandler;
    private Handler mUIHandler;
    private TaskHanler(){
        HandlerThread thread = new HandlerThread("task");
        thread.start();
        mTaskHandler = new Handler(thread.getLooper());
        mUIHandler = new Handler(Looper.getMainLooper());
    }

    public static TaskHanler getInstance(){
        return mInsatnce;
    }

    public void sendTask(final CFAsyncTask task, final Object ...params){
        Runnable action = new Runnable() {
            @Override
            public void run() {
                Object result = task.onTaskExecuted(params);
                dispatchResultToMainThread(task,result);
            }
        };
        mTaskHandler.post(action);

    }

    public void sendTaskDelayed(Runnable runnable, int time){
        mTaskHandler.postDelayed(runnable, time);
    }

    public void sendTaskDelayed(final CFAsyncTask task,int time, final Object ...params){
        Runnable action = new Runnable() {
            @Override
            public void run() {
                Object result = task.onTaskExecuted(params);
                dispatchResultToMainThread(task,result);
            }
        };
        mTaskHandler.postDelayed(action,time);

    }
    private void dispatchResultToMainThread(CFAsyncTask task,Object result){
        ResultAction action = new ResultAction(task,result);
        mUIHandler.post(action);
    }
    private class ResultAction implements Runnable{
        private CFAsyncTask task;
        private Object result;

        public ResultAction(CFAsyncTask task,Object result){
            this.task = task;
            this.result = result;
        }

        @Override
        public void run() {
            task.onTaskFinished(result);
        }
    }

    public void quit(){
        mTaskThread.quit();
    }
}
