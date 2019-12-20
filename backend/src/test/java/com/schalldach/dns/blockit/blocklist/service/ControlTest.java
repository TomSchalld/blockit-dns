package com.schalldach.dns.blockit.blocklist.service;

import org.junit.Ignore;
import org.junit.Test;

import javax.net.SocketFactory;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by @author Thomas Schalldach on 16/12/2019 software@thomas-schalldach.de.
 */
public class ControlTest {


    public static final String UBCT_1_STATS = "UBCT1 status";

    @Test
    @Ignore
    public void sslConnect() throws IOException {
        final String ip = "192.168.188.90";
        final String port = "8953";

        final SocketFactory socketFactory = SocketFactory.getDefault();
            try (Socket socket = socketFactory.createSocket(ip, Integer.parseInt(port));
                 PrintWriter writer = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
                 BufferedReader reader =
                         new BufferedReader(new InputStreamReader(socket.getInputStream()))) {


                writer.write(UBCT_1_STATS);
                writer.write("\n");
                writer.flush();
                reader.lines().forEach(System.out::println);

        }


    }
}

