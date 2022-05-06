public class Main {
    public static void main(String[] args) {
        var threadCounts = new ThreadCounts();
        new MyThread(1, threadCounts::count).start();
        new MyThread(2, threadCounts::count).start();
    }
}
