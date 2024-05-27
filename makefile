encode: encoder.class
	@java encoder

encoder.class: encoder.java
	@javac encoder.java

encode2: encoder2.class
	@java encoder2 $(ARGS)

encoder2.class: encoder2.java
	@javac encoder2.java $(ARGS)

decode2: encoder2.class
	@java encoder2 $(ARGS)
