encode: encoder2.class
	@java encoder2 $(ARGS)

encoder.class: encoder2.java
	@javac encoder2.java

decode: encoder2.class
	@java encoder2 $(ARGS)

clean:
	@rm -f *.class
