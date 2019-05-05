package dataHandler

import (
	"fmt"
	"net"
	"os/exec"

	"../serverData"
	"../serverData/playerdata"
)

func UpdateData(srv *serverData.Server, cmd *exec.Cmd, ch chan playerdata.Player) {
	defer srv.Wg.Done()
	outwardPipe, err := cmd.StdinPipe()

	defer outwardPipe.Close()

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
		player := <-ch
		outwardPipe.Write(append(player.Marshal(), '\n'))
	}
}

////////////////////////////
////////////////////////////
//Data Comm: Worker to manage the channels to data
////////////////////////////
////////////////////////////

func CommunicateData(srv *serverData.Server, ch chan playerdata.Player, chByte chan []byte, ipChan chan net.Addr, idChan chan byte) {
	defer srv.Wg.Done()
	defer func() {
		close(ch)
	}()

	for {
		select {
		case playerData := <-chByte:
			id := playerData[0]
			data := playerData[1]
			srv.PlayerData[id].Movement = data

		case addr := <-ipChan:
			sent := false
			for i := 0; i < len(srv.PlayerData); i++ {
				if srv.PlayerData[i].Addr == nil {
					srv.PlayerData[i].Addr = addr
					srv.PlayerData[i].ID = byte(i)
					srv.PlayerData[i].Movement = 0
					idChan <- byte(i)
					sent = true
				}
			}

			if !sent {
				srv.PlayerData = append(srv.PlayerData, playerdata.Player{Addr: addr, ID: byte(len(srv.PlayerData))})
				idChan <- byte(len(srv.PlayerData))
			}
		}

		for i := 0; i < len(srv.PlayerData); i++ {
			ch <- srv.PlayerData[i]
		}
	}

}
