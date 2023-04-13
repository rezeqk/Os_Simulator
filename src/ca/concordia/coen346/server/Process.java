package ca.concordia.coen346.server;

import ca.concordia.coen346.client.ConsumerClient;
import ca.concordia.coen346.client.ProducerClient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Process {

    public final static String NUM_ITEMS = "getNumItems";
    public final static String GET_ITEM = "getItemInPos";
    public final static String NEXT_ITEM_POS = "getNextItemPos";
    public final static String TERMINATE = "terminate";

    private int processId;

    private Buffer buffer;
    private BufferedReader reader;
    private PrintWriter writer;

    public Process(int id, Socket socket, Buffer buffer) throws Exception{
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.buffer = buffer;
        this.processId = id;
        // writing the PID to the client
        writer.println(processId);

        System.out.println("Process " + processId + " is created");
    }

    // TODO: 2023-04-06 read and write to buffer 
    public int run(int times){
        System.out.println("Process " + getPID() + " is schedulled");

        for(int i = 0; i < times; i++) {
            try{
                //msg to send to the client
                String msg = "Process with ID: " + processId + " is scheduled to run on thread " + Thread.currentThread().getName();
                sendMessage(msg);

                String instruction = reader.readLine();
                System.out.println(instruction);

                if(instruction.equals(NUM_ITEMS)){
                    int numItems = buffer.size();
                    writer.println(numItems);
                }
                else if(instruction.equals(GET_ITEM)){
                    int item = buffer.getNextItem();
                    writer.println(item);
                }
                else if(instruction.equals(NEXT_ITEM_POS)){
                    int index = buffer.getNextPosition();
                    writer.println(index);
                }
                else if(instruction.equals(TERMINATE)){
                    return -1;
                }
            }catch(IOException e){
                return -1;
            }
        }
        return 0;
    }


    // todo : implement the getNextItem Position 
//    public void writingtoBuffer(){
//
//    }
//
//    public String readingfromBuffer(){
//        return null;
//    }

    public  int getPID(){
        return processId;
    }

    public void insertItem(int item, int pos){buffer.insertItem(item, pos);}
    public static final void printInstruction(){
        System.out.println("Please select one of the following instruction");
        System.out.println("1 : " +
                "\n");
    }

    // function to write messages to client
    public void sendMessage(String message){
        writer.println(message);
    }
}
