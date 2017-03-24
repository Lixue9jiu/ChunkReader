import java.io.*;
import javax.security.auth.*;

public class ChunkReader implements Destroyable
{
	private TerrainData m_TerrainData;
	private TerrainReader m_TerrainReader;
	private boolean isAvaliable;

	private ChunkReader(String path, TerrainReader reader, Point origin, int chunkCount)
	{
		m_TerrainReader = reader;
		m_TerrainReader.load(path);

		m_TerrainData = new TerrainData(origin, chunkCount);
		loadTerrain();

		isAvaliable = true;
	}

	public static ChunkReader create(String path, Point origin, int chunkCount)
	{
		if (new File(path).getName().equals("Chunks32.dat"))
		{
			return new ChunkReader(path, new TerrainReader129(), origin, chunkCount);
		}
		throw new RuntimeException("unknown chunk file name");
	}

	@Override
	public boolean isDestroyed()
	{
		return isAvaliable;
	}

	@Override
	public void destroy() throws DestroyFailedException
	{
		try
		{
			m_TerrainReader.close();
			isAvaliable = false;
		}
		catch (IOException e)
		{
			throw new DestroyFailedException(e.getMessage());
		}
	}

	public TerrainData TerrainData()
	{
		return m_TerrainData;
	}

	private void loadTerrain()
	{
		final int endx = m_TerrainData.ChunkCount + m_TerrainData.originChunkX;
		final int endy = m_TerrainData.ChunkCount + m_TerrainData.originChunkY;
		for (int x = m_TerrainData.originChunkX; x < endx; x++)
		{
			for (int y = m_TerrainData.originChunkY; y < endy; y++)
			{
				m_TerrainReader.getChunk(new Point(x, y), m_TerrainData);
			}
		}
	}
}
