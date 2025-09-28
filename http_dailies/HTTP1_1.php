<?php
// HTTP2.php

function my_socket_create($port) {
    $sock = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
    if ($sock === false) {
        throw new Exception("socket_create failed: " . socket_strerror(socket_last_error()));
    }

    socket_set_option($sock, SOL_SOCKET, SO_REUSEADDR, 1);

    if (!socket_bind($sock, '0.0.0.0', $port)) {
        throw new Exception("socket_bind failed: " . socket_strerror(socket_last_error($sock)));
    }

    return $sock;
}

function my_socket_listen($sock) {
    if (!socket_listen($sock)) {
        throw new Exception("socket_listen failed: " . socket_strerror(socket_last_error($sock)));
    }
}

function my_socket_accept($sock) {
    $client = socket_accept($sock);
    if ($client === false) {
        throw new Exception("socket_accept failed: " . socket_strerror(socket_last_error($sock)));
    }
    return $client;
}

// Prompt for port
echo "Enter the port: ";
$port = trim(fgets(STDIN));
while (!is_numeric($port) || $port < 1 || $port > 65535) {
    echo "Invalid port. Enter the port: ";
    $port = trim(fgets(STDIN));
}
$port = (int)$port;

try {
    $server = my_socket_create($port);
    my_socket_listen($server);
    echo "Server is now live!\n";

    while (true) {
        $client = my_socket_accept($server);

        $request = '';
        while ($line = socket_read($client, 1024)) {
            $request .= $line;
            if (strpos($line, "\r\n\r\n") !== false) break;
        }

        foreach (explode("\r\n", $request) as $line) {
            if (trim($line) === '') break;
            echo $line . "\n";
        }

        $date = date('r');
        $response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n$date";
        socket_write($client, $response);
        socket_close($client);
    }
} catch (Exception $e) {
    echo "Error: " . $e->getMessage() . "\n";
    exit(1);
}
?>
