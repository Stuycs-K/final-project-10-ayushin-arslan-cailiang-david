import java.io.*;

public class test {
    public static void main(String[] args) {
        BufferedReader f = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        while (line != null) {
            try {
                line = f.readLine();
                System.out.println(line);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

