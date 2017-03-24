import java.io.*;
import java.nio.*;
import java.util.*;

public class TerrainReader129 implements TerrainReader
{
	private final long EntrySize = 65536;
	private final long ChunkSize = 132112;

	private int chunkEnd;

	private RandomAccessFile mFile;
	private HashMap<Point, Long> chunkOffsets;

	private ByteBuffer mBuffer;

	@Override
	public void load(String path)
	{
		try
		{
			mFile = new RandomAccessFile(path, "rw");
			chunkOffsets = new HashMap<Point, Long>();
			mBuffer = ByteBuffer.allocateDirect(131072);
			chunkEnd = 0;
			while (true)
			{
				int x = readInt(mFile);
				int y = readInt(mFile);
				int off = readInt(mFile);
				if (off != -1)
				{
					chunkOffsets.put(new Point(x, y), EntrySize + off * ChunkSize);
					chunkEnd++;
				}
				else
				{
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
	public void getChunk(Point chunk, TerrainData data)
	{
		try
		{
			if (chunkOffsets.containsKey(chunk))
			{
				mFile.seek(chunkOffsets.get(chunk) + 16);
				mFile.read(mBuffer.array());
				mBuffer.position(0);
				int index;
				for (int x = 0; x < 16; x++){
					for (int y = 0; y < 16; y++){
						index = data.calculateCellIndex(chunk.X * 16 + x, 0, chunk.Y * 16 + y);
						data.setBytes(index * 4, mBuffer.array());
//						while(k < 128){
//							data.setCellFast(index, mBuffer.getInt());
//							k++;
//							index++;
//						}
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void setChunk(Point chunk, TerrainData data)
	{
		try
		{
			int index = data.calculateCellIndex(chunk.X * 16, 0, chunk.Y * 16);
			data.getBytes(index, mBuffer.array());
			
			if (chunkOffsets.containsKey(chunk))
			{
				mFile.seek(chunkOffsets.get(chunk) + 16);
				mFile.write(mBuffer.array());
			}
			else if (chunkEnd < 65537)
			{
				mFile.seek(chunkEnd * 12);
				writeInt(mFile, chunk.X);
				writeInt(mFile, chunk.Y);
				writeInt(mFile, chunkEnd);
				long off = EntrySize + chunkEnd * ChunkSize;
				chunkOffsets.put(chunk, off);
				chunkEnd++;

				mFile.seek(off + 16);
				mFile.write(mBuffer.array());
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException
	{
		mFile.close();
		mBuffer = null;
	}

	private static int readInt(RandomAccessFile f) throws IOException
	{
		return f.readByte() << 24 + f.readByte() << 16 + f.readByte() << 8 + f.readByte();
	}

	private static void writeInt(RandomAccessFile f, int i) throws IOException
	{
		f.write((byte)(i >> 24));
		f.write((byte)(i >> 16));
		f.write((byte)(i >> 8));
		f.write((byte)i);
	}
}
