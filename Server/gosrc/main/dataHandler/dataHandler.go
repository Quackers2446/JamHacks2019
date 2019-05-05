package dataHandler

import (
	"fmt"
	"os/exec"
	"strconv"

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
	idChan chan int) {

	defer srv.Wg.Done()

	for {
		DataUpdated := false

		select {
		case playerData := <-chByte:
			id, _ := strconv.Atoi(string(playerData[0:1]))
			dataInt, _ := strconv.Atoi(string(playerData[1:]))
			data := byte(dataInt)
			if data <= 127 {
				srv.PlayerData[id].Movement = data
			} else {
				fmt.Println("User", srv.PlayerData[id].Name, "has disconnected")
				srv.PlayerData[id].Name = ""
				ch <- srv.PlayerData[id]
				fmt.Println(id, "user should now be open")
			}
			DataUpdated = true

		case name := <-nameChan:
			sent := false
			possibleID := -1

			for i := 0; i < len(srv.PlayerData); i++ {
				if srv.PlayerData[i].Name == "" {
					possibleID = i
				} else if srv.PlayerData[i].Name == name {
					possibleID = -1
					sent = true
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
				println("Allowing", name, "to join the server as user", possibleID)
				srv.PlayerData[possibleID].ID = possibleID
				srv.PlayerData[possibleID].Movement = 0
				srv.PlayerData[possibleID].Name = name
				idChan <- possibleID
				sent = true

			}

			if !sent {
				idChan <- len(srv.PlayerData)
				fmt.Println("Allowing", name, "to join the server as user", len(srv.PlayerData))
				srv.PlayerData = append(srv.PlayerData, playerdata.Player{Name: name,
					ID: len(srv.PlayerData)})
			}

			DataUpdated = true
		}

		if DataUpdated {
			for i := 0; i < len(srv.PlayerData); i++ {
				if srv.PlayerData[i].Name != "" {
					ch <- srv.PlayerData[i]
				}
			}
		}
	}

}
