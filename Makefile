GOCMD=go
GOBUILD=$(GOCMD) build
GOCLEAN=$(GOCMD) clean
GOGET=$(GOCMD) get
GORUN=$(GOCMD) run
BINARY=ServerBin
TARGET=Server/gosrc/main/main.go
TARGETOS=None
EXTENSION=
ifeq ($(TARGETOS), windows)
	BINARY=ServerBin.exe
endif

ifeq ($(OS),Windows_NT)
	TARGET = Server\gosrc\main\main.go
	BINARY = ServerBin.exe
endif

run: clean
	$(GORUN) $(TARGET)

build: $(TARGET) clean
ifeq ($(TARGETOS),None)
	echo "Making Binary for current OS"
	$(GOBUILD) -o $(BINARY) $(TARGET)
else
	echo "Making Binary for" $(TARGETOS)
	GOOS=$(TARGETOS) $(GOBUILD) -o $(BINARY) $(TARGET)
endif

clean:
ifneq ($(wildcard $(BINARY)),)
	$(GOCLEAN)
	rm $(BINARY)
endif