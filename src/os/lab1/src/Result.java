package os.lab1.src;

public enum Result {
    F_FALSE(0),
    F_TRUE(1),
    F_SOFT_FAIL(2),
    F_HARD_FAIL(3),
    G_FALSE(4),
    G_TRUE(5),
    G_SOFT_FAIL(6),
    G_HARD_FAIL(7);

    private int v;

    Result(int val) {
        v = val;
    }

    public static Result valueOf(int val) {
        return switch (val) {
            case 0 -> Result.F_FALSE;
            case 1 -> Result.F_TRUE;
            case 2 -> Result.F_SOFT_FAIL;
            case 3 -> Result.F_HARD_FAIL;
            case 4 -> Result.G_FALSE;
            case 5 -> Result.G_TRUE;
            case 6 -> Result.G_SOFT_FAIL;
            case 7 -> Result.G_HARD_FAIL;
            default -> throw new RuntimeException("Incorrect result value");
        };
    }

    public int getV() {
        return v;
    }
}
