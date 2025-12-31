use std::io;
use std::cmp::Ordering;
use rand::Rng;

pub fn guess_me() {
    println!("__start");
    let sn = rand::thread_rng().gen_range(1..=100);
    loop {
        println!("> ");
        let mut guess = String::new();
        io::stdin()
            .read_line(&mut guess)
            .expect("[!]");
        let guess: i8 = match guess.trim().parse() {
            Ok(num) => num,
            Err(_) => continue,
        };
        match guess.cmp(&sn) {
            Ordering::Less => println!("Low"),
            Ordering::Greater => println!("High"),
            Ordering::Equal => {
                println!("matched");
                break;
            }
        }
    }
}
