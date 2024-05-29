encode: encoder.class
	@java encoder

encoder.class: encoder.java
	@javac.exe encoder.java

encode2: encoder2.class
	@java encoder2 $(ARGS)

encoder2.class: encoder2.java
	@javac.exe encoder2.java

decode2: encoder2.class
	@java encoder2 $(ARGS)
