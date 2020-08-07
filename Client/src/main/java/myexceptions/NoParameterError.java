package myexceptions;

public class NoParameterError extends RuntimeException {
	public NoParameterError() {};
    public NoParameterError(String msg) {
        super(msg);
    };
}

