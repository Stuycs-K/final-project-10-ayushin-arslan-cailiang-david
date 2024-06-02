javac test.java

# AtoB="A_to_B"
# BtoA="B_to_A"

# if !([ -p $AtoB ]); then
#     echo "Creating pipe";
#     mkfifo $AtoB;
# fi
# if !([ -p $BtoA ]); then
#     echo "Creating pipe";
#     mkfifo $BtoA;
# fi

AtoB="A_to_B.txt"
BtoA="B_to_A.txt"

rm $AtoB
touch $AtoB
rm $BtoA
touch $BtoA



gcc -o input_a input.c

# ./input_a $AtoB B | java test encode > $BtoA
./input_a $AtoB B
# ./input_a $BtoA > $AtoB
# cat | java test B encode > $BtoA

