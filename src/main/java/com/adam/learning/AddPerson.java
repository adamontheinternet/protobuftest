package com.adam.learning;

import com.adam.protos.AddressBookProtos;

import java.io.*;

/**
 * Created by alaplante on 1/27/16.
 */
public class AddPerson {
    static AddressBookProtos.Person PromptForAddress(BufferedReader in, PrintStream out) throws IOException {
        AddressBookProtos.Person.Builder person = AddressBookProtos.Person.newBuilder();

        out.print("Enter person ID: ");
        person.setId(Integer.valueOf(in.readLine().trim()));

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

//    static AddressBookProtos.Person PromptForAddress(Long id, String name, String email, String number, String type) throws IOException {
//        AddressBookProtos.Person.Builder person = AddressBookProtos.Person.newBuilder();
//
//        person.setId(id.intValue());
//
//        person.setName(name.trim());
//
//        person.setEmail(email.trim());
//
//        AddressBookProtos.Person.PhoneNumber.Builder phoneNumber = AddressBookProtos.Person.PhoneNumber.newBuilder().setNumber(number.trim());
//
//        type = type.trim();
//        if(type.equalsIgnoreCase("mobile")) {
//            phoneNumber.setType(AddressBookProtos.Person.PhoneType.MOBILE);
//        } else if(type.equalsIgnoreCase("home")) {
//            phoneNumber.setType(AddressBookProtos.Person.PhoneType.HOME);
//        } else if(type.equalsIgnoreCase("work")) {
//            phoneNumber.setType(AddressBookProtos.Person.PhoneType.WORK);
//        } else {
//            System.out.println("Invalid input. Using default value.");
//        }
//        person.addPhone(phoneNumber);
//
//        return person.build();
//    }

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.err.println("Usage: AddPerson <filename>");
            System.exit(-1);
        }

//        Long id = System.currentTimeMillis();
//        Long id = 1453943058031L;
//        String filename = "AddressBookProtoFile-" + id;
        String filename = args[0];

        AddressBookProtos.AddressBook.Builder addressBook = AddressBookProtos.AddressBook.newBuilder();

        // Read existing
        try {
            addressBook.mergeFrom(new FileInputStream(filename));
            System.out.println("Opened protocol file and found: " + addressBook.getPersonList().iterator().next().getName());
        } catch(FileNotFoundException e) {
            System.out.println(filename + ": File not found. Create new file");
        }

        // Add address
        addressBook.addPerson(PromptForAddress(new BufferedReader(new InputStreamReader(System.in)), System.out));
//        addressBook.addPerson(PromptForAddress(id, "Adam LaPlante", "haha@yo.com", "555-1234", "mobile"));


        // Write back to disk
        FileOutputStream out = new FileOutputStream(filename);
        addressBook.build().writeTo(out);
        out.close();
    }
}
