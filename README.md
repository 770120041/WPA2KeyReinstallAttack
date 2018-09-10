# WPA2 Key Reinstall Attack
A Project insipired by a paper, and try to pratice socket programming in Java
## Usage
first needs to run 'javac ap.java ccmp.java' to compile the ap and use 'java ap' to  start the ap server
then needs to run 'javac middle.java ccmp.java' to compile the middle man and use 'java middle' to start the middle man
finally run 'javac client.java ccmp.java' to compile the client and use 'java client' to start the client

## Details
### AP server
The implementation of the AP is using ServerSocket to monitor the infomation from the specific port(4417 in my program), and it is a state machine, when receiving specific messages, it will change to another state, according to the WPA2 protocol, there are multiple states, so the server needs to change from one state to another states. In my cases, I just considered the Msg3 missed during the transmission and the AP server will resend a cipered message using the same nounce, and this is the principle of key reinstall attack. When Msg3 is missed, the server will try to resend messages.

### Client
The implementation of the client is using socket to establish a connection to a specific port(port 4416 in my program), and it will send messages and begin to send out infomation once a WPA2 connection is established

### Man in the middle 
The implementation of clinet is using socket to establish a connetion to the AP server using socket and port 4417 and receive message using Serversocket to receive messages from the client(port 4416 in my program), thus it can know how the server and the client established the connetion thus get the necessage info to implement the key reinstall attack.

## Miscellaneous
This is a Course Project for the course <Network Security> in Zhejiang University in 2018
