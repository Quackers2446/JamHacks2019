GOCMD=go
GOBUILD=$(GOCMD) build
GOCLEAN=$(GOCMD) clean
GOGET=$(GOCMD) get
GORUN=$(GOCMD) run
BINARY=ServerBin
TARGET=Server/src/main/main.go
OS=None
EXTENSION=
ifeq ($(OS), windows)
	EXTENSION=.exe
endif

run: clean
	$(GORUN) $(TARGET)


build: $(TARGET) clean
ifeq ($(OS),None)
	echo "Making Binary for current OS"
	$(GOBUILD) -o $(BINARY) $(TARGET)
else
	echo "Making Binary for" $(OS)
	GOOS=$(OS) $(GOBUILD) -o $(BINARY) $(TARGET)
endif

clean:
ifneq ($(wildcard $(BINARY)),)
	$(GOCLEAN)
	rm $(BINARY)
endif