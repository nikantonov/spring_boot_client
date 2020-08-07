package ki_test;

import static org.hamcrest.Matchers.is;

import java.util.HashSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import MessagesBase.Move;
import MessagesBase.Terrain;
import MessagesGameState.FortState;
import MessagesGameState.FullMapNode;
import MessagesGameState.PlayerPositionState;
import MessagesGameState.TreasureState;
import ki.KI;
import model.Karte;

public class Class_for_testing_ki {
private KI k = null;
	
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
		k = new KI();
		System.out.println("setUp");
	}
	
	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown");
	}
	
	@Test
	public void test_next_move_1_haelfte_datengetrieben_shouldShowMoveDown() {
		java.util.Set<FullMapNode> map = new HashSet<FullMapNode>();
		FullMapNode f1 = new FullMapNode(Terrain.Grass, PlayerPositionState.MyPosition, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,1);
		k.setAct(f1);
		k.setHalb(1);
		map.add(f1);
		FullMapNode f2 = new FullMapNode(Terrain.Mountain, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,1,1);
		map.add(f2);
		FullMapNode f3 = new FullMapNode(Terrain.Mountain, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,3,1);
		map.add(f3);
		FullMapNode f4 = new FullMapNode(Terrain.Water, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,0);
		map.add(f4);
		
		FullMapNode f5 = new FullMapNode(Terrain.Grass, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,2);
		map.add(f5);
		
		Move test = k.getMove(map);
		Assert.assertThat(test, is(Move.Down));
	}
	
	@Test
	public void test_next_move_2_haelfte_datengetrieben_shouldShowMoveRight() {
		java.util.Set<FullMapNode> map = new HashSet<FullMapNode>();
		FullMapNode f1 = new FullMapNode(Terrain.Grass, PlayerPositionState.MyPosition, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,6);
		k.setAct(f1);
		k.setHalb(2);
		map.add(f1);
		FullMapNode f2 = new FullMapNode(Terrain.Water, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,1,6);
		map.add(f2);
		FullMapNode f3 = new FullMapNode(Terrain.Mountain, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,5);
		map.add(f3);
		FullMapNode f4 = new FullMapNode(Terrain.Water, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,7);
		map.add(f4);
		
		FullMapNode f5 = new FullMapNode(Terrain.Grass, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,3,6);
		map.add(f5);
		
		Move test = k.getMove(map);
		Assert.assertThat(test, is(Move.Right));
	}
	
	@Test
	public void test_next_move_1_haelfte_datengetrieben_with_treasure_shouldShowMoveDown() {
		java.util.Set<FullMapNode> map = new HashSet<FullMapNode>();
		FullMapNode f1 = new FullMapNode(Terrain.Grass, PlayerPositionState.MyPosition, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,1);
		k.setAct(f1);
		k.setHalb(1);
		k.setTreasure(true);
		map.add(f1);
		FullMapNode f2 = new FullMapNode(Terrain.Grass, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,1,1);
		map.add(f2);
		FullMapNode f3 = new FullMapNode(Terrain.Grass, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,3,1);
		map.add(f3);
		FullMapNode f4 = new FullMapNode(Terrain.Grass, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,0);
		map.add(f4);
		
		FullMapNode f5 = new FullMapNode(Terrain.Mountain, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,2);
		map.add(f5);
		
		Move test = k.getMove(map);
		Assert.assertThat(test, is(Move.Down));
	}
	
	@Test
	public void test_next_move_2_haelfte_datengetrieben_with_treasure_shouldShowMoveUp() {
		java.util.Set<FullMapNode> map = new HashSet<FullMapNode>();
		FullMapNode f1 = new FullMapNode(Terrain.Grass, PlayerPositionState.MyPosition, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,6);
		k.setAct(f1);
		k.setHalb(2);
		k.setTreasure(true);
		map.add(f1);
		FullMapNode f2 = new FullMapNode(Terrain.Grass, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,1,6);
		map.add(f2);
		FullMapNode f3 = new FullMapNode(Terrain.Mountain, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,5);
		map.add(f3);
		FullMapNode f4 = new FullMapNode(Terrain.Grass, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,7);
		map.add(f4);
		
		FullMapNode f5 = new FullMapNode(Terrain.Grass, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,3,6);
		map.add(f5);
		
		Move test = k.getMove(map);
		Assert.assertThat(test, is(Move.Up));
	}
	
	@Test
	public void proof_zahlen_shouldBe2BetweenGrass_shouldBe4BetweenMountains() {
		FullMapNode f4 = new FullMapNode(Terrain.Grass, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,6);
		
		FullMapNode f5 = new FullMapNode(Terrain.Grass, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,3,6);
		k.zahlen(f4, f5);
		int i = k.getI();
		Assert.assertThat(i, is(2));
		
		FullMapNode f6 = new FullMapNode(Terrain.Mountain, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,2,6);
		
		FullMapNode f7 = new FullMapNode(Terrain.Mountain, PlayerPositionState.NoPlayerPresent, TreasureState.NoOrUnknownTreasureState,
				FortState.NoOrUnknownFortState,3,6);
		k.zahlen(f6, f7);
		int i1 = k.getI();
		Assert.assertThat(i1, is(4));
	}
	
	
	
	//FullMapNode(Terrain terrain, PlayerPositionState playerPos, TreasureState treasure, FortState fort, int X, int Y)

}
