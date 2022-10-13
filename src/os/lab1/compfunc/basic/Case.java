package os.lab1.compfunc.basic;

import java.util.Optional;
import os.lab1.compfunc.ComputationAttrs;

public class Case<T> {
    public Optional<ComputationAttrs<T>> fAttrs;
    public Optional<ComputationAttrs<T>> gAttrs;

    public Case(ComputationAttrs<T> var1, ComputationAttrs<T> var2) {
        this.fAttrs = Optional.ofNullable(var1);
        this.gAttrs = Optional.ofNullable(var2);
    }
}