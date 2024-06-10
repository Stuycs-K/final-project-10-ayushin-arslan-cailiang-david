# Work Log

## David Cai Liang

### 5/22/2024

Worked on Stage 2 of the algorithm. It broke after I added 16 bits to the shift registers.

### 5/23/2024

Continue struggling on Stage 2.

### 5/24/2024

Can't get Stage 2 to agree with better implementation of A5/1

### 5/26/2024

Throw together an implementation of A5/1. It doesn't follow the C version or the interactive version. The beginning of my implementation follows the interactive version up to the point where we load the 64th bit of key into the registers. From that point on, I use the basic structure of the c version. The loading of initialization vector and the key are different though. I can't figure out how to edit my loops to closer follow the c version, as fundamentally the algorithm should be the same, even though I currently have no way to test it as the loading between their version and mine is different.

### 5/27/2024

Did research for presentation and started writing presentation.

### 5/28/2024

Continue Debugging algorithm (Had issues with little endian vs big endian)

### 5/29/2024

Got algorithm to work with reference algorithm.

### 5/30/2024

Did more research on GSM standard.

### 5/31/2024

Rewrote the algorithm using bitwise operations primarily. Added proper padding to the encryption and decryption algorithm. Edited the README to match correct features of the algorithm.

### 6/3/2024

Added citations to the presentation.

### 6/5/2024

Continue working on presentation.

### 6/7/2024

Did presentation prep.

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

### 6/5/24

- added links to info to presentation about cracking a5/1

### 6/7/24

- working on algorithm presentation in class
