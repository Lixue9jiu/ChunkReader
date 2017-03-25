import java.io.*;
import javax.security.auth.*;

public class World implements Destroyable
{
	private boolean isAvaliable;
	private Option m_opt;

	private String m_path;
	private ChunkReader m_chunk;
	private ProjectReader m_project;

	public World(String path, Option opt)
	{
		m_opt = opt != null ? opt : new Option();
		m_path = path;

		File project;
		isAvaliable = true;
		if ((project = Utils.UnzipFile(path, "Project.xml")) != null) {
			m_project = new ProjectReader(project);
			File chunk;
			if ((chunk = Utils.UnzipFile(path, "Chunks.dat")) != null || (chunk = Utils.UnzipFile(path, "Chunks32.dat")) != null) {
				m_chunk = new ChunkReader(chunk);
			}
		}
		isAvaliable = initWorld();
	}
	
	public World(String path)
	{
		this(path, null);
	}

	public boolean save()
	{
		return save(m_path.replace(".scworld", "(edited).scworld"));
	}

	public boolean save(String path)
	{
		File[] files = new File[3];
		files[0] = m_project.save();
		files[1] = m_chunk.save();
		files[2] = Utils.UnzipFile(m_path, "Project.bak");
		return Utils.ZipFiles(files, new File(path));
	}
	
	public ProjectReader Project(){
		return m_project;
	}
	
	public ChunkReader Chunks(){
		return m_chunk;
	}

	@Override
	public void destroy() throws DestroyFailedException
	{
		m_chunk.destroy();
	}

	@Override
	public boolean isDestroyed()
	{
		return isAvaliable = m_chunk.isDestroyed();
	}

	private boolean initWorld()
	{
		if (m_project == null || m_chunk == null)
		{
			return false;
		}
		return m_project.load(m_opt) && m_chunk.load(m_opt);
	}

	public class Option
	{
		public Point origin;
		public int chunkCount = 5;
	}
}
