# Test build file

JFLAGS = -g -Xlint
JC = javac
JAR = jar cvfm

MANIFEST = calculator.mf
TARGET = calculator.jar

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $<

SOURCES = $(wildcard *.java) $(wildcard $(ACCOUNT_SIR)/*.java)

DEP_CLASSES = $(SOURCES:.java=.class)

ALL_CLASSES = *.class

default: $(TARGET)

$(TARGET): classes
	$(JAR) $@ $(MANIFEST) $(ALL_CLASSES)

classes: $(DEP_CLASSES:.java=.class)

clean:
	$(RM) *.class $(TARGET)

