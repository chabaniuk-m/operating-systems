
## Lab1

<b>Task</b> </br>
Use Java, processes, sockets (java.nio.channels. SocketChannel), and selector (java.nio.channels. Selector). The main process is a single-threaded
socket server.

- <u>computation f & g</u>: advanced
- <u>cancellation</u>: basic (remark 2)
- <u>repetition</u>: basic

▶️ To run the project it is necessary to firstly run process AServer and then run processes ClientF and ClinetG

## Lab2

<b>Task</b> </br>
Examine modern os scheduling simulator. Implement Earliest Deadline First scheduling algorithm. Introduce parameters required by the scheduling algorithm. Extend the process model to address gaps in the original system and to accommodate specific algorithm. Add more parameters to the system if necessary. Improve (refactor) simulator.

The EDF scheduling algorithm is implemented according to article on Wikipedia
https://en.wikipedia.org/wiki/Earliest_deadline_first_scheduling

▶️ To run the project it is necessary to specify the command line argument (path to the configuration file scheduling.conf)
⚒️ In this file you can manually set the number of processes and their attributes. Program also provides an auto generation of n periodic processes.
