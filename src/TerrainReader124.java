import java.io.*;
import java.util.*;
import java.nio.*;

public class TerrainReader124 implements TerrainReader
{
	private final int entrySize = 768444;
	private final int chunkSize = 66576;

	private RandomAccessFile mFile;
	private HashMap<Point, Integer> chunkOffsets;
	private ByteBuffer mBuffer;
	
	private int chunkEnd;
	
	@Override
	public void load(String path)
	{
		try
		{
			mFile = new RandomAccessFile(path, "rw");
			chunkOffsets = new HashMap<>();
			mBuffer = ByteBuffer.allocateDirect(65536);
			chunkEnd = 0;
			while (true)
			{
				int x = Utils.readInt(mFile);
				int y = Utils.readInt(mFile);
				int off = Utils.readInt(mFile);
				if (off != 0)
				{
					chunkOffsets.put(new Point(x, y), off);
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
				int index, k;
				for (int x = 0; x < 16; x++){
					for (int y = 0; y < 16; y++){
						index = data.calculateCellIndex(chunk.X * 16 + x, 0, chunk.Y * 16 + y);
//						data.setBytes(index * 2, mBuffer.array());
						
						k = 0;
						while(k < 128){
							data.setCellFast(index, mBuffer.getShort());
							k++;
							index++;
						}
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
				int off = entrySize + chunkEnd * chunkSize;
				Utils.writeInt(mFile, chunk.X);
				Utils.writeInt(mFile, chunk.Y);
				Utils.writeInt(mFile, off);
				chunkOffsets.put(chunk, off);
				chunkEnd++;

				mFile.setLength(mFile.length() + chunkSize + 16);
				writeMagic(mFile);
				mFile.seek(off);
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
	}
	
	private static void writeMagic(RandomAccessFile f) throws IOException{
		Utils.writeInt(f, 0xDEADBEEF);
		Utils.writeInt(f, 0xFFFFFFFF);
	}
}
