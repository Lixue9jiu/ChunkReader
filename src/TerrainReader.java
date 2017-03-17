import java.io.*;
import java.nio.*;

public interface TerrainReader
{
	void load(String path);
	void getChunk(Point chunk, TerrainData data);
	void setChunk(Point chunk, TerrainData data);
}
