use std::io::Read;
use std::net::TcpStream;
use std::time::Duration;
use std::thread;

const PORTS: [u16; 5] = [22, 80, 443, 8080, 3306];

// Function to scan a single port
fn scan_port(target_ip: &str, port: u16) -> Option<String> {
    let address = format!("{}:{}", target_ip, port);

    // Set a timeout for the connection attempt (3 seconds)
    let stream = TcpStream::connect_timeout(&address.parse().unwrap(), Duration::new(3, 0));
    
    match stream {
        Ok(mut stream) => {
            stream.set_read_timeout(Some(Duration::new(1, 0))).unwrap();
            let mut buffer = [0u8; 256];
            let _ = stream.read(&mut buffer); // Attempt to read the banner
            let banner = String::from_utf8_lossy(&buffer).to_string();
            if banner.trim().is_empty() {
                return None; // No banner found
            } else {
                return Some(banner);
            }
        }
        Err(_) => {
            return None; // Connection failed
        }
    }
}

// Main function to run the scan
fn run(target_ip: &str) {
    println!("Scanning {}", target_ip);

    let mut handles = vec![];

    for &port in &PORTS {
        // Spawn a new thread for each port scan
        let target_ip = target_ip.to_string();
        let handle = thread::spawn(move || {
            match scan_port(&target_ip, port) {
                Some(banner) => {
                    println!("Port {} is open. Banner: {}", port, banner);
                }
                None => {
                    println!("Port {} is closed or filtered.", port);
                }
            }
        });
        handles.push(handle);
    }

    // Wait for all threads to finish
    for handle in handles {
        handle.join().unwrap();
    }
}

fn main() {
    // Get the target IP from command-line arguments
    let args: Vec<String> = std::env::args().collect();
    if args.len() < 2 {
        eprintln!("Usage: {} <target_ip>", args[0]);
        std::process::exit(1);
    }

    let target_ip = &args[1];
    run(target_ip);
}
