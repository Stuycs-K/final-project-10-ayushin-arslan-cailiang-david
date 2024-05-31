javac test.java

AtoB="mario"

if !([ -p $AtoB ]); then
    echo "Creating pipe";
    mkfifo $AtoB;
fi


gcc -o input_a input.c

./input_a | java test AtoB encode > $AtoB

