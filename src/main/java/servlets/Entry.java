package servlets;

/**
 * Created by tayvs on 05.12.2016.
 */
public class Entry {

    private final String tag, value;

    public Entry(String tag, String value) {
        this.tag = tag;
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "tag = " + tag + "\t" + "value = " + value;
    }
}
