import java.io.*;
import java.util.*;
public class encoder5{
  public static void main(String[] args) {
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
      ////
      try {
        FileOutputStream myWriter = new FileOutputStream(output);
        for (int k = 0; k < ((plainText.size()*8)/114)*114+114; k+=114) {
        int[] keyStream = byteStreamer(SESSION_KEY,INITIALIZATION_VECTOR,atob);
        HexStringer(keyStream);
        updateVector(INITIALIZATION_VECTOR);
        for (int i = 0; i < 114; i++) {
          if ((k+i)/8 < plainText.size()) {
            keyStream[i] ^= ((plainText.get((k+i)/8) >> (7-(k+i)%8)) & 1);
          }
        }
        for (int i = 0; i < 120; i+=8) {
          myWriter.write(keyStream[i] << 7 | keyStream[i+1] << 6 | keyStream[i+2] << 5 | keyStream[i+3] << 4 | keyStream[i+4] << 3 | keyStream[i+5] << 2 | keyStream[i+6] << 1 | keyStream[i+7]);
        }
    }
      }
      catch (IOException ex) {
        System.out.println(ex);
      }
      ////
    }
    catch (FileNotFoundException ex) {
      System.out.println(ex);
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
    int reg1 = 0;
    int reg2 = 0;
    int reg3 = 0;
    int start1 = 0;
    int start2 = 0;
    int start3 = 0;
    for (int k = 0; k < 8; k++) {
      for (int i = 0; i < 8; i++) {
        reg1 = ((reg1 & 0x3FFFF) << 1) | ((SESSION_KEY[k] >> i & 1) ^ (reg1 >> 13 & 1) ^ (reg1 >> 16 & 1) ^ (reg1 >> 17 & 1) ^ (reg1 >> 18 & 1));
        reg2 = ((reg2 & 0x1FFFFF) << 1) | ((SESSION_KEY[k] >> i & 1) ^ (reg2 >> 20 & 1) ^ (reg2 >> 21 & 1));
        reg3 = ((reg3 & 0x3FFFFF) << 1) | ((SESSION_KEY[k] >> i & 1) ^ (reg3 >> 7 & 1) ^ (reg3 >> 20 & 1) ^ (reg3 >> 21 & 1) ^ (reg3 >> 22 & 1));
      }
    }
    for (int k = 2; k > 0; k--) {
      for (int i = 0; i < 8; i++) {
        reg1 = ((reg1 & 0x3FFFF) << 1) | ((INITIALIZATION_VECTOR[k] >> i & 1) ^ (reg1 >> 13 & 1) ^ (reg1 >> 16 & 1) ^ (reg1 >> 17 & 1) ^ (reg1 >> 18 & 1));
        reg2 = ((reg2 & 0x1FFFFF) << 1) | ((INITIALIZATION_VECTOR[k] >> i & 1) ^ (reg2 >> 20 & 1) ^ (reg2 >> 21 & 1));
        reg3 = ((reg3 & 0x3FFFFF) << 1) | ((INITIALIZATION_VECTOR[k] >> i & 1) ^ (reg3 >> 7 & 1) ^ (reg3 >> 20 & 1) ^ (reg3 >> 21 & 1) ^ (reg3 >> 22 & 1));
      }
    }
    for (int i = 0; i < 6; i++) {
      reg1 = ((reg1 & 0x3FFFF) << 1) | ((INITIALIZATION_VECTOR[0] >> i & 1) ^ (reg1 >> 13 & 1) ^ (reg1 >> 16 & 1) ^ (reg1 >> 17 & 1) ^ (reg1 >> 18 & 1));
      reg2 = ((reg2 & 0x1FFFFF) << 1) | ((INITIALIZATION_VECTOR[0] >> i & 1) ^ (reg2 >> 20 & 1) ^ (reg2 >> 21 & 1));
      reg3 = ((reg3 & 0x3FFFFF) << 1) | ((INITIALIZATION_VECTOR[0] >> i & 1) ^ (reg3 >> 7 & 1) ^ (reg3 >> 20 & 1) ^ (reg3 >> 21 & 1) ^ (reg3 >> 22 & 1));
    }
    int[] output = new int[120];
    for (int i = 0; i < 328; i++) {
      int maj_bit = ((reg1>>8&1)+(reg2>>10&1)+(reg3>>10&1))/2;
      if ((reg1>>8&1) == maj_bit) {
        reg1 = ((reg1 & 0x3FFFF) << 1) | ((reg1 >> 13 & 1) ^ (reg1 >> 16 & 1) ^ (reg1 >> 17 & 1) ^ (reg1 >> 18 & 1));
      }
      if ((reg2>>10&1) == maj_bit) {
        reg2 = ((reg2 & 0x1FFFFF) << 1) | ((reg2 >> 20 & 1) ^ (reg2 >> 21 & 1));
      }
      if ((reg3>>10&1) == maj_bit) {
        reg3 = ((reg3 & 0x3FFFFF) << 1) | ((reg3 >> 7 & 1) ^ (reg3 >> 20 & 1) ^ (reg3 >> 21 & 1) ^ (reg3 >> 22 & 1));
      }
      if (atob && i > 99 && i < 214) {
        output[i-100] = (reg1 >> 18 & 1) ^ (reg2 >> 21 & 1) ^ (reg3 >> 22 & 1);
      }
      if (!atob && i > 213) {
        output[i-213] = (reg1&1) ^ (reg2&1) ^ (reg3&1);
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

  public static void debugger(int input, int size) {
    String output = "";
    for (int i = 0; i < size; i++) {
      output+=(input >> i & 1);
    }
    System.out.println(output);
  }

  public static void updateVector(int[] INITIALIZATION_VECTOR) {
    int newVector = ((INITIALIZATION_VECTOR[0] << 16 | INITIALIZATION_VECTOR[1] << 8 | INITIALIZATION_VECTOR[2]) + 1)   % 8388607;
    INITIALIZATION_VECTOR[0] = newVector >> 16 & 0xFF;
    INITIALIZATION_VECTOR[1] = newVector >> 8 & 0xFF;
    INITIALIZATION_VECTOR[2] = newVector & 0xFF;
  }
}
