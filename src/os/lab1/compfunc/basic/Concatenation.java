package os.lab1.compfunc.basic;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import os.lab1.compfunc.ComputationAttrs;
import os.lab1.compfunc.Configurations;

public class Concatenation {
    static Case<?>[] cases;

    public Concatenation() {
    }

    private static Optional<String> uncheckedF(int var0) throws InterruptedException {
        Case var1 = cases[var0];
        return Configurations.genericFunc(var1.fAttrs);
    }

    private static Optional<String> uncheckedG(int var0) throws InterruptedException {
        Case var1 = cases[var0];
        return Configurations.genericFunc(var1.gAttrs);
    }

    public static Optional<String> trialF(int var0) throws InterruptedException {
        return Utility.sandbox(() -> {
            return uncheckedF(var0);
        });
    }

    public static Optional<String> trialG(int var0) throws InterruptedException {
        return Utility.sandbox(() -> {
            return uncheckedG(var0);
        });
    }

    static {
        cases = new Case[]{new Case(new ComputationAttrs("hello ", 1L, TimeUnit.SECONDS), new ComputationAttrs("world", 3L, TimeUnit.SECONDS)), new Case(new ComputationAttrs((Object)null, 3L, TimeUnit.SECONDS), new ComputationAttrs("world", 1L, TimeUnit.SECONDS)), new Case((ComputationAttrs)null, new ComputationAttrs((Object)null, 3L, TimeUnit.SECONDS)), new Case(new ComputationAttrs("hello", 1L, TimeUnit.SECONDS), (ComputationAttrs)null)};
    }
}
