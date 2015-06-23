package persisters;

public enum TermType {

    WORD("word"),
    TERM("term");

    private final String type;

    TermType(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
}
