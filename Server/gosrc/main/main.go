package main

import (
	"fmt"
	"io"
	"net"
	"os/exec"
)

const javaAppLocation = "Server.Java_App.Temp.TEMPAPP"

func main() {
	PORT := "3456"

	fmt.Println("Starting Server on Port", PORT+"...")
	socket, err := net.Listen("tcp", ":"+PORT)
	if err != nil {
		panic("Server Failed to Initialize")
	}
	fmt.Println("Successfully Launched Server!")

	cmd := exec.Command("java", javaAppLocation)
	outPipe, _ := cmd.StdoutPipe()
	err = cmd.Start()
	if err != nil {
		fmt.Println(err)
		panic("Something went wrong with initializing command!")
	}

	dataInChan := make(chan string)

	go updateData(outPipe, dataInChan)

	go connectionAcceptor(socket)

	for {
	}
}

func updateData(pipe io.ReadCloser, ch chan string) {
	buf := make([]byte, 4096)
	for {
		_, err := pipe.Read(buf)
		if err != nil {
			fmt.Println(err)
			panic("Something went wrong with the pipe!")
		}
		fmt.Print(string(buf))
	}
}

func connectionAcceptor(socket net.Listener) {
	for {
		// connection, err := socket.Accept()
	}

}
