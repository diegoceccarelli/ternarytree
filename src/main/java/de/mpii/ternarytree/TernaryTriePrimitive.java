package de.mpii.ternarytree;

import gnu.trove.list.TCharList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TCharArrayList;
import gnu.trove.list.array.TIntArrayList;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class TernaryTriePrimitive implements Trie, Serializable<Trie> {
	private final TCharList labels;
	private final TIntList nodes;
	private int root;
	private final double threshold;
	private final char delimiter;

	public TernaryTriePrimitive(double t) {
		this(t, ' ');
	}

	public TernaryTriePrimitive(double t, char d) {
		labels = new TCharArrayList();
		nodes = new TIntArrayList();
		root = -1;
		threshold = t;
		delimiter = d;
	}

	public String[] getLongestMatch(String[] tokens, int start) {
		int node = root;
		for (int iToken = start; iToken < tokens.length; iToken++) {
			String token = getRelevantPrefix(tokens[iToken]);
			int pos = 0;
			while (node != -1) {
				if (token.charAt(pos) < getNodeKey(node)) {
					node = getLessChild(node);
				} else if (token.charAt(pos) == getNodeKey(node)) {
					if (pos == token.length() - 1) {
						break;
					} else {
						node = getEqualChild(node);
						pos++;
					}
				} else {
					node = getGreatChild(node);
				}
			}
			if (node == -1) {
				return Arrays.copyOfRange(tokens, start, iToken);
			} else {
				// match delimiter
				if (delimiter < getNodeKey(node)) {
					node = getLessChild(node);
				} else if (delimiter == getNodeKey(node)) {
					node = getEqualChild(node);
				} else {
					node = getGreatChild(node);
				}
			}
		}
		return Arrays.copyOfRange(tokens, start, tokens.length);
	}

	public int get(String key) {
		key = getRelevantPrefix(key);
		int node = root;
		int pos = 0;
		while (node != -1) {
			if (key.charAt(pos) < getNodeKey(node)) {
				node = getLessChild(node);
			} else if (key.charAt(pos) == getNodeKey(node)) {
				if (pos == key.length() - 1) {
					break;
				} else {
					node = getEqualChild(node);
					pos++;
				}
			} else {
				node = getGreatChild(node);
			}
		}
		if (node == -1) {
			return -1;
		} else {
			return getNodeValue(node);
		}
	}

	public void put(String key, int value) {
		key = getRelevantPrefix(key);
		root = put(root, key, 0, value);
	}

	private int put(int node, String key, int pos, int value) {
		char chr = key.charAt(pos);
		if (node == -1) {
			node = getNewNode(chr);
		}
		if (chr < getNodeKey(node)) {
			setLessChild(node, put(getLessChild(node), key, pos, value));
		} else if (chr == getNodeKey(node)) {
			if (pos < key.length() - 1) {
				setEqualChild(node,
						put(getEqualChild(node), key, pos + 1, value));
			} else {
				setNodeValue(node, value);
			}
		} else {
			setGreatChild(node, put(getGreatChild(node), key, pos, value));
		}
		return node;
	}

	private int getLessChild(int node) {
		return nodes.get(node);
	}

	private int getEqualChild(int node) {
		return nodes.get(node + 1);
	}

	private int getGreatChild(int node) {
		return nodes.get(node + 2);
	}

	private int getNodeValue(int node) {
		return nodes.get(node + 3);
	}

	private char getNodeKey(int node) {
		return labels.get(node / 4);
	}

	private int getNewNode(char chr) {
		int newNode = nodes.size();
		for (int i = 0; i < 4; i++) {
			nodes.add(-1);
		}
		labels.add(chr);
		return newNode;
	}

	private void setLessChild(int parentNode, int childNode) {
		nodes.set(parentNode, childNode);
	}

	private void setEqualChild(int parentNode, int childNode) {
		nodes.set(parentNode + 1, childNode);
	}

	private void setGreatChild(int parentNode, int childNode) {
		nodes.set(parentNode + 2, childNode);
	}

	private void setNodeValue(int node, int value) {
		nodes.set(node + 3, value);
	}

	public String getContent() {
		StringBuilder repr = getContent(root, new StringBuilder(), "");
		return repr.toString();
	}

	private StringBuilder getContent(int node, StringBuilder repr, String prefix) {
		if (node != -1) {
			if (nodes.get(node + 3) != -1) {
				repr.append(prefix + labels.get(node / 4) + "\t"
						+ String.valueOf(nodes.get(node + 3)) + "\n");
			}
			repr = getContent(nodes.get(node), repr, prefix);
			repr = getContent(nodes.get(node + 1), repr,
					prefix + labels.get(node / 4));
			repr = getContent(nodes.get(node + 2), repr, prefix);
		}
		return repr;
	}

	private String getRelevantPrefix(String key) {
		int cutLength = (int) Math.ceil(key.length() * threshold);
		return key.substring(0, cutLength);
	}

	public void serialize(String file) throws IOException {
		serialize(new FileOutputStream(new File(file)));
	}

	public void serialize(OutputStream stream) throws IOException {
		DataOutputStream writer = new DataOutputStream(
				new BufferedOutputStream(stream));
		writer.writeInt(nodes.size());
		for (int i = 0; i < nodes.size(); i++) {
			writer.writeInt(nodes.get(i));
		}
		writer.writeInt(labels.size());
		for (int i = 0; i < labels.size(); i++) {
			writer.writeChar(labels.get(i));
		}
		writer.close();
	}

	public Trie deserialize(String file) throws FileNotFoundException,
			IOException {
		return deserialize(new FileInputStream(new File(file)));
	}

	public Trie deserialize(InputStream stream) throws IOException {
		DataInputStream reader = new DataInputStream(new BufferedInputStream(
				stream));
		nodes.clear();
		labels.clear();
		int numNodes = reader.readInt();
		for (int i = 0; i < numNodes; i++) {
			nodes.add(reader.readInt());
		}
		int numLabels = reader.readInt();
		for (int i = 0; i < numLabels; i++) {
			labels.add(reader.readChar());
		}
		return this;
	}
}
