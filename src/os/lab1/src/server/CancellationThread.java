package os.lab1.src.server;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CancellationThread extends Thread {
    private final Scanner scanner;
    private boolean stop = false;
    private final Selector selector;

    public CancellationThread(Scanner scanner, Selector selector) {
        this.scanner = scanner;
        this.selector = selector;
    }

    @Override
    public void run() {
        while (true) {
            try {
                var s = scanner.nextLine();
                if (stop) return;
                if (s.equals("c")) {
                    System.out.println("✖ Execution of the program is cancelled");
                    AServer.exit(1);
                }
            } catch (NoSuchElementException | IOException | InterruptedException e) {
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
