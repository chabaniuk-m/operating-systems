package os.lab1.compfunc.basic;

import java.util.Optional;

public class Disjunction {
    public Disjunction() {
    }

    public static Optional<Boolean> trialF(int var0) throws InterruptedException {
        return Conjunction.trialG(var0);
    }

    public static Optional<Boolean> trialG(int var0) throws InterruptedException {
        return Conjunction.trialF(var0);
    }
}
