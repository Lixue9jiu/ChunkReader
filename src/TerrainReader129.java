import java.io.*;
import java.nio.*;
import java.util.*;

public class TerrainReader129 implements TerrainReader, Closeable
{
	private final long EntrySize = 768444;
	private final long ChunkSize = 132112;
	
	private int chunkEnd;
	
	private RandomAccessFile mFile;
	private HashMap<Point, Long> chunkOffsets;
	
	private ByteBuffer mBuffer;

	@Override
	public void load(String path)
	{
		// TODO: Implement this method
		try
		{
			mFile = new RandomAccessFile(path, "rw");
			chunkOffsets = new HashMap<Point, Long>();
			mBuffer = ByteBuffer.allocateDirect(131072);
			chunkEnd = 0;
			while(true) {
				int x = readInt(mFile);
				int y = readInt(mFile);
				int off = readInt(mFile);
				if (off != -1){
					chunkOffsets.put(new Point(x, y), EntrySize + off * ChunkSize);
					chunkEnd++;
				}else {
					break;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getChunk(Point chunk)
	{
		try
		{
			if(chunkOffsets.containsKey(chunk)){
				mFile.seek(chunkOffsets.get(chunk) + 16);
				mFile.read(mBuffer.array());
				return mBuffer.array();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setChunk(Point chunk, byte[] buffer)
	{
		try {
			if (chunkOffsets.containsKey(chunk)){
				mFile.seek(chunkOffsets.get(chunk) + 16);
				mFile.write(buffer);
			}else if(chunkEnd < 65537) {
				mFile.seek(chunkEnd * 12);
				writeInt(mFile, chunk.X);
				writeInt(mFile, chunk.Y);
				writeInt(mFile, chunkEnd);
				long off = EntrySize + chunkEnd * ChunkSize;
				chunkOffsets.put(chunk, off);
				chunkEnd++;
				
				mFile.seek(off);
				mFile.write(buffer);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException
	{
		mFile.close();
		mBuffer = null;
	}
	
	private static int readInt(RandomAccessFile f) throws IOException{
		return f.readByte() << 24 + f.readByte() << 16 + f.readByte() << 8 + f.readByte();
	}
	
	private static void writeInt(RandomAccessFile f, int i) throws IOException{
		f.write((byte)(i >> 24));
		f.write((byte)(i >> 16));
		f.write((byte)(i >> 8));
		f.write((byte)i);
	}
}
