package playerdata

type Player struct {
	ID       byte
	Movement byte
	Name     string
}

func (p *Player) Marshal() []byte {
	var result []byte
	result = append(result, p.ID, ',')
	result = append(result, p.Movement)
	result = append(result, []byte(p.Name)...)
	return result
}

func Unmarshal(data []byte) *Player {
	p := &Player{}
	p.ID = data[0]
	p.Movement = data[1]
	p.Name = string(data[3:])
	return p
}
