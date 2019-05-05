package main

import (
	"bufio"
	"fmt"
	"net"
	"os"
	"os/exec"
	"strconv"
	"sync"
	"time"
)

type server struct {
	wg          sync.WaitGroup
	timeData    int
	javaAppPath string
}

var timeoutDuration time.Duration

func main() {
	PORT := "3456"
	timeoutDuration = time.Second * 10
	srv := &server{}

	sep := string(os.PathSeparator)
	srv.javaAppPath = "Server" + sep + "Java_App" + sep + "Temp.TEMPAPP"

	cmd := exec.Command("java", srv.javaAppPath)

	// dataInChan := make(chan int)
	dataCommChan := make(chan int)

	srv.wg.Add(3)
	go srv.updateData(cmd, dataCommChan)
	go srv.connectionAcceptor(PORT, dataCommChan)
	go srv.communicateData(dataCommChan)
	srv.wg.Wait()
}

func (srv *server) updateData(cmd *exec.Cmd, ch chan int) {
	defer srv.wg.Done()

	outPipe, err := cmd.StdoutPipe()
	bufReader := bufio.NewReader(outPipe)

	defer outPipe.Close()

	if err != nil {
		fmt.Println(err)
		panic("Did not get Pipe")
	}

	err = cmd.Start()
	if err != nil {
		fmt.Println(err)
		panic("Something went wrong with initializing command!")
	}

	for {
		data, _, err := bufReader.ReadLine()
		if err != nil {
			fmt.Println(err)
			panic("Something went wrong with the pipe!")
		}
		if len(data) >= 1 {
			s := string(data)
			fmt.Println("In Updater:", s)
			i, _ := strconv.Atoi(s)
			ch <- i
			i2 := <-ch
			fmt.Println("Back Message", i2)
		}
	}

}

////////////////////////////
////////////////////////////
//connection Acceptor: the goroutine to accept connections to server and pass them onto other goroutines
////////////////////////////
////////////////////////////

func (srv *server) connectionAcceptor(port string, dataCh chan int) {
	defer srv.wg.Done()

	fmt.Println("Starting Server on Port", port+"...")
	socket, err := net.Listen("tcp", ":"+port)
	if err != nil {
		panic("Server Failed to Initialize")
	}
	fmt.Println("Successfully Launched Server!")

	for {
		connection, err := socket.Accept()
		if err != nil {
			fmt.Println(err)
			panic("Error in accepting connection.")
		}
		go srv.handleConnection(connection, dataCh)
	}

}

////////////////////////////
////////////////////////////
//connection Handler: Goroutine function that will allow to receive and send data to clients
////////////////////////////
////////////////////////////

func (srv *server) handleConnection(conn net.Conn, ch chan int) {
	defer conn.Close()

}

////////////////////////////
////////////////////////////
//Data Comm: Worker to manage the channels to data
////////////////////////////
////////////////////////////

func (srv *server) communicateData(ch chan int) {
	defer srv.wg.Done()
	defer func() {
		close(ch)
	}()

	for {
		select {
		case newData := <-ch:
			srv.timeData = newData
		case ch <- srv.timeData:

		}
	}

}
