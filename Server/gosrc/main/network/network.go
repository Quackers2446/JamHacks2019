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

func ConnectionAcceptor(srv *serverData.Server, dataCh chan []byte, ipChan chan net.Addr, idChan chan byte) {
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
		go handleConnection(connection, dataCh, ipChan, idChan, srv)
	}

}

////////////////////////////
////////////////////////////
//connection Handler: Goroutine function that will allow to receive and send data to clients
////////////////////////////
////////////////////////////

func handleConnection(conn net.Conn, ch chan []byte, ipChan chan net.Addr, idChan chan byte, srv *serverData.Server) {
	defer conn.Close()

	bufReader := bufio.NewReader(conn)
	connectRequested := false

	for {
		conn.SetReadDeadline(time.Now().Add(srv.TimeoutDuration))
		bytes, _, err := bufReader.ReadLine()
		if err != nil {
			fmt.Println(err)
			return
		}

		if !connectRequested {
			ipChan <- conn.RemoteAddr()
			connectRequested = true
			id := <-idChan
			conn.Write([]byte{id, '\n'})
		} else {
			ch <- bytes
		}
	}
}
