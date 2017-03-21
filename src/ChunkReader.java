import java.io.*;
import javax.security.auth.*;

public class ChunkReader implements Destroyable
{
	private boolean isAvaliable;
	private TerrainReader m_TerrainReader;
	private TerrainData m_TerrainData;
	
	private ChunkReader(String path, TerrainReader reader, Point origin, int chunkCount) {
		m_TerrainReader = reader;
		m_TerrainReader.load(path);
		
		m_TerrainData = new TerrainData(origin, chunkCount);
		isAvaliable = true;
	}
	
	public static ChunkReader create(String path, Point origin, int chunkCount){
		if (new File(path).getName().equals("Chunks32.dat")) {
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
}
