package Project;
public class WordAlreadyExistsException extends Exception {
    public WordAlreadyExistsException() {
        super("already exists");
    } 

    public WordAlreadyExistsException(String s) {
        super(s + "already exists");
    }
}