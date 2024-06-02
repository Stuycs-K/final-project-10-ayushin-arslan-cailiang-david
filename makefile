encode: encoder5.class
	@java encoder5 $(ARGS)

encoder5.class: encoder5.java
	@javac encoder5.java

decode: decoder4.class
	@java decoder4 $(ARGS)

decoder4.class: decoder4.java
	@javac decoder4.java

clean:
	@rm -f *.class
