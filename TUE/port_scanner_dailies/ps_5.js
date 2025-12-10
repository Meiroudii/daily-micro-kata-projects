const net = require('net');

// List of ports to scan
const ports = [22, 80, 443, 8080, 3306];

// Timeout settings
const CONNECTION_TIMEOUT = 3000; // 3 seconds timeout for connection
const BANNER_TIMEOUT = 1000; // 1 second timeout for banner

// Function to scan a single port
async function scanPort(targetIP, port) {
  return new Promise((resolve) => {
    const socket = new net.Socket();
    socket.setTimeout(CONNECTION_TIMEOUT);

    socket.once('connect', () => {
      socket.setTimeout(BANNER_TIMEOUT);
      socket.once('data', (data) => {
        resolve(`Port ${port} is open. Banner: ${data.toString().trim()}`);
        socket.destroy();
      });
      socket.once('timeout', () => {
        resolve(`Port ${port} is open, but no banner received.`);
        socket.destroy();
      });
    });

    socket.once('error', () => {
      resolve(`Port ${port} is closed or filtered.`);
      socket.destroy();
    });

    socket.connect(port, targetIP);
  });
}

// Main function to run the scan
async function runScan(targetIP) {
  console.log(`Scanning ${targetIP}...`);
  const scanPromises = ports.map(port => scanPort(targetIP, port));
  const results = await Promise.all(scanPromises);
  results.forEach(result => console.log(result));
}

// Run the scan (pass target IP as a command-line argument)
const targetIP = process.argv[2];
if (!targetIP) {
  console.log('Usage: node port_scanner.js <target_ip>');
  process.exit(1);
}

runScan(targetIP);
