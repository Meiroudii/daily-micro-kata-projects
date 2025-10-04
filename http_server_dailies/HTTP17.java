import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.*;
import java.util.regex.*;

public class HTTP17 {
    static class Page {
        String title;
        byte[] body;

        Page(String title, byte[] body) {
            this.title = title;
            this.body = body;
        }

        void save() throws IOException {
            Path filename = Paths.get(title + ".txt");
            Files.write(filename, body);
        }

        static Page loadPage(String title) throws IOException {
            Path filename = Paths.get(title + ".txt");
            byte[] body = Files.readAllBytes(filename);
            return new Page(title, body);
        }
    }

    // crude "templates" just to keep it simple
    static String renderTemplate(String tmpl, Page p) throws IOException {
        String html = Files.readString(Paths.get(tmpl + ".html"));
        return html.replace("{{.Title}}", p.title)
                   .replace("{{.Body}}", p.body == null ? "" : new String(p.body));
    }

    static final Pattern validPath = Pattern.compile("^/(edit|save|view)/([a-zA-Z0-9]+)$");

    interface PageHandler {
        void handle(HttpExchange exchange, String title) throws IOException;
    }

    static HttpHandler makeHandler(PageHandler fn) {
        return exchange -> {
            String path = exchange.getRequestURI().getPath();
            var m = validPath.matcher(path);
            if (!m.matches()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }
            fn.handle(exchange, m.group(2));
        };
    }

    static void viewHandler(HttpExchange exchange, String title) throws IOException {
        Page p;
        try {
            p = Page.loadPage(title);
        } catch (IOException e) {
            redirect(exchange, "/edit/" + title);
            return;
        }
        sendHtml(exchange, renderTemplate("view", p));
    }

    static void editHandler(HttpExchange exchange, String title) throws IOException {
        Page p;
        try {
            p = Page.loadPage(title);
        } catch (IOException e) {
            p = new Page(title, null);
        }
        sendHtml(exchange, renderTemplate("edit", p));
    }

    static void saveHandler(HttpExchange exchange, String title) throws IOException {
        var form = new String(exchange.getRequestBody().readAllBytes());
        // crude parse, expects body=...
        String body = form.replaceFirst("(?s).*body=([^&]*).*", "$1");
        Page p = new Page(title, body.getBytes());
        try {
            p.save();
        } catch (IOException e) {
            sendError(exchange, 500, e.getMessage());
            return;
        }
        redirect(exchange, "/view/" + title);
    }

    // helpers
    static void sendHtml(HttpExchange exchange, String html) throws IOException {
        byte[] bytes = html.getBytes();
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    static void sendError(HttpExchange exchange, int code, String msg) throws IOException {
        byte[] bytes = msg.getBytes();
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    static void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(302, -1);
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
        server.createContext("/view/", makeHandler(HTTP17::viewHandler));
        server.createContext("/edit/", makeHandler(HTTP17::editHandler));
        server.createContext("/save/", makeHandler(HTTP17::saveHandler));
        server.start();
        System.out.println("Server running at http://localhost:3000/");
    }
}
