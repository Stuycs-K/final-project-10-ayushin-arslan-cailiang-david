import java.io.*;
import java.util.*;
public class encoder2{
  public static void main(String[] args) {
    // if (args.length = 4 && args[0].length == 16 && Integer.parseInt(args[1]) > 0 && Integer.parseInt(args[1]) < Math.pow(2,23)) {
    //   encoder(args[0], args[1], args[2], args[3]);
    //   /*
    //   args[0]: SESSION_KEY
    //   args[1]: INITIALIZATION_VECTOR
    //   args[2]: Input String
    //   args[3]: Output File Name
    //   */
    // }
    encoder("1223456789ABCDEF", "133", "I love fish", "output.txt");
  }

  public static void encoder(String KEY, String INITIALIZATION_VECTOR, String inputText, String OutputFile) {
    int[] SESSION_KEY = generateSessionKey(KEY);
    //hexdump(SESSION_KEY);
    int[] LSFR1 = new int[19];
    int[] LSFR2 = new int[22];
    int[] LSFR3 = new int[23];
    int start1 = 0;
    int start2 = 0;
    int start3 = 0;
    for (int k = 0; k < 8; k++) {
      for (int i = 7; i >= 0; i--) {
        LSFR1[Math.floorMod(start1-1,19)] = (SESSION_KEY[k] >> i & 1) ^ LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
        start1 = Math.floorMod(start1-1,19);
        LSFR2[Math.floorMod(start2-1,22)] = (SESSION_KEY[k] >> i & 1) ^ LSFR2[(start2+20)%22] ^ LSFR2[(start2+21)%22];
        start2 = Math.floorMod(start2-1,22);
        LSFR3[Math.floorMod(start3-1,23)] = (SESSION_KEY[k] >> i & 1) ^ LSFR3[(start3+7)%23] ^ LSFR3[(start3+20)%23] ^ LSFR3[(start3+21)%23] ^ LSFR3[(start3+22)%23];
        start3 = Math.floorMod(start3-1,23);
      }
    }
    System.out.println(Arrays.toString(LSFR1));
    System.out.println(Arrays.toString(LSFR2));
    System.out.println(Arrays.toString(LSFR3));
    ////
    int[] output = new int[12];
    for (int i = 0; i < 12; i++) {
      int temp = 0;
      for (int j = 0; j < 8; j++) {
        int clock1 = LSFR1[Math.floorMod(start1 + 8, 19)];
        int clock2 = LSFR2[Math.floorMod(start2 + 10, 22)];
        int clock3 = LSFR3[Math.floorMod(start3 + 10, 23)];
        int maj_bit = (clock1+clock2+clock3)/2;
        //temp = temp << 1 | (LSFR1[Math.floorMod(start1-1,19)] ^ LSFR2[Math.floorMod(start2-1,22)] ^ LSFR3[Math.floorMod(start3-1,23)]);
        if (clock1 == maj_bit) {
          LSFR1[Math.floorMod(start1-1,19)] = LSFR1[(start1+13)%19] ^ LSFR1[(start1+16)%19] ^ LSFR1[(start1+17)%19] ^ LSFR1[(start1+18)%19];
          start1 = Math.floorMod(start1-1,19);
        }
        if (clock2 == maj_bit) {
          LSFR2[Math.floorMod(start2-1,22)] = LSFR2[(start2+20)%22] ^ LSFR2[(start2+21)%22];
          start2 = Math.floorMod(start2-1,22);
        }
        if (clock3 == maj_bit) {
          LSFR3[Math.floorMod(start3-1,23)] = LSFR3[(start3+7)%23] ^ LSFR3[(start3+20)%23] ^ LSFR3[(start3+21)%23] ^ LSFR3[(start3+22)%23];
          start3 = Math.floorMod(start3-1,23);
        }
         temp = temp << 1 | (LSFR1[start1] ^ LSFR2[start2] ^ LSFR3[start3]);
      }
      output[i] = temp;
    }
    hexdump(output);
  }

  public static int[] generateSessionKey(String KEY) {
    int[] SESSION_KEY = new int[8];
    for (int i = 0; i < 16; i+=2) {
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

  public static void hexdump(int[] input) {
    String[] output = new String[input.length];
    for (int i = 0; i < input.length; i++) {
      output[i] = Integer.toHexString(input[i]);
    }
    System.out.println(Arrays.toString(output));
  }


}
