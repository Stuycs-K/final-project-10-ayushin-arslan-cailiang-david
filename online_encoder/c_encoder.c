#include <stdio.h>

/*
 * A pedagogical implementation of A5/1.
 *
 * Copyright (C) 1998-1999: Marc Briceno, Ian Goldberg, and David Wagner
 *
 * The source code below is optimized for instructional value and clarity.
 * Performance will be terrible, but that's not the point.
 * The algorithm is written in the C programming language to avoid ambiguities
 * inherent to the English language. Complain to the 9th Circuit of Appeals
 * if you have a problem with that.
 *
 * This software may be export-controlled by US law.
 *
 * This software is free for commercial and non-commercial use as long as
 * the following conditions are aheared to.
 * Copyright remains the authors' and as such any Copyright notices in
 * the code are not to be removed.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHORS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * The license and distribution terms for any publicly available version or
 * derivative of this code cannot be changed.  i.e. this code cannot simply be
 * copied and put under another distribution license
 * [including the GNU Public License.]
 *
 * Background: The Global System for Mobile communications is the most widely
 * deployed cellular telephony system in the world. GSM makes use of
 * four core cryptographic algorithms, neither of which has been published by
 * the GSM MOU. This failure to subject the algorithms to public review is all
 * the more puzzling given that over 100 million GSM
 * subscribers are expected to rely on the claimed security of the system.
 *
 * The four core GSM algorithms are:
 * A3		authentication algorithm
 * A5/1		"strong" over-the-air voice-privacy algorithm
 * A5/2		"weak" over-the-air voice-privacy algorithm
 * A8		voice-privacy key generation algorithm
 *
 * In April of 1998, our group showed that COMP128, the algorithm used by the
 * overwhelming majority of GSM providers for both A3 and A8
 * functionality was fatally flawed and allowed for cloning of GSM mobile
 * phones.
 * Furthermore, we demonstrated that all A8 implementations we could locate,
 * including the few that did not use COMP128 for key generation, had been
 * deliberately weakened by reducing the keyspace from 64 bits to 54 bits.
 * The remaining 10 bits are simply set to zero!
 *
 * See http://www.scard.org/gsm for additional information.
 *
 * The question so far unanswered is if A5/1, the "stronger" of the two
 * widely deployed voice-privacy algorithm is at least as strong as the
 * key. Meaning: "Does A5/1 have a work factor of at least 54 bits"?
 * Absent a publicly available A5/1 reference implementation, this question
 * could not be answered. We hope that our reference implementation below,
 * which has been verified against official A5/1 test vectors, will provide
 * the cryptographic community with the base on which to construct the
 * answer to this important question.
 *
 * Initial indications about the strength of A5/1 are not encouraging.
 * A variant of A5, while not A5/1 itself, has been estimated to have a
 * work factor of well below 54 bits. See http://jya.com/crack-a5.htm for
 * background information and references.
 *
 * With COMP128 broken and A5/1 published below, we will now turn our attention
 * to A5/2. The latter has been acknowledged by the GSM community to have
 * been specifically designed by intelligence agencies for lack of security.
 *
 */


#include <stdio.h>

/* Masks for the three shift registers */
#define R1MASK	0x07FFFF /* 19 bits, numbered 0..18 */
#define R2MASK	0x3FFFFF /* 22 bits, numbered 0..21 */
#define R3MASK	0x7FFFFF /* 23 bits, numbered 0..22 */

/* Middle bit of each of the three shift registers, for clock control */
#define R1MID	0x000100 /* bit 8 */
#define R2MID	0x000400 /* bit 10 */
#define R3MID	0x000400 /* bit 10 */

/* Feedback taps, for clocking the shift registers.
 * These correspond to the primitive polynomials
 * x^19 + x^5 + x^2 + x + 1, x^22 + x + 1,
 * and x^23 + x^15 + x^2 + x + 1. */
#define R1TAPS	0x072000 /* bits 18,17,16,13 */
#define R2TAPS	0x300000 /* bits 21,20 */
#define R3TAPS	0x700080 /* bits 22,21,20,7 */

/* Output taps, for output generation */
#define R1OUT	0x040000 /* bit 18 (the high bit) */
#define R2OUT	0x200000 /* bit 21 (the high bit) */
#define R3OUT	0x400000 /* bit 22 (the high bit) */

typedef unsigned char byte;
typedef unsigned long word;
typedef word bit;

/* Calculate the parity of a 32-bit word, i.e. the sum of its bits modulo 2 */
bit parity(word x) {
	x ^= x>>16;
	x ^= x>>8;
	x ^= x>>4;
	x ^= x>>2;
	x ^= x>>1;
	return x&1;
}

/* Clock one shift register */
word clockone(word reg, word mask, word taps) {
	word t = reg & taps;
	reg = (reg << 1) & mask;
	reg |= parity(t);
	return reg;
}

/* The three shift registers.  They're in global variables to make the code
 * easier to understand.
 * A better implementation would not use global variables. */
word R1, R2, R3;

/* Look at the middle bits of R1,R2,R3, take a vote, and
 * return the majority value of those 3 bits. */
bit majority() {
	int sum;
	sum = parity(R1&R1MID) + parity(R2&R2MID) + parity(R3&R3MID);
	if (sum >= 2)
		return 1;
	else
		return 0;
}

/* Clock two or three of R1,R2,R3, with clock control
 * according to their middle bits.
 * Specifically, we clock Ri whenever Ri's middle bit
 * agrees with the majority value of the three middle bits.*/
void clock() {
	bit maj = majority();
	if (((R1&R1MID)!=0) == maj)
		R1 = clockone(R1, R1MASK, R1TAPS);
	if (((R2&R2MID)!=0) == maj)
		R2 = clockone(R2, R2MASK, R2TAPS);
	if (((R3&R3MID)!=0) == maj)
		R3 = clockone(R3, R3MASK, R3TAPS);
}

/* Clock all three of R1,R2,R3, ignoring their middle bits.
 * This is only used for key setup. */
void clockallthree() {
	R1 = clockone(R1, R1MASK, R1TAPS);
	R2 = clockone(R2, R2MASK, R2TAPS);
	R3 = clockone(R3, R3MASK, R3TAPS);
}

/* Generate an output bit from the current state.
 * You grab a bit from each register via the output generation taps;
 * then you XOR the resulting three bits. */
bit getbit() {
	return parity(R1&R1OUT)^parity(R2&R2OUT)^parity(R3&R3OUT);
}


void printbinary(word n, int count) {
	char* buff = calloc(count + 64, sizeof(char));
	int i = 0;
	while (n && i < count) {
		if (n & 1) {
			buff[i] = '1';
			i++;
		}
		else {
			buff[i] = '0';
			i++;
		}
		n >>= 1;
	}
	for (i = 0; i < count; i++) {
		if (buff[i] == 0) {
			buff[i] = '0';
		}
	}
	printf("%s\n", buff);
}
void printbinaryfrombyte(byte n, int count) {
	char* buff = calloc(count + 64, sizeof(char));
	int i = 0;
	while (n && i < count) {
		if (n & 1) {
			buff[i] = '1';
			i++;
		}
		else {
			buff[i] = '0';
			i++;
		}
		n >>= 1;
	}
	for (i = 0; i < count; i++) {
		if (buff[i] == 0) {
			buff[i] = '0';
		}
	}
	printf("%s\n", buff);
}

/* Do the A5/1 key setup.  This routine accepts a 64-bit key and
 * a 22-bit frame number. */
void keysetup(byte key[8], word frame) {
	int i;
	bit keybit, framebit;

	/* Zero out the shift registers. */
	R1 = R2 = R3 = 0;

	/* Load the key into the shift registers,
	 * LSB of first byte of key array first,
	 * clocking each register once for every
	 * key bit loaded.  (The usual clock
	 * control rule is temporarily disabled.) */
	for (i=0; i<64; i++) {
		clockallthree(); /* always clock */
		keybit = (key[i/8] >> (i&7)) & 1; /* The i-th bit of the
key */
		R1 ^= keybit; R2 ^= keybit; R3 ^= keybit;

		// if (i < 4) {
		// 	printf("Read %d\nKey: ", i);
		// 	printbinary(keybit, 1);
		// 	printf("R1: %x\n", R1);
		// 	printbinary(R1, 19);
		// 	printf("R2: %x\n", R2);
		// 	printbinary(R2, 22);
		// 	printf("R3: %x\n", R3);
		// 	printbinary(R3, 23);

		// 	printf("\n");
		// }
	}

	// printf("stage 2\n");
	// printf("R1: %x\n", R1);
	// printbinary(R1, 19);
	// printf("R2: %x\n", R2);
	// printbinary(R2, 22);
	// printf("R3: %x\n", R3);
	// printbinary(R3, 23);

	// printf("\n");

	/* Load the frame number into the shift
	 * registers, LSB first,
	 * clocking each register once for every
	 * key bit loaded.  (The usual clock
	 * control rule is still disabled.) */
	for (i=0; i<22; i++) {
		clockallthree(); /* always clock */
		framebit = (frame >> i) & 1; /* The i-th bit of the frame #
*/
		R1 ^= framebit; R2 ^= framebit; R3 ^= framebit;
		// printf("after %d\n", i);
		// printf("R1: %x\n", R1);
		// printbinary(R1, 19);
		// printf("R2: %x\n", R2);
		// printbinary(R2, 22);
		// printf("R3: %x\n", R3);
		// printbinary(R3, 23);
		// printf("\n");
	}

	// printf("after reading frame number\n");
	// printf("R1: %x\n", R1);
	// printbinary(R1, 19);
	// printf("R2: %x\n", R2);
	// printbinary(R2, 22);
	// printf("R3: %x\n", R3);
	// printbinary(R3, 23);

	// printf("\n");

	/* Run the shift registers for 100 clocks
	 * to mix the keying material and frame number
	 * together with output generation disabled,
	 * so that there is sufficient avalanche.
	 * We re-enable the majority-based clock control
	 * rule from now on. */
	for (i=0; i<100; i++) {
		clock();
		if ( i < 10 ) {
			printf("after %d clocks\n", i+1);
			printf("R1: %x\n", R1);
			printbinary(R1, 19);
			printf("R2: %x\n", R2);
			printbinary(R2, 22);
			printf("R3: %x\n", R3);
			printbinary(R3, 23);
 			printf("\n");
		 }
	}

	printf("after 100 clocks\n");
	printf("R1: %x\n", R1);
	printbinary(R1, 19);
	printf("R2: %x\n", R2);
	printbinary(R2, 22);
	printf("R3: %x\n", R3);
	printbinary(R3, 23);

	// printf("\n");

	/* Now the key is properly set up. */
}

/* Generate output.  We generate 228 bits of
 * keystream output.  The first 114 bits is for
 * the A->B frame; the next 114 bits is for the
 * B->A frame.  You allocate a 15-byte buffer
 * for each direction, and this function fills
 * it in. */
void run(byte AtoBkeystream[], byte BtoAkeystream[]) {
	int i;

	/* Zero out the output buffers. */
	for (i=0; i<=113/8; i++)
		AtoBkeystream[i] = BtoAkeystream[i] = 0;

	/* Generate 114 bits of keystream for the
	 * A->B direction.  Store it, MSB first. */
	for (i=0; i<114; i++) {
		clock();
		AtoBkeystream[i/8] |= getbit() << (7-(i&7));
		if (i < 20) {
		printf("one loop\n");
		printf("R1: %x\n", R1);
		printbinary(R1, 19);
		printf("R2: %x\n", R2);
		printbinary(R2, 22);
		printf("R3: %x\n", R3);
		printbinary(R3, 23);
		printf("Output: %x\n", getbit());
		printf("\n");
	}
// if (i == 113) {
// printf("113 loop\n");
// printf("R1: %x\n", R1);
// printbinary(R1, 19);
// printf("R2: %x\n", R2);
// printbinary(R2, 22);
// printf("R3: %x\n", R3);
// printbinary(R3, 23);
// printf("Output: %x\n", getbit());
// printf("\n");
// }
	}

	/* Generate 114 bits of keystream for the
	 * B->A direction.  Store it, MSB first. */
	for (i=0; i<114; i++) {
		clock();
		BtoAkeystream[i/8] |= getbit() << (7-(i&7));
	}
}
unsigned int hexchartovalue(char ch)
{
  static char hexchars[] = "0123456789ABCDEF";
  char *p = strchr(hexchars, ch);
  if(!p)
  {
    fprintf(stderr, "Invalid hex digit: %c\n", ch);
    return(0);
  }
  return p - hexchars;
}
unsigned int str_to_hex(char *str)
{
  unsigned int len = strlen(str);
  unsigned int nibble;
  char *s;
  unsigned int rv = 0;

  for(s = str + len - 1;s >= str;s -= 2)
  {
    nibble = hexchartovalue(*s);
    if(s > str)
      nibble |= hexchartovalue(*(s - 1)) << 4;
    rv |= nibble << (((len - (s - str)) / 2) * 8);
  }

  return rv;
}

void test1(char *hex, char *f) {

char *hexstring = "1223456789ABCDEF", *pos = hexstring;
byte key[8];
word frame;


hexstring=hex;

    int j, n;
    for(j = 0; j < 8;j++) {
        sscanf(hexstring+2*j, "%2X", &n);
        key[j] = (char)n;
    }

frame = str_to_hex(f);


	/*word frame = 0x134;*/

	byte AtoB[15], BtoA[15];
	int i, failed=0;

	printf("Key = ");
	for (i = 0; i < 8; i++) {
		printf("%uc ", key[i]);
	}
	printf("\n");
	for (i = 0; i < 8; i++) {
		printbinaryfrombyte(key[i], 8);
	}
	printf("\n\n");

	keysetup(key, frame);
	run(AtoB, BtoA);

	/* Print some debugging output. */

	printf("key: 0x");
	for (i=0; i<8; i++)
		printf("%02X", key[i]);

	printf("\n");
	printf("frame number: 0x%06X\n", (unsigned int)frame);

	printf("observed output:\n");
	printf(" A->B: 0x");
	for (i=0; i<15; i++)
		printf("%02X", AtoB[i]);
	printf("  B->A: 0x");
	for (i=0; i<15; i++)
		printf("%02X", BtoA[i]);
	printf("\n");


}


int main( int argc, char * argv [] ) {

	  char *testkey="1223456789ABCDEF";
	// char *testkey="4E2F4D7C1EB88B3A"; // from interactive sim
	// 01110010 11110100 10110010 00111110 01111000 00011101 11010001 01011100
	//char *testkey="72F4B23E781DD15C"; // reversed version
	// char *frame="133";
	char *frame = "134";
	if (argc==1)
	test1(testkey,frame);
	else if (argc==3)
{

	test1(argv[1],argv[2]);
}
	return 0;
}
