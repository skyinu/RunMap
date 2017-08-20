package com.stdnull.baselib.logcatch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by chen on 2017/7/15.
 */

public class LogSender {
    static void sendLogs(String host, BufferedReader source) {
        String line;
        Socket socket = null;
        try {
            socket = new Socket(host, 9090);
            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            while (socket.isConnected() && (line = source.readLine()) != null) {
                bw.write(line+"\r\n");
                bw.flush();
            }
            bw.flush();
            source.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
