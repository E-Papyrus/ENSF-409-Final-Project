# Example Wildlife Rescue Volunteer Task Scheduler

This application was developed as a term project for ENSF409 at the University of Calgary.
The goal of this application is to create a daily schedule for volunteer work at a
fictional wildlife rescue.

The application was created in Java using the java.sql, java.swing, and java.awt libraries.
All user interaction is done through a GUI and the application connects to a database for
data input. Note that use of this application requires mySQL.

To use the application, clone the repository, and run one of the SQL scripts in the
'example_DB_scripts' directory to create a database with the url
"jdbc:mysql://localhost:3306/ewr". Upon running the Interface class, you will be prompted
to enter a username and password; these are the login credentials you would use to connect 
to the database. By default, the user 'oop'@'localhost' with the password 'password' is
created and given permissions in the EWR database.

Try out all of the SQL scripts to see how the application responds to a database in
multiple different states: 'no_problem.sql' demonstrates a situation where the
application is able to create a schedule with no issues, 'backup.sql' demonstrates
a situation where the application can create the schedule, but a backup volunteer
is required, and 'impossible.sql' demonstrates a situation where there are too
many tasks to schedule, even with a backup volunteer.

Project created by: [Ethan Reed](https://github.com/E-Papyrus) & [Mariyah Malik](https://github.com/mariyahmalik)
