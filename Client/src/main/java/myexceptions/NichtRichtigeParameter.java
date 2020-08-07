package myexceptions;

import java.io.IOException;

public class NichtRichtigeParameter extends RuntimeException{
	public NichtRichtigeParameter(){};
    public NichtRichtigeParameter(String msg){
        super(msg);
    };
}