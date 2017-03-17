import java.nio.*;

public class TerrainData
{
	private int originBlockX;
	private int originBlockZ;
	private int endBlockX;
	private int endBlockZ;
	
	private ByteBuffer byteCells;
	private IntBuffer mCells;

	public TerrainData(Point originChunk, int chunkCount){
		originBlockX = originChunk.X * 16;
		originBlockZ = originChunk.Y * 16;
		int blockCount = chunkCount * 16;
		endBlockX = originBlockX + blockCount;
		endBlockZ = originBlockZ + blockCount;
		
		byteCells = ByteBuffer.allocateDirect(blockCount * blockCount);
		mCells = byteCells.asIntBuffer();
	}
	
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
	
	public void setCell(int x, int y, int z, int value){
		if(isCellAvaliable(x, y, z)){
			setCellFast(x, y, z, value);
		}
	}
	
	public void setCellFast(int x, int y, int z, int value){
		setCellFast(calculateCellIndex(x, y, z), value);
	}
	
	public void setCellFast(int index, int value){
		mCells.put(index, value);
	}
	
	public void getBytes(int index, byte[] buffer) {
		byteCells.position(index);
		byteCells.get(buffer);
	}
	
	public void setBytes(int index, byte[] buffer) {
		byteCells.position(index);
		byteCells.put(buffer);
	}
}
