import java.nio.*;

public class TerrainData
{
	private int originBlockX;
	private int originBlockZ;
	private int endBlockX;
	private int endBlockZ;
	
	private ByteBuffer byteCells;
	private IntBuffer mCells;

	public boolean isCellAvaliable(int x, int y, int z)
	{
		return originBlockX < x && endBlockX > x && y >= 0 && y < 128 && originBlockZ < z && endBlockZ > z;
	}
	
	public int calculateCellIndex(int x, int y, int z)
	{
		return x * 128 + y + z * 2048;
	}
	
	public int getCell(int x, int y, int z)
	{
		if (isCellAvaliable(x, y, z)){
			return getCellFast(x, y, z);
		}
		return 0;
	}
	
	public int getCellFast(int x, int y, int z)
	{
		return getCellFast(calculateCellIndex(x, y, z));
	}
	
	public int getCellFast(int index)
	{
		return mCells.get(index);
	}
}
