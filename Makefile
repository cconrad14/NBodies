all: NBodies TwoBodies

NBodies:
        javac -cp ".:*.jar" -g *.java

TwoBodies:
        javac -cp ".:*.jar" -g *.java

clean:
        rm -f *.class
