package os.lab1.src.client;

import os.lab1.compfunc.advanced.Conjunction;
import os.lab1.src.Result;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class Client {
    protected final static SocketAddress address = new InetSocketAddress("localhost", 444);
    protected static int counter = 0;

    protected static void run(boolean isF) throws IOException, InterruptedException {
        int x = requestX();
        calcAndSendResult(x, isF);
    }

    private static void calcAndSendResult(int x, boolean isF) throws IOException, InterruptedException {
        Optional<Optional<Boolean>> res = randomResult();
        int sleepTime = 1000;
        if (++counter == 1) {
            var future = calcFuncResult(x, isF);
            System.out.println("виконуються обрахунки");
            for (int i = 0; i < 100; i++) {
                if (future.isDone()) break;
                Thread.sleep(31);
            }
            System.out.println("обрахунки завершилися");
            res = Optional.empty();
            if (future.isDone()) {
                try {
                    res = future.get();
                } catch (ExecutionException e) {
                    System.out.println(e.getMessage());
                }
            }
            sleepTime = 0;
        }
        Thread.sleep(sleepTime);
        sendResult(res, x, isF);
    }

    private static Optional<Optional<Boolean>> randomResult() {
        Random random = new Random(System.currentTimeMillis());
        int i = random.nextInt();
        if (i % 5 == 0) return Optional.of(Optional.of(false));
        else if (i % 5 == 1) return Optional.of(Optional.of(true));
        else return Optional.empty();
    }

    private static Future<Optional<Optional<Boolean>>> calcFuncResult(int x, boolean isF) {
        return Executors.newSingleThreadExecutor().submit(() -> isF ? Conjunction.trialF(x) : Conjunction.trialG(x));
    }
    private static int requestX() throws IOException {
        SocketChannel client = SocketChannel.open(address);
        ByteBuffer buffer = ByteBuffer.allocate(256);
        client.read(buffer);
        buffer.flip();
        int x = 0;
        try {
            String str = toStr(buffer);
            x = Integer.parseInt(str);
            System.out.println("x = " + x);
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException");
        }
        client.close();
        return x;
    }

    private static String toStr(ByteBuffer buffer) {
        byte nil = buffer.get(buffer.limit() - 1);
        int len = buffer.limit();
        for (int i = 0; i < buffer.limit(); i++) {
            if (buffer.array()[i] == nil) {
                len = i + 1;
                break;
            }
        }
        return new String(Arrays.copyOf(buffer.array(), len));
    }

    private static void sendResult(Optional<Optional<Boolean>> res, int x, boolean isF) throws IOException, InterruptedException {
        Result result;
        int fOrG = isF ? 0 : Result.values().length / 2;
        boolean softFail = false;
        if (res.isEmpty()) {
            // Soft fail
            result = Result.valueOf(fOrG + 2);
            softFail = true;
        } else {
            if (res.get().isEmpty()) {
                // Hard fail
                result = Result.valueOf(fOrG + 3);
            } else {
                boolean b = res.get().get();
                if (b) {
                    // True
                    result = Result.valueOf(fOrG + 1);
                } else {
                    // False
                    result = Result.valueOf(fOrG);
                }
            }
        }
        SocketChannel client = SocketChannel.open(address);
        client.write(ByteBuffer.wrap(("" + result.getV()).getBytes()));
        client.close();
        if (softFail) {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            client = SocketChannel.open(address);
            client.read(buffer);
            String repl = toStr(buffer);
            System.out.println(repl);
            if (repl.equals("n")) System.exit(0);
            client.close();
            calcAndSendResult(x, isF);
        }
        System.out.println("🏁 finish");
        System.exit(0);
    }
}
