/**
 * This class serves as an info collection server
 * It contains code copyrighted to Tomas Vilda
 * http://stilius.net/java/java_ssl.php
 *
 * Justin Fulner
 */
import javax.net.ssl.*;
import java.io.*;
import java.util.ArrayList;

public class server {

    public static void main(String[] args) {
        server s = new server();
        int port = 0;
        String[] queries = {"User name: ","Full Name: ","Address: ", "Phone Number: ", "Email Address: ",
                            "Add new user? (yes or any for no)"};
        if (args.length != 1) {
            System.out.print("Usage: java server <server port number>");
            System.exit(-1);
        }else {
            port = Integer.parseInt(args[0]);
        }

        System.setProperty("javax.net.ssl.keyStore", "...");
        System.setProperty("javax.net.ssl.keyStorePassword", "...");

        String goAgain = "y";
        String string = null;
        String polar = null;

        InputStream serverIn = System.in;
        BufferedReader sIn = new BufferedReader(new InputStreamReader(serverIn));

        try {
            SSLServerSocketFactory factory =
                    (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

            SSLServerSocket serverSocket =
                    (SSLServerSocket) factory.createServerSocket(port);
            do {
                SSLSocket socket = (SSLSocket) serverSocket.accept();

                // input from the connected socket
                InputStream sslin = socket.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(sslin));

                // output to socket
                OutputStream sslos = socket.getOutputStream();
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sslos));

                //retrieve session information
                SSLSession session = socket.getSession();
                s.getSessionInfo(session);
                System.out.println();

                do {
                    ArrayList<String> answers = new ArrayList<>();
                    for (String q: queries) {
                        out.write(q + '\n');
                        out.flush();
                        string = in.readLine();
                        answers.add(string);
                        System.out.println(string);
                    }
                    s.exportInfo(answers);
                    if (!answers.get(5).toLowerCase().contains("yes")) {
                        goAgain = "n";
                    }
                }while(goAgain.equalsIgnoreCase("y"));

                System.out.println("Continue listening to port? Y/N");
                polar = sIn.readLine();

            }while(polar.equalsIgnoreCase("y"));

        }catch (Exception e) {
            System.out.println("Something weird happened");
            e.printStackTrace();
        }
    }

    private void getSessionInfo(SSLSession sesh) {
        byte[] bytes = sesh.getId();
        StringBuilder sb = new StringBuilder();
        for (byte b: bytes)
            sb.append(String.format("%02x", b));
        String id = sb.toString();

        System.out.println("Peer host is " + sesh.getPeerHost() + "\n" +
                "Cipher suite is " + sesh.getCipherSuite() + "\n" +
                "Protocol is " + sesh.getProtocol() + "\n" +
                "Session ID is " + id + "\n" +
                "The creation time of this session is " + sesh.getCreationTime() + "\n" +
                "The last accessed time of this session is " + sesh.getLastAccessedTime());
    }

    private void exportInfo(ArrayList<String> arr) throws IOException {
        FileOutputStream file = new FileOutputStream(arr.get(0) + ".txt", true);
        PrintWriter writer = new PrintWriter(file, true);

        writer.println("User Name: " + arr.get(0));
        writer.println("Full Name: " + arr.get(1));
        writer.println("Address: " + arr.get(2));
        writer.println("Phone Number: " + arr.get(3));
        writer.println("Email Address: " + arr.get(4));

        file.close();
        writer.close();

    }
}
