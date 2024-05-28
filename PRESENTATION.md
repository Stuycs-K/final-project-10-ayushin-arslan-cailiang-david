# History of GSM

GSM stands for Global System for Mobile Communications. It is a standard for digital cellular communications.
It was first implemented by European Telecommunications Standards Institute, in 1991. The trademark is currently held by Global System
for Mobile Communications (aka GSM Association). GSM was built upon earlier standards like American Mobile Phone System (AMPS)
and Nordic Mobile Telephone (NMT) 450. But these earlier standards were built on analog systems rather than digital systems,
making them significantly more costly to operate compared to GSM and other more modern communications standards.

An important part of GSM is narrowband time division multiple accession (TDMA), which allows multiple people to use the
same band of radio frequencies simultaneous, by switching between connections rapidly. This is allowed for by using digital
techniques rather than analog techniques.

In addition, GSM was fully duplex meaning uplink and downlink frequencies were different allowing you to simultaneously receive
and transmit, which allows a lot more data to be sent. This need for additional frequencies was compensated for by the use of TDMA,
which makes each channel much more data dense. This allowed for higher speed non-voice cellular data.

GSM was also one of the first protocols to encrypt its communications streams.

Improvement on GSM:

Enhanced Data rates for GSM Evolution (EDGE)

Evolved EDGE

3G (Switch to A5/3 aka KASUMI)

4G (AES-Based Encryption Only)

5G

# Security of GSM

The four core GSM algorithms are:

A3		authentication algorithm (based on COMP128)
A5/1	"strong" over-the-air voice-privacy algorithm (Developed in 1987)
A5/2	"weak" over-the-air voice-privacy algorithm   (Developed in 1989)
A8		voice-privacy key generation algorithm (based on COMP128)

# Vulnerabilities in GSM

In 1999, Ian Goldberg and David A. Wagner developed a method to crack A5/2 just one month after it was reverse engineered.
The algorithm turned out to be so weak that it was possible to crack it in real time in 1999.

In 2003, Elad Barkan, Eli Biham and Nathan Keller found a vulnerability in error correction code that allowed a ciphertext-only
attack. A ciphertext-only attack would allow someone who only has access to a bunch ciphertext to crack the cipher. They also
found a way to force A5/2 capable device to use A5/2 over A5/1, making the other vulnerability they found extremely powerful.

In 2006, A5/2 was banned from implementation in new devices. All existing devices capable of using A5/1 were forced to use A5/1
from this point forward. All devices only capable of using A5/2 were forced to use a unencrypted connection, which isn't ideal.
But this does give an idea as to how weak A5/2 is.

That being said, A5/1 is also pretty weak. It just wasn't completely neutered by sanctions and export restrictions.

In 2000, Alex Biryukov, Adi Shamir and David Wagner figured out that you cryptanalyze A5/1 in real time using a time-memory tradeoff
attack. One tradeoff allows an attacker to reconstruct the key in one second from two minutes of known plaintext or in several minutes
from two seconds of known plain text, but he must first complete an expensive preprocessing stage which requires 248 steps to compute
around 300 GB of data.

This attack was then further optimized by Ekdahl and Johansson, Maximov et al., and Elad Barkan and Eli Biham so that the attack requires
less than one minute of computations, and a few seconds of known conversation".

In December 2009, The A5/1 Cracking Project attack tables were released by Chris Paget and Karsten Nohl. These tables included rainbow
tables and were 1.7TB in size.

------
In 1998, COMP128 was reversed engineered and fully published online. COMP128 takes a 128-bit key and a 128-bit RAND to produce a 128-bit
output. COMP128 is considered a hashing algorithm as it is extremely difficult to go back unless you already know both parts: the key and
the RAND. The RAND is a challenge code sent from the cell tower. There were 3 versions of the algorithm named -1, -2, and -3. -1 is
considered extremely weak but -2 and -3 are considered weak. AES based algorithms have replaced the COMP128 algorithms almost completely.
The COMP128 algorithm's main problem is the small output and weak diffusion.  

# Confidentiality in GSM and GSM-derived protocols (aka most modern protocols EDGE, 3G, and 4G)

Note: 5G uses more complex methods to maintain confidentiality.

GSM uses a International mobile subscriber identity (IMSI) number to identify a subscriber. It is normally a 15 digit number, with a two or three digit country code, a three digit service provider code, and a nine or ten digit subscriber code. This is different from a
International Mobile Equipment Identity (IMEI) number which identifies a phone or a Integrated Circuit Card Identifier (ICCID) number
 which identifies a SIM card. A Mobile Station International Subscriber Directory (MSSIDN) number is simply your whole phone number.
 IMEI is generated using a Type Allocation Code (TAC), which is managed by the GMS Association.

Your IMSI is transmitted as infrequently as possible as to prevent tracking and identification of the user. Instead a Temporary Mobile Subscriber Identity (TMSI) is sent, if possible. Sometimes the IMSI itself has to be sent to re-establish communications after a desynchronization in security related information like challenge codes. It also has to be sent when a device is first connect to a
network and needs to receive up to date security information from the nearest cell tower.

# SIM Cards

A Subscriber Identity Module (SIM) contains the following:

1) security authentication and cipher information. (eg: 128-bit authentication key)

2) temporary information regarding the local network (eg: challenge codes and local area identity)

3) IMSI (identities the subscriber)

4) ICCID (identifies the SIM card)

5) PIN (used to unlock access to the SIM card to allow non-emergency calls and data to go through)

A SIM card often also contains additional user authentication information and a contact list.

# IMSI-Catcher

A IMSI-Catcher is a eavesdropping device used to intercept network traffic and get location information of subscribers by being a fake
cell tower. 3G and beyond has protection against this through the use of mutual authentication. But GSM doesn't have this protection.
Meaning that if you are able to degrade LTE and 3G communications enough, you may be able to force a device to use GSM, which is more
venerable to this attack. Advanced IMSI-Catchers do exist that can fake the mutual authentication step as well.

The units that are capable of doing this are exclusively active units, which act as fully functionally cell towers. Passive units, on the
 other hand, act a lot more like pure listening devices, even though some passive units are capable of sending out limited commands to
 mobile devices.

# DVB-T

DVB-T is a satellite communications protocol meant for TV. The receivers for this standard is extremely common and many of them are able
to operate on frequencies not used by satellite TV. Many of them also tend to be software-defined meaning they don't decode in hardware
but rather than software, allowing them to work with many communications protocols such as GSM. These devices allow for the assembly of
a purely passive IMSI-catcher for less than $50.

# Additional Technical Specifications of GSM

The two GSM radio channels commonly used in the US:

GSM Band    Uplink (MHz)       Downlink (MHz)

GSM-850     824.2 – 848.8      869.2 – 893.8

PCS-1900 	  1850.2 – 1909.8 	 1930.2 – 1989.8 	

# How A5/1 functions

<details>
<summary>Goals of A5/1</summary>
<img src="https://github.com/Stuycs-K/final-project-10-ayushin-arslan-cailiang-david/blob/main/Image0.png" alt="General Goals of A5/1" width="250">
</details>

<details>
<summary>Registers Before A5/1</summary>
<img src="https://github.com/Stuycs-K/final-project-10-ayushin-arslan-cailiang-david/blob/main/Image1.png" alt="Registers Before A5/1" width="250">
</details>

<details>
<summary>General Information about Registers</summary>
<img src="https://github.com/Stuycs-K/final-project-10-ayushin-arslan-cailiang-david/blob/main/Image2.png" alt="General Information about Registers" width="250">
</details>

<details>
<summary>Clock Once</summary>
<img src="https://github.com/Stuycs-K/final-project-10-ayushin-arslan-cailiang-david/blob/main/Image3.png" alt="Clock Once" width="250">
</details>

<details>
<summary>Insert Key Into Registers</summary>
<img src="https://github.com/Stuycs-K/final-project-10-ayushin-arslan-cailiang-david/blob/main/Image4.png" alt="Insert Key Into Registers" width="250">
</details>

1) We xor the first bit of the key with the tapping bits for all the registers.

For LFSR1, we xor the first bit of the key with bits from index 13, 16, 17, and 18.

For LFSR2, we xor the first bit of the key with bits from index 20 and 21.

For LFSR3, we xor the first bit of the key with bits from index 7, 20, 21, and 22.

2) We shift all the registers up by one and insert the new xored bit at index 0.

3) We repeat this process for the entire 64-bit key.

<details>
<summary>Insert Vector Into Registers</summary>
<img src="https://github.com/Stuycs-K/final-project-10-ayushin-arslan-cailiang-david/blob/main/Image5.png" alt="Insert Vector Into Registers" width="250">
</details>

4) We repeat the process for the key on the initialization vector.

<details>
<summary>Outcome after Key & Vector Insertion</summary>
<img src="https://github.com/Stuycs-K/final-project-10-ayushin-arslan-cailiang-david/blob/main/Image8.png" alt="Outcome after Key & Vector Insertion" width="250">
</details>

<details>
<summary>Irregularly Clock V1</summary>
<img src="https://github.com/Stuycs-K/final-project-10-ayushin-arslan-cailiang-david/blob/main/Image6.png" alt="Irregularly Clock V1" width="250">
</details>

<details>
<summary>Irregularly Clock V2</summary>
<img src="https://github.com/Stuycs-K/final-project-10-ayushin-arslan-cailiang-david/blob/main/Image7.png" alt="Irregularly Clock V2" width="250">
</details>

<details>
<summary>Clocking Rules</summary>
<img src="https://github.com/Stuycs-K/final-project-10-ayushin-arslan-cailiang-david/blob/main/Image9.png" alt="Clocking Rules" width="250">
</details>

5) We find the majority bit in the clock bit index of all 3 registers. 1 is the majority if it is present in the clock bit index
of two or more registers.
6) Registers with the majority bit in the clock bit index have all their tapping bits xored together, are then shifted by one, and then have the new xored value placed at index 0. The same process as Step 1-3.
7) We repeat this process 99 more times to mix the registers.

<details>
<summary>Generate 114 bit RAND</summary>
<img src="https://github.com/Stuycs-K/final-project-10-ayushin-arslan-cailiang-david/blob/main/Image10.png" alt="Generate 114 bit RAND" width="250">
</details>

8) Save and xor the last bit of all the registers. Perform Steps 7-9 again.
9) Repeat Step 10, another 227 times so you get a 228 bit keyStream.
10) Xor this 228 bit keyStream with the first 228 bits of your plaintext.

11) Add 1 to your 22-bit initialization vector.
12) Repeat Step 1-11 until your entire plaintext is encrypted.



# Resources

http://koclab.cs.ucsb.edu/teaching/cren/project/2017/jensen+andersen.pdf <= Documentation On A5/1

https://epgp.inflibnet.ac.in/epgpdata/uploads/epgp_content/S000305IT/P001487/M017205/ET/147022641604ET.pdf <= GSM Security Documentation

https://www.uky.edu/~jclark/mas355/GSM.PDF <= General Information On GSM

https://en.wikipedia.org/wiki/A5/1 <= Wikipedia A5/1

https://en.wikipedia.org/wiki/A5/2 <= Wikipedia A5/2

https://en.wikipedia.org/wiki/KASUMI <= Wikipedia A5/3

https://en.wikipedia.org/wiki/GSM <= Wikipedia GSM

https://en.wikipedia.org/wiki/Enhanced_Data_rates_for_GSM_Evolution <= Wikipedia EDGE

https://en.wikipedia.org/wiki/3G <= Wikipedia 3G

https://link.springer.com/chapter/10.1007/978-3-540-45146-4_35 <= A5/2 Force Vulnerability

https://en.wikipedia.org/wiki/Digital_AMPS <= Digital AMPS (Competing Standard with GSM)

https://www.ericsson.com/en/blog/2021/6/evolution-of-cryptographic-algorithms <= History of Modern Cellular Cryptography.

https://www.efani.com/blog/difference-between-imei-imsi-iccid-and-msisdn-numbers <= Confidentiality Related Information

https://en.wikipedia.org/wiki/International_mobile_subscriber_identity <= Wikipedia IMSI

https://en.wikipedia.org/wiki/DVB-T <= Wikipedia DVB-T

https://en.wikipedia.org/wiki/GSM_frequency_bands <= Wikipedia GSM Frequency Bands

https://en.wikipedia.org/wiki/SIM_card <= Wikipedia SIM cards
