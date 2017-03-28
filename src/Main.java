import java.util.*;
import java.io.*;

public class Main
{
	public static void main(String[] args)
	{
		HashMap<Point, Integer> map = new HashMap<>();
		map.put(new Point(2, 1), 20);
		map.put(new Point(1, 2), 10);
		System.out.println(new Point(2, 1).hashCode());
		System.out.println(new Point(1, 2).hashCode());
		System.out.println(map.get(new Point(1, 2)));
		System.out.println(map.get(new Point(2, 1)));
		World w = new World("/storage/emulated/0/Survivalcraft/andy2.scworld");
		int i = w.Chunks().TerrainData().getCellFast(0);
		System.out.println(i);
	}
}
