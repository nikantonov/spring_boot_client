package myexceptions;

public class ControllerNetworkProblem extends Exception{
	public ControllerNetworkProblem(){};
    public ControllerNetworkProblem(String msg){
        super(msg);
    };

}
