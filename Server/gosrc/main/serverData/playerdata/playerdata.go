package playerdata

import "encoding/binary"

type Player struct {
	ID       int
	Movement byte
	Name     string
}

func (p *Player) Marshal() []byte {
	var result []byte
	buf := make([]byte, 4)
	_ = binary.PutVarint(buf, int64(p.ID))
	bArray := append(buf, '\n')
	result = append(result, bArray...)
	result = append(result, ',')
	result = append(result, p.Movement)
	result = append(result, []byte(p.Name)...)
	return result
}

func Unmarshal(data []byte) *Player {
	p := &Player{}
	// p.ID = (data[:4]
	p.Movement = data[4]
	p.Name = string(data[5:])
	return p
}
