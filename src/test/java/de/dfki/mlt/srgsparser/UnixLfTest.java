package de.dfki.mlt.srgsparser;

import static de.dfki.mlt.srgsparser.OfficialTest.parseGrammar;
import static org.jvoicexml.processor.srgs.abnf.AbnfParserTest.RESOURCE_DIR;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.jvoicexml.processor.srgs.GrammarException;

@RunWith(Parameterized.class)
public class UnixLfTest {
  private Path path;
  
  @Parameterized.Parameters
  public static Collection<Path> unixlf() throws IOException {
    return Files.walk(Path.of(RESOURCE_DIR + "/unixlf/test/"), 1)
        .filter(p -> p.toFile().isFile()).collect(Collectors.toList()); 
  }
  
  public UnixLfTest(Path p) {
    this.path = p;
  }

  @Test 
  public void grammarTest() throws GrammarException {
    parseGrammar(path);
  }
}
