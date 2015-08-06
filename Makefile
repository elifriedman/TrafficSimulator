compile: src/edu/cooper/*.java
	rm -rf ./class
	mkdir class
	javac -d class src/edu/cooper/* -d class/ -classpath :

run:
	java -cp class/: edu.cooper.Main

clean:
	rm -rf ./class
