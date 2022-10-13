package os.lab1.compfunc.basic;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import os.lab1.compfunc.ComputationAttrs;
import os.lab1.compfunc.Configurations;

public class DoubleOps {
    static Case<?>[] cases;

    public DoubleOps() {
    }

    private static Optional<Double> uncheckedF(int var0) throws InterruptedException {
        Case var1 = cases[var0];
        return Configurations.genericFunc(var1.fAttrs);
    }

    private static Optional<Double> uncheckedG(int var0) throws InterruptedException {
        Case var1 = cases[var0];
        return Configurations.genericFunc(var1.gAttrs);
    }

    public static Optional<Double> trialF(int var0) throws InterruptedException {
        return Utility.sandbox(() -> {
            return uncheckedF(var0);
        });
    }

    public static Optional<Double> trialG(int var0) throws InterruptedException {
        return Utility.sandbox(() -> {
            return uncheckedG(var0);
        });
    }

    static {
        cases = new Case[]{new Case(new ComputationAttrs(3.0D, 1L, TimeUnit.SECONDS), new ComputationAttrs(5.0D, 3L, TimeUnit.SECONDS)), new Case((ComputationAttrs)null, new ComputationAttrs((Object)null, 3L, TimeUnit.SECONDS)), new Case(new ComputationAttrs(3.0D, 1L, TimeUnit.SECONDS), (ComputationAttrs)null)};
    }
}
