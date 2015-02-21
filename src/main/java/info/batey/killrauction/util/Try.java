package info.batey.killrauction.util;

import java.util.Optional;

/*
Repalce with Guava once I have connectivity
 */
public class Try<A, B extends Throwable> {
    private A left;
    private B right;

    private Try(A left, B right) {
        this.left = left;
        this.right = right;
    }

    public static <A> Try success(A value) {
        return new Try<>(value, null);
    }


    public static <B extends Throwable> Try failure(B exception) {
        return new Try<>(null, exception);
    }

}
