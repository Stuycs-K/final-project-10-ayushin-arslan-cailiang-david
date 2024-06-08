encode: encoder5.class
	@java encoder5 $(ARGS)

encoder5.class: encoder5.java
	@javac encoder5.java

decode: decoder5.class
	@java decoder5 $(ARGS)

decoder5.class: decoder5.java
	@javac decoder5.java

setupA:
	@cd ab && ./setupA.sh

setupB:
	@cd ab && ./setupB.sh

clean:
	@rm -f *.class
