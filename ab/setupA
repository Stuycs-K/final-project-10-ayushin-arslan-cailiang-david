# javac test.java
# javac test2.java
# javac encoder.java
make -C .. encoder5.class > /dev/null

AtoB="A_to_B"
BtoA="B_to_A"

if !([ -p $AtoB ]); then
    # echo "Creating pipe";
    mkfifo $AtoB;
fi
if !([ -p $BtoA ]); then
    # echo "Creating pipe";
    mkfifo $BtoA;
fi



gcc -o input_a input.c
# gcc -o output_a output.c

vector="vector_already_used"
rm -f $vector
touch $vector

KEY="72F4B23E781DD15C"
INITIALIZATION_VECTOR="000134"
echo -n $INITIALIZATION_VECTOR > $vector

# ./input_a $BtoA A | java test encode > $AtoB

# ./input_a $BtoA A | java test "A->B" | ./output_a $AtoB

# ./input_a $BtoA A | java test2 $KEY $INITIALIZATION_VECTOR "A->B" | ./output_a $AtoB

# ./input_a $BtoA A | ./output_a $AtoB "A->B"

./input_a $BtoA A $AtoB


# ./input_a $BtoA > $AtoB
# cat | java test A encode > $AtoB

