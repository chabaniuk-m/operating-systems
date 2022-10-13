package os.lab1.compfunc.basic;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import os.lab1.compfunc.ComputationAttrs;
import os.lab1.compfunc.Configurations;

public class Conjunction {
    static Case<?>[] cases;

    public Conjunction() {
    }

    private static Optional<Boolean> uncheckedF(int var0) throws InterruptedException {
        Case var1 = cases[var0];
        return Configurations.genericFunc(var1.fAttrs);
    }

    private static Optional<Boolean> uncheckedG(int var0) throws InterruptedException {
        Case var1 = cases[var0];
        return Configurations.genericFunc(var1.gAttrs);
    }

    public static Optional<Boolean> trialF(int var0) throws InterruptedException {
        return Utility.sandbox(() -> {
            return uncheckedF(var0);
        });
    }

    public static Optional<Boolean> trialG(int var0) throws InterruptedException {
        return Utility.sandbox(() -> {
            return uncheckedG(var0);
        });
    }

    static {
        cases = new Case[]{new Case(new ComputationAttrs(true, 1L, TimeUnit.SECONDS), new ComputationAttrs(true, 3L, TimeUnit.SECONDS)), new Case(new ComputationAttrs((Object)null, 3L, TimeUnit.SECONDS), (ComputationAttrs) null), new Case(new ComputationAttrs(true, 1L, TimeUnit.SECONDS), (ComputationAttrs) null)};
    }
}
