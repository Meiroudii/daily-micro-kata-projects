#include <iostream>
#include <sstream>
#include <chrono>
#include <thread>
#include <vector>
#include <cstring>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <fcntl.h>
#include <mutex>

// List of ports to scan
const std::vector<int> PORTS = {22, 80, 443, 8080, 3306};

// Timeout for connection in seconds
const int CONNECTION_TIMEOUT = 3;
const int BANNER_TIMEOUT = 1;

// Mutex for synchronized output
std::mutex output_mutex;

// Function to set socket timeout
void set_socket_timeout(int sockfd, int timeout) {
    struct timeval tv;
    tv.tv_sec = timeout;
    tv.tv_usec = 0;
    setsockopt(sockfd, SOL_SOCKET, SO_RCVTIMEO, (const char*)&tv, sizeof tv);
}

// Function to scan a single port on the target IP
std::string scan_port(const std::string& target_ip, int port) {
    int sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) {
        return "";
    }

    struct sockaddr_in target_addr;
    target_addr.sin_family = AF_INET;
    target_addr.sin_port = htons(port);
    target_addr.sin_addr.s_addr = inet_addr(target_ip.c_str());

    // Set socket timeout for connection
    set_socket_timeout(sockfd, CONNECTION_TIMEOUT);

    // Attempt to connect to the target IP and port
    int connection_status = connect(sockfd, (struct sockaddr*)&target_addr, sizeof(target_addr));
    
    if (connection_status < 0) {
        close(sockfd);
        return ""; // Connection failed
    }

    // Set socket timeout for banner reading
    set_socket_timeout(sockfd, BANNER_TIMEOUT);

    // Attempt to read the banner from the service
    char buffer[256];
    int bytes_received = recv(sockfd, buffer, sizeof(buffer) - 1, 0);
    if (bytes_received > 0) {
        buffer[bytes_received] = '\0'; // Null-terminate the buffer
        close(sockfd);
        return std::string(buffer);
    }

    close(sockfd);
    return "";
}

// Function to run the port scan on the target IP (per port scan)
void scan_port_concurrently(const std::string& target_ip, int port) {
    std::string banner = scan_port(target_ip, port);

    // Lock the mutex to ensure that output is not garbled in case of concurrent threads
    std::lock_guard<std::mutex> lock(output_mutex);
    
    if (!banner.empty()) {
        std::cout << "Port " << port << " is open.\n";
        std::cout << "  Banner: " << banner << "\n";
    } else {
        std::cout << "Port " << port << " is closed or filtered.\n";
    }
}

// Function to run the port scan on the target IP
void run_scan(const std::string& target_ip) {
    std::cout << "Scanning " << target_ip << "...\n";

    std::vector<std::thread> threads;

    // Create a new thread for each port scan
    for (int port : PORTS) {
        threads.push_back(std::thread(scan_port_concurrently, target_ip, port));
    }

    // Wait for all threads to finish
    for (auto& t : threads) {
        t.join();
    }
}

int main(int argc, char* argv[]) {
    if (argc < 2) {
        std::cerr << "Usage: " << argv[0] << " <target_ip>\n";
        return 1;
    }

    std::string target_ip = argv[1];
    run_scan(target_ip);

    return 0;
}
