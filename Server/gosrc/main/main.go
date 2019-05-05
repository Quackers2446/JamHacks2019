package main

import (
	"net"
	"os"
	"os/exec"
	"time"

	"./dataHandler"
	"./network"
	"./serverData"
	"./serverData/playerdata"
)

func main() {
	srv := &serverData.Server{}
	sep := string(os.PathSeparator)
	srv.JavaAppPath = "Server" + sep + "Java_App" + sep + "Temp.TEMPAPP"
	srv.TimeoutDuration = time.Second * 10
	srv.Port = "3456"

	cmd := exec.Command("java", srv.JavaAppPath)

	dataCommChan := make(chan playerdata.Player)
	byteCommChan := make(chan []byte)
	ipChan := make(chan net.Addr)
	idChan := make(chan byte)

	srv.Wg.Add(3)
	go dataHandler.UpdateData(srv, cmd, dataCommChan)
	go network.ConnectionAcceptor(srv, byteCommChan, ipChan, idChan)
	go dataHandler.CommunicateData(srv, dataCommChan, byteCommChan, ipChan, idChan)
	srv.Wg.Wait()
}
