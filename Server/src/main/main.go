package main

import (
	"fmt"
	"os/user"
)

func main() {
	cUser, _ := user.Current()
	fmt.Println("Hello", cUser.Username)
}
