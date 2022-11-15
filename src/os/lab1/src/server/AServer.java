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
    private static int nFSoftFails = 0;
    private static int nGSoftFails = 0;
    private static final int maxAttempts = 3;
    private static final ServerSocketChannel exitServer;
    private static ServerSocketChannel server;
    private static int x;

    static {
        try {
            exitServer = ServerSocketChannel.open();
            exitServer.bind(new InetSocketAddress("localhost", 5000));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        server = ServerSocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", 4000);
        server.bind(socketAddr);
        Selector selector = Selector.open();
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);

        Scanner scanner = new Scanner(System.in);
        x = readX();
        CancellationThread cancellation = new CancellationThread(scanner);
        cancellation.start();

        SocketChannel client;
        for (int i = 0; i < 2; i++) {
            selector.select();
            SelectionKey key = selector.selectedKeys().iterator().next();
            if (key.isAcceptable()) {
                client = server.accept();
                client.write(ByteBuffer.wrap((x + "").getBytes()));
                client.close();
            }
        }
        Result[] arr = new Result[2];
        int limit = 2;
        StringBuilder hardFailFuncs = new StringBuilder();
        for (int j = 0; j < limit; j++) {
            selector.select();
            var res = readResult(server);
            arr[Math.min(j, 1)] = res;
            if (res.getV() < 4) {
                switch (res) {
                    case F_HARD_FAIL -> hardFailFuncs.append("f");
                    case F_SOFT_FAIL -> {
                        if (!cancellation.isInterrupted()) cancellation.interrupt();
                        limit = processSoftFail(hardFailFuncs, "f", selector, limit);
                        cancellation = new CancellationThread(scanner);
                        cancellation.start();
                    }
                    case F_TRUE -> System.out.printf("f(%d) = true\n", x);
                    case F_FALSE -> System.out.printf("f(%d) = false\n", x);
                }
            } else {
                switch (res) {
                    case G_HARD_FAIL -> hardFailFuncs.append("g");
                    case G_SOFT_FAIL -> {
                        if (!cancellation.isInterrupted()) cancellation.interrupt();
                        limit = processSoftFail(hardFailFuncs, "g", selector, limit);
                        cancellation = new CancellationThread(scanner);
                        cancellation.start();
                    }
                    case G_TRUE -> System.out.printf("g(%d) = true\n", x);
                    case G_FALSE -> System.out.printf("g(%d) = false\n", x);
                }
            }
        }
        processPossibleHardFail(hardFailFuncs.toString());
        if (arr[0] == Result.F_TRUE && arr[1] == Result.G_TRUE) {
            System.out.printf("f(%d) ∧ g(%d) = true\n", x, x);
        } else {
            System.out.printf("f(%d) ∧ g(%d) = false\n", x, x);
        }
        cancellation.interrupt();
        exit(0);
    }

    public static synchronized void exit(int code) throws IOException, InterruptedException {
        server.close();
        ByteBuffer buffer = ByteBuffer.wrap(("" + code).getBytes());
        SocketChannel client = exitServer.accept();
        client.write(buffer);
        client.close();
        client = exitServer.accept();
        client.write(buffer);
        client.close();
        if (code == 1) {
            System.out.printf("f(%d) ∧ g(%d) = <fail>\n", x, x);
        } else if (code == 3) {
            System.out.printf("f(%d) ∧ g(%d) = <undefined>\n", x, x);
        } else if (code == 5) {
            System.out.printf("f(%d) ∧ g(%d) = <execution canceled>\n", x, x);
        }
        System.exit(code);
    }

    private static void processPossibleHardFail(String funcs) throws IOException, InterruptedException {
        boolean exit = false;
        if (!funcs.isEmpty()) {
            System.out.println("❌ Hard fail of function " + funcs.charAt(0));
            exit = true;
            if (funcs.length() == 2) {
                System.out.println("❌ Hard fail of function " + funcs.charAt(1));
            }
        }
        if (exit) {
            exit(1);
        }
    }

    public static void interruptClient(Selector selector) throws IOException {
        selector.select();
        SelectionKey key = selector.selectedKeys().iterator().next();
        SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
        client.write(ByteBuffer.wrap("n".getBytes()));
        client.close();
    }

    private static int processSoftFail(StringBuilder hardFailFuncs, String funcName, Selector selector, int limit) throws IOException, InterruptedException {
        if (hardFailFuncs.toString().equals("")) {
            ++limit;
            processSoftFail(funcName, selector);
        } else {
            interruptClient(selector);
        }
        return limit;
    }

    private static void processSoftFail(String funcName, Selector selector) throws IOException, InterruptedException {
        System.out.println("❗Soft fail of function " + funcName);

        if (funcName.equals("f")) {
            if (nFSoftFails++ == maxAttempts) {
                interruptClient(selector);
                System.out.println("✖ Maximum amount of attempts to obtain value is reached");
                System.exit(3);
            }
        } else {
            if (nGSoftFails++ == maxAttempts) {
                interruptClient(selector);
                System.out.println("✖ Maximum amount of attempts to obtain value is reached");
                System.exit(2);
            }
        }
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 1; i++) {
            System.out.print("Try again? y/n: ");
            String repl = scanner.next();
            if (repl.equals("y")) {
                selector.select();
                SelectionKey key = selector.selectedKeys().iterator().next();
                SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
                client.write(ByteBuffer.wrap("y".getBytes()));
                client.close();
            } else if (repl.equals("n")) {
                System.out.println("🤷🏻 It is impossible to define the result of computation\nbecause of soft fail of function " + funcName);
                System.out.println("Number of attempts to obtain the result - " + (funcName.equals("f") ? nFSoftFails : nGSoftFails));
                exit(2);
            } else --i;
        }
    }

    private static Result readResult(ServerSocketChannel server) throws IOException {
        SocketChannel client = server.accept();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        client.read(buffer);
        return Result.valueOf(Integer.parseInt(new String(Arrays.copyOf(buffer.array(), 1))));
    }

    private static int readX() {
        int x;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("x=");
            try {
                x = Integer.parseInt(scanner.next());
                if (!(0 <= x && x <= 2)) {
                    System.out.printf("%d is not the domain of definition of functions f & g\n", x);
                    exit(3);
                }
                break;
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Enter a natural number");
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return x;
    }
}
