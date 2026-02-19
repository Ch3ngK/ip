# Vex User Guide

![Vex Screenshot](Ui.png)

Vex is a lightweight command-line task manager that helps you manage todos, deadlines, and events efficiently.

## Features
Vex allows you to:
- Add and manage tasks
- Track deadlines and events
- Mark tasks as completed
- Search tasks quickly
- Store your tasks automatically


## Adding todos

Adds a Todo task to the task list, with completed/uncompleted status.

Todos have a description.

Format: `todo DESCRIPTION`

Example:
`todo Coding Assignment`

Expected output:
```
Added to the roster:
  [T][ ] Coding Assignment
```

## Adding Deadlines

Adds a Deadline task to the task list, with completed/uncompleted status.

Deadlines have a description and by date.

Format: `deadline DESCRIPTION /by DATE`

Example:
`deadline Essay /by 2026-02-19 1400`

Example output:
```
Added to the roster:
  [D][ ] Essay (by: Feb 19 2026 14:00)
```

## Adding Events

Adds an Event task to the task list, with completed/uncompleted status.

Events have a description, start and end date.

Format: `event Description /from YYYY-MM-DD /to YYYY-MM-DD`

Example: `event Project Meeting /from 2026-02-20 1300 /to 2026-02-20 1430`

Example output:
```
Added to the roster:
  [E][ ] Project Meeting (from: Feb 20 2026 13:00 to: Feb 20 2026 14:30)
```

## Listing tasks

Provides the list of tasks previously added.

Format: list

## Mark and Unmark tasks

Marks or unmarks a task as completed or uncompleted.

Format: `mark INDEX` or `unmark INDEX`

Example:
```
mark 1
```

Example output:
```
Objective complete. Marked as done:
[E][X] meeting (from: Feb 20 2026 13:00 to: Feb 20 2026 14:30)
```
Example:
```
unmark 1
```

Example output:
```
Reopened. Marked as not done:
[E][ ] meeting (from: Feb 20 2026 13:00 to: Feb 20 2026 14:30)
```

## Delete tasks

Removes the task from the list.

Format: `delete INDEX`

Example:
```
delete 3
```

Example output:
```
Struck from the roster:
  [T][ ] walk dog
You now have 2 objective(s) in your campaign.
```

## Find tasks

Find task based on keywords.

Format: `find KEYWORD`

Example:
```
find homework
```

Example output:
```
Intel matching your search:
1. [D][ ] homework (by: Feb 19 2026 18:00)
```

## Reminders for tasks

Find tasks that are due within the time input.

If no time is given, default is 7 days from current date.

Format: `remind DAYS`

Example:
```
remind 3
```

Example output:
```
Upcoming engagements in the next 3 day(s):
1. [E][ ] meeting (from: Feb 20 2026 13:00 to: Feb 20 2026 14:30)
```

## Exit

Exits program with an additional goodbye message.

Format: `bye`



