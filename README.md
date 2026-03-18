# Expenses Tracker

A command-line Expenses Tracker application written in Java that uses SQLite to store income and expense records locally.

---

## Features

- Add income entries
- Add expense entries (with duplicate-name detection)
- View all transactions with a running balance
- Delete expense entries

---

## Prerequisites

- **Java JDK 8 or higher** – [Download here](https://www.oracle.com/java/technologies/downloads/)
- **Make** – Usually pre-installed on Linux/macOS. On Windows, install via [GnuWin32](http://gnuwin32.sourceforge.net/packages/make.htm) or use WSL.
- **SQLite JDBC driver** – See setup instructions below.

---

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/wenjuin95/Expenses_Tracker.git
cd Expenses_Tracker
```

### 2. Create the `libs` folder

The `libs` folder is required to hold the SQLite JDBC `.jar` file. The folder structure is already tracked in the repository (via `.gitkeep`), but the JAR file itself is not included and must be added manually. If for any reason the folder is missing, create it with:

```bash
mkdir libs
```

### 3. Download the SQLite JDBC driver

1. Go to the [SQLite JDBC releases page on Maven Central](https://central.sonatype.com/artifact/org.xerial/sqlite-jdbc/versions) or the [GitHub releases page](https://github.com/xerial/sqlite-jdbc/releases).
2. Download the latest `sqlite-jdbc-<version>.jar` file.  
   For example: `sqlite-jdbc-3.47.1.0.jar`
3. Move or copy the downloaded `.jar` file into the `libs/` folder you just created:

```
Expenses_Tracker/
├── libs/
│   └── sqlite-jdbc-3.47.1.0.jar   ← place the file here
├── expenses/
│   ├── Color.java
│   ├── Expenses.java
│   └── ExpensesTracker.java
├── Makefile
└── README.md
```

> **Tip:** You can also download the JAR directly using `curl` or `wget`:
> ```bash
> # Replace <version> with the version number you want, e.g. 3.47.1.0
> curl -L -o libs/sqlite-jdbc-<version>.jar \
>   https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/<version>/sqlite-jdbc-<version>.jar
> ```

---

## Building and Running

Use the provided `Makefile`:

| Command      | Description                              |
|-------------|------------------------------------------|
| `make`       | Compile the project (same as `make compile`) |
| `make compile` | Compile all Java source files          |
| `make run`   | Compile (if needed) and run the application |
| `make clean` | Remove compiled class files             |

### Compile

```bash
make compile
```

### Run

```bash
make run
```

---

## Usage

After running the application, you will see the main menu:

```
=========== Expenses Tracker Menu ===========
0 | add income
1 | add expenses
2 | view expenses
3 | delete expenses
4 | exit
Enter your choice:
```

- **0** – Add an income entry (enter an amount)
- **1** – Add an expense entry (enter an item name and amount)
- **2** – View all recorded transactions with totals and balance
- **3** – Delete an expense entry by item name
- **4** – Exit the application

All data is stored in a local SQLite database file `expenses.db` in the project root directory.

---

## Project Structure

```
Expenses_Tracker/
├── expenses/
│   ├── Color.java           # ANSI color constants for terminal output
│   ├── Expenses.java        # Entry point – main menu loop
│   └── ExpensesTracker.java # Core logic – add/view/delete operations
├── libs/                    # Place the SQLite JDBC .jar here (JAR not included)
├── Makefile                 # Build and run commands
└── README.md
```

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| `No suitable driver found for jdbc:sqlite:` | Make sure `sqlite-jdbc-*.jar` is inside the `libs/` folder. |
| `javac: command not found` | Install Java JDK and ensure it is on your `PATH`. |
| `make: command not found` | Install `make` (see Prerequisites above). |
| Compilation fails with `package expenses does not exist` | Run `make` from the project root directory, not from inside `expenses/`. |