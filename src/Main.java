import java.util.*;
import java.io.*;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			File f = File.createTempFile("Chunks", null);
			f.deleteOnExit();
			System.out.println(f.getAbsolutePath());
		}
		catch (IOException e)
		{}

	}
}
