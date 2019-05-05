package interfaces

type Marshalable interface {
	Marshal() []byte
	Unmarshal() Marshalable
}
