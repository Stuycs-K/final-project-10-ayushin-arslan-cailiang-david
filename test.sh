javac test.java

PIPE="mario"

if !([ -p $PIPE ]); then
    echo "Creating pipe";
    mkfifo $PIPE;
fi

cat | java test > $PIPE

