import java.io.*;
import java.util.zip.*;

public class Utils
{
	
	public static final int readInt(RandomAccessFile f) throws IOException
	{
		return (f.read() & 0xff) | (f.read() & 0xff) << 8 | (f.read() & 0xff) << 16 | (f.read() & 0xff) << 24;
	}

	public static final void writeInt(RandomAccessFile f, int i) throws IOException
	{
		f.write(0xff & (i));
		f.write(0xff & (i >> 8));
		f.write(0xff & (i >> 16));
		f.write(0xff & (i >> 24));
	}
	
	public static final File UnzipFile(String zipPath, String filename) {
        File outFile = null;
        try {
            File file = new File(zipPath);
            outFile = File.createTempFile(filename, null);
			outFile.deleteOnExit();
            ZipFile zipFile = new ZipFile(file);
            ZipEntry entry = zipFile.getEntry(filename);
            if (entry == null) {
                return null;
            }
            BufferedInputStream input = new BufferedInputStream(zipFile.getInputStream(entry));
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outFile));

            int offset;
            byte[] buffer = new byte[4096];
            while ((offset = input.read(buffer, 0, 4096)) != -1) {
                output.write(buffer, 0, offset);
            }
            input.close();
            output.close();
            zipFile.close();
			System.out.println("done unziping " + filename);
			return outFile;
        } catch (Exception e) {
            e.printStackTrace();
			return null;
        }
    }
	
	public static final boolean ZipFiles(File[] srcfile, File zipfile) {
        byte[] buf = new byte[4096];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcfile[i]));
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));

                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
			System.out.println("done ziping");
			return true;
        } catch (IOException e) {
            e.printStackTrace();
			return false;
        }
    }
}
