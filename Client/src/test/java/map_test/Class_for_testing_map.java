package map_test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

import MessagesBase.HalfMapNode;
import MessagesBase.Terrain;
import model.Karte;

public class Class_for_testing_map {
	private Karte k = null;
	
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
		k = new Karte();
		System.out.println("setUp");
	}
	
	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown");
	}
	
	@Test
	public void Test_karte_generator_should_have_size_32() {
		
		Assert.assertThat(k.getChunks().size(), is(32));
	}
	
	@Test
	public void Test_that_fort_is_present() {
		boolean i = false;
		for(HalfMapNode hmn : k.getChunks()) {
			if(hmn.isFortPresent() == true) {
				i = true;
			}
		}
		
		Assert.assertThat(i, is(true));
	}
	
	@Test
	public void Test_karte_generator_should_have_more_than_2_berg() {
		int anzahl_berg = 0;
		for(HalfMapNode hmn : k.getChunks()) {
			if(hmn.getTerrain() == Terrain.Mountain) {
				anzahl_berg++;
			}
		}
		
		Assert.assertThat(anzahl_berg, greaterThan(2));
		
	}
	
	@Test
	public void Test_karte_generator_should_have_more_than_3_water() {
		int anzahl_water = 0;
		for(HalfMapNode hmn : k.getChunks()) {
			if(hmn.getTerrain() == Terrain.Water) {
				anzahl_water++;
			}
		}
		
		Assert.assertThat(anzahl_water, greaterThan(3));
		
	}
	
	@Test
	public void proof_border_algorithmus_should_be_false() {
		
		for(int i = 0; i < 4; i++) {
			HalfMapNode hmn = new HalfMapNode(0, i, false, Terrain.Water);
			k.getChunks().add(hmn);
		}
		
		boolean f = k.proof();
		
		Assert.assertThat(f, is(false));
	}
	
	@Test
	public void proof_anti_island_algorithmus_should_be_false() {
		HalfMapNode hmn1 = new HalfMapNode(2, 0, false, Terrain.Water);
		HalfMapNode hmn2 = new HalfMapNode(1, 1, false, Terrain.Water);
		HalfMapNode hmn3 = new HalfMapNode(2, 1, false, Terrain.Grass);
		HalfMapNode hmn4 = new HalfMapNode(2, 2, false, Terrain.Water);
		HalfMapNode hmn5 = new HalfMapNode(3, 1, false, Terrain.Water);
		Karte k2 = new Karte();
		for(int i = 0; i < 4; i++) {
			for(int y = 0; y < 8; y++) {
				if(i == 2) {
					if(y != 1 && y != 2 && y != 0) {
						HalfMapNode hmn = new HalfMapNode(i, y, false, Terrain.Grass);
						k2.getChunks().add(hmn);
					}
				}
				
				if(i == 1 || i == 3) {
					if(y != 1) {
						HalfMapNode hmn = new HalfMapNode(i, y, false, Terrain.Grass);
						k2.getChunks().add(hmn);
					}
				}
			}
		}
		
		k2.getChunks().add(hmn1);
		k2.getChunks().add(hmn2);
		k2.getChunks().add(hmn3);
		k2.getChunks().add(hmn4);
		k2.getChunks().add(hmn5);
        boolean f = k2.proof();
		
		Assert.assertThat(f, is(false));
	}
	
	@Test
	public void test_for_random_functions() {
		int i = k.wahl_breit();
		int ii = k.wahl_lang();
		
		Assert.assertThat(i, lessThan(4));
		Assert.assertThat(ii, lessThan(8));
	}


}
