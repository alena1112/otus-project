import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadCounts {
    private static final Logger logger = LoggerFactory.getLogger(ThreadCounts.class);

    private int count = 0;
    private boolean isIncrease = true;
    private int currentThreadId = 2;

    public synchronized void count() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (((MyThread) Thread.currentThread()).getThreadId() == currentThreadId) {
                    this.wait();
                }

                currentThreadId = ((MyThread) Thread.currentThread()).getThreadId();
                if (currentThreadId == 1) {
                    setCount();
                }
                logger.info("Поток {} {}", currentThreadId, count);
                sleep();

                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void setCount() {
        if (count == 1) {
            isIncrease = true;
        } else if (count == 10) {
            isIncrease = false;
        }
        if (isIncrease) {
            count++;
        } else {
            count--;
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
