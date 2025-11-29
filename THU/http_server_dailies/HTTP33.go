package main

import (
	"fmt"
	"log"
	"net/http"
	"time"
)

func main() {
	body := `<p style="font-size: 2rem;">WORKING?</p>`

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		today := time.Now().Format(time.RFC1123)
		
		// Log request
		fmt.Printf("\033[32m[%s] %s %s\033[0m\n", 
			time.Now().Format("15:04:05"), r.Method, r.URL.Path)
		
		for key, values := range r.Header {
			for _, value := range values {
				fmt.Printf("\033[31m  %s: %s\033[0m\n", key, value)
			}
		}
		fmt.Println()

		// Send response
		fmt.Fprintf(w, "%s\n%s", today, body)
	})

	fmt.Println("\033[32m🚀 Server running on http://localhost:3000\033[0m")
	log.Fatal(http.ListenAndServe(":3000", nil))
}
