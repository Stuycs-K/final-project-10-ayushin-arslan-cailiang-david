encode: encoder.class
	@java encoder

encoder.class: encoder.java
	@javac encoder.java
