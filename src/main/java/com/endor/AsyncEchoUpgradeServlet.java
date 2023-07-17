package com.endor;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

@WebServlet(urlPatterns = "/upgrade")
public class AsyncEchoUpgradeServlet extends HttpServlet {
    private static final long serialVersionUID = -6955518532146927509L;

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp) throws ServletException, IOException {
        req.upgrade(Handler.class);
    }

    public static class Handler implements HttpUpgradeHandler {
        @Override
        public void init(final WebConnection wc) {
            Listener listener = new Listener(wc);
            try {
                // we have to set the write listener before the read listener
                // otherwise the output stream could be written to before it is
                // in async mode
                wc.getOutputStream().setWriteListener(listener);
                wc.getInputStream().setReadListener(listener);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public void destroy() {
        }
    }

    private static class Listener implements WriteListener, ReadListener {
        private final WebConnection connection;
        private final Queue<String> queue = new ArrayDeque<String>();

        private Listener(final WebConnection connection) {
            this.connection = connection;
        }

        @Override
        public void onDataAvailable() throws IOException {
            byte[] data = new byte[100];
            while (connection.getInputStream().isReady()) {
                int read;
                if ((read = connection.getInputStream().read(data)) != -1) {
                    queue.add(new String(data, 0, read));
                }
                onWritePossible();
            }
        }

        @Override
        public void onAllDataRead() throws IOException {
        }

        @Override
        public void onWritePossible() throws IOException {
            while (!queue.isEmpty() && connection.getOutputStream().isReady()) {
                String data = queue.poll();
                connection.getOutputStream().write(data.getBytes());
            }
        }

        @Override
        public void onError(final Throwable t) {
        }
    }
}
