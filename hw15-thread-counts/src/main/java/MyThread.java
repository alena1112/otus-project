public class MyThread extends Thread {
    private final int threadId;

    public MyThread(int threadId, Runnable target) {
        super(target);
        this.threadId = threadId;
    }

    public int getThreadId() {
        return threadId;
    }
}
