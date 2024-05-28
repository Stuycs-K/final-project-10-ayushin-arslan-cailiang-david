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

The 2 smallest bits are discarded automatically.

INITIALIZATION_VECTOR must end in: 0,4,8,c,C

1) make encode KEY_STRING INITIALIZATION_VECTOR CLEARTEXT_FILE CIPHERTEXT_FILE

2) make decode KEY_STRING INITIALIZATION_VECTOR CIPHERTEXT_FILE CLEARTEXT_FILE

3) Read PRESENTATION.md for History on GSM and A5/1
