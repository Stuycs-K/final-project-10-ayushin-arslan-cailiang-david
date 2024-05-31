javac test.java

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

# ./input_a $BtoA | java test A encode > $AtoB
./input_a $BtoA > $AtoB
# cat | java test A encode > $AtoB

