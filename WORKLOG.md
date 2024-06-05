# Work Log

## David Cai Liang

### 5/22/2024

1) Worked on Stage 2 of the algorithm. It broke after I added 16 bits to the shift registers.

### date y

info


## Arslan Ayushin

### 5/22/24

- Set up java encoder in class
- step 2 seems to be working except for one bit
- added first iteration of irregular clock step

### 5/23/24

- fixed 100 clocks step + test steps 1-4

### 5/24/24

- fixed step 5 key stream outputs the last bit of register

### 5/28/24

- downloaded an online C encoder to compare results
- tested both of our versions and the c version with same key and vector
- reversed the bytes of key because our versions was reading each byte starting from least significant bit, now first step matches

### 5/29/24

- fixed last byte of key stream by padding with 0s (114 bits not divisible by 8)
- our encoder step 5 generates correct key stream
- small fix to print format

### 5/30/24

- started experimenting with piping into java encoder (want to make very rough simulation of the A->B and B->A communication)
- added c program that gets stdin and write to pipe

### 5/31/24

- added and fixed bugs in pipe stuff, still not working

### 6/3/24

- fixed ab tester, uses execlp and a script to encode/decode, but vector is same for every message for now
- merged with encoder5

