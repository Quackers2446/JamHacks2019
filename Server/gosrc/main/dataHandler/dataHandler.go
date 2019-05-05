package dataHandler

import (
	"fmt"
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

func CommunicateData(srv *serverData.Server,
	ch chan playerdata.Player,
	chByte chan []byte,
	nameChan chan string,
	idChan chan byte) {

	defer srv.Wg.Done()

	for {
		DataUpdated := false

		select {
		case playerData := <-chByte:
			id := playerData[0]
			data := playerData[1]
			if data <= 127 {
				srv.PlayerData[id].Movement = data
			} else {
				srv.PlayerData[id].Name = ""
			}
			DataUpdated = true

		case name := <-nameChan:
			sent := false
			possibleID := -1

			fmt.Println("Checking for availability for user ", name)

			for i := 0; i < len(srv.PlayerData); i++ {
				if srv.PlayerData[i].Name == "" {
					possibleID = i
				} else if srv.PlayerData[i].Name == name {
					break
				}

				if possibleID >= 0 {
					for i := possibleID; i < len(srv.PlayerData); i++ {
						if srv.PlayerData[i].Name == name {
							possibleID = -1
							sent = true
							break
						}
					}
				}

			}
			if possibleID >= 0 {
				println("Allowing ", name, " to join the server")
				srv.PlayerData[possibleID].ID = byte(possibleID)
				srv.PlayerData[possibleID].Movement = 0
				srv.PlayerData[possibleID].Name = name
				idChan <- byte(possibleID)
				sent = true

			}

			if !sent {
				srv.PlayerData = append(srv.PlayerData, playerdata.Player{Name: name,
					ID: byte(len(srv.PlayerData))})
				idChan <- byte(len(srv.PlayerData))
				println("Allowing ", name, " to join the server")
			}

			DataUpdated = true
		}

		if DataUpdated {
			for i := 0; i < len(srv.PlayerData); i++ {
				ch <- srv.PlayerData[i]
			}
		}
	}

}
