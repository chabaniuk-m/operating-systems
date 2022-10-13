package os.lab1.src.server;

import os.lab1.src.Result;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocketChannel server = ServerSocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", 444);
        server.bind(socketAddr);
        Selector selector = Selector.open();
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);

        Scanner scanner = new Scanner(System.in);
        int x = readX(scanner);
        CancellationThread cancellation = new CancellationThread(scanner);
        cancellation.start();

        SocketChannel []client = new SocketChannel[2];
        for (int i = 0; i < 2; i++) {
            selector.select();
            SelectionKey key = selector.selectedKeys().iterator().next();
            if (key.isAcceptable()) {
                client[i] = server.accept();
                client[i].write(ByteBuffer.wrap((x + "").getBytes()));
            }
        }
        Result[] arr = new Result[2];
        int limit = 2;
        for (int j = 0; j < limit; j++) {
            selector.select();
            SelectionKey key = selector.selectedKeys().iterator().next();
            var res = readResult(server);
            arr[Math.min(j, 1)] = res;
            if (res.getV() < 4) {
                switch (res) {
                    case F_HARD_FAIL -> processHardFail("f");
                    case F_SOFT_FAIL -> {
                        if (!cancellation.isInterrupted()) cancellation.interrupt();
                        ++limit;
                        processSoftFail("f", selector, limit - 2);
                    }
                    case F_TRUE -> System.out.printf("f(%d) = true\n", x);
                    case F_FALSE -> System.out.printf("f(%d) = false\n", x);
                }
            } else {
                switch (res) {
                    case G_HARD_FAIL -> processHardFail("g");
                    case G_SOFT_FAIL -> {
                        if (!cancellation.isInterrupted()) cancellation.interrupt();
                        ++limit;
                        processSoftFail("g", selector, limit - 2);
                    }
                    case G_TRUE -> System.out.printf("g(%d) = true\n", x);
                    case G_FALSE -> System.out.printf("g(%d) = false\n", x);
                }
            }
        }
        if (arr[0] == Result.F_TRUE && arr[1] == Result.G_TRUE) {
            System.out.printf("f(%d) ∧ g(%d) = true\n", x, x);
        } else {
            System.out.printf("f(%d) ∧ g(%d) = false\n", x, x);
        }
        System.exit(0);
    }

    private static void processSoftFail(String funcName, Selector selector, int attempts) throws IOException {
        System.out.println("❗Soft fail of function " + funcName);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Try again? y/n: ");
        String repl = scanner.next();
        if (repl.equals("y")) {
            selector.select();
            SelectionKey key = selector.selectedKeys().iterator().next();
            SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
            client.write(ByteBuffer.wrap("y".getBytes()));
            client.close();
        } else if (repl.equals("c")) {
            System.out.println("✖ Execution of the program is cancelled");
            System.exit(1);
        } else {
            selector.select();
            SelectionKey key = selector.selectedKeys().iterator().next();
            SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
            client.write(ByteBuffer.wrap("n".getBytes()));
            client.close();
            System.out.println("🤷🏻 It is impossible to define the result of computation\nbecause of soft fail of function " + funcName);
            System.out.println("Number of attempts to obtain the result - " + attempts);
            System.exit(1);
        }
    }

    private static Result readResult(ServerSocketChannel server) throws IOException {
        SocketChannel client = server.accept();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        client.read(buffer);
        return Result.valueOf(Integer.parseInt(new String(Arrays.copyOf(buffer.array(), 1))));
    }

    private static int readX(Scanner scanner) {
        int x;
        scanner = new Scanner(System.in);
        while (true) {
            System.out.print("x=");
            try {
                x = Integer.parseInt(scanner.next());
                if (!(0 <= x && x <= 2)) {
                    System.out.printf("%d is not the domain of definition of functions f & g\n", x);
                } else {
                    break;
                }
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Enter a natural number");
            }
        }
        return x;
    }

    private static void processHardFail(String funcName) {
        System.out.println("❌ Hard fail of function " + funcName);
        System.exit(1);
    }
}
