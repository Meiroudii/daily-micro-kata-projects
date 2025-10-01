package main

import (
	"fmt"
	"net/http"
)

func handler(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Working")
}

func main() {
	http.HandleFunc("/", handler)
	fmt.Println("Don't use postman, use curl -v http://localhost:3000")
	http.ListenAndServe(":3000", nil)
}
