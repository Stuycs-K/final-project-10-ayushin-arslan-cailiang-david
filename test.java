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
            while (line != null) {
                line = stdin.readLine();
                if (args.length == 2) {
                    if (args[1].equals("encode")) {
                        System.out.println(args[0] + " encoded" + line);
                    }
                    else {
                        System.out.println(args[0] + " decoded" + line);
                    }
                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

