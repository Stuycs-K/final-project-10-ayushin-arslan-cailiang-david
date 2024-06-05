import java.io.*;

public class test {
    public static void main(String[] args) {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        // String PipeLocation = "mario";
        // File pipe_file = new File(PipeLocation);
        // if(!pipe_file.exists()) { 
        //     System.out.println("Named pipe does not exist");
        //     System.exit(0);
        // }

        try {
            // RandomAccessFile pipe = new RandomAccessFile(PipeLocation, "rw");

            // String message = "luigi";
            // pipe.write(message.getBytes());


            String line = "";
            line = stdin.readLine();
            while (line != null) {
                if (args.length == 1) {
                    if (args[0].equals("A->B")) {
                        System.out.println("(XOR A->B) " + line);
                    }
                    else {
                        System.out.println("(XOR B->A) " + line);
                    }
                }
                line = stdin.readLine();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

