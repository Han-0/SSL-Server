/**
 * This class serves as an info collection client
 * It contains code copyrighted to Tomas Vilda
 * http://stilius.net/java/java_ssl.php
 *
 * Justin Fulner
 */
import javax.net.ssl.*;
import java.io.*;

public class client {
    public static void main(String[] args) {
        client c = new client();
        String dns = "";
        int port = 0;
        if(args.length != 2) {
            System.out.println("Usage: java client <ip/dns> <server port number>");
            System.exit(-1);
        }else {
            dns = args[0];
            port = Integer.parseInt(args[1]);
        }

        System.setProperty("javax.net.ssl.trustStore", "...");
        System.setProperty("javax.net.ssl.trustStorePassword", "...");

        try {
            SSLSocketFactory sslsocketfactory =
                    (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket =
                    (SSLSocket) sslsocketfactory.createSocket(dns, port);

            InputStream inputstream = System.in;
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader in = new BufferedReader(inputstreamreader);

            OutputStream outputstream = sslsocket.getOutputStream();
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
            BufferedWriter out = new BufferedWriter(outputstreamwriter);

            InputStream sslssIN = sslsocket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(sslssIN));

            SSLSession session = sslsocket.getSession();
            c.getSessionInfo(session);
            System.out.println();

            String string = null;
            String servertoclient = null;
            int count = 0;

            do {
                count++;
                servertoclient = br.readLine();
                System.out.println(servertoclient);
                string = in.readLine();
                out.write(string + '\n');
                out.flush();

                if (servertoclient.contains("Add new user?")
                        && string.equalsIgnoreCase("yes")) {
                    count = 0;
                }
            }while(count < 6);

        } catch (Exception exception) {
            System.out.println("Something weird happened");
            exception.printStackTrace();
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
}
