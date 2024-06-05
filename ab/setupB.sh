# javac test.java
# javac test2.java
# javac encoder.java
make -C .. encoder.class > /dev/null

AtoB="A_to_B"
BtoA="B_to_A"

if !([ -p $AtoB ]); then
    echo "Creating pipe";
    mkfifo $AtoB;
fi
if !([ -p $BtoA ]); then
    echo "Creating pipe";
    mkfifo $BtoA;
fi



gcc -o input_a input.c
# gcc -o output_a output.c

vector="vector_already_used"
rm -f $vector
touch $vector

KEY="72F4B23E781DD15C"
INITIALIZATION_VECTOR="000134"

# ./input_a $AtoB B | java test encode > $BtoA

# ./input_a $AtoB B | java test "B->A" | ./output_a $BtoA

# ./input_a $AtoB B | java test2 $KEY $INITIALIZATION_VECTOR "B->A" | ./output_a $BtoA

# ./input_a $AtoB B | ./output_a $BtoA "B->A"

./input_a $AtoB B $BtoA


# ./input_a $BtoA > $AtoB
# cat | java test B encode > $BtoA

