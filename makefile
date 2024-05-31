encode: encoder.class
	@java encoder $(ARGS)

encoder.class: encoder.java
	@javac encoder.java

decode: decoder.class
	@java decoder $(ARGS)

decoder.class: decoder.java
	@javac decoder.java

clean:
	@rm -f *.class
