package com.schalldach.dns.blockit.unbound.service.control;

import com.schalldach.dns.blockit.control.RemoteCommand;
import com.schalldach.dns.blockit.control.ServiceControl;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.SocketFactory;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author Thomas Schalldach on 14/12/2019 software@thomas-schalldach.de.
 */
@Slf4j
@Service
@Setter
public class UnboundControl implements ServiceControl {

    private final SocketFactory socketFactory = SocketFactory.getDefault();
    @Value("${dns.server.remote.address:127.0.0.1}")
    private String ip;
    @Value("${dns.server.remote.port:8953}")
    private Integer port;


    @Override
    public List<String> execRemoteCommand(RemoteCommand command, String... args) {
        final List<String> answer = new ArrayList<>();
        try (Socket socket = socketFactory.createSocket(ip, port);
             PrintWriter writer = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
             BufferedReader reader =
                     new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            log.trace("Executing command: [{}] with args [{}]", command, args);
            writer.write(command.execute(args));
            writer.write("\n");
            writer.flush();
            answer.addAll(reader.lines().collect(Collectors.toList()));
        } catch (IOException e) {
            log.warn("Remote command execution failed", e);
        }
        return answer;

    }


    @Override
    public void applyChanges() {
        execRemoteCommand(UnboundCommands.RELOAD);
    }
}
