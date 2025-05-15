package de.dfki.mlt.srgsparser;

import static org.jvoicexml.processor.srgs.abnf.AbnfParserTest.RESOURCE_DIR;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.jvoicexml.processor.AbstractParser;


@RunWith(Parameterized.class)
public class OfficialTestEarley {
  @BeforeClass
  public static void beforeClass() {
    AbstractParser.useLeftCorner = false;
  }

  @AfterClass
  public static void afterClass() {
    AbstractParser.useLeftCorner = true;
  }

  private Path path;

  @Parameterized.Parameters
  public static Collection<Path> official() throws IOException {
    return Files.walk(Path.of(RESOURCE_DIR + "/official/test/"), 1)
        .filter(p -> p.toFile().isFile()).collect(Collectors.toList());
  }

  //private static Pattern METAPAT = Pattern.compile("in\\.([0-9]+)");

  public OfficialTestEarley(Path p) {
    this.path = p;
  }


  @Test
  public void grammarTest() {
    OfficialTest.parseGrammar(path);
  }
}
