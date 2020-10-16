import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    public static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind( new InetSocketAddress("localhost", PORT));

        while (serverSocketChannel.isOpen()) {
            try (SocketChannel socketChannel = serverSocketChannel.accept()) {
                final ByteBuffer byteBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {
                    int bytes = socketChannel.read(byteBuffer);
                    if (bytes == -1) break;
                    final String msg = new String(byteBuffer.array(), 0, bytes, StandardCharsets.UTF_8);
                    byteBuffer.clear();
                    socketChannel.write(ByteBuffer.wrap((stringTrimmer(msg)).getBytes(StandardCharsets.UTF_8)));
                }

                serverSocketChannel.close();
            }
        }
    }

    public static String stringTrimmer(String str) {
        return str.replaceAll("\\s+","");
    }

}
