# Test build file

JFLAGS = -g -Xlint
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $<

SOURCES = $(wildcard *.java)

CLASSES = $(SOURCES:.java=.class)

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

