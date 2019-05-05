package playerdata

import "net"

type Player struct {
	ID       byte
	Movement byte
	Addr     net.Addr
}

func (p *Player) Marshal() []byte {
	var result []byte
	result = append(result, p.ID, ',')
	result = append(result, p.Movement)
	return result
}

func Unmarshal(data []byte) *Player {
	p := &Player{}
	p.ID = data[0]
	p.Movement = data[len(data)-1]
	return p
}
