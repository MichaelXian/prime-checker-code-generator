public class Counter {

    private int count;

    public Counter(int initialValue) {
        count = initialValue;
    }

    public void increment() {
        count++;
    }

    public int get() {
        return count;
    }
}
