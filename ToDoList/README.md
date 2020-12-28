# My Personal Project

### What will the application do?  

This project is about a to-do list application. The application can be used to keep track of things to do, staring 
to-dos, mark tasks as completed, and display them in different ways.

### Who will use it?

- Employees who are struggling to multi-task at work
- Student who have trouble planning before deadlines
- etc.

In general, **all people** who wish to make time arrangements and organize their life can use this application.

### Why is this project of interest to me?

I am used to taking down the to-dos and making my schedule on paper. It occurs to me that an online to-do list will be more 
concise and clear, thus help improve study efficiency.

###User Stories 

- As a user, I want to be able to add a task to my to-do list
- As a user, I want to be able to delete a task from my to-do list
- As a user, I want to be able to mark a task as completed on my to-do list
- As a user, I want to be able to mark a task as starred on my to-do list
- As a user, I want to be able to see the number of uncompleted, completed tasks and starred tasks on my to-do list
- As a user, I want to be able to see labels and case numbers of all tasks on my to-do list
- As a user, I want to be able to save my to-do list to file
- As a user, I want to be able to load my to-do list from file 


###Phase 4: Task 2

- I choose to test and design a robust class. I removed the required clause from the constructor of Task class and make 
it throw InvalidLabelException when the label is of zero-length.


###Phase 4: Task 3

- If I had more time, I would create UncompletedTask and CompletedTask classes as subclasses of the Task class 
and make Task class abstract. In this way, the Task class can capture the common methods, and the subclasses are only 
required to capture their unique methods. Also, instead of two lists of Task, the ToDoList class can have a list of 
UncompletedTask and a list of CompletedTask as its fields, which would make the code more consistent.
