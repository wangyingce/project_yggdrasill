package cc.leevi.webbase.utils;

public class MathUtils {

    /**
     * 除法向上取整
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return
     */
    public static int ceilDiv(int dividend, int divisor) {
        int x = Math.floorDiv(dividend, divisor);
        if (Math.floorMod(dividend, divisor) != 0) {
            x = Math.floorDiv(dividend, divisor) + 1;
        }
        return x;
    }

    /**
     * 除法向上取整
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return
     */
    public static long ceilDiv(long dividend, long divisor) {
        long x = Math.floorDiv(dividend, divisor);
        if (Math.floorMod(dividend, divisor) != 0) {
            x = Math.floorDiv(dividend, divisor) + 1;
        }
        return x;
    }
}
