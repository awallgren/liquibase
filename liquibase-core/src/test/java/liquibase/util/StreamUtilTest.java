package liquibase.util;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

public class StreamUtilTest {

	@Test
	public void testGetStreamContents() throws IOException {
		byte[] contents = "TEST2".getBytes();
		ByteArrayInputStream stream = new ByteArrayInputStream(contents);
		String result = StreamUtil.getStreamContents(stream);
		assertEquals("TEST2", result);
	}

	@Test
	public void testGetReaderContents() throws IOException {
		String contents = "TEST";
		StringReader reader = new StringReader(contents);
		String result = StreamUtil.getReaderContents(reader);
		assertEquals(contents, result);
	}

	@Test
	public void testWithBomNoEncodingGiven() throws IOException {
		String contents = "abc";
		ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {
				(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0x61, 0x62, 0x63 });

		assertEquals(contents, StreamUtil.getStreamContents(bais));
	}

	@Test
	public void testWithBomCorrectEncodingGiven() throws IOException {
		String contents = "abc";
		ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {
				(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0x61, 0x62, 0x63 });

		assertEquals(contents, StreamUtil.getStreamContents(bais, "UTF8"));
	}

	@Test
	public void testWithoutBomUtf8() throws IOException {
		String contents = "abc";
		ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { 0x61,
				0x62, 0x63 });

		assertEquals(contents, StreamUtil.getStreamContents(bais, "UTF8"));
	}

	@Test
	public void testWithoutBomLatin1() throws IOException {
		String contents = "abc";
		ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { 0x61,
				0x62, 0x63 });

		assertEquals(contents, StreamUtil.getStreamContents(bais, "Latin1"));
	}
	
	@Test
	public void testWithBomWrongEncodingGiven() throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {
				(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0x61, 0x62, 0x63 });

		try {
			StreamUtil.getStreamContents(bais, "UTF-16BE");
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("UTF-16BE"));
		}
	}

}
