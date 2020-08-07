package controller_test;

import java.net.URISyntaxException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestClientException;

import controller.Controller;
import ki.KI;
import main.MainClient;
import myexceptions.ControllerNetworkProblem;
import myexceptions.NichtRichtigeParameter;

public class Class_for_testing_controller {
private Controller c = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("setUpBeforeClass");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("tearDownAfterClass");
	}
	
	@Before
	public void setUp() throws Exception {
		c = new Controller();
		System.out.println("setUp");
	}
	
	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown");
	}
	
	@Test(expected = NullPointerException.class)
	public void falseContollerStart_withoutarguments_shouldThrowException() throws RestClientException, URISyntaxException, ControllerNetworkProblem {
		c.start();
	}

}
