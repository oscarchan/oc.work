package lang.util.zip;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class ZipTest
{
	private static Log log = LogFactory.getLog(ZipTest.class);

	@Test
	public void testList() throws Exception
	{
		String filename = "/Users/ochan/workspace-android/oc.playback/bin/oc.playback.apk";
		
		ZipInputStream input = new ZipInputStream(new FileInputStream(filename));

		for(ZipEntry entry = input.getNextEntry();entry!=null;entry = input.getNextEntry()) { 
			String name = entry.getName();
			String comment = entry.getComment();
			long size = entry.getSize();
			long time = entry.getTime();
			long compressedSize = entry.getCompressedSize();
			
			log.info("entry: " + name + ": " + comment +  ": " + size + ": " + time +":" + compressedSize);
			
		}
		
		input.close();
	}
	
	
	public void testZipFile() throws Exception
	{
		String filename = "/Users/ochan/workspace-android/oc.playback/bin/oc.playback.apk";
		String entryName = "AndroidManifest.xml";
		
		ZipFile file = new ZipFile(filename);
		ZipEntry entry = file.getEntry(entryName);
		InputStream input = file.getInputStream(entry);

		OutputStream out = new FileOutputStream(entryName);

		byte[] buf = new byte[1024];

		int len;
		while ((len = input.read(buf)) > 0) {
			out.write(buf, 0, len);
		}

		out.close();
		
	}
	public void testVersion() throws Exception
	{
		String filename = "/Users/ochan/workspace-android/oc.playback/bin/oc.playback.apk";
		String entryName = "AndroidManifest.xml";
		
		ZipInputStream input = new ZipInputStream(new FileInputStream(filename));

		for(ZipEntry entry = input.getNextEntry();entry!=null;entry = input.getNextEntry()) { 
			String name = entry.getName();
			String comment = entry.getComment();
			long size = entry.getSize();
			long compressedSize = entry.getCompressedSize();
			long time = entry.getTime();
			
			if(entryName.equals(name)) {
				log.info("entry: " + name + ": " + comment +  ": " + size + ": " + time +":" + compressedSize);
			
//				BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
//				for(String line = reader.readLine();line!=null;line = reader.readLine()) { 
//					log.info("> " + line);
//				}

				OutputStream out = new FileOutputStream(name);
				
			    byte[] buf = new byte[1024];

			    int len;
		        while ((len = input.read(buf)) > 0) {
		        	out.write(buf, 0, len);
		        }

		        out.close();
			}
			
			input.closeEntry();
		}
		
		input.close();
	}	

}
