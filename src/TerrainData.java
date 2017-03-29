import java.nio.*;

public class TerrainData
{
	public final int originChunkX;
	public final int originChunkY;
	public final int ChunkCount;
	
	public final int originBlockX;
	public final int originBlockZ;
	public final int endBlockX;
	public final int endBlockZ;
	
	private ByteBuffer byteCells;
	private IntBuffer mCells;

	private int originOffset;
	
	public TerrainData(Point originChunk, int chunkCount)
	{
		originChunkX = originChunk.X;
		originChunkY = originChunk.Y;
		this.ChunkCount = chunkCount;
		
		originBlockX = originChunk.X * 16;
		originBlockZ = originChunk.Y * 16;
		int blockCount = chunkCount * 16;
		endBlockX = originBlockX + blockCount;
		endBlockZ = originBlockZ + blockCount;

		originOffset = originBlockX * 128 + originBlockZ * 2048;
		
		byteCells = ByteBuffer.allocateDirect(blockCount * blockCount * 512);
		mCells = byteCells.asIntBuffer();
		System.out.println(String.format(("terrain loading finished\nchunk count: %d\nbuffer size: %d\norigin: %d, %d"), new Object[]{ chunkCount * chunkCount, byteCells.capacity(), originBlockX, originBlockZ }));
	}

	public boolean isCellAvaliable(int x, int y, int z)
	{
		return originBlockX < x && endBlockX > x && y >= 0 && y < 128 && originBlockZ < z && endBlockZ > z;
	}

	public int calculateCellIndex(int x, int y, int z)
	{
		return x * 128 + y + z * 2048 - originOffset;
	}

	public int getCell(int x, int y, int z)
	{
		if (isCellAvaliable(x, y, z))
		{
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

	public void setCell(int x, int y, int z, int value)
	{
		if (isCellAvaliable(x, y, z))
		{
			setCellFast(x, y, z, value);
		}
	}

	public void setCellFast(int x, int y, int z, int value)
	{
		setCellFast(calculateCellIndex(x, y, z), value);
	}

	public void setCellFast(int index, int value)
	{
		mCells.put(index, value);
	}

	public void getBytes(int index, byte[] buffer)
	{
		byteCells.position(index);
		byteCells.get(buffer);
	}

	public void setBytes(int index, byte[] buffer)
	{
		byteCells.position(index);
		byteCells.put(buffer);
	}
}
