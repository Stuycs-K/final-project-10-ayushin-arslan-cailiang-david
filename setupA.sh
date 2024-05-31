javac test.java

PIPE="mario"

if !([ -p $PIPE ]); then
    echo "Creating pipe";
    mkfifo $PIPE;
fi


gcc -o input_a input.c

./input_a | java test > $PIPE

