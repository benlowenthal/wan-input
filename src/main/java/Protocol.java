import java.util.Map;

import static java.util.Map.entry;

public class Protocol {
    public static final String IP = "localhost";
    public static final int PORT = 1234;

    public static final String ACK = "ACK";

    public static final Map<String, Integer> BINDINGS = Map.ofEntries(
            entry("x",  1),
            entry("y",  2),
            entry("z",  3),
            entry("rx", 7),
            entry("ry", 8)
    );
}
