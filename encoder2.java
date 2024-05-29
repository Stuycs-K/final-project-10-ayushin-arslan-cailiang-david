import java.io.*;
import java.util.*;
public class encoder2{
  public static void main(String[] args) {
    // int[] SESSION_KEY = generateHex("4E2F4D7C1EB88B3A",8);
    // int[] SESSION_KEY = generateHex("1223456789ABCDEF",8);
    // int[] INITIALIZATION_VECTOR = generateHex("000133",3);
    // int[] Stream = byteStreamer(SESSION_KEY,INITIALIZATION_VECTOR);
    // System.out.println(Arrays.toString(Stream));
    if (args.length != 4) {
      System.out.println("Enter 4 items.");
      System.out.println("KEY INITIALIZATION_VECTOR INPUT_FILE OUTPUT_FILE");
      System.exit(0);
    }
    if (args[0].length() != 16) {
      System.out.println("Wrong sized key entered.");
      System.out.println("Keys are 16 character long hex strings.");
      System.exit(0);
    }
    if (!args[0].matches("[0-9a-fA-F]{16}")) {
      System.out.println("Key is a hex value.");
      System.exit(0);
    }
    if (args[1].length() != 6) {
      System.out.println("Wrong sized initialization vector entered.");
      System.out.println("Keys are 6 character long hex strings.");
      System.exit(0);
    }
    if (!args[1].matches("[0-9a-fA-F]{6}")) {
      System.out.println("Initialization vector is a hex value.");
      System.exit(0);
    }
    if (args[1].charAt(0) != 48 && args[1].charAt(0) != 52 && args[1].charAt(0) != 56 && args[1].charAt(0) != 67 && args[1].charAt(0) != 99) {
      System.out.println("Invalid initial character for initialization vector.");
      System.out.println("Must start with: 0,4,8,c,C");
      System.exit(0);
    }
    int[] SESSION_KEY = generateHex(args[0],8);
    int[] INITIALIZATION_VECTOR = generateHex(args[1],3);
    Random random = new Random((long) (INITIALIZATION_VECTOR[0] << 16 | INITIALIZATION_VECTOR[1] << 8 | INITIALIZATION_VECTOR[2]));
    try {
      ArrayList<Integer> plainText = new ArrayList<Integer> ();
      //File plain = new File("The-A5-1-stream-cipher-algorithm.png");
      File plain = new File(args[2]);
      FileInputStream plainReader = new FileInputStream(plain);
      File output = new File("output.dat");
      try {
        output.createNewFile();
      }
      catch(IOException ex) {
        System.out.println(ex);
      }
      int temp = -1;
      try {
        temp = plainReader.read();
      }
      catch(IOException ex) {
        System.out.println(ex);
      }
      while(temp != -1) {
        plainText.add(temp);
        try {
          temp = plainReader.read();
        }
        catch(IOException ex) {
          System.out.println(ex);
        }
      }
      int[] keyStream = new int[plainText.size()*8];
      for (int i = 0; i < keyStream.length+228; i+=228) {
        int[] temp2 = byteStreamer(SESSION_KEY,INITIALIZATION_VECTOR);
        hexify(temp2);
        int rand = random.nextInt(INITIALIZATION_VECTOR[0] << 16 | INITIALIZATION_VECTOR[1] << 8 | INITIALIZATION_VECTOR[2]);
        INITIALIZATION_VECTOR[0] = rand >> 16 | 0xFF;
        INITIALIZATION_VECTOR[1] = rand >> 8 | 0xFF;
        INITIALIZATION_VECTOR[2] = rand | 0xFF;
        for (int k = 0; k < 228; k++) {
          if (i+k < keyStream.length) {
          keyStream[i+k] = temp2[k];
        }
      }
    }
    //System.out.println(Arrays.toString(keyStream));
    //System.out.println(keyStream.length);
      try {
        FileOutputStream myWriter = new FileOutputStream(output);
        for (int i = 0; i < plainText.size(); i++) {
          int temp2 = (keyStream[8*i] << 7) | (keyStream[8*i+1] << 6) | (keyStream[8*i+2] << 5) | (keyStream[8*i+3] << 4) | (keyStream[8*i+4] << 3) | (keyStream[8*i+5] << 2) | (keyStream[8*i+6] << 1) | keyStream[8*i+7];
          //System.out.println(temp2);
          myWriter.write((byte) (plainText.get(i)^temp2));
        }
        myWriter.close();
      }
      catch (IOException ex) {
        System.out.println(ex);
      }
    }
    catch (FileNotFoundException ex) {
      System.out.println(ex);
    }
  }

  public static int[] byteStreamer(int[] SESSION_KEY, int[] INITIALIZATION_VECTOR) {
    //make encode2 ARGS="4E2F4D7C1EB88B3A 000134 input.dat output.dat"
    //make encode2 ARGS="72F4B23E781DD15C 000134 input.dat output.dat"
    System.out.println("KEY = ");
    for (int i = 0; i < SESSION_KEY.length; i++) {
      for (int b = 0; b < 8; b++) {
        System.out.print(SESSION_KEY[i] >> b & 1);
      }
      System.out.print(" ");
    }
    System.out.println("\n");

    int[] LSFR1 = new int[19];
    int[] LSFR2 = new int[22];
    int[] LSFR3 = new int[23];
    int start1 = 0;
    int start2 = 0;
    int start3 = 0;
    for (int k = 0; k < 8; k++) {
      for (int i = 0; i < 8; i++) {
        LSFR1[(start1+18)%19] = (SESSION_KEY[k] >> i & 1) ^ LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
        start1 = (start1+18)%19;
        LSFR2[(start2+21)%22] = (SESSION_KEY[k] >> i & 1) ^ LSFR2[(start2+20)%22] ^ LSFR2[(start2+21)%22];
        start2 = (start2+21)%22;
        LSFR3[(start3+22)%23] = (SESSION_KEY[k] >> i & 1) ^ LSFR3[(start3+7)%23] ^ LSFR3[(start3+20)%23] ^ LSFR3[(start3+21)%23] ^ LSFR3[(start3+22)%23];
        start3 = (start3+22)%23;
        // debugger(LSFR1,start1);
        // debugger(LSFR2,start2);
        // debugger(LSFR3,start3);
        // System.out.println("");
      }
    }
    // debugger(LSFR1,start1);
    // debugger(LSFR2,start2);
    // debugger(LSFR3,start3);
    // System.out.println("");
    ////
    ////
    for (int i = 2; i < 8; i++) {
      LSFR1[Math.floorMod(start1-1,19)] = (INITIALIZATION_VECTOR[0] >> i & 1) ^ LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
      start1 = Math.floorMod(start1-1,19);
      LSFR2[Math.floorMod(start2-1,22)] = (INITIALIZATION_VECTOR[0] >> i & 1) ^ LSFR2[(start2+20)%22] ^ LSFR2[(start2+21)%22];
      start2 = Math.floorMod(start2-1,22);
      LSFR3[Math.floorMod(start3-1,23)] = (INITIALIZATION_VECTOR[0] >> i & 1) ^ LSFR3[(start3+7)%23] ^ LSFR3[(start3+20)%23] ^ LSFR3[(start3+21)%23] ^ LSFR3[(start3+22)%23];
      start3 = Math.floorMod(start3-1,23);
    }
    for (int k = 1; k < 3; k++) {
      for (int i = 0; i < 8; i++) {
        LSFR1[(start1+18)%19] = (INITIALIZATION_VECTOR[k] >> i & 1) ^ LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
        start1 = (start1+18)%19;
        LSFR2[Math.floorMod(start2-1,22)] = (INITIALIZATION_VECTOR[k] >> i & 1) ^ LSFR2[(start2+20)%22] ^ LSFR2[(start2+21)%22];
        start2 = (start2+21)%22;
        LSFR3[Math.floorMod(start3-1,23)] = (INITIALIZATION_VECTOR[k] >> i & 1) ^ LSFR3[(start3+7)%23] ^ LSFR3[(start3+20)%23] ^ LSFR3[(start3+21)%23] ^ LSFR3[(start3+22)%23];
        start3 = (start3+22)%23;
      }
    }
    System.out.println("after reading frame number\n");
    debugger(LSFR1,start1);
    debugger(LSFR2,start2);
    debugger(LSFR3,start3);
    System.out.println("");
    ////
    ////
    // for (int i = 0; i < 100; i++) {
    //   int clock1 = LSFR1[Math.floorMod(start1 + 8, 19)];
    //   int clock2 = LSFR2[Math.floorMod(start2 + 10, 22)];
    //   int clock3 = LSFR3[Math.floorMod(start3 + 10, 23)];
    //   int maj_bit = (clock1+clock2+clock3)/2;
    //   if (clock1 == maj_bit) {
    //     LSFR1[(start1+18)%19] = LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
    //     start1 = (start1+18)%19;
    //   }
    //   if (clock2 == maj_bit) {
    //     LSFR2[(start2+21)%22] = LSFR2[(start2+20)%22] ^ LSFR2[(start2+21)%22];
    //     start2 = (start2+21)%22;
    //   }
    //   if (clock3 == maj_bit) {
    //     LSFR3[(start3+22)%23] = LSFR3[(start3+7)%23] ^ LSFR3[(start3+20)%23] ^ LSFR3[(start3+21)%23] ^ LSFR3[(start3+22)%23];
    //     start3 = (start3+22)%23;
    //   }
    // }
    // debugger(LSFR1,start1);
    // debugger(LSFR2,start2);
    // debugger(LSFR3,start3);
    // System.out.println("");
    ////
    ////
    int[] output = new int[228];
    for (int i = 0; i < 228; i++) {
      int clock1 = LSFR1[Math.floorMod(start1 + 8, 19)];
      int clock2 = LSFR2[Math.floorMod(start2 + 10, 22)];
      int clock3 = LSFR3[Math.floorMod(start3 + 10, 23)];
      int maj_bit = (clock1+clock2+clock3)/2;
      if (clock1 == maj_bit) {
        output[i] ^= LSFR1[(start1+18)%19];
        LSFR1[(start1+18)%19] = LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
        start1 = (start1+18)%19;
      }
      if (clock2 == maj_bit) {
        output[i] ^= LSFR2[(start2+21)%22];
        LSFR2[(start2+21)%22] = LSFR2[(start2+20)%22] ^ LSFR2[(start2+21)%22];
        start2 = (start2+21)%22;

      }
      if (clock3 == maj_bit) {
        output[i] ^= LSFR3[(start3+22)%23];
        LSFR3[(start3+22)%23] = LSFR3[(start3+7)%23] ^ LSFR3[(start3+20)%23] ^ LSFR3[(start3+21)%23] ^ LSFR3[(start3+22)%23];
        start3 = (start3+22)%23;
      }
    }
    return output;
  }

  public static int[] generateHex(String KEY,int size) {
    int[] SESSION_KEY = new int[size];
    for (int i = 0; i < 2*size; i+=2) {
      char temp1 = KEY.charAt(i);
      char temp2 = KEY.charAt(i+1);
      if (47 < temp1 && temp1 < 58) {
        temp1-=48;
      }
      else if (64 < temp1 && temp1 < 71) {
        temp1-=55;
      }
      if (47 < temp2 && temp2 < 58) {
        temp2-=48;
      }
      else if (64 < temp2 && temp2 < 71) {
        temp2-=55;
      }
      SESSION_KEY[i/2]= temp1 << 4 | temp2;
    }
    return SESSION_KEY;
  }

  public static void debugger(int[] input, int start1) {
    String[] output = new String[input.length];
    int pos = 0;
    for (int i = start1; i < input.length; i++) {
      output[pos]=input[i]+"";
      pos++;
    }
    for (int i = 0; i < start1; i++) {
      output[pos]=input[i]+"";
      pos++;
    }
    System.out.println(Arrays.toString(output));
  }

  public static void hexify(int[] data) {
    int output = 0;
    for (int i = 0; i < 114; i++) {
      output = output << 1 | data[i];
    }
    System.out.println(Integer.toHexString(output));
  }


}
