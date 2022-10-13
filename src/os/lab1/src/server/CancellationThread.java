package os.lab1.src.server;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class CancellationThread extends Thread {
    private final Scanner scanner;
    private boolean stop = false;

    public CancellationThread(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void run() {
        while (true) {
            try {
                var s = scanner.next();
                if (stop) return;
                if (s.equals("c")) {
                    System.out.println("✖ Execution of the program is cancelled");
                    System.exit(1);
                }
            } catch (NoSuchElementException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void interrupt() {
        stop = true;
        super.interrupt();
    }
}
