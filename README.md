[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/ecp4su41)
# THIS DOCUMENT IS REQUIRED
## Group Info
Arslan Ayushin and David Cai Liang
## Overview
Encryption and Decryption of the Files using the A5/1 algorithm
## Instructions

Notes:

KEY_STRING and INITIALIZATION_VECTOR are both in Hexadecimal.

KEY_STRING is required to be 16 characters long.

INITIALIZATION_VECTOR is required to 6 characters long and represent a value that can fit inside 22 bits.

INITIALIZATION_VECTOR must start in: 0, 1, 2, or 3.

\[AtoB?\] and \[HexDump?\] are optional.

\[AtoB?\] parameter only accepts the two following responses: \'atob\' or \'btoa\'

\[AtoB?\] parameter doesn't care about capitalization.

\[AtoB?\] parameter must be provided for the \[HexDump?\] parameter to be accessible.

\[HexDump?\] parameter only accepts the two following responses: \'dump\' or \'true\'

\[HexDump?\] parameter doesn't care about capitalization.

\[HexDump?\] parameter should ONLY be provided if you want hexdump, otherwise leave it blank.
______

1) make encode KEY_STRING INITIALIZATION_VECTOR INPUT_FILE OUTPUT_FILE \[AtoB?\] \[HexDump?\]

  Sample: make encode ARGS="1223456789ABCDEF 000133 input.dat output.dat"

  Sample: make encode ARGS="4E2F4D7C1EB88B3A 000134 input1 output.dat btoa"

  Sample: make encode ARGS="72F4B23E781DD15C 0CF1D4 makefile output.dat atob dump"

2) make decode KEY_STRING INITIALIZATION_VECTOR INPUT_FILE OUTPUT_FILE \[AtoB?\]

  Sample: make decode ARGS="1223456789ABCDEF 000133 output.dat decrypt.dat"

  Sample: make decode ARGS="4E2F4D7C1EB88B3A 000134 output.dat decrypt.dat btoa"

  Sample: make decode ARGS="72F4B23E781DD15C 0CF1D4 output.dat decrypt.dat atob"
____

3) 2-Way Communications Demo

    1) Create two terminal windows
    2) cd into /ab for both windows
    3) In one of the terminal windows, run ./setupA.sh
    4) In the other terminal window, run ./setupB.sh
    5) Type messages into one of the terminal window.
    6) Go to the other terminal window to see the encrypted message that was sent and the decrypted message.

  Note: The algorithm works on large messages as well. It will automatically break large messages into 114 bit segments. The messages are treated like files.

4) Read PRESENTATION.md for History on GSM and A5/1

5) Watch Video: https://drive.google.com/file/d/17sF_GEx2Tu6kE0JtfZXqcQ4auktd-bMo/view?usp=sharing
______
Testing:

make encode ARGS="1223456789ABCDEF 000133 input.dat output.dat"; make decode ARGS="1223456789ABCDEF 000133 output.dat decrypt.dat"; diff input.dat decrypt.dat

make encode ARGS="4E2F4D7C1EB88B3A 000134 input1 output.dat btoa"; make decode ARGS="4E2F4D7C1EB88B3A 000134 output.dat decrypt.dat btoa"; diff input1 decrypt.dat

make encode ARGS="72F4B23E781DD15C 0CF1D4 makefile output.dat atob dump"; make decode ARGS="72F4B23E781DD15C 0CF1D4 output.dat decrypt.dat atob"; diff makefile decrypt.dat
