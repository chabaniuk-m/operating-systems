package os.lab1.compfunc;

import java.util.concurrent.TimeUnit;

public class ComputationAttrs<R> {
    R result;
    long delay;
    TimeUnit timeUnit;

    public ComputationAttrs(R var1, long var2, TimeUnit var4) {
        this.result = var1;
        this.delay = var2;
        this.timeUnit = var4;
    }
}
