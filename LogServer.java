import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by chen on 2017/7/15.
 */
public class LogServer {
    public static void main(String []args){
        try {
            ServerSocket serverSocket = new ServerSocket(9090);
            AtomicLong mCountLogs = new AtomicLong(0);
            while (true) {
                System.out.println("start write log"+serverSocket.getLocalSocketAddress().toString());
                Socket socket = serverSocket.accept();
                System.out.println("start write log");
                new Thread(new LogWriter(mCountLogs.getAndDecrement(), socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class LogWriter implements Runnable{
        private long mLogSerial;
        private Socket mSocket;
        public LogWriter(long logSerial,Socket socket) {
            this.mLogSerial = logSerial;
            this.mSocket = socket;
        }

        @Override
        public void run() {
            try {
                File destLogFile = new File("D:\\logs"+mLogSerial+".txt");
                destLogFile.createNewFile();
                InputStream ins = mSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
                OutputStream outputStream = new FileOutputStream(destLogFile);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                String line;
                while(mSocket.isConnected() && (line = reader.readLine()) != null){
                    System.out.println("line is " +line);
                    writer.write(line);
                }
                reader.close();
                writer.flush();
                writer.close();
                System.out.println("end write log");
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
