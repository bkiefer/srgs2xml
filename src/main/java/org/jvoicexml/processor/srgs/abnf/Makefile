ROOT       = $(HOME)/src/external/jflex
#JFLEX      = $(ROOT)/jflex/bin/jflex
JFLEX	   = jflex
JAVA       = java
JAVAC      = javac
BISON	   = bison


# --------------------------------------------------

%.java : %.y
	$(BISON) -Wall -rall --language java --debug $<

all: SrgsAbnf.java SrgsLexer.java

%.java: %.flex
	$(JFLEX) $<

clean:
	rm -f *.class
	rm -f *~
	rm -f SrgsAbnf.java SrgsLexer.java SrgsAbnf.output
