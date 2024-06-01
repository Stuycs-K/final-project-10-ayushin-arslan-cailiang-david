import java.io.*;
import java.util.*;
public class decoder4{
  public static void main(String[] args) {
    checkInput(args);
    boolean atob = args.length == 4 || args[4].toLowerCase().equals("atob");
    int[] SESSION_KEY = generateHex(args[0],8);
    int[] INITIALIZATION_VECTOR = generateHex(args[1],3);
    printKey(SESSION_KEY);
    try {
      ArrayList<Integer> plainText = new ArrayList<Integer> ();
      File plain = new File(args[2]);
      FileInputStream plainReader = new FileInputStream(plain);
      File output = new File(args[3]);
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
      ArrayList<Integer> cipher = new ArrayList<Integer> ();
      for (int i = 0; i < plainText.size(); i++) {
        if (i%15 != 14) {
          cipher.add(plainText.get(i) >> 7 & 1);
          cipher.add(plainText.get(i) >> 6 & 1);
          cipher.add(plainText.get(i) >> 5 & 1);
          cipher.add(plainText.get(i) >> 4 & 1);
          cipher.add(plainText.get(i) >> 3 & 1);
          cipher.add(plainText.get(i) >> 2 & 1);
          cipher.add(plainText.get(i) >> 1 & 1);
          cipher.add(plainText.get(i) & 1);
        }
        else {
          cipher.add(plainText.get(i) >> 7 & 1);
          cipher.add(plainText.get(i) >> 6 & 1);
        }
      }
      int[] keyStream = new int[cipher.size()];
      for (int i = 0; i < keyStream.length; i+=114) {
        int[] subKey = byteStreamer(SESSION_KEY,INITIALIZATION_VECTOR,atob);
        HexStringer(subKey);
        int newVector = ((INITIALIZATION_VECTOR[0] << 16 | INITIALIZATION_VECTOR[1] << 8 | INITIALIZATION_VECTOR[2]) + 1)   % 8388607;
        INITIALIZATION_VECTOR[0] = newVector >> 16 & 0xFF;
        INITIALIZATION_VECTOR[1] = newVector >> 8 & 0xFF;
        INITIALIZATION_VECTOR[2] = newVector & 0xFF;
        for (int k = 0; k < 114; k++) {
          if (i+k < keyStream.length) {
            keyStream[i+k] = subKey[k];
          }
        }
      }
      try {
        FileOutputStream myWriter = new FileOutputStream(output);
        for (int i = 0; i < keyStream.length; i++) {
          keyStream[i] ^= (cipher.get(i));
        }
        int end = ((keyStream.length-1)/8)*8;
        while (keyStream[end] == 0 && keyStream[end-1] == 0 && keyStream[end-2] == 0 && keyStream[end-3] == 0 && keyStream[end-4] == 0 && keyStream[end-5] == 0 && keyStream[end-6] == 0 && keyStream[end-7] == 0) {
          end-=8;
        }
        for (int i = 0; i < end; i+=8) {
          myWriter.write(keyStream[i] << 7 | keyStream[i+1] << 6 | keyStream[i+2] << 5 | keyStream[i+3] << 4 | keyStream[i+4] << 3 | keyStream[i+5] << 2 | keyStream[i+6] << 1 | keyStream[i+7]);
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

  public static void checkInput(String[] args) {
    if (args.length != 5 && args.length != 4) {
      System.out.println("Enter 4 OR 5 Parameters");
      System.out.println("KEY INITIALIZATION_VECTOR INPUT_FILE OUTPUT_FILE MODE");
      System.out.println("OR");
      System.out.println("KEY INITIALIZATION_VECTOR INPUT_FILE OUTPUT_FILE MODE");
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
    if (48 > args[1].charAt(0) || args[1].charAt(0) > 51)  {
      System.out.println("Invalid initial character for initialization vector.");
      System.out.println("Must start with: 0,1,2,3");
      System.out.println("Problem Character: "+args[1].charAt(0));
      System.exit(0);
    }
    if (args.length == 5) {
      if (!args[4].toLowerCase().equals("atob") && !args[4].toLowerCase().equals("btoa")) {
        System.out.println("Enter \'atob\' or \'btoa\' as MODE");
        System.exit(0);
      }
    }
  }

  public static void printKey(int[] SESSION_KEY) {
    System.out.println("KEY = ");
    for (int i = 0; i < SESSION_KEY.length; i++) {
      for (int b = 0; b < 8; b++) {
        System.out.print(SESSION_KEY[i] >> b & 1);
      }
      System.out.print(" ");
    }
    System.out.println("\n");
  }

  public static int[] byteStreamer(int[] SESSION_KEY, int[] INITIALIZATION_VECTOR, boolean atob) {
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
      }
    }
    for (int k = 2; k > 0; k--) {
      for (int i = 0; i < 8; i++) {
        LSFR1[(start1+18)%19] = (INITIALIZATION_VECTOR[k] >> i & 1) ^ LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
        start1 = (start1+18)%19;
        LSFR2[(start2+21)%22] = (INITIALIZATION_VECTOR[k] >> i & 1) ^ LSFR2[(start2+20)%22] ^ LSFR2[(start2+21)%22];
        start2 = (start2+21)%22;
        LSFR3[(start3+22)%23] = (INITIALIZATION_VECTOR[k] >> i & 1) ^ LSFR3[(start3+7)%23] ^ LSFR3[(start3+20)%23] ^ LSFR3[(start3+21)%23] ^ LSFR3[(start3+22)%23];
        start3 = (start3+22)%23;
      }
    }
    for (int i = 0; i < 6; i++) {
      LSFR1[(start1+18)%19] = (INITIALIZATION_VECTOR[0] >> i & 1) ^ LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
      start1 = (start1+18)%19;
      LSFR2[(start2+21)%22] = (INITIALIZATION_VECTOR[0] >> i & 1) ^ LSFR2[(start2+20)%22] ^ LSFR2[(start2+21)%22];
      start2 = (start2+21)%22;
      LSFR3[(start3+22)%23] = (INITIALIZATION_VECTOR[0] >> i & 1) ^ LSFR3[(start3+7)%23] ^ LSFR3[(start3+20)%23] ^ LSFR3[(start3+21)%23] ^ LSFR3[(start3+22)%23];
      start3 = (start3+22)%23;
    }
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
    //////Mode 1
    int[] output = new int[114];
    for (int i = 0; i < 114; i++) {
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
      if (atob) {
        output[i] ^= LSFR1[(start1+18)%19];
        output[i] ^= LSFR2[(start2+21)%22];
        output[i] ^= LSFR3[(start3+22)%23];
      }
    }
    //////Mode 2
    if (!atob) {
      for (int i = 114; i < 228; i++) {
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
        output[i-114] ^= LSFR1[(start1+18)%19];
        output[i-114] ^= LSFR2[(start2+21)%22];
        output[i-114] ^= LSFR3[(start3+22)%23];
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


  public static void HexStringer(int[] data) {
    int[] outNum = new int[29];
    for (int i = 0; i < 114; i++) {
      outNum[i/4] = outNum[i/4] << 1 | data[i];
    }
    for (int i = 114; i < 116; i++) {
      outNum[i/4] = outNum[i/4] << 1;
    }
    String outHex = "";
    for (int i = 0; i < 29; i++) {
      // outHex+=""+Integer.toHexString(outNum[i]);
      outHex += String.format("%1$01X",outNum[i]);
    }
    System.out.println(outHex);
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

}
