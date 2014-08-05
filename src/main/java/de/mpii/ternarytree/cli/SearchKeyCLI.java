/**
 *  Copyright 2014 Diego Ceccarelli
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package de.mpii.ternarytree.cli;

import it.cnr.isti.hpc.benchmark.Stopwatch;
import it.cnr.isti.hpc.cli.AbstractCommandLineInterface;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mpii.ternarytree.TernaryTriePrimitive;
import de.mpii.ternarytree.Trie;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Aug 5, 2014
 */
public class SearchKeyCLI extends AbstractCommandLineInterface {

	private static final Logger logger = LoggerFactory
			.getLogger(SearchKeyCLI.class);

	public SearchKeyCLI(String[] args) {
		super(
				args,
				new String[] { "index", "key" },
				"java -cp $jar de.mpii.ternarytree.cli.SearchKeyCLI -index serialization-file.bin -key key");

	}

	public static void main(String[] args) throws IOException {
		SearchKeyCLI cli = new SearchKeyCLI(args);

		Trie t = new TernaryTriePrimitive(1).deserialize(cli.getParam("index"));
		Stopwatch sw = new Stopwatch();
		sw.start("time");
		int value = t.get(cli.getParam("key"));
		long time = sw.stop("time");
		System.out.println("key " + cli.getParam("key") + "\nvalue:" + value);
		System.out.println("time: " + time + " millis");
	}
}
