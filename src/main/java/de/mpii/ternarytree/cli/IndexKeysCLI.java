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

import it.cnr.isti.hpc.cli.AbstractCommandLineInterface;
import it.cnr.isti.hpc.log.ProgressLogger;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mpii.ternarytree.TernaryTriePrimitive;
import de.mpii.ternarytree.TrieBuilder;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Aug 5, 2014
 */
public class IndexKeysCLI extends AbstractCommandLineInterface {

	private static final Logger logger = LoggerFactory
			.getLogger(IndexKeysCLI.class);

	public IndexKeysCLI(String[] args) {
		super(
				args,
				new String[] { INPUT, OUTPUT },
				"java -cp $jar de.mpii.ternarytree.cli.IndexKeysCLI -input keys(one per line) -output serialization-file.bin");

	}

	public static void main(String[] args) throws IOException {
		IndexKeysCLI cli = new IndexKeysCLI(args);
		ProgressLogger pl = new ProgressLogger("index {} keys ", 10000);
		String line = "";
		cli.openInput();
		TernaryTriePrimitive t = new TernaryTriePrimitive(1);
		while ((line = cli.readLineFromInput()) != null) {
			Scanner scanner = new Scanner(line).useDelimiter("\t");
			String key = scanner.next();
			int id = pl.getStatus();
			if (scanner.hasNextInt()) {
				id = scanner.nextInt();

			}
			logger.debug("adding {} -> {}", key, id);
			t.put(key, id);
			pl.up();
		}
		logger.info("done, seriazing in {}", cli.getOutput());
		TrieBuilder tb = new TrieBuilder();
		tb.write(t, new File(cli.getOutput()));
		logger.info("done");
		cli.closeInput();

	}

}
