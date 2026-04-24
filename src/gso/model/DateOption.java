package gso.model;

public class DateOption {
    public final String value;
    public final String label;

    public DateOption(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
