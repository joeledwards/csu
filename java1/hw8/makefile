# Test build file

JFLAGS = -g -Xlint
JC = javac
JAR = jar cvfm

ACCOUNT_DIR = account
MANIFEST = banking.mf
TARGET = banking.jar

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $<

SOURCES = $(wildcard *.java) $(wildcard $(ACCOUNT_SIR)/*.java)

DEP_CLASSES = $(SOURCES:.java=.class)

ALL_CLASSES = *.class $(ACCOUNT_DIR)/*.class

default: $(TARGET)

$(TARGET): classes
	$(JAR) $@ $(MANIFEST) $(ALL_CLASSES)

classes: $(DEP_CLASSES:.java=.class)

clean:
	$(RM) *.class $(ACCOUNT_DIR)/*.class $(TARGET)

