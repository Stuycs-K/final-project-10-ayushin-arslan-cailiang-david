encode: encoder4.class
	@java encoder4 $(ARGS)

encoder4.class: encoder4.java
	@javac encoder4.java

decode: decoder4.class
	@java decoder4 $(ARGS)

decoder4.class: decoder4.java
	@javac decoder4.java

clean:
	@rm -f *.class
