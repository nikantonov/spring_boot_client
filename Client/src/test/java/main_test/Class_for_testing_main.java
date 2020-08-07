package main_test;

import java.net.URISyntaxException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestClientException;

import main.MainClient;
import model.Karte;
import myexceptions.NichtRichtigeParameter;
import myexceptions.NoParameterError;

public class Class_for_testing_main {

	
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
		System.out.println("setUp");
	}
	
	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown");
	}
	
	@Test(expected = NichtRichtigeParameter.class)
	public void falseModus_test_main_should_throw_Exception() throws RestClientException, URISyntaxException {
		String[] s = new String[3];
		s[0] = "FR";
		s[1] = "http://swe.wst.univie.ac.at:18235";
		s[2] = "aaaa";
		MainClient.main(s);
	}
	
	

}
