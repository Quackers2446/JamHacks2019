package playerdata

import "strconv"

type Player struct {
	ID       int
	Movement byte
	Name     string
}

func (p *Player) Marshal() []byte {
	resultString := strconv.Itoa(p.ID) + "," + strconv.Itoa(int(p.Movement)) + "," + p.Name
	return []byte(resultString + "\n")
}

func Unmarshal(data []byte) *Player {
	p := &Player{}
	// p.ID = (data[:4]
	p.Movement = data[4]
	p.Name = string(data[5:])
	return p
}
