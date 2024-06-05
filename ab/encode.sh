# javac encoder.java
make -C .. encoder.class > /dev/null

KEY="72F4B23E781DD15C"
INITIALIZATION_VECTOR="000134"

# INPUT_FILE="encode_plain.txt"
INPUT_FILE="$1"
OUTPUT_FILE="encode_encoded.txt"
# rm -f $INPUT_FILE; touch $INPUT_FILE
rm -f $OUTPUT_FILE; touch $OUTPUT_FILE


# echo "$1" > $INPUT_FILE
# cat $INPUT_FILE > this_is_input_file
# java encoder $KEY $INITIALIZATION_VECTOR $INPUT_FILE $OUTPUT_FILE > /dev/null
make -C .. encode ARGS="$KEY $INITIALIZATION_VECTOR ab/$INPUT_FILE ab/$OUTPUT_FILE" > /dev/null

# "$3" pipe_location
# cat $OUTPUT_FILE > "$3"
echo $OUTPUT_FILE > "$3"
