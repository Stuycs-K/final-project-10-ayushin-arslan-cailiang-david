import java.util.*;
import java.io.*;
public class encoder {
    public static void main(String args[]) {
        int[] SESSION_KEY = {0b01001110,0b00101111,0b01001101,0b01111100,0b00011110,0b10111000,0b10001011,0b00111010};
        long INITIALIZATION_VECTOR = 2000;
        String Message = "Happy bunnies will live forever.";
        encode(SESSION_KEY,INITIALIZATION_VECTOR,Message);
    }

    public static String encode(int[] SESSION_KEY,long INITIALIZATION_VECTOR,String Message) {
      boolean[] LSFR1 = new boolean[19];
      boolean[] LSFR2 = new boolean[22];
      boolean[] LSFR3 = new boolean[23];
      int start1 = 0;
      int start2 = 0;
      int start3 = 0;
      for (int k = 0; k < 1; k++) {
      for (int i = 7; i >= 0; i--) {
        LSFR1[Math.floorMod(start1-1,19)] = ((SESSION_KEY[k] >> i & 1) != 0) ^ LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
        start1 = Math.floorMod(start1-1,19);
        debugger(LSFR1,start1);
    }
    }
      return "";
    }

    public static void debugger(boolean[] LSFR1, int start1) {
      boolean[] output = new boolean[LSFR1.length];
      int pos = 0;
      for (int i = start1; i < LSFR1.length; i++) {
        output[pos] = LSFR1[i];
        pos++;
      }
      for (int i = 0; i < start1; i++) {
        output[pos] = LSFR1[i];
      }
      System.out.println(Arrays.toString(output));
}

}
