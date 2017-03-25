import java.io.*;
import javax.security.auth.*;

public class ChunkReader implements Destroyable
{
	private File m_file;
	
	private TerrainData m_TerrainData;
	private TerrainReader m_TerrainReader;
	private boolean isAvaliable = true;

	public ChunkReader(File file)
	{
		m_file = file;
		String name = file.getName();
		if (name.equals("Chunks.dat")){
			m_TerrainReader = new TerrainReader124();
		}else if (name.equals("Chunks32.dat")){
			m_TerrainReader = new TerrainReader129();
		}else{
			isAvaliable = false;
			return;
		}
	}
	
	public boolean load(World.Option opt){
		if (!isAvaliable) {
			return false;
		}
		m_TerrainReader.load(m_file.getAbsolutePath());
		m_TerrainData = new TerrainData(opt.origin, opt.chunkCount);
		loadTerrain();
		return true;
	}
	
	public File save(){
		return m_file;
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
