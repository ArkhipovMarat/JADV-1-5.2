import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static final int PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress socketAddress = new InetSocketAddress("localhost",PORT);
        final SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(socketAddress);

        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer byteBuffer = ByteBuffer.allocate(2 << 10);

            String msg;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("String without spaces:");


            while (true) {
                System.out.print("Message for server:");
                msg = scanner.nextLine();

                if ("end".equals(msg)) {
                    System.out.println(stringBuilder);
                    break;
                }

                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                int bytes = socketChannel.read(byteBuffer);
                stringBuilder.append(new String(byteBuffer.array(), 0, bytes, StandardCharsets.UTF_8));
                byteBuffer.clear();
            }
        } finally {
            socketChannel.close();
        }
    }
}
