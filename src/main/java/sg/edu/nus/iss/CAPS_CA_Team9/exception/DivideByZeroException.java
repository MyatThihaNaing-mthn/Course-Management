package sg.edu.nus.iss.CAPS_CA_Team9.exception;

public class DivideByZeroException extends RuntimeException{
    public DivideByZeroException(){}

    public DivideByZeroException(String message){
        super(message);
    }
}
