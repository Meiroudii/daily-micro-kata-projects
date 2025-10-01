<?php

// Define the IP and port for the server
$host = 'localhost';
$port = 3000;

// Create a socket to listen on the given port
$server = stream_socket_server("tcp://$host:$port", $errno, $errstr);

if (!$server) {
    echo "Error: $errstr ($errno)\n";
    exit(1);
}

echo "Server is running on $host:$port...\n";

while ($client = stream_socket_accept($server)) {
    // Get the current date and time
    $date = new DateTime();
    $currentDate = $date->format('Y-m-d H:i:s');

    // Prepare the HTTP Response
    $response = "HTTP/1.1 200 OK\r\n";
    $response .= "Date: $currentDate\r\n";
    $response .= "Content-Type: text/html; charset=UTF-8\r\n\r\n";
    $response .= "<html><body>";
    $response .= "<h1>HTTP Server Response</h1>";
    $response .= "<p>Date: $currentDate</p>";
    $response .= "</body></html>";

    // Send the HTTP response to the client
    fwrite($client, $response);

    // Log the request
    $request = stream_get_contents($client);
    $logMessage = "$currentDate - " . strtok($request, "\n") . "\n"; // Log the first line (e.g., GET / HTTP/1.1)
    file_put_contents('http_log.txt', $logMessage, FILE_APPEND);

    fclose($client);  // Close the client connection
}

fclose($server);  // Close the server socket when done
