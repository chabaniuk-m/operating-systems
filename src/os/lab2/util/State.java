package os.lab2.util;

public enum State {
    AVAILABLE("🆓"),
    EXECUTING("🔥"),
    DONE("▫️");

    private final String value;

    State(String val) {
        value = val;
    }

    @Override
    public String toString() {
        return value;
    }
}
