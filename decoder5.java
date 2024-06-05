import java.io.*;
import java.util.*;
public class decoder5{
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
      ////
      try {
        FileOutputStream myWriter = new FileOutputStream(output);
        int[] keyBits = new int[plainText.size()*38/5+114];
        for (int k = 0; k < plainText.size()*8; k+=120) {
          byteStreamer(SESSION_KEY,INITIALIZATION_VECTOR, atob, keyBits, k/120*114);
          updateVector(INITIALIZATION_VECTOR);
          for (int i = 0; i < 114; i++) {
            if ((k+i)/8 < plainText.size()) {
              keyBits[k/120*114+i] ^= ((plainText.get((k+i)/8) >> (7-(k+i)%8)) & 1);
            }
          }
        }
        int end = ((keyBits.length-1)/8)*8;
        while (keyBits[end] == 0 && keyBits[end-1] == 0 && keyBits[end-2] == 0 && keyBits[end-3] == 0 && keyBits[end-4] == 0 && keyBits[end-5] == 0 && keyBits[end-6] == 0 && keyBits[end-7] == 0) {
          end-=8;
        }
        for (int i = 0; i < end; i+=8) {
          myWriter.write(keyBits[i] << 7 | keyBits[i+1] << 6 | keyBits[i+2] << 5 | keyBits[i+3] << 4 | keyBits[i+4] << 3 | keyBits[i+5] << 2 | keyBits[i+6] << 1 | keyBits[i+7]);
        }
      }
        catch (IOException ex) {
          System.out.println(ex);
        }
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

    public static int[] byteStreamer(int[] SESSION_KEY, int[] INITIALIZATION_VECTOR, boolean atob, int[] output, int start) {
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
      for (int i = 0; i < 328; i++) {
        int maj_bit = ((reg1>>8&1) & (reg2>>10&1)) | ((reg1>>8&1) & (reg3>>10&1)) | ((reg2>>10&1) & (reg3>>10&1));
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
          output[start+i-100] = (reg1 >> 18 & 1) ^ (reg2 >> 21 & 1) ^ (reg3 >> 22 & 1);
        }
        if (!atob && i > 213) {
          output[start+i-213] = (reg1&1) ^ (reg2&1) ^ (reg3&1);
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

    public static void checkInput(String[] args) {
      if (3 > args.length || args.length > 5) {
        System.out.println("Enter 4-6 Parameters");
        System.out.println("KEY INITIALIZATION_VECTOR INPUT_FILE OUTPUT_FILE [AtoB?]");
        System.out.println("Items in brackets are optional.");
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
          System.out.println("Enter \'atob\' or \'btoa\' as [AtoB?]");
          System.exit(0);
        }
      }
    }
  }
