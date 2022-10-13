package os.lab1.compfunc.basic;

import java.util.Optional;
import java.util.concurrent.Callable;

class Utility {
    Utility() {
    }

    public static <T> Optional<T> sandbox(Callable<Optional<T>> var0) throws InterruptedException {
        try {
            return (Optional)var0.call();
        } catch (ArrayIndexOutOfBoundsException var2) {
            return Optional.empty();
        } catch (InterruptedException var3) {
            throw var3;
        } catch (Exception var4) {
            var4.printStackTrace();
            return Optional.empty();
        }
    }
}