package android.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import android.util.AndroidUtils.ManifestInfo;

public class AndroidUtilsTest
{
	private static Log log = LogFactory.getLog(AndroidUtilsTest.class);

	@Test
	public void testGetVersionCode() throws Exception
	{
//		String filename = "/Users/ochan/workspace-android/oc.playback/bin/oc.playback.apk";
		String filename = "/Users/ochan/tmp/test/test/oc.playback.apk";
		String entryName = "AndroidManifest.xml";
		
		ZipFile file = new ZipFile(filename);
		ZipEntry entry = file.getEntry(entryName);
		InputStream input = file.getInputStream(entry);

		OutputStream out = new FileOutputStream(entryName);

		byte[] buf = new byte[(int) entry.getSize()];

		int len;
		while ((len = input.read(buf)) > 0) {
			out.write(buf, 0, len);
		}

		out.close();
		
		ManifestInfo manifest = AndroidUtils.getManifest(buf);
		log.info("name=" + manifest);
		
	}


}
