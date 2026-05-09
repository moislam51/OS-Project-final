package utils;


public class InputValidator {

    public static boolean isValid(String id, int at, int bt, int prio, int q) {
        if (q <= 0) {
            return false;
        }
        if (at < 0 || bt <= 0 || prio < 0) {
            return false;
        }

        if (id == null || id.trim().isEmpty()) {
            return false;
        }

        return true;
    }
}
