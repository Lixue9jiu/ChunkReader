import java.io.*;
import java.nio.*;

public interface TerrainReader
{
	void load(String path);
	void save();
	byte[] getChunk(Point chunk);
	void setChunk(Point chunk, byte[] buffer);
}
