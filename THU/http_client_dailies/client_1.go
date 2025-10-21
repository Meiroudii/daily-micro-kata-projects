package main

import (
	"bufio"
	"fmt"
	"net/http"
)

func main() {

	response, errors := http.Get("https://gobyexample.com")
	if errors != nil {
		panic(errors)
	}
	defer response.Body.Close()

	fmt.Println("Response status:", response.Status)

	scanner := bufio.NewScanner(response.Body)
	for i := 0; scanner.Scan() && i < 5; i++ {
		fmt.Println(scanner.Text())
	}

	if errors := scanner.Err(); errors != nil {
		panic(errors)
	}
}
