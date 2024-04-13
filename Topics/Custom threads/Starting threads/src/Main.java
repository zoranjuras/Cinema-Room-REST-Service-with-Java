public class Main {

    public static void main(String[] args) {

        // create threads and start them using the class RunnableWorker
        for (int i = 1; i <= 3; i++) {
            new Thread(new RunnableWorker(), "worker-" + i).start();
        }
    }
}

// Don't change the code below       
class RunnableWorker implements Runnable {

    @Override
    public void run() {
        final String name = Thread.currentThread().getName();

        if (name.startsWith("worker-")) {
            System.out.println("too hard calculations...");
        } else {
            return;
        }
    }
}