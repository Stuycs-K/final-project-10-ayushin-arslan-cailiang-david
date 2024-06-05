# javac test.java
# javac test2.java
# javac encoder.java
make -C .. encoder.class > /dev/null

KEY="72F4B23E781DD15C"
INITIALIZATION_VECTOR="000134"

# INPUT_FILE="print_encoded.txt"
INPUT_FILE="$1"
OUTPUT_FILE="print_decoded.txt"
# rm -f $INPUT_FILE; touch $INPUT_FILE
rm -f $OUTPUT_FILE; touch $OUTPUT_FILE

printf "\nEncoded:\n" > /dev/tty
xxd $INPUT_FILE > /dev/tty
printf "Decoded:\n" > /dev/tty
# echo "$1" | java test "$2" > /dev/tty
# "$2" is A->B or B->A
# echo "$1" | java test2 $KEY $INITIALIZATION_VECTOR "$2" > /dev/tty

# echo "$1" > $INPUT_FILE
# java encoder $KEY $INITIALIZATION_VECTOR $INPUT_FILE $OUTPUT_FILE > /dev/null
make -C .. encode ARGS="$KEY $INITIALIZATION_VECTOR ab/$INPUT_FILE ab/$OUTPUT_FILE" > /dev/null
cat $OUTPUT_FILE > /dev/tty

printf "\n\n" > /dev/tty
