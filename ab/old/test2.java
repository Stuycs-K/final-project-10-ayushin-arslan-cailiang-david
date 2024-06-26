import java.io.*;
import java.util.*;
public class test2{
  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("Enter 3 items.");
      System.out.println("KEY   INITIALIZATION_VECTOR   MODE(A->B,B->A)");
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
    // ////
    // //Print Key
    // System.out.println("KEY = ");
    // for (int i = 0; i < SESSION_KEY.length; i++) {
    //   for (int b = 0; b < 8; b++) {
    //     System.out.print(SESSION_KEY[i] >> b & 1);
    //   }
    //   System.out.print(" ");
    // }
    // System.out.println("\n");
    // ////
    int[] INITIALIZATION_VECTOR = generateHex(args[1],3);
    // try {
      // ArrayList<Integer> plainText = new ArrayList<Integer> ();
      // File plain = new File(args[2]);
      // FileInputStream plainReader = new FileInputStream(plain);
      // File output = new File(args[3]);
      // try {
      //   output.createNewFile();
      // }
      // catch(IOException ex) {
      //   System.out.println(ex);
      // }
      // int temp = -1;
      // try {
      //   temp = plainReader.read();
      // }
      // catch(IOException ex) {
      //   System.out.println(ex);
      // }
      // while(temp != -1) {
      //   plainText.add(temp);
      //   try {
      //     temp = plainReader.read();
      //   }
      //   catch(IOException ex) {
      //     System.out.println(ex);
      //   }
      // }

      // int[] keyStream = new int[8*plainText.size()+228];
      // for (int i = 0; i < keyStream.length; i+=228) {
      //   int[] temp2 = byteStreamer(SESSION_KEY,INITIALIZATION_VECTOR);
      //   String MODE = args[2];
      //   HexStringer(temp2, MODE);
      //   int newVector = ((INITIALIZATION_VECTOR[0] << 16 | INITIALIZATION_VECTOR[1] << 8 | INITIALIZATION_VECTOR[2]) + 1)   % 8388607;
      //   INITIALIZATION_VECTOR[0] = newVector >> 16 & 0xFF;
      //   INITIALIZATION_VECTOR[1] = newVector >> 8 & 0xFF;
      //   INITIALIZATION_VECTOR[2] = newVector & 0xFF;
      //   for (int k = 0; k < 228; k++) {
      //     if (i+k < keyStream.length) {
      //       keyStream[i+k] = temp2[k];
      //     }
      //   }
      // }
      BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

      File vectorAlreadyUsed = new File("vector_already_used");
      // FileReader usedReader = new FileReader(vectorAlreadyUsed);
      // FileWriter usedWriter = new FileWriter(vector_already_used);
      // int ch;
      // String numstring = "";
      // while (ch = usedReader.read() != 1) {
      //   numstring += ((char) ch);
      // }
      // int ALREADY_USED = Integer.parseInt(numstring);

      try {
          String line = "";
          line = stdin.readLine();
          int index = 0;
          while (line != null) {
            int[] temp2 = byteStreamer(SESSION_KEY, INITIALIZATION_VECTOR);

            // System.err.println("LINE: " + line);

            String MODE = args[2];

            int newVector = ((INITIALIZATION_VECTOR[0] << 16 | INITIALIZATION_VECTOR[1] << 8 | INITIALIZATION_VECTOR[2]) + 1)   % 8388607;
            INITIALIZATION_VECTOR[0] = newVector >> 16 & 0xFF;
            INITIALIZATION_VECTOR[1] = newVector >> 8 & 0xFF;
            INITIALIZATION_VECTOR[2] = newVector & 0xFF;

            int[] keyStream = new int[1234567];


            if (MODE.equals("A->B")) {
              for (int k = 0; k < 114; k++) {
                if (index+k < keyStream.length) {
                  keyStream[index+k] = temp2[k];
                }
              }
            }
            else {
              for (int k = 114; k < 228; k++) {
                if (index+k < keyStream.length) {
                  keyStream[index+k] = temp2[k];
                }
              }
            }

            byte[] line_bytes = line.getBytes();
            byte[] bytesToWrite = new byte[1234567];
            for (int i = 0; i < line.length(); i++) {
              int streambyte = (keyStream[8*i] << 7) | (keyStream[8*i+1] << 6) | (keyStream[8*i+2] << 5) | (keyStream[8*i+3] << 4) | (keyStream[8*i+4] << 3) | (keyStream[8*i+5] << 2) | (keyStream[8*i+6] << 1) | keyStream[8*i+7];
              bytesToWrite[i] = ((byte) (line_bytes[i]^streambyte));
            }
            System.out.write(bytesToWrite);


            index += 228;
            line = stdin.readLine();
          }

      }
      catch (IOException e) {
          e.printStackTrace();
      }

      // try {
      //   FileOutputStream myWriter = new FileOutputStream(output);
      //   for (int i = 0; i < plainText.size(); i++) {
      //     int temp2 = (keyStream[8*i] << 7) | (keyStream[8*i+1] << 6) | (keyStream[8*i+2] << 5) | (keyStream[8*i+3] << 4) | (keyStream[8*i+4] << 3) | (keyStream[8*i+5] << 2) | (keyStream[8*i+6] << 1) | keyStream[8*i+7];
      //     myWriter.write((byte) (plainText.get(i)^temp2));
      //   }
      //   ////
      //   for (int i = plainText.size(); i < keyStream.length/8; i++) {
      //     int temp2 = (keyStream[8*i] << 7) | (keyStream[8*i+1] << 6) | (keyStream[8*i+2] << 5) | (keyStream[8*i+3] << 4) | (keyStream[8*i+4] << 3) | (keyStream[8*i+5] << 2) | (keyStream[8*i+6] << 1) | keyStream[8*i+7];
      //     myWriter.write((byte) temp2);
      //   }
      //   ////
      //   myWriter.close();
      // }
      // catch (IOException ex) {
      //   System.out.println(ex);
      // }
    // }
    // catch (FileNotFoundException ex) {
    //   System.out.println(ex);
    // }
  }

  public static int[] byteStreamer(int[] SESSION_KEY, int[] INITIALIZATION_VECTOR) {
    //make encode2 ARGS="4E2F4D7C1EB88B3A 000134 input.dat output.dat"
    //make encode2 ARGS="72F4B23E781DD15C 000134 input.dat output.dat"
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
    // System.out.println("after reading key");
    // debugger(LSFR1,start1);
    // debugger(LSFR2,start2);
    // debugger(LSFR3,start3);
    // System.out.println("");

    for (int k = 2; k > 0; k--) {
      for (int i = 0; i < 8; i++) {
        LSFR1[(start1+18)%19] = (INITIALIZATION_VECTOR[k] >> i & 1) ^ LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
        start1 = (start1+18)%19;
        LSFR2[Math.floorMod(start2-1,22)] = (INITIALIZATION_VECTOR[k] >> i & 1) ^ LSFR2[(start2+20)%22] ^ LSFR2[(start2+21)%22];
        start2 = (start2+21)%22;
        LSFR3[Math.floorMod(start3-1,23)] = (INITIALIZATION_VECTOR[k] >> i & 1) ^ LSFR3[(start3+7)%23] ^ LSFR3[(start3+20)%23] ^ LSFR3[(start3+21)%23] ^ LSFR3[(start3+22)%23];
        start3 = (start3+22)%23;
      }
    }
    for (int i = 0; i < 6; i++) {
      LSFR1[Math.floorMod(start1-1,19)] = (INITIALIZATION_VECTOR[0] >> i & 1) ^ LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
      start1 = Math.floorMod(start1-1,19);
      LSFR2[Math.floorMod(start2-1,22)] = (INITIALIZATION_VECTOR[0] >> i & 1) ^ LSFR2[(start2+20)%22] ^ LSFR2[(start2+21)%22];
      start2 = Math.floorMod(start2-1,22);
      LSFR3[Math.floorMod(start3-1,23)] = (INITIALIZATION_VECTOR[0] >> i & 1) ^ LSFR3[(start3+7)%23] ^ LSFR3[(start3+20)%23] ^ LSFR3[(start3+21)%23] ^ LSFR3[(start3+22)%23];
      start3 = Math.floorMod(start3-1,23);
    }
    // System.out.println("after reading frame number\n");
    // debugger(LSFR1,start1);
    // debugger(LSFR2,start2);
    // debugger(LSFR3,start3);
    // System.out.println("");

    for (int i = 0; i < 100; i++) {
      int clock1 = LSFR1[Math.floorMod(start1 + 8, 19)];
      int clock2 = LSFR2[Math.floorMod(start2 + 10, 22)];
      int clock3 = LSFR3[Math.floorMod(start3 + 10, 23)];
      int maj_bit = (clock1+clock2+clock3)/2;
      if (clock1 == maj_bit) {
        LSFR1[(start1+18)%19] = LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
        start1 = (start1+18)%19;
      }
      if (clock2 == maj_bit) {
        LSFR2[(start2+21)%22] = LSFR2[(start2+20)%22] ^ LSFR2[(start2+21)%22];
        start2 = (start2+21)%22;
      }
      if (clock3 == maj_bit) {
        LSFR3[(start3+22)%23] = LSFR3[(start3+7)%23] ^ LSFR3[(start3+20)%23] ^ LSFR3[(start3+21)%23] ^ LSFR3[(start3+22)%23];
        start3 = (start3+22)%23;
      }
    }
    // System.out.println("after 100 clocks");
    // debugger(LSFR1,start1);
    // debugger(LSFR2,start2);
    // debugger(LSFR3,start3);
    // System.out.println("");

    int[] output = new int[228];
    for (int i = 0; i < 228; i++) {
      int clock1 = LSFR1[Math.floorMod(start1 + 8, 19)];
      int clock2 = LSFR2[Math.floorMod(start2 + 10, 22)];
      int clock3 = LSFR3[Math.floorMod(start3 + 10, 23)];
      int maj_bit = (clock1+clock2+clock3)/2;
      if (clock1 == maj_bit) {
        LSFR1[(start1+18)%19] = LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
        start1 = (start1+18)%19;
      }
      if (clock2 == maj_bit) {
        LSFR2[(start2+21)%22] = LSFR2[(start2+20)%22] ^ LSFR2[(start2+21)%22];
        start2 = (start2+21)%22;
      }
      if (clock3 == maj_bit) {
        LSFR3[(start3+22)%23] = LSFR3[(start3+7)%23] ^ LSFR3[(start3+20)%23] ^ LSFR3[(start3+21)%23] ^ LSFR3[(start3+22)%23];
        start3 = (start3+22)%23;
      }
      output[i] ^= LSFR1[(start1+18)%19];
      output[i] ^= LSFR2[(start2+21)%22];
      output[i] ^= LSFR3[(start3+22)%23];
      // if (i == 1) {
      //   System.out.println("one loop");
      //   debugger(LSFR1,start1);
      //   debugger(LSFR2,start2);
      //   debugger(LSFR3,start3);
      //   System.out.println("Output: "+output[i]);
      // }
      // if (i == 2) {
      //   System.out.println("two loop");
      //   debugger(LSFR1,start1);
      //   debugger(LSFR2,start2);
      //   debugger(LSFR3,start3);
      //   System.out.println("Output: "+output[i]);
      // }
      // if (i == 5) {
      //   System.out.println("five loop");
      //   debugger(LSFR1,start1);
      //   debugger(LSFR2,start2);
      //   debugger(LSFR3,start3);
      //   System.out.println("Output: "+output[i]);
      // }
      // if (i >= 112 && i < 114) {
      //   System.out.println(i + " loop");
      //   debugger(LSFR1,start1);
      //   debugger(LSFR2,start2);
      //   debugger(LSFR3,start3);
      //   System.out.println("Output: "+output[i]);
      // }
      // if (i >= 226 && i < 228) {
      //   System.out.println(i + " loop");
      //   debugger(LSFR1,start1);
      //   debugger(LSFR2,start2);
      //   debugger(LSFR3,start3);
      //   System.out.println("Output: "+output[i]);
      // }
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

  public static void HexStringer(int[] data) {
    int[] outNum = new int[15];
    for (int i = 0; i < 114; i++) {
      outNum[i/8] = outNum[i/8] << 1 | data[i];
    }
    for (int i = 114; i < 120; i++) {
      outNum[i/8] = outNum[i/8] << 1 | 0;
    }
    String outHex = "";
    for (int i = 0; i < 15; i++) {
      // outHex+=""+Integer.toHexString(outNum[i]);
      outHex += String.format("%1$02X",outNum[i]);
    }

    System.out.println(outHex);
    outNum = new int[15];
    for (int i = 114; i < 228; i++) {
      outNum[(i-114)/8] = outNum[(i-114)/8] << 1 | data[i];
    }
    for (int i = 228; i < 234; i++) {
      outNum[(i-114)/8] = outNum[(i-114)/8] << 1 | 0;
    }
    outHex = "";
    for (int i = 0; i < 15; i++) {
      // outHex+=""+Integer.toHexString(outNum[i]);
      outHex += String.format("%1$02X",outNum[i]);
    }
    System.out.println(outHex+"\n");
  }

}
