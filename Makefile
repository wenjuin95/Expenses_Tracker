JCC = javac
JFLAG = -g

GREEN_HIGHLIGHT = \033[32m
RESET_HIGHLIGHT = \033[0m

JVM = java
CP = expenses_class:libs/*

SRC := $(wildcard expenses/*.java)

.PHONY: all compile run clean

all: compile

compile:
	mkdir -p expenses_class
	@$(JCC) $(JFLAG) -cp $(CP) -d expenses_class $(SRC)
	@echo "${GREEN_HIGHLIGHT}Compilation successful.${RESET_HIGHLIGHT}"

run: compile
	@echo "${GREEN_HIGHLIGHT}Running Expenses Tracker...${RESET_HIGHLIGHT}"
	@$(JVM) -cp $(CP) expenses.Expenses

clean:
	@rm -rf expenses_class
	@echo "${GREEN_HIGHLIGHT}Cleaned up class files and expenses directory.${RESET_HIGHLIGHT}"
