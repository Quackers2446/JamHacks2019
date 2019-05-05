package network

import (
	"bufio"
	"fmt"
	"net"
	"time"

	"../serverData"
)

////////////////////////////
////////////////////////////
//connection Acceptor: the goroutine to accept connections to server and pass them onto other goroutines
////////////////////////////
////////////////////////////

func ConnectionAcceptor(srv *serverData.Server, dataCh chan []byte, nameChan chan string, idChan chan byte) {
	var err error

	fmt.Println("Starting Server on Port", srv.Port+"...")
	srv.Socket, err = net.Listen("tcp", ":"+srv.Port)
	if err != nil {
		panic("Server Failed to Initialize")
	}
	fmt.Println("Successfully Launched Server!")

	for {
		connection, err := srv.Socket.Accept()
		if err != nil {
			fmt.Println(err)
			panic("Error in accepting connection.")
		}

		fmt.Println("Got Connection!")

		go handleConnection(connection, dataCh, nameChan, idChan, srv)
	}

}

////////////////////////////
////////////////////////////
//connection Handler: Goroutine function that will allow to receive and send data to clients
////////////////////////////
////////////////////////////

func handleConnection(conn net.Conn, ch chan []byte, nameChan chan string, idChan chan byte, srv *serverData.Server) {
	defer conn.Close()

	bufReader := bufio.NewReader(conn)
	connectRequested := false

	for {
		conn.SetReadDeadline(time.Now().Add(srv.TimeoutDuration))
		bytes, _, err := bufReader.ReadLine()

		fmt.Println("Received Data")

		if err != nil {
			fmt.Println(err)
			return
		}

		if !connectRequested {
			nameChan <- string(bytes)
			connectRequested = true
			id := <-idChan
			defer func() {
				ch <- []byte{id, 128}
			}()
			conn.Write([]byte{id, '\n'})
		} else {
			ch <- bytes
		}
	}
}
