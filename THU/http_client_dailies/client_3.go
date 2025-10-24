package main
import (
	"bufio"
	"fmt"
	"net/http"
)

func main() {
	res, err := http.Get("http://127.0.0.1:3000")
	if err != nil {
		panic(err)
	}
	defer res.Body.Close()

	fmt.Println("res status: ", res.Status)

	scanner := bufio.NewScanner(res.Body)
	for i := 0; scanner.Scan() && i < 5; i++ {
		fmt.Println(scanner.Text())
	}

	if err := scanner.Err(); err != nil {
		panic(err)
	}
}
