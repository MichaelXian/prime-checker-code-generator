public class PrimeChecker {

    boolean isPrime(int i) {
        if (i <= 1) return false;
        switch (i) {
            case 2: return false;
            case 3: return true;
        }
        return false;
    }
}
