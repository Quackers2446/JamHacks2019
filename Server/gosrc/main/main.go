package main

import (
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
	srv.TimeoutDuration = time.Second * 30
	srv.Port = "3456"

	cmd := exec.Command(os.Args[1], os.Args[2:]...)

	dataCommChan := make(chan playerdata.Player)
	byteCommChan := make(chan []byte)
	nameChan := make(chan string)
	idChan := make(chan int)

	defer func() {
		close(dataCommChan)
		close(byteCommChan)
		close(nameChan)
		close(idChan)
	}()

	srv.Wg.Add(3)
	go dataHandler.UpdateData(srv, cmd, dataCommChan)
	go network.ConnectionAcceptor(srv, byteCommChan, nameChan, idChan)
	go dataHandler.CommunicateData(srv, dataCommChan, byteCommChan, nameChan, idChan)
	srv.Wg.Wait()
}
