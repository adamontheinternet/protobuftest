package com.adam.learning;

import com.adam.protos.AddressBookProtos;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by alaplante on 1/27/16.
 */
public class AddressBookApplication {
    private final static AtomicInteger counter = new AtomicInteger();

    static AddressBookProtos.Person PromptForAddress(BufferedReader in, PrintStream out) throws IOException {
        AddressBookProtos.Person.Builder person = AddressBookProtos.Person.newBuilder();

//        out.print("Enter person ID: ");
//        person.setId(Integer.valueOf(in.readLine().trim()));
        person.setId(counter.incrementAndGet());

        out.print("Enter name: ");
        person.setName(in.readLine().trim());

        out.print("Enter email address (optional): ");
        String email = in.readLine().trim();
        if(email.length() > 0) {
            person.setEmail(email);
        }

        while(true) {
            out.print(("Enter phone (optional): "));
            String number = in.readLine().trim();
            if(number.length() == 0) break;

            AddressBookProtos.Person.PhoneNumber.Builder phoneNumber = AddressBookProtos.Person.PhoneNumber.newBuilder().setNumber(number);

            out.print("Enter mobile, home, or work ");
            String type = in.readLine();
            if(type.equalsIgnoreCase("mobile")) {
                phoneNumber.setType(AddressBookProtos.Person.PhoneType.MOBILE);
            } else if(type.equalsIgnoreCase("home")) {
                phoneNumber.setType(AddressBookProtos.Person.PhoneType.HOME);
            } else if(type.equalsIgnoreCase("work")) {
                phoneNumber.setType(AddressBookProtos.Person.PhoneType.WORK);
            } else {
                out.println("Invalid input. Using default value.");
            }
            person.addPhone(phoneNumber);
        }

        return person.build();
    }

    static void Print(AddressBookProtos.AddressBook addressBook) {
        for(AddressBookProtos.Person person : addressBook.getPersonList()) {
            System.out.println("Person ID: " + person.getId());
            System.out.println("    Name: " + person.getName());
            if(person.hasEmail()) {
                System.out.println("    Email: " + person.getEmail());
            }

            for(AddressBookProtos.Person.PhoneNumber phoneNumber : person.getPhoneList()) {
                switch (phoneNumber.getType()) {
                    case MOBILE:
                        System.out.print("  Mobile #: ");
                        break;
                    case HOME:
                        System.out.print("  Home #: ");
                    case WORK:
                        System.out.print("  Work #: ");
                        break;
                }
                System.out.println(phoneNumber.getNumber());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.err.println("Usage: AddPerson <filename>");
            System.exit(-1);
        }
        String filename = args[0];

        AddressBookProtos.AddressBook.Builder addressBook = AddressBookProtos.AddressBook.newBuilder();
        // Read existing
        try {
            addressBook.mergeFrom(new FileInputStream(filename));
            System.out.println("Opened protocol file");
        } catch(FileNotFoundException e) {
            System.out.println(filename + ": File not found. Create new file");
        }

        // Add address
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintStream out = System.out;
        out.print("Add new person (a) or list existing (l): ");
        String addOrList = in.readLine().trim();
        if(addOrList.equalsIgnoreCase("a")) {


            addressBook.addPerson(PromptForAddress(in, out));
        } else if(addOrList.equalsIgnoreCase("l")){
//            AddressBookProtos.AddressBook addressBook1 = AddressBookProtos.AddressBook.parseFrom(new FileInputStream(filename));
            Print(addressBook.build());
        } else {
            System.err.println("Invalid input. Exiting.");
            System.exit(-1);
        }

        // Write back to disk
        FileOutputStream fos = new FileOutputStream(filename);
        addressBook.build().writeTo(fos);
        fos.close();
    }
}
