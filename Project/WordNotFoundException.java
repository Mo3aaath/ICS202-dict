package Project;
public class WordNotFoundException extends Exception{
    public WordNotFoundException() {
        super("not found");
    }
    public WordNotFoundException(String s) {
        super(s + "not found");
    }
}
