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
		if(opt != null){
			m_opt = opt;
		}else{
			m_opt = new Option();
			m_opt.isInited = false;
		}
		m_path = path;

		File project;
		isAvaliable = true;
		long time = System.currentTimeMillis();
		System.out.println("unziping");
		if ((project = Utils.UnzipFile(path, "Project.xml")) != null) {
			m_project = new ProjectReader(project);
			File chunk;
			if ((chunk = Utils.UnzipFile(path, "Chunks.dat")) != null) {
				m_chunk = new ChunkReader(chunk, false);
			}else if((chunk = Utils.UnzipFile(path, "Chunks32.dat")) != null) {
				m_chunk = new ChunkReader(chunk, true);
			}
		}
		System.out.println("unzip finished, time: " + (System.currentTimeMillis() - time));
		System.out.println("init world...");
		time = System.currentTimeMillis();
		isAvaliable = initWorld();
		System.out.println("init world finished, time: " + (System.currentTimeMillis() - time));
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
		public int chunkCount = 16;
		
		boolean isInited = true;
	}
}
