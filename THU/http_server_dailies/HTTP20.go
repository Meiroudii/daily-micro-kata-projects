package main

import (
	"fmt"
	"net/http"
)

func index(w http.ResponseWriter, request *http.Request) {
	fmt.Fprintf(w, "Welcome to the homepage\n")
}

func headers(w http.ResponseWriter, req *http.Request) {
	for name, headers := range req.Header {
		for _, h := range headers {
			fmt.Fprintf(w, "%v: %v\n", name, h)
		}
	}
}


func main() {
	http.HandleFunc("/", index)
	http.HandleFunc("/headers", headers)
	
	http.ListenAndServe(":3000", nil)
}
