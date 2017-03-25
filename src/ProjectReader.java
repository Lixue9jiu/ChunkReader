import java.io.*;

public class ProjectReader
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
}
