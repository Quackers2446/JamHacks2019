package serverData

import (
	"net"
	"sync"
	"time"

	"./playerdata"
)

type Server struct {
	Wg              sync.WaitGroup
	TimeData        int
	JavaAppPath     string
	TimeoutDuration time.Duration
	Port            string
	Socket          net.Listener
	PlayerData      []playerdata.Player
}
