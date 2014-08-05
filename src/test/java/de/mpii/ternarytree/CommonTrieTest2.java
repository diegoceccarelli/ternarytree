package de.mpii.ternarytree;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class CommonTrieTest2 {

	private Trie t;

	@Test
	public void testBasicInsertionAndGetContent() {
		t = new TernaryTriePrimitive(1.0);
		t.put("barack", 0);
		t.put("barack obama", 1);
		t.put("barack obama", 2);
		t.put("barricade", 2);

		assertEquals(0, t.get("barack"));
		assertEquals(2, t.get("barack obama"));
		assertEquals(2, t.get("barricade"));
		// System.out.println(t.getContent());
	}

	@Test
	public void testBasicInsertionAndGetContent2() {
		t = new TernaryTriePrimitive(1.0);
		t.put("pippo", 0);
		t.put("pluto", 1);
		t.put("parigi", 2);

		assertEquals(0, t.get("pippo"));
		assertEquals(1, t.get("pluto"));
		assertEquals(2, t.get("parigi"));
		// System.out.println(t.getContent());
	}

	@Test
	public void serialize() throws FileNotFoundException, IOException {
		TernaryTriePrimitive t = new TernaryTriePrimitive(1.0);
		File tmp = File.createTempFile("test", ".bin");
		t.put("pippo", 0);
		t.put("pluto", 1);
		t.put("parigi", 2);
		t.serialize(tmp.getAbsolutePath());

		Trie s = t.deserialize(tmp.getAbsolutePath());

		assertEquals(0, s.get("pippo"));
		assertEquals(1, s.get("pluto"));
		assertEquals(2, s.get("parigi"));

		s = new TernaryTriePrimitive(1.0).deserialize(tmp.getAbsolutePath());

		assertEquals(0, s.get("pippo"));
		assertEquals(1, s.get("pluto"));
		assertEquals(2, s.get("parigi"));
		// System.out.println(t.getContent());
	}
}
