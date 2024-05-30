encode: encoder.class
	@java encoder $(ARGS)

encoder.class: encoder.java
	@javac encoder.java

decode: encoder.class
	@java encoder $(ARGS)

clean:
	@rm -f *.class
