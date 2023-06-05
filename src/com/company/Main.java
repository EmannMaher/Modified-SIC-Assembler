package com.company;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        //input parsing and creating immediate file
        FileReader file = new FileReader("input.txt");
        FileWriter intermediate = new FileWriter("intermediate.txt");
        BufferedReader read = new BufferedReader(file);
        String s;
        while ((s = read.readLine()) != null) {
            String[] newString = s.split(" ");
            int i = 1;
            while (!newString[i].startsWith("//")) {
                intermediate.write(newString[i] + " ");
                i++;
            }
            intermediate.write("\n");
        }
        file.close();
        intermediate.close();

        //reading from intermediate file
        FileReader input = new FileReader("intermediate.txt");
        String[] label = new String[49];
        String[] instruction = new String[49];
        String[] reference = new String[49];

        int i = 0;
        BufferedReader reader = new BufferedReader(input);
        String st;
        while ((st = reader.readLine()) != null) {
            String[] newString = st.split(" ");
            if (newString.length == 3) {
                label[i] = newString[0];
                instruction[i] = newString[1];
                reference[i] = newString[2];
            } else if (newString.length == 2) {
                label[i] = "___";
                instruction[i] = newString[0];
                reference[i] = newString[1];
            } else if (newString.length == 1) {
                label[i] = "___";
                instruction[i] = newString[0];
                reference[i] = "___";
            }
            i++;
        }
        reader.close();

        String[][] OpcodeTable = new String[32][3];
        OpcodeTable[0] = new String[]{"ADD", "3", "18"};
        OpcodeTable[1] = new String[]{"AND", "3", "40"};
        OpcodeTable[2] = new String[]{"COMP", "3", "28"};
        OpcodeTable[3] = new String[]{"DIV", "3", "24"};
        OpcodeTable[4] = new String[]{"J", "3", "3C"};
        OpcodeTable[5] = new String[]{"JEQ", "3", "30"};
        OpcodeTable[6] = new String[]{"JGT", "3", "34"};
        OpcodeTable[7] = new String[]{"JLT", "3", "38"};
        OpcodeTable[8] = new String[]{"JSUB", "3", "48"};
        OpcodeTable[9] = new String[]{"LDA", "3", "00"};
        OpcodeTable[10] = new String[]{"LDCH", "3", "50"};
        OpcodeTable[11] = new String[]{"LDL", "3", "08"};
        OpcodeTable[12] = new String[]{"LDX", "3", "04"};
        OpcodeTable[13] = new String[]{"MUL", "3", "20"};
        OpcodeTable[14] = new String[]{"OR", "3", "44"};
        OpcodeTable[15] = new String[]{"RD", "3", "D8"};
        OpcodeTable[16] = new String[]{"RSUB", "3", "4C"};
        OpcodeTable[17] = new String[]{"STA", "3", "0C"};
        OpcodeTable[18] = new String[]{"STCH", "3", "54"};
        OpcodeTable[19] = new String[]{"STL", "3", "14"};
        OpcodeTable[20] = new String[]{"STSW", "3", "E8"};
        OpcodeTable[21] = new String[]{"STX", "3", "10"};
        OpcodeTable[22] = new String[]{"SUB", "3", "1C"};
        OpcodeTable[23] = new String[]{"TD", "3", "E0"};
        OpcodeTable[24] = new String[]{"TIX", "3", "2C"};
        OpcodeTable[25] = new String[]{"WD", "3", "DC"};
        OpcodeTable[26] = new String[]{"FIX", "1", "C4"};
        OpcodeTable[27] = new String[]{"FLOAT", "1", "C0"};
        OpcodeTable[28] = new String[]{"HIO", "1", "F4"};
        OpcodeTable[29] = new String[]{"NORM", "1", "C8"};
        OpcodeTable[30] = new String[]{"SIO", "1", "F0"};
        OpcodeTable[31] = new String[]{"TIO", "1", "F8"};
        //--------------------------------------------------------------
        //Location Counter
        String[] addresses = new String[49];
        addresses[0] = reference[0];
        addresses[1] = reference[0];

        for (int n = 1; n < 49; n++) {
            int a = Integer.parseInt(addresses[n], 16);
            if (instruction[n].equals("BYTE")) {
                if (reference[n].startsWith("C")) {
                    int o = (reference[n].length() - 3);
                    addresses[n + 1] = Integer.toHexString(a + o);
                } else if (reference[n].startsWith("X")) {
                    int o = (reference[n].length() - 3) / 2;
                    addresses[n + 1] = Integer.toHexString(a + o);
                }

            } else if (instruction[n].equals("WORD")) {
                addresses[n + 1] = Integer.toHexString(a+3);
            } else if (instruction[n].equals("RESW")) {
                addresses[n + 1] = Integer.toHexString(a + (Integer.parseInt(reference[n]) * 3));
                if (String.valueOf(addresses[n]).length() == 1) {
                    addresses[n] = "000" + addresses[n];
                } else if (addresses[n].length() == 2) {
                    addresses[n] = "00" + addresses[n];
                } else if (addresses[n].length() == 3) {
                    addresses[n] = "0" + addresses[n];
                }

            } else if (instruction[n].equals("RESB")) {
                addresses[n + 1] = Integer.toHexString(a + (Integer.parseInt(reference[n])));
                if (addresses[n].length() == 1) {
                    addresses[n] = "000" + addresses[n];
                } else if (addresses[n].length() == 2) {
                    addresses[n] = "00" + addresses[n];
                } else if (addresses[n].length() == 3) {
                    addresses[n] = "0" + addresses[n];
                }
            } else {
                for (int k = 0; k < OpcodeTable.length; k++) {
                    if (instruction[n].equals(OpcodeTable[k][0])) {
                        if (OpcodeTable[k][1].equals("1")) {
                            addresses[n + 1] = Integer.toHexString(a + 1);
                            if (addresses[n].length() == 1) {
                                addresses[n] = "000" + addresses[n];
                            } else if (addresses[n].length() == 2) {
                                addresses[n] = "00" + addresses[n];
                            } else if (addresses[n].length() == 3) {
                                addresses[n] = "0" + addresses[n];
                            }
                        } else {
                            addresses[n + 1] = Integer.toHexString(a + 3);
                            if (addresses[n].length() == 1) {
                                addresses[n] = "000" + addresses[n];
                            } else if (addresses[n].length() == 2) {
                                addresses[n] = "00" + addresses[n];
                            } else if (addresses[n].length() == 3) {
                                addresses[n] = "0" + addresses[n];
                            }
                        }
                    }

                }
            }

        }
        FileWriter out_pass1 = new FileWriter("out_pass1.txt");
        for (int k = 0; k < 49; k++) {
            out_pass1.write(addresses[k] + "\n");
        }
        out_pass1.close();

        //Symbol Table
        FileWriter symboltable = new FileWriter("SympolTable.txt");
        symboltable.write("symbol" + "      " + "location" + "\n");
        symboltable.write("-------------------------" + "\n");
        for (int k = 1; k < 49; k++) {
            if (label[k] != "___") {
                symboltable.write(label[k] + "      " + addresses[k] + "\n");
            }
        }

        symboltable.close();
        //------------------------------------------------------------------
        //Obcode
        String[] ObjectCode = new String[50];
        for (int j = 0; j < 49; j++) {

            if (instruction[j].equals("RESW")) {
                ObjectCode[j] = "There is no ObCode :(";
            } else if (instruction[j].equals("RESB")) {
                ObjectCode[j] = "There is no ObCode :(";
            } else if (instruction[j].equals("WORD")) {
                ObjectCode[j] = "";
                for (int r = 0; r < (6 - reference[j].length()); r++) {
                    ObjectCode[j] += '0';
                }
                ObjectCode[j] += Integer.toHexString(Integer.parseInt(reference[j]));
            } else if (instruction[j].equals("BYTE")) {
                String substring = reference[j].substring(2, reference[j].length() - 1);
                if (reference[j].startsWith("X")) {
                    ObjectCode[j] = substring;
                } else {
                    ObjectCode[j] = "";
                    for (int k = 0; k < substring.length(); k++) {
                        ObjectCode[j] = ObjectCode[j] + Integer.toHexString((int) (substring.charAt(k)));
                    }
                }
            } else {
                for (int y = 0; y < OpcodeTable.length; y++) {
                    if (instruction[j].equals(OpcodeTable[y][0])) {

                        if (OpcodeTable[y][1].equals("1"))
                            ObjectCode[j] = OpcodeTable[y][2];

                        else if (instruction[j].equals("RSUB"))
                            ObjectCode[j] = OpcodeTable[y][2] + "0000";

                        else if (reference[j].contains(",X")) {
                            String z = reference[j].substring(0, reference[j].length() - 2);
                            for (int k = 0; k < 49; k++) {
                                if (z.equals(label[k])) {
                                    ObjectCode[j] = OpcodeTable[y][2] + Integer.toHexString(Integer.parseInt(addresses[k]) + 32768);
                                }
                            }
                        } else if (reference[j].startsWith("#")) {
                            String substring1 = reference[j].substring(1);
                            String f = "";
                            for (int r = 0; r < 4 - (substring1.length()); r++)
                                f += "0";
                            String d = "" + (Integer.parseInt(OpcodeTable[y][2]) + 1);
                            if (d.length() == 1)
                                d = "0" + d;
                            ObjectCode[j] = d + f + substring1;
                        } else {
                            for (int k = 0; k < 49; k++) {
                                if (reference[j].equals(label[k])) {
                                    ObjectCode[j] = OpcodeTable[y][2] + addresses[k];
                                }
                            }
                        }
                    }
                }
            }
            //Printing code with loc counter with ob code
            System.out.println(addresses[j] + "   " + label[j] + "    " + instruction[j] + "     " + reference[j] + "   " + ObjectCode[j]);
        }
        FileWriter out_pass2 = new FileWriter("out_pass2.txt");
        for (int k = 0; k < 49; k++) {
            out_pass2.write(ObjectCode[k] + "\n");
        }
        out_pass2.close();
        FileWriter hterecord = new FileWriter("hterecord.txt");
        String start = addresses[0];
        String end = addresses[addresses.length-1];
        int diff  = (Integer.parseInt(end,16)) -(Integer.parseInt(start,16));
        String length = Integer.toHexString(diff);
        int u=0;
        String str=label[0];
        while(u<(6-label[0].length())){
            str+="X";
            u++;
        }
        String len="";
        for(int k=0;k<6-addresses[1].length();k++){
            len+="0";
        }
        len+=addresses[1];
        String len2="";
        for(int k=0;k<6-length.length();k++){
            len2+="0";
        }
        len2+=length;
        hterecord.write("H^"+str+"^"+len+"^"+len2+"\n");
        int t = 1;

        while(t < addresses.length-1){
            String sidehte ="";
            String l="";
            for(int k=0;k<6-addresses[t].length();k++){
                l+="0";
            }
            l+=addresses[t];
            int c = 0;
            while(c<30){

                if((instruction[t]).equals("RESW") ||(instruction[t].equals("RESB"))){
                    break;
                }
                if((instruction[t]).equals("END"))
                    break;
                sidehte+="^"+ObjectCode[t];
                if(instruction[t].equals("WORD") ){
                    c = c+3;
                }


                else if(instruction[t].equals("BYTE")){
                    String temp = reference[t-1];

                    double byt1;
                    int byt;
                    if(temp.charAt(0) == 'C'){
                        byt = temp.length() - 3;

                    }
                    else{
                        byt1 = (temp.length() -3)/2.0;
                        byt = (int) Math.ceil(byt1);
                    }
                    c = c + byt;

                }

                        else if (instruction[t].equals("FLOAT") || instruction[t].equals("TIO")
                                || instruction[t].equals("SIO") || instruction[t].equals("NORM")
                                || instruction[t].equals("HIO") || instruction[t].equals("FIX"))
                            c += 1;
                        else if (instruction[t].equals("BYTE")) {
                            c += (ObjectCode[t].length()) / 2;
                        } else
                            c += 3;



                t++;
            }
            if(c != 0){
                String tlength = Integer.toHexString(c);
                hterecord.write("T^"+l+"^"+tlength+sidehte+"\n");

            }
            t++;
        }

        hterecord.write("E^"+len);
        hterecord.close();



    }

}



