import java.io.*;

public class test {
    public static void main(String[] args) {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String PipeLocation = "mario";
        File pipe = new File(PipeLocation);
        if(!pipe.exists()) { 
            System.out.println("Named pipe does not exist");
            System.exit(0);
        }
        System.out.println("A");
        try {
            RandomAccessFile mario = new RandomAccessFile(PipeLocation, "rw");

            System.out.println("A");

            String line = "";
            while (line != null) {
                try {
                    line = stdin.readLine();
                    System.out.println(line);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

}

