import java.io.*;
import javax.security.auth.*;

public class ProjectReader implements Destroyable
{
	private File m_file;
	
	public ProjectReader(File file){
		m_file = file;
	}
	
	public boolean load(World.Option opt){
		return true;
	}
	
	public File save(){
		return m_file;
	}

	@Override
	public void destroy() throws DestroyFailedException
	{
		// TODO: Implement this method
	}

	@Override
	public boolean isDestroyed()
	{
		// TODO: Implement this method
		return false;
	}
}
