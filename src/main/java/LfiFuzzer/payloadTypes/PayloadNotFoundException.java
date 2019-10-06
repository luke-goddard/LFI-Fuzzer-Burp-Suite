package LfiFuzzer.payloadTypes;

public class PayloadNotFoundException extends RuntimeException{
    PayloadNotFoundException(String errorMessage) {
            super(errorMessage);
        }
}
