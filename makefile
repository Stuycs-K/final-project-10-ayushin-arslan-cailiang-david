encode: encoder3.class
	@java encoder3 $(ARGS)

encoder3.class: encoder3.java
	@javac encoder3.java

decode: decoder3.class
	@java decoder3 $(ARGS)

decoder3.class: decoder3.java
	@javac decoder3.java

clean:
	@rm -f *.class
