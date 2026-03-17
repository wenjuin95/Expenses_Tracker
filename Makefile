JCC = javac
JFLAG = -g

GREEN_HIGHLIGHT = \033[32m
RESET_HIGHLIGHT = \033[0m

JVM = java
CP = .

SRC := $(wildcard *.java)

.PHONY: all compile run clean

all: compile

compile:
	@$(JCC) $(JFLAG) -d . $(SRC)
	@echo "${GREEN_HIGHLIGHT}Compilation successful.${RESET_HIGHLIGHT}"

run: compile
	@echo "${GREEN_HIGHLIGHT}Running Expenses Tracker...${RESET_HIGHLIGHT}"
	@$(JVM) -cp $(CP) expenses.Expenses

clean:
	@find . -name '*.class' -delete
	@rm -rf expenses/
	@echo "${GREEN_HIGHLIGHT}Cleaned up class files and expenses directory.${RESET_HIGHLIGHT}"
