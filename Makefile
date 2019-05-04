GOCMD=go
GOBUILD=$(GOCMD) build
GOCLEAN=$(GOCMD) clean
GOGET=$(GOCMD) get
GORUN=$(GOCMD) run
BINARY=ServerBin
TARGET=Server/src/main/main.go
TARGETOS=None
EXTENSION=
ifeq ($(TARGETOS), windows)
	EXTENSION=.exe
endif



run: clean
	
ifeq ($(OS),Windows_NT)
	echo "ON WINDOWS"
else
	echo "NOT WINDOWS"
endif


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